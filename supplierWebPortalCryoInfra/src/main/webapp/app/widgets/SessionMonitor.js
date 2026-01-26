// Crear namespace global seguro al inicio del archivo
(function() {
  if (typeof window.SupplierApp === 'undefined') {
    window.SupplierApp = {};
  }
  if (typeof SupplierApp.widgets === 'undefined') {
    SupplierApp.widgets = {};
  }
})();

Ext.define('SupplierApp.widgets.SessionMonitor', {
  singleton: true,  // ExtJS manejará el patrón singleton

  // Configuración de tiempos
  interval: 1000 * 60 * 5,  // Verificar cada 5 minutos
  maxInactive: 1000 * 60 * 15,  // 15 minutos de inactividad permitidos
  remaining: 0,
  
  // Estados
  isPageVisible: true,
  isOnline: navigator.onLine,
  
  // Referencias
  ui: null,
  _lastActive: null, // Propiedad privada para anti-tampering
  
  // Tasks (referencias directas)
  sessionTask: null,
  countDownTask: null,
  keepAliveTask: null,
  
  // Flags para controlar si las tasks están activas
  isSessionTaskActive: false,
  isKeepAliveTaskActive: false,
  isCountDownTaskActive: false,
  
  // Handlers para remover event listeners
  visibilityHandler: null,
  onlineHandler: null,
  offlineHandler: null,
  storageHandler: null,
  
  // Para throttling
  throttledCapture: null,
  
  // Para sincronización entre pestañas
  storageKey: 'supplierApp_lastActivity_' + (Math.random().toString(36).substr(2, 9)),
  storageSyncInterval: null,

  // Ventana modal (se inicializa en constructor)
  window: null,

  constructor: function(config) {
    var me = this;
    
    // Inicializar ventana modal
    me.window = Ext.create('Ext.window.Window', {
      bodyPadding: 10,
      closable: false,
      closeAction: 'hide',
      modal: true,
      resizable: false,
      title: 'Advertencia de inactividad',
      width: 625,
      items: [{
        xtype: 'container',
        frame: true,
        html: "El tiempo de la sesión ha sobrepasado los 15 minutos sin actividad. </br></br>Si desea continuar trabajando, presione el botón 'Continuar Trabajando'</br></br>"
      },{
        xtype: 'label',
        itemId: 'countdownLabel',
        text: ''
      },{
        xtype: 'container',
        itemId: 'keepAliveIndicator',
        hidden: true,
        html: '<div style="color: green; font-size: 12px;">✓ Sesión mantenida activa</div>'
      }],
      buttons: [{
        text: 'Continuar Trabajando',
        handler: function() {
          me.handleContinue();
        }
      },{
        text: 'Terminar sesión',
        handler: function() {
          me.forceLogout();
        }
      }]
    });
    
    // Configurar throttled capture
    me.throttledCapture = Ext.Function.createThrottled(function() {
      me.captureActivity();
    }, 1000, me); // Máximo una vez por segundo
    
    // Handler para visibility change
    me.visibilityHandler = function() {
      me.handleVisibilityChange();
    };
    
    // Handlers para online/offline
    me.onlineHandler = function() {
      me.isOnline = true;
      // Reanudar verificación cuando vuelve online
      if (me.isPageVisible) {
        me.monitorUI();
      }
    };
    
    me.offlineHandler = function() {
      me.isOnline = false;
      // Pausar tasks cuando está offline
      me.stopKeepAliveTask();
    };
    
    // Handler para storage
    me.storageHandler = function(e) {
      if (e.key === me.storageKey) {
        me.handleStorageChange(e);
      }
    };
    
    // Registrar event listeners globales
    document.addEventListener('visibilitychange', me.visibilityHandler);
    window.addEventListener('online', me.onlineHandler);
    window.addEventListener('offline', me.offlineHandler);
    window.addEventListener('storage', me.storageHandler);
    
    // Inicializar timestamp
    me._lastActive = new Date();
    me.syncAcrossTabs();
    
    return me;
  },
  
  // Sincronizar actividad entre pestañas
  syncAcrossTabs: function() {
    try {
      if (this._lastActive) {
        localStorage.setItem(this.storageKey, this._lastActive.getTime().toString());
      }
    } catch(e) {
      console.warn('Error sincronizando entre pestañas:', e);
    }
  },
  
  // Manejar cambios en localStorage de otras pestañas
  handleStorageChange: function(e) {
    var storedTime = parseInt(e.newValue);
    if (storedTime && !isNaN(storedTime)) {
      // Actualizar lastActive si es más reciente
      if (!this._lastActive || storedTime > this._lastActive.getTime()) {
        this._lastActive = new Date(storedTime);
      }
    }
  },
  
  // Manejar cambio de visibilidad
  handleVisibilityChange: function() {
    var wasVisible = this.isPageVisible;
    this.isPageVisible = !document.hidden;
    
    // Si acaba de volver visible
    if (!document.hidden && !wasVisible) {
      // Verificar si el navegador suspendió el tab
      if (this._lastActive) {
        var timePassed = new Date() - this._lastActive;
        var maxExpected = this.maxInactive * 2; // Tolerancia para suspension
        
        if (timePassed > maxExpected) {
          // Posible suspensión del navegador
          this._lastActive = new Date() - this.maxInactive; // Considerar inactivo
        }
      }
      // Verificar inmediatamente al volver
      this.monitorUI();
    }
    
    // Controlar tasks según visibilidad
    if (document.hidden) {
      this.stopKeepAliveTask();
      this.stopSessionTask();
    } else if (this.isOnline) {
      this.startSessionTask();
      this.startKeepAliveTask();
    }
  },
  
  // Capturar actividad con protección
  captureActivity: function() {
    // Verificar que la pestaña esté visible
    if (!this.isPageVisible) {
      return;
    }
    
    // Verificar que sea una interacción real del usuario
    if (!document.hasFocus()) {
      return; // Solo registrar actividad si la ventana tiene foco
    }
    
    // Actualizar timestamp privado
    this._lastActive = new Date();
    
    // Sincronizar con otras pestañas
    this.syncAcrossTabs();
  },
  
  // Getter seguro para lastActive
  getLastActive: function() {
    return this._lastActive || new Date(0); // Retorna fecha mínima si es null
  },
  
  // Detener task de forma segura
  safeStopTask: function(task) {
    try {
      if (task) {
        Ext.TaskManager.stop(task);
      }
    } catch(e) {
      // Ignorar error si la task ya no existe
      console.warn('Error deteniendo task:', e);
    }
  },
  
  // Iniciar task de sesión
  startSessionTask: function() {
    if (this.isSessionTaskActive) {
      return; // Ya está activa
    }
    
    this.stopSessionTask(); // Asegurarse de que no hay otra activa
    
    this.sessionTask = Ext.TaskManager.start({
      run: function() {
        this.monitorUI();
      },
      interval: this.interval,
      scope: this
    });
    
    this.isSessionTaskActive = true;
  },
  
  // Detener task de sesión
  stopSessionTask: function() {
    this.safeStopTask(this.sessionTask);
    this.sessionTask = null;
    this.isSessionTaskActive = false;
  },
  
  // Iniciar task de keep-alive
  startKeepAliveTask: function() {
    if (this.isKeepAliveTaskActive) {
      return; // Ya está activa
    }
    
    this.stopKeepAliveTask(); // Asegurarse de que no hay otra activa
    
    this.keepAliveTask = Ext.TaskManager.start({
      run: function() {
        this.sendKeepAlive();
      },
      interval: 1000 * 60 * 8, // cada 8 minutos
      scope: this
    });
    
    this.isKeepAliveTaskActive = true;
  },
  
  // Detener task de keep-alive
  stopKeepAliveTask: function() {
    this.safeStopTask(this.keepAliveTask);
    this.keepAliveTask = null;
    this.isKeepAliveTaskActive = false;
  },
  
  // Iniciar task de countdown
  startCountDownTask: function() {
    if (this.isCountDownTaskActive) {
      return; // Ya está activa
    }
    
    this.stopCountDownTask(); // Asegurarse de que no hay otra activa
    
    this.countDownTask = Ext.TaskManager.start({
      run: function() {
        this.countDown();
      },
      interval: 1000,
      scope: this
    });
    
    this.isCountDownTaskActive = true;
  },
  
  // Detener task de countdown
  stopCountDownTask: function() {
    this.safeStopTask(this.countDownTask);
    this.countDownTask = null;
    this.isCountDownTaskActive = false;
  },
  
  // Enviar keep-alive con verificación atómica
  sendKeepAlive: function() {
    var me = this;
    
    // Verificaciones atómicas
    if (!me.isPageVisible || !me.isOnline) {
      return;
    }
    
    // Verificar inactividad actual
    var now = new Date();
    if ((now - me.getLastActive()) >= me.maxInactive) {
      return; // Usuario ya inactivo
    }
    
    // Verificación atómica del modal
    if (me.window && me.window.isVisible()) {
      return; // Modal visible
    }
    
    // Mostrar indicador visual
    me.showKeepAliveIndicator();
    
    // Enviar ping
    Ext.Ajax.request({
      url: 'isAlive/ping.action',
      timeout: 10000,
      callback: function() {
        // Ocultar indicador después de un tiempo
        setTimeout(function() {
          me.hideKeepAliveIndicator();
        }, 2000);
      },
      failure: function() {
        me.hideKeepAliveIndicator();
      }
    });
  },
  
  // Mostrar indicador visual de keep-alive
  showKeepAliveIndicator: function() {
    // Indicador sutil en el título del documento
    if (typeof document.title !== 'undefined') {
      var originalTitle = document.title.replace(/^\[•\]\s*/, '');
      document.title = '[•] ' + originalTitle;
    }
  },
  
  hideKeepAliveIndicator: function() {
    if (typeof document.title !== 'undefined') {
      document.title = document.title.replace(/^\[•\]\s*/, '');
    }
  },
  
  // Verificar inactividad
  monitorUI: function() {
    // Verificar estado de red
    if (!this.isOnline) {
      return; // No verificar inactividad si está offline
    }
    
    var now = new Date();
    var inactive = (now - this.getLastActive());
    
    if (inactive >= this.maxInactive) {
      this.stop();
      
      // Solo mostrar si está online y visible
      if (this.isOnline && this.isPageVisible) {
        this.window.show();
        this.remaining = 60; // seconds remaining
        this.startCountDownTask();
      } else {
        // Si no está visible o no hay conexión, forzar logout
        var me = this;
        setTimeout(function() {
          me.forceLogout();
        }, 1000);
      }
    }
  },
  
  // Manejar clic en "Continuar Trabajando"
  handleContinue: function() {
    var me = this;
    
    // Detener countdown inmediatamente
    me.stopCountDownTask();
    
    // Mostrar indicador de validación
    var indicator = me.window.down('#keepAliveIndicator');
    if (indicator) {
      indicator.update('<div style="color: blue; font-size: 12px;">Validando sesión...</div>');
      indicator.show();
    }
    
    Ext.Ajax.request({
      url: 'isAlive/ping.action',
      timeout: 10000,

      success: function(response) {
        var resp = Ext.decode(response.responseText, true);

        if (resp && resp.data === 'OK') {
          // Sesión válida
          if (indicator) {
            indicator.update('<div style="color: green; font-size: 12px;">✓ Sesión válida</div>');
          }
          
          setTimeout(function() {
            me.window.hide();
            if (indicator) {
              indicator.hide();
            }
            me.start(); // Reiniciar con nueva actividad
          }, 1000);
          
        } else {
          // Sesión inválida
          me.forceLogout();
        }
      },

      failure: function() {
        // Error técnico
        me.forceLogout();
      }
    });
  },

  // Iniciar monitor
  start: function() {
    // Detener cualquier task existente primero
    this.stop();
    
    // Inicializar timestamp
    this._lastActive = new Date();
    
    // Sincronizar con localStorage
    this.syncAcrossTabs();
    
    // Configurar UI
    this.ui = Ext.getBody();
    
    // Registrar event listeners con throttling y eventos táctiles
    var events = ['mousemove', 'keydown', 'touchstart', 'touchmove', 'click', 'scroll'];
    Ext.Array.each(events, function(event) {
      if (this.ui) {
        this.ui.on(event, this.throttledCapture, this);
      }
    }, this);
    
    // Iniciar tasks solo si está online
    if (this.isOnline) {
      this.startSessionTask();
      this.startKeepAliveTask();
    }
    
    // Configurar intervalo de sincronización
    if (this.storageSyncInterval) {
      clearInterval(this.storageSyncInterval);
    }
    
    this.storageSyncInterval = setInterval(function() {
      this.syncAcrossTabs();
    }.bind(this), 10000);
    
    console.log('SessionMonitor iniciado');
    return this;
  },
  
  // Detener monitor
  stop: function() {
    // Detener todas las tasks
    this.stopSessionTask();
    this.stopCountDownTask();
    this.stopKeepAliveTask();
    
    // Remover event listeners del UI si existe
    if (this.ui) {
      try {
        var events = ['mousemove', 'keydown', 'touchstart', 'touchmove', 'click', 'scroll'];
        Ext.Array.each(events, function(event) {
          this.ui.un(event, this.throttledCapture, this);
        }, this);
      } catch(e) {
        console.warn('Error removiendo event listeners:', e);
      }
    }
    
    console.log('SessionMonitor detenido');
  },
  
  // Limpieza completa
  destroy: function() {
    this.stop();
    
    // Remover event listeners globales
    if (this.visibilityHandler) {
      document.removeEventListener('visibilitychange', this.visibilityHandler);
    }
    if (this.onlineHandler) {
      window.removeEventListener('online', this.onlineHandler);
    }
    if (this.offlineHandler) {
      window.removeEventListener('offline', this.offlineHandler);
    }
    if (this.storageHandler) {
      window.removeEventListener('storage', this.storageHandler);
    }
    
    // Limpiar intervalo de sincronización
    if (this.storageSyncInterval) {
      clearInterval(this.storageSyncInterval);
      this.storageSyncInterval = null;
    }
    
    // Destruir ventana
    if (this.window) {
      try {
        this.window.destroy();
      } catch(e) {
        console.warn('Error destruyendo ventana:', e);
      }
      this.window = null;
    }
    
    // Limpiar localStorage
    try {
      localStorage.removeItem(this.storageKey);
    } catch(e) {
      // Ignorar error si no hay acceso a localStorage
    }
    
    console.log('SessionMonitor destruido');
  },
  
  // Contador regresivo
  countDown: function() {
    var label = this.window.down('#countdownLabel');
    if (label) {
      label.update('La sesión se cerrará automáticamente en ' +  
                  this.remaining + ' segundo' + 
                  (this.remaining !== 1 ? 's' : '') + '.');
    }
    
    this.remaining--;

    if (this.remaining < 0) {
      this.stopCountDownTask();
      this.forceLogout();
    }
  },
  
  // Forzar logout de forma segura
  forceLogout: function() {
    // Ocultar ventana si está visible
    if (this.window && this.window.isVisible()) {
      try {
        this.window.hide();
      } catch(e) {
        console.warn('Error ocultando ventana:', e);
      }
    }
    
    // Limpiar recursos
    try {
      this.destroy();
    } catch(e) {
      console.warn('Error en destroy durante forceLogout:', e);
    }
    
    // Redirigir a logout
    setTimeout(function() {
      try {
        location.href = "j_spring_security_logout";
      } catch(e) {
        console.error('Error redirigiendo a logout:', e);
      }
    }, 100);
  }
});

// Inicialización segura cuando ExtJS está listo
Ext.onReady(function() {
  try {
    // Pequeño delay para asegurar que todo esté cargado
    setTimeout(function() {
      try {
        // Verificar que la clase esté definida
        if (typeof SupplierApp !== 'undefined' && 
            SupplierApp.widgets && 
            SupplierApp.widgets.SessionMonitor) {
          
          // Obtener la instancia singleton
          var monitor = SupplierApp.widgets.SessionMonitor;
          
          // Verificar si ya está iniciado
          if (!monitor._lastActive || (new Date() - monitor.getLastActive()) > 60000) {
            monitor.start();
            console.log('SessionMonitor iniciado correctamente');
          } else {
            console.log('SessionMonitor ya estaba iniciado, reanudando...');
            // Solo reanudar las tasks si es necesario
            if (monitor.isOnline) {
              monitor.startSessionTask();
              monitor.startKeepAliveTask();
            }
          }
          
        } else {
          console.error('SessionMonitor no está definido correctamente');
        }
      } catch(e) {
        console.error('Error en inicialización de SessionMonitor:', e);
      }
    }, 1000); // Delay de 1 segundo para mayor seguridad
  } catch(e) {
    console.error('Error en Ext.onReady:', e);
  }
});