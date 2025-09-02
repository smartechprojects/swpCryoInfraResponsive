Ext.define('SupplierApp.widgets.SessionMonitor', {
  singleton: true,

  interval: 1000 * 600 ,  // run every 10 minutes.
  lastActive: null,
  maxInactive: 1000 * 600,  // 10 minutes of inactivity allowed; set it to 1 for testing.
  remaining: 0,
  ui: Ext.getBody(),
  
  window: Ext.create('Ext.window.Window', {
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
      text: ''
    }],
    buttons: [{
      text: 'Continuar Trabajando',
      handler: function() {
        Ext.TaskManager.stop(SupplierApp.widgets.SessionMonitor.countDownTask);
        SupplierApp.widgets.SessionMonitor.window.hide();
        SupplierApp.widgets.SessionMonitor.start();
        Ext.Ajax.request({
          url: 'isAlive/ping.action'
        });
      }
    },{
      text: 'Terminar sesión',
      action: 'logout',
      handler: function() {
        Ext.TaskManager.stop(SupplierApp.widgets.SessionMonitor.countDownTask);
        SupplierApp.widgets.SessionMonitor.window.hide();
        location.href = "j_spring_security_logout";
      }
    }]
  }),

  constructor: function(config) {
    var me = this;
   
    this.sessionTask = {
      run: me.monitorUI,
      interval: me.interval,
      scope: me
    };

    this.countDownTask = {
      run: me.countDown,
      interval: 1000,
      scope: me
    };
  },
 
  captureActivity : function(eventObj, el, eventOptions) {
    this.lastActive = new Date();
  },


  monitorUI : function() {
    var now = new Date();
    var inactive = (now - this.lastActive);
        
    if (inactive >= this.maxInactive) {
      this.stop();

      this.window.show();
      //this.remaining = 60;  // seconds remaining.
      this.remaining = 0;  // Evita mensaje sobre permanecer en sesion
      Ext.TaskManager.start(this.countDownTask);
    }
  },

  start : function() {
    this.lastActive = new Date();

    this.ui = Ext.getBody();

    this.ui.on('mousemove', this.captureActivity, this);
    this.ui.on('keydown', this.captureActivity, this);
        
    Ext.TaskManager.start(this.sessionTask);
  },
 
  stop: function() {
    Ext.TaskManager.stop(this.sessionTask);
    this.ui.un('mousemove', this.captureActivity, this);  //  always wipe-up after yourself...
    this.ui.un('keydown', this.captureActivity, this);
  },

  countDown: function() {
    this.window.down('label').update('La sesión se cerrará automáticamente en ' +  this.remaining + ' segundos' + ((this.remaining == 1) ? '.' : 's.') );
    
    --this.remaining;

    if (this.remaining < 0) {
      this.window.down('button[action="logout"]').handler();
    }
  }
 
});