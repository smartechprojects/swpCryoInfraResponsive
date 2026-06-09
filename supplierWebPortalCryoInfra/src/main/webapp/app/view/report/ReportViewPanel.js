Ext.define('SupplierApp.view.report.ReportViewPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.reportViewPanel',
    layout: 'border',
    bodyPadding: 5,
    title: 'Reportes Disponibles',
    iconCls: 'x-fa fa-file-text-o',

    initComponent: function() {
        var me = this;

        // Stores
        me.reportStore = Ext.create('Ext.data.Store', {
            fields: ['id', 'name', 'description', 'pdfExportEnabled', 'excelExportEnabled', 'csvExportEnabled', 'htmlExportEnabled', 'rtfExportEnabled', 'textExportEnabled', 'imageExportEnabled', 'role'],
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: 'reports/list.action',
                reader: { type: 'json', rootProperty: 'data' }
            }
        });


        // Grilla de Reportes (Izquierda)
        me.reportGrid = Ext.create('Ext.grid.Panel', {
            region: 'west',
            width: 320,
            split: true,
            title: 'Listado de Reportes',
            store: me.reportStore,
            forceFit: true,
            columns: [
                { text: 'Nombre', dataIndex: 'name', width: 150 },
                { text: 'Descripci\u00f3n', dataIndex: 'description', width: 130 },
                {
                    text: 'Rol(es)',
                    dataIndex: 'role',
                    width: 140,
                    hidden: (typeof role !== 'undefined' && role !== 'ROLE_ADMIN'),
                    renderer: function(v) {
                        if (!v) return '<span style="color:#aaa;font-style:italic">Sin rol</span>';
                        var roles = v.split(',');
                        var html = '';
                        var colors = {'ROLE_ADMIN':'#c0392b','ROLE_SUPPLIER':'#2980b9','ROLE_PURCHASE':'#27ae60','ROLE_QUALITY':'#8e44ad'};
                        for (var i = 0; i < roles.length; i++) {
                            var r = roles[i].trim();
                            var label = r.replace('ROLE_','');
                            var bg = colors[r] || '#7f8c8d';
                            html += '<span style="display:inline-block;background:' + bg + ';color:#fff;padding:1px 6px;border-radius:3px;font-size:10px;margin:1px 2px;">' + label + '</span>';
                        }
                        return html;
                    }
                }
            ],
            listeners: {
                selectionchange: me.onReportSelect,
                scope: me
            }
        });
 
        // Panel de Parámetros y Descarga (Centro)
        me.paramsForm = Ext.create('Ext.form.Panel', {
            title: 'Configuraci\u00f3n y Par\u00e1metros',
            bodyPadding: 15,
            scrollable: true,
            layout: 'anchor',
            defaults: { anchor: '100%', margin: '0 0 12 0' },
            items: [
                {
                    xtype: 'container',
                    itemId: 'dynamicParamsContainer',
                    layout: 'anchor',
                    defaults: { anchor: '100%', margin: '0 0 10 0' }
                }
            ],
            buttons: [
                {
                    text: 'Previsualizar Datos',
                    iconCls: 'x-fa fa-table',
                    cls: 'buttonStyle',
                    itemId: 'btnPreview',
                    hidden: true,
                    handler: me.onPreviewReport,
                    scope: me
                }
            ]
        });


        me.previewGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            title: 'Vista Previa de Datos',
            hidden: true,
            forceFit: true,
            emptyText: 'Presione "Previsualizar Datos" para cargar la información.',
            viewConfig: {
                deferEmptyText: false
            },
            columns: [],
            tbar: [
                {
                    text: 'Exportar PDF',
                    iconCls: 'x-fa fa-file-pdf-o',
                    itemId: 'btnGridPdf',
                    disabled: true,
                    handler: function() { me.exportGridPreview('pdf'); }
                },
                {
                    text: 'Exportar Excel',
                    iconCls: 'x-fa fa-file-excel-o',
                    itemId: 'btnGridExcel',
                    disabled: true,
                    handler: function() { me.exportGridPreview('xlsx'); }
                },
                {
                    text: 'Exportar CSV',
                    iconCls: 'x-fa fa-file-text-o',
                    itemId: 'btnGridCsv',
                    disabled: true,
                    handler: function() { me.exportGridPreview('csv'); }
                }
            ],
            bbar: {
                xtype: 'pagingtoolbar',
                displayInfo: true,
                displayMsg: 'Mostrando registros {0} - {1} de {2}',
                emptyMsg: 'No hay datos'
            }
        });

        // Contenedor principal de Trabajo
        me.centerPanel = Ext.create('Ext.panel.Panel', {
            region: 'center',
            layout: 'border',
            items: [
                Ext.apply(me.paramsForm, {
                    region: 'north',
                    height: 230,
                    split: true,
                    collapsible: true,
                    collapseMode: 'mini',
                    header: false
                }),
                me.previewGrid
            ]
        });

        Ext.apply(me, {
            items: [me.reportGrid, me.centerPanel]
        });

        me.callParent(arguments);
    },

    onReportSelect: function(selModel, selected) {
        var me = this;
        var container = me.paramsForm.down('#dynamicParamsContainer');
        var btnPreview = me.paramsForm.down('#btnPreview');

        // Limpiar parámetros dinámicos previos y ocultar/resetear vista previa anterior
        container.removeAll();
        if (me.previewGrid) {
            me.previewGrid.hide();
            me.toggleExportButtons(false);
        }

        if (selected.length > 0) {
            var report = selected[0];
            var reportId = report.get('id');

            btnPreview.show();

            // Cargar parámetros desde el servidor
            Ext.Ajax.request({
                url: 'reports/parameters.action',
                method: 'GET',
                params: { id: reportId },
                success: function(resp) {
                    var r = Ext.decode(resp.responseText, true);
                    if (r && r.success && r.data) {
                        var params = r.data;
                        var visibleParamsCount = 0;
                        for (var i = 0; i < params.length; i++) {
                            var pm = params[i];
                            var rp = pm.reportParameter;
                            if (!rp) continue;
                            
                            // Aplanar los parámetros para el renderizador
                            var p = {
                                id: rp.id,
                                name: rp.name,
                                description: rp.description,
                                parameterClass: rp.className,
                                defaultValue: rp.defaultValue,
                                sqlQuery: rp.data, // mapea a sqlQuery/DATA en BD
                                required: pm.required || false
                            };

                            var field = me.createDynamicField(p);
                            
                            // Si el parámetro es para filtrar proveedor y el usuario actual es un proveedor, ocultarlo del formulario
                            var isSupplierParam = (p.name === 'addressNumber' || p.name === 'supplier');
                            var isUserSupplier = (typeof role !== 'undefined' && role === 'ROLE_SUPPLIER');
                            if (isSupplierParam && isUserSupplier) {
                                field.setHidden(true);
                                field.allowBlank = true; // no exigir valor en el cliente
                            } else {
                                visibleParamsCount++;
                            }
                            
                            container.add(field);
                        }
                        
                        // Ajustar la altura del panel north según el número de parámetros visibles
                        var newHeight = 90 + (visibleParamsCount * 55);
                        if (newHeight > 250) newHeight = 250;
                        if (newHeight < 90) newHeight = 90;
                        me.paramsForm.setHeight(newHeight);
                        me.centerPanel.updateLayout();
                    }
                }
            });

        } else {
            btnPreview.hide();
            me.paramsForm.setHeight(90);
            if (me.centerPanel) {
                me.centerPanel.updateLayout();
            }
        }
    },

    createDynamicField: function(param) {
        var fieldConfig = {
            name: param.name,
            fieldLabel: param.description || param.name,
            allowBlank: !param.required,
            labelWidth: 150,
            margin: '0 0 10 0',
            anchor: '100%'
        };

        // Si tiene consulta de SQL, es ComboBox cargando opciones dinámicas
        if (param.sqlQuery && param.sqlQuery.trim() !== "") {
            var comboStore = Ext.create('Ext.data.Store', {
                fields: ['id', 'description'],
                autoLoad: true,
                proxy: {
                    type: 'ajax',
                    url: 'reports/parameterValues.action',
                    extraParams: { paramId: param.id },
                    reader: { type: 'json', rootProperty: 'data' }
                }
            });

            return Ext.create('Ext.form.ComboBox', Ext.apply(fieldConfig, {
                store: comboStore,
                displayField: 'description',
                valueField: 'id',
                queryMode: 'local',
                forceSelection: true,
                anyMatch: true,
                typeAhead: true
            }));
        }

        // De lo contrario, usar tipos de datos estándar
        var type = param.parameterClass;
        if (type === 'java.util.Date') {
            return Ext.create('Ext.form.field.Date', Ext.apply(fieldConfig, {
                format: 'Y-m-d',
                submitFormat: 'yyyy-MM-dd'
            }));
        } else if (type === 'java.lang.Integer' || type === 'java.lang.Double') {
            return Ext.create('Ext.form.field.Number', fieldConfig);
        } else if (type === 'java.lang.Boolean') {
            return Ext.create('Ext.form.field.ComboBox', Ext.apply(fieldConfig, {
                store: [
                    ['true', 'Verdadero / Sí'],
                    ['false', 'Falso / No']
                ],
                value: 'false'
            }));
        } else {
            return Ext.create('Ext.form.field.Text', fieldConfig);
        }
    },


    onPreviewReport: function() {
        var me = this;
        var selected = me.reportGrid.getSelectionModel().getSelection();
        if (selected.length === 0) return;
        if (!me.paramsForm.isValid()) return;

        var report = selected[0];
        var reportId = report.get('id');

        // Extraer valores de campos
        var formVals = me.paramsForm.getValues();
        delete formVals.format; // Eliminar formato del mapa de params del reporte

        // Formatear fechas para que vayan en formato yyyy-MM-dd en lugar de localizados
        me.paramsForm.down('#dynamicParamsContainer').items.each(function(field) {
            if (field.getXType() === 'datefield') {
                var dVal = field.getValue();
                if (dVal) {
                    formVals[field.getName()] = Ext.Date.format(dVal, 'yyyy-MM-dd');
                }
            }
        });

        // Habilitar máscara
        me.mask('Generando vista previa...');

        Ext.Ajax.request({
            url: 'reports/preview.action',
            method: 'POST',
            params: Ext.apply({ id: reportId, start: 0, limit: 25 }, formVals),
            success: function(resp) {
                me.unmask();
                var res = Ext.decode(resp.responseText, true);
                if (res && res.success) {
                    // 1. Reconfigurar las columnas de la grilla
                    var gridColumns = [];
                    Ext.Array.each(res.columns, function(col) {
                        var colCfg = {
                            text: col.text,
                            dataIndex: col.dataIndex,
                            sortable: true
                        };
                        // Formateador según tipo
                        if (col.type === 'NUMBER') {
                            colCfg.align = 'right';
                            colCfg.renderer = function(v) { return v !== null && v !== undefined ? Ext.util.Format.number(v, '0,000') : ''; };
                        } else if (col.type === 'CURRENCY') {
                            colCfg.align = 'right';
                            colCfg.renderer = function(v) { return v !== null && v !== undefined ? Ext.util.Format.usMoney(v) : ''; };
                        } else if (col.type === 'PERCENTAGE') {
                            colCfg.align = 'right';
                            colCfg.renderer = function(v) { return v !== null && v !== undefined ? Ext.util.Format.number(v, '0.0%') : ''; };
                        }
                        gridColumns.push(colCfg);
                    });

                    // 2. Crear el nuevo store con los campos dinámicos
                    var fields = res.fields || [];
                    var store = Ext.create('Ext.data.Store', {
                        fields: fields,
                        pageSize: 25,
                        remoteSort: true,
                        proxy: {
                            type: 'ajax',
                            url: 'reports/preview.action',
                            actionMethods: { read: 'POST' },
                            extraParams: Ext.apply({ id: reportId }, formVals),
                            reader: {
                                type: 'json',
                                rootProperty: 'data',
                                totalProperty: 'total'
                            }
                        }
                    });

                    // Cargar los datos iniciales
                    store.loadRawData(res);

                    // 3. Reconfigurar grilla y mostrarla
                    me.previewGrid.reconfigure(store, gridColumns);
                    me.previewGrid.show();
                    me.previewGrid.down('pagingtoolbar').setStore(store);

                    // 4. Habilitar exportadores
                    me.toggleExportButtons(true, report, formVals);
                } else {
                    Ext.Msg.alert('Error', res.error || 'No se pudo previsualizar los datos');
                }
            },
            failure: function() {
                me.unmask();
                Ext.Msg.alert('Error', 'Fallo de red al conectar al servidor');
            }
        });
    },

    toggleExportButtons: function(enable, report, formVals) {
        var me = this;
        var btnPdf = me.previewGrid.down('#btnGridPdf');
        var btnExcel = me.previewGrid.down('#btnGridExcel');
        var btnCsv = me.previewGrid.down('#btnGridCsv');

        if (!enable) {
            if (btnPdf) btnPdf.disable();
            if (btnExcel) btnExcel.disable();
            if (btnCsv) btnCsv.disable();
            return;
        }

        if (btnPdf) {
            btnPdf.setDisabled(!report.get('pdfExportEnabled'));
        }
        if (btnExcel) {
            btnExcel.setDisabled(!(report.get('excelExportEnabled') || report.get('xlsExportEnabled')));
        }
        if (btnCsv) {
            btnCsv.setDisabled(!report.get('csvExportEnabled'));
        }
    },

    exportGridPreview: function(format) {
        var me = this;
        var store = me.previewGrid.getStore();
        if (!store) return;
        var params = Ext.apply({}, store.getProxy().getExtraParams());
        params.format = format;
        
        // Generar URL final y descargar
        var url = 'reports/run.action?' + Ext.Object.toQueryString(params);
        window.open(url, '_blank');
    }
});
