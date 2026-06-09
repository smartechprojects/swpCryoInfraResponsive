Ext.define('SupplierApp.view.report.ReportAdminPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.reportAdminPanel',
    layout: 'border',
    bodyPadding: 5,
    title: 'Administrador de Reportes',
    iconCls: 'x-fa fa-file-text-o',

    initComponent: function() {
        var me = this;

        // Modelos locales
        Ext.define('AdminReportModel', {
            extend: 'Ext.data.Model',
            fields: [
                { name: 'id', type: 'int', allowNull: true },
                { name: 'name', type: 'string' },
                { name: 'description', type: 'string' },
                { name: 'file', type: 'string' },
                { name: 'pdfExportEnabled', type: 'boolean' },
                { name: 'csvExportEnabled', type: 'boolean' },
                { name: 'xlsExportEnabled', type: 'boolean' },
                { name: 'htmlExportEnabled', type: 'boolean' },
                { name: 'rtfExportEnabled', type: 'boolean' },
                { name: 'textExportEnabled', type: 'boolean' },
                { name: 'excelExportEnabled', type: 'boolean' },
                { name: 'imageExportEnabled', type: 'boolean' },
                { name: 'virtualizationEnabled', type: 'boolean' },
                { name: 'hidden', type: 'boolean' },
                { name: 'query', type: 'string' },
                { name: 'role', type: 'string' },
                { name: 'reportChart', type: 'auto' },
                { name: 'reportExportOption', type: 'auto' },
                { name: 'parameters', type: 'auto' },
                { name: 'pdfDesignConfig', type: 'string' }
            ]
        });

        Ext.define('AdminReportFileModel', {
            extend: 'Ext.data.Model',
            fields: ['name']
        });

        // Stores
        me.reportStore = Ext.create('Ext.data.Store', {
            model: 'AdminReportModel',
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: 'admin/reports/list.action',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            }
        });

        me.fileStore = Ext.create('Ext.data.Store', {
            fields: ['name'],
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: 'admin/reports/files.action',
                reader: {
                    type: 'json',
                    rootProperty: 'files',
                    transform: function(data) {
                        var arr = [];
                        if (data && data.files) {
                            for (var i = 0; i < data.files.length; i++) {
                                arr.push({ name: data.files[i] });
                            }
                        }
                        return { files: arr };
                    }
                }
            }
        });

        me.paramStore = Ext.create('Ext.data.Store', {
            fields: ['id', 'name', 'description', 'parameterClass', 'required', 'sortOrder', 'defaultValue', 'sqlQuery'],
            data: []
        });

        me.pdfColStore = Ext.create('Ext.data.Store', {
            fields: ['colName', 'header', 'align', 'widthPct'],
            data: []
        });

        // Componentes de UI
        me.grid = Ext.create('Ext.grid.Panel', {
            region: 'west',
            width: 380,
            split: true,
            store: me.reportStore,
            title: 'Reportes Registrados',
            forceFit: true,
            tbar: [
                {
                    text: 'Nuevo Reporte',
                    iconCls: 'x-fa fa-plus',
                    handler: me.onNewReport,
                    scope: me
                },
                {
                    text: 'Subir Archivo',
                    iconCls: 'x-fa fa-upload',
                    handler: me.onUploadFile,
                    scope: me
                },
                {
                    text: 'Recargar',
                    iconCls: 'x-fa fa-refresh',
                    handler: function() {
                        me.reportStore.load();
                        me.fileStore.load();
                    }
                }
            ],
            columns: [
                { text: 'Nombre', dataIndex: 'name', width: 130 },
                {
                    text: 'Rol(es)',
                    dataIndex: 'role',
                    width: 150,
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
                },
                {
                    text: 'Estado',
                    dataIndex: 'hidden',
                    width: 60,
                    renderer: function(v) {
                        return v ? '<span style="color:red">Oculto</span>' : '<span style="color:green">Activo</span>';
                    }
                }
            ],
            listeners: {
                selectionchange: me.onReportSelect,
                scope: me
            }
        });

        me.form = Ext.create('Ext.form.Panel', {
            region: 'center',
            title: 'Detalle del Reporte',
            bodyPadding: 15,
            scrollable: true,
            disabled: true,
            layout: 'anchor',
            defaults: {
                anchor: '100%',
                margin: '0 0 12 0',
                labelAlign: 'top'
            },
            items: [
                {
                    xtype: 'hiddenfield',
                    name: 'id'
                },
                {
                    xtype: 'container',
                    layout: 'hbox',
                    defaults: { flex: 1, margin: '0 10 0 0', labelAlign: 'top' },
                    items: [
                        {
                            xtype: 'textfield',
                            name: 'name',
                            fieldLabel: 'Nombre del Reporte',
                            allowBlank: false
                        },
                        {
                            xtype: 'textfield',
                            name: 'description',
                            fieldLabel: 'Descripci\u00f3n',
                            allowBlank: false
                        }
                    ]
                },
                {
                    xtype: 'container',
                    layout: 'hbox',
                    defaults: { flex: 1, margin: '0 10 0 0', labelAlign: 'top' },
                    items: [
                        {
                            xtype: 'combobox',
                            name: 'file',
                            fieldLabel: 'Archivo de Jasper (Opcional)',
                            store: me.fileStore,
                            displayField: 'name',
                            valueField: 'name',
                            queryMode: 'local',
                            allowBlank: true,
                            forceSelection: true
                        },
                        {
                            xtype: 'tagfield',
                            name: 'role',
                            itemId: 'roleTagField',
                            fieldLabel: 'Roles Autorizados',
                            store: getUDCStore('ROLES', '', '', ''),
                            displayField: 'strValue1',
                            valueField: 'strValue1',
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            multiSelect: true,
                            stacked: false,
                            filterPickList: true,
                            emptyText: 'Seleccionar Rol(es)...'
                        }
                    ]
                },
                {
                    xtype: 'textarea',
                    name: 'query',
                    fieldLabel: 'Consulta SQL del Reporte',
                    height: 100,
                    emptyText: 'Opcional si usa archivo Jasper. Obligatorio si no usa plantilla (ej. SELECT col1, col2 FROM table WHERE addressNumber = $P{addressNumber})',
                    allowBlank: true
                },
                {
                    xtype: 'fieldset',
                    title: 'Formatos de Exportaci\u00f3n Disponibles',
                    layout: 'column',
                    defaults: {
                        columnWidth: 0.25,
                        margin: '5'
                    },
                    items: [
                        { xtype: 'checkbox', boxLabel: 'PDF', name: 'pdfExportEnabled', checked: true },
                        { xtype: 'checkbox', boxLabel: 'Excel (XLSX)', name: 'excelExportEnabled', checked: true },
                        { xtype: 'checkbox', boxLabel: 'XLS Antiguo', name: 'xlsExportEnabled' },
                        { xtype: 'checkbox', boxLabel: 'CSV', name: 'csvExportEnabled' },
                        { xtype: 'checkbox', boxLabel: 'HTML', name: 'htmlExportEnabled' },
                        { xtype: 'checkbox', boxLabel: 'RTF', name: 'rtfExportEnabled' },
                        { xtype: 'checkbox', boxLabel: 'Texto Plano', name: 'textExportEnabled' },
                        { xtype: 'checkbox', boxLabel: 'Im\u00e1genes', name: 'imageExportEnabled' }
                    ]
                },
                {
                    xtype: 'container',
                    layout: 'hbox',
                    margin: '5 0 15 0',
                    items: [
                        { xtype: 'checkbox', boxLabel: 'Habilitar Virtualizaci\u00f3n (Reportes Grandes)', name: 'virtualizationEnabled', margin: '0 20 0 0' },
                        { xtype: 'checkbox', boxLabel: 'Ocultar en men\u00fa de usuarios', name: 'hidden' }
                    ]
                },
                {
                    xtype: 'fieldset',
                    title: '\ud83c\udfa8 Dise\u00f1ador de PDF (Reporte Din\u00e1mico)',
                    collapsible: true,
                    collapsed: true,
                    layout: 'anchor',
                    margin: '0 0 12 0',
                    items: [
                        {
                            xtype: 'container',
                            html: '<div style="background:#f0f4ff;border:1px solid #d0d9f0;border-radius:4px;padding:8px 12px;margin-bottom:8px;font-size:12px;color:#555;">' +
                                  '<b>\u2139\ufe0f Configuraci\u00f3n visual del PDF din\u00e1mico.</b> Primero ingrese la consulta SQL y presione <b>"Analizar Columnas"</b> para cargar las columnas y configurar encabezados, alineaci\u00f3n y ancho por columna.' +
                                  '</div>',
                            margin: '0 0 8 0'
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '0 0 10 0',
                            defaults: { margin: '0 10 0 0', labelAlign: 'top' },
                            items: [
                                {
                                    xtype: 'combobox',
                                    itemId: 'pdfOrientation',
                                    fieldLabel: 'Orientaci\u00f3n de P\u00e1gina',
                                    width: 160,
                                    store: [['false', 'Vertical (Portrait)'], ['true', 'Horizontal (Landscape)']],
                                    value: 'false',
                                    editable: false
                                },
                                {
                                    xtype: 'combobox',
                                    itemId: 'pdfTitleAlign',
                                    fieldLabel: 'Alineaci\u00f3n del T\u00edtulo',
                                    width: 160,
                                    store: [['CENTER', 'Centrado'], ['LEFT', 'Izquierda'], ['RIGHT', 'Derecha']],
                                    value: 'CENTER',
                                    editable: false
                                },
                                {
                                    xtype: 'numberfield',
                                    itemId: 'pdfTitleFontSize',
                                    fieldLabel: 'Tama\u00f1o T\u00edtulo',
                                    width: 130,
                                    value: 16,
                                    minValue: 8,
                                    maxValue: 36
                                },
                                {
                                    xtype: 'numberfield',
                                    itemId: 'pdfDataFontSize',
                                    fieldLabel: 'Tama\u00f1o Datos',
                                    width: 130,
                                    value: 9,
                                    minValue: 6,
                                    maxValue: 20
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '0 0 10 0',
                            defaults: { margin: '0 10 0 0', labelAlign: 'top' },
                            items: [
                                {
                                    xtype: 'container',
                                    width: 180,
                                    layout: 'anchor',
                                    labelAlign: 'top',
                                    items: [
                                        {
                                            xtype: 'displayfield',
                                            fieldLabel: 'Color Fondo Encabezado',
                                            value: ''
                                        },
                                        {
                                            xtype: 'textfield',
                                            itemId: 'pdfHeaderBgColor',
                                            value: '#00306E',
                                            inputAttrTpl: ' type="color"',
                                            width: 120
                                        }
                                    ]
                                },
                                {
                                    xtype: 'container',
                                    width: 180,
                                    layout: 'anchor',
                                    items: [
                                        {
                                            xtype: 'displayfield',
                                            fieldLabel: 'Color Texto Encabezado',
                                            value: ''
                                        },
                                        {
                                            xtype: 'textfield',
                                            itemId: 'pdfHeaderFgColor',
                                            value: '#FFFFFF',
                                            inputAttrTpl: ' type="color"',
                                            width: 120
                                        }
                                    ]
                                },
                                {
                                    xtype: 'container',
                                    width: 180,
                                    layout: 'anchor',
                                    items: [
                                        {
                                            xtype: 'displayfield',
                                            fieldLabel: 'Color T\u00edtulo',
                                            value: ''
                                        },
                                        {
                                            xtype: 'textfield',
                                            itemId: 'pdfTitleColor',
                                            value: '#00306E',
                                            inputAttrTpl: ' type="color"',
                                            width: 120
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'fieldset',
                            title: 'Logo del Reporte (Opcional - solo PDF/HTML/RTF)',
                            layout: 'anchor',
                            margin: '10 0 10 0',
                            defaults: { labelAlign: 'top', margin: '0 0 10 0' },
                            items: [
                                {
                                    xtype: 'container',
                                    layout: 'hbox',
                                    defaults: { margin: '0 10 0 0', labelAlign: 'top' },
                                    items: [
                                        {
                                            xtype: 'filefield',
                                            itemId: 'pdfLogoFile',
                                            fieldLabel: 'Cargar Logo (.png, .jpg)',
                                            buttonText: 'Buscar...',
                                            width: 300,
                                            listeners: {
                                                change: function(fileField, value) {
                                                    var file = fileField.fileInputEl.dom.files[0];
                                                    if (file) {
                                                        var reader = new FileReader();
                                                        reader.onload = function(e) {
                                                            var base64Data = e.target.result;
                                                            var b64Text = me.form.down('[itemId=pdfLogoBase64]');
                                                            if (b64Text) {
                                                                b64Text.setValue(base64Data);
                                                            }
                                                            var imgPrev = me.form.down('[itemId=pdfLogoPreview]');
                                                            if (imgPrev) {
                                                                imgPrev.setSrc(base64Data);
                                                                imgPrev.show();
                                                            }
                                                        };
                                                        reader.readAsDataURL(file);
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            xtype: 'numberfield',
                                            itemId: 'pdfLogoWidth',
                                            fieldLabel: 'Ancho Logo (px)',
                                            width: 120,
                                            value: 80,
                                            minValue: 10,
                                            maxValue: 300
                                        },
                                        {
                                            xtype: 'numberfield',
                                            itemId: 'pdfLogoHeight',
                                            fieldLabel: 'Alto Logo (px)',
                                            width: 120,
                                            value: 40,
                                            minValue: 10,
                                            maxValue: 150
                                        },
                                        {
                                            xtype: 'button',
                                            text: 'Quitar Logo',
                                            margin: '25 0 0 0',
                                            style: 'background:#6c757d; color:white; border-color:#6c757d;',
                                            handler: function() {
                                                var fileField = me.form.down('[itemId=pdfLogoFile]');
                                                if (fileField) fileField.reset();
                                                var b64Text = me.form.down('[itemId=pdfLogoBase64]');
                                                if (b64Text) b64Text.setValue('');
                                                var imgPrev = me.form.down('[itemId=pdfLogoPreview]');
                                                if (imgPrev) {
                                                    imgPrev.setSrc('');
                                                    imgPrev.hide();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    xtype: 'hiddenfield',
                                    itemId: 'pdfLogoBase64',
                                    value: ''
                                },
                                {
                                    xtype: 'image',
                                    itemId: 'pdfLogoPreview',
                                    style: 'max-height:60px; border:1px solid #ddd; padding:4px; border-radius:4px; margin-top:5px; background-color:#f9f9f9;',
                                    hidden: true,
                                    src: ''
                                }
                            ]
                        },
                        {
                            xtype: 'toolbar',
                            plain: true,
                            margin: '0 0 6 0',
                            items: [
                                {
                                    text: '\ud83d\udd0d Analizar Columnas del SQL',
                                    iconCls: 'x-fa fa-search',
                                    handler: me.onAnalyzePdfColumns,
                                    scope: me
                                },
                                '-',
                                { xtype: 'tbtext', text: 'Haga click para detectar columnas del query SQL ingresado arriba.' }
                            ]
                        },
                        {
                            xtype: 'grid',
                            itemId: 'pdfColGrid',
                            store: me.pdfColStore,
                            height: 160,
                            anchor: '100%',
                            plugins: {
                                ptype: 'cellediting',
                                clicksToEdit: 1
                            },
                            columns: [
                                { text: 'Columna SQL', dataIndex: 'colName', width: 140, editable: false },
                                { text: 'Encabezado Personalizado', dataIndex: 'header', flex: 1, editor: { xtype: 'textfield' } },
                                {
                                    text: 'Alineaci\u00f3n Datos',
                                    dataIndex: 'align',
                                    width: 140,
                                    editor: {
                                        xtype: 'combobox',
                                        store: [['LEFT', 'Izquierda'], ['CENTER', 'Centro'], ['RIGHT', 'Derecha']],
                                        editable: false
                                    },
                                    renderer: function(v) {
                                        if (v === 'LEFT') return 'Izquierda';
                                        if (v === 'RIGHT') return 'Derecha';
                                        if (v === 'CENTER') return 'Centro';
                                        return v || 'Auto';
                                    }
                                },
                                {
                                    text: 'Ancho %',
                                    dataIndex: 'widthPct',
                                    width: 90,
                                    editor: { xtype: 'numberfield', minValue: 1, maxValue: 100 },
                                    renderer: function(v) { return v ? v + '%' : 'Auto'; }
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'fieldset',
                    title: 'Par\u00e1metros del Reporte',
                    layout: 'anchor',
                    items: [
                        {
                            xtype: 'grid',
                            title: 'Par\u00e1metros del Reporte',
                            store: me.paramStore,
                            height: 180,
                            anchor: '100%',
                            tbar: [
                                {
                                    text: 'Agregar Par\u00e1metro',
                                    iconCls: 'x-fa fa-plus',
                                    handler: me.onAddParameter,
                                    scope: me
                                },
                                {
                                    text: 'Eliminar Seleccionado',
                                    iconCls: 'x-fa fa-trash',
                                    handler: me.onDeleteParameter,
                                    scope: me
                                }
                            ],
                            plugins: {
                                ptype: 'cellediting',
                                clicksToEdit: 1
                            },
                            columns: [
                                { text: 'Nombre T\u00e9cnico', dataIndex: 'name', width: 120, editor: { xtype: 'textfield', allowBlank: false } },
                                { text: 'Etiqueta / Descripci\u00f3n', dataIndex: 'description', width: 150, editor: { xtype: 'textfield' } },
                                {
                                    text: 'Clase Java',
                                    dataIndex: 'parameterClass',
                                    width: 150,
                                    editor: {
                                        xtype: 'combobox',
                                        store: [
                                            'java.lang.String',
                                            'java.lang.Integer',
                                            'java.lang.Double',
                                            'java.lang.Boolean',
                                            'java.util.Date'
                                        ],
                                        allowBlank: false
                                    }
                                },
                                { text: 'Valor Defecto', dataIndex: 'defaultValue', width: 100, editor: { xtype: 'textfield' } },
                                { text: 'Consulta SQL Combobox (Opcional)', dataIndex: 'sqlQuery', width: 250, editor: { xtype: 'textfield' } },
                                { xtype: 'checkcolumn', text: 'Requerido', dataIndex: 'required', width: 80 }
                            ]
                        }
                    ]
                }
            ],
            buttons: [
                {
                    text: 'Guardar Cambios',
                    formBind: true,
                    cls: 'buttonStyle',
                    handler: me.onSaveReport,
                    scope: me
                },
                {
                    text: '\u2705 Validar Reporte',
                    iconCls: 'x-fa fa-check-circle',
                    style: 'background:#28a745; color:white; border-color:#28a745; font-weight:bold;',
                    handler: me.onValidateReport,
                    scope: me
                },
                {
                    text: 'Eliminar Reporte',
                    cls: 'deleteStyle',
                    style: 'background:#cc0000; color:white; border-color:#cc0000;',
                    handler: me.onDeleteReport,
                    scope: me
                }
            ]
        });

        Ext.apply(me, {
            items: [me.grid, me.form]
        });

        me.callParent(arguments);
    },

    onReportSelect: function(selModel, selected) {
        var me = this;
        if (selected.length > 0) {
            var rec = selected[0];
            me.form.enable();
            me.form.loadRecord(rec);

            // Multi-rol: splitear el string de roles separados por comas en un array para el TagField
            var roleTagField = me.form.down('#roleTagField');
            if (roleTagField) {
                var roleStr = rec.get('role') || '';
                if (roleStr) {
                    var rolesArr = roleStr.split(',');
                    for (var ri = 0; ri < rolesArr.length; ri++) {
                        rolesArr[ri] = rolesArr[ri].trim();
                    }
                    roleTagField.setValue(rolesArr);
                } else {
                    roleTagField.setValue([]);
                }
            }

            // Cargar parámetros en la grilla interna (aplanando la relación)
            me.paramStore.removeAll();
            if (rec.get('parameters')) {
                var flatParams = [];
                Ext.Array.each(rec.get('parameters'), function(pm) {
                    var param = pm.reportParameter || {};
                    flatParams.push({
                        id: param.id || null,
                        name: param.name || '',
                        description: param.description || '',
                        parameterClass: param.className || 'java.lang.String',
                        defaultValue: param.defaultValue || '',
                        sqlQuery: param.data || '',
                        required: pm.required || false,
                        sortOrder: pm.sortOrder || 1
                    });
                });
                me.paramStore.add(flatParams);
            }

            // Cargar configuraci\u00f3n del dise\u00f1ador PDF
            me.pdfColStore.removeAll();
            var configStr = rec.get('pdfDesignConfig');
            if (configStr) {
                try {
                    var cfg = Ext.decode(configStr, true);
                    if (cfg) {
                        var ds = me.form.down('[itemId=pdfOrientation]');
                        if (ds) ds.setValue(cfg.landscape ? 'true' : 'false');
                        var ta = me.form.down('[itemId=pdfTitleAlign]');
                        if (ta) ta.setValue(cfg.titleAlign || 'CENTER');
                        var tf = me.form.down('[itemId=pdfTitleFontSize]');
                        if (tf) tf.setValue(cfg.titleFontSize || 16);
                        var df = me.form.down('[itemId=pdfDataFontSize]');
                        if (df) df.setValue(cfg.dataFontSize || 9);
                        var hbg = me.form.down('[itemId=pdfHeaderBgColor]');
                        if (hbg) hbg.setValue(cfg.headerBgColor || '#00306E');
                        var hfg = me.form.down('[itemId=pdfHeaderFgColor]');
                        if (hfg) hfg.setValue(cfg.headerFgColor || '#FFFFFF');
                        var tc = me.form.down('[itemId=pdfTitleColor]');
                        if (tc) tc.setValue(cfg.titleColor || '#00306E');
                        
                        // Cargar valores del logo
                        var logoB64 = me.form.down('[itemId=pdfLogoBase64]');
                        if (logoB64) logoB64.setValue(cfg.logoBase64 || '');
                        var logoW = me.form.down('[itemId=pdfLogoWidth]');
                        if (logoW) logoW.setValue(cfg.logoWidth || 80);
                        var logoH = me.form.down('[itemId=pdfLogoHeight]');
                        if (logoH) logoH.setValue(cfg.logoHeight || 40);
                        
                        var fileField = me.form.down('[itemId=pdfLogoFile]');
                        if (fileField) fileField.reset();
                        
                        var imgPrev = me.form.down('[itemId=pdfLogoPreview]');
                        if (imgPrev) {
                            if (cfg.logoBase64) {
                                imgPrev.setSrc(cfg.logoBase64);
                                imgPrev.show();
                            } else {
                                imgPrev.setSrc('');
                                imgPrev.hide();
                            }
                        }
                        
                        if (cfg.columns && cfg.columns.length > 0) {
                            for (var i = 0; i < cfg.columns.length; i++) {
                                var col = cfg.columns[i];
                                me.pdfColStore.add({
                                    colName: col.colName || col.header || ('col' + i),
                                    header: col.header || '',
                                    align: col.align || '',
                                    widthPct: col.widthPct || 0
                                });
                            }
                        }
                    }
                } catch(e) {}
            }
        } else {
            me.form.disable();
            me.form.reset();
            me.paramStore.removeAll();
            me.pdfColStore.removeAll();
            
            var logoB64 = me.form.down('[itemId=pdfLogoBase64]');
            if (logoB64) logoB64.setValue('');
            var logoW = me.form.down('[itemId=pdfLogoWidth]');
            if (logoW) logoW.setValue(80);
            var logoH = me.form.down('[itemId=pdfLogoHeight]');
            if (logoH) logoH.setValue(40);
            var fileField = me.form.down('[itemId=pdfLogoFile]');
            if (fileField) fileField.reset();
            var imgPrev = me.form.down('[itemId=pdfLogoPreview]');
            if (imgPrev) {
                imgPrev.setSrc('');
                imgPrev.hide();
            }
        }
    },

    onNewReport: function() {
        var me = this;
        me.grid.getSelectionModel().deselectAll();
        me.form.enable();
        me.form.reset();
        me.paramStore.removeAll();
        me.pdfColStore.removeAll();
        
        // Multi-rol: resetear el TagField
        var roleTagField = me.form.down('#roleTagField');
        if (roleTagField) roleTagField.setValue([]);
        
        var logoB64 = me.form.down('[itemId=pdfLogoBase64]');
        if (logoB64) logoB64.setValue('');
        var logoW = me.form.down('[itemId=pdfLogoWidth]');
        if (logoW) logoW.setValue(80);
        var logoH = me.form.down('[itemId=pdfLogoHeight]');
        if (logoH) logoH.setValue(40);
        var fileField = me.form.down('[itemId=pdfLogoFile]');
        if (fileField) fileField.reset();
        var imgPrev = me.form.down('[itemId=pdfLogoPreview]');
        if (imgPrev) {
            imgPrev.setSrc('');
            imgPrev.hide();
        }
        
        me.form.down('[name=name]').focus();
    },

    onAnalyzePdfColumns: function() {
        var me = this;
        var queryField = me.form.down('[name=query]');
        if (!queryField || !queryField.getValue()) {
            Ext.Msg.alert('Aviso', 'Ingrese primero la consulta SQL del reporte para analizar las columnas.');
            return;
        }
        var sql = queryField.getValue();
        Ext.MessageBox.wait('Analizando columnas...', 'Por favor espere');
        Ext.Ajax.request({
            url: 'admin/dashboard/tables/analyze.action',
            method: 'POST',
            params: { sqlQuery: sql },
            success: function(fp) {
                Ext.MessageBox.hide();
                var res = Ext.decode(fp.responseText, true);
                if (res && res.success && res.columns) {
                    me.pdfColStore.removeAll();
                    Ext.Array.each(res.columns, function(col) {
                        var defaultAlign = col.type === 'NUMBER' ? 'RIGHT' : 'LEFT';
                        var defaultPct = Math.floor(100 / res.columns.length);
                        me.pdfColStore.add({
                            colName: col.key,
                            header: col.key,
                            align: defaultAlign,
                            widthPct: defaultPct
                        });
                    });
                    Ext.Msg.alert('\u00c9xito', res.columns.length + ' columna(s) detectada(s). Configure encabezados, alineaci\u00f3n y ancho.');
                } else {
                    Ext.Msg.alert('Error', res.error || 'No se pudieron analizar las columnas del query.');
                }
            },
            failure: function() {
                Ext.MessageBox.hide();
                Ext.Msg.alert('Error', 'Error de comunicaci\u00f3n con el servidor.');
            }
        });
    },

    onAddParameter: function() {
        var me = this;
        me.paramStore.add({
            name: 'PARAM_NAME',
            description: 'Etiqueta del Par\u00e1metro',
            parameterClass: 'java.lang.String',
            required: false,
            defaultValue: '',
            sqlQuery: '',
            sortOrder: me.paramStore.getCount() + 1
        });
    },

    onDeleteParameter: function(btn) {
        var me = this;
        var grid = btn.up('grid');
        var selected = grid.getSelectionModel().getSelection();
        if (selected.length > 0) {
            me.paramStore.remove(selected[0]);
        }
    },

    onSaveReport: function() {
        var me = this;
        if (!me.form.isValid()) return;

        var values = me.form.getValues();
        var reportData = Ext.clone(values);

        // Multi-rol: convertir el array del TagField en un string separado por comas
        var roleTagField = me.form.down('#roleTagField');
        if (roleTagField) {
            var roleVal = roleTagField.getValue();
            if (Ext.isArray(roleVal)) {
                reportData.role = roleVal.join(',');
            } else if (roleVal) {
                reportData.role = roleVal;
            } else {
                reportData.role = '';
            }
        }

        // Convertir strings booleanos a Boolean real
        var boolFields = ['pdfExportEnabled', 'csvExportEnabled', 'xlsExportEnabled', 'htmlExportEnabled',
                          'rtfExportEnabled', 'textExportEnabled', 'excelExportEnabled', 'imageExportEnabled',
                          'virtualizationEnabled', 'hidden'];
        for (var i = 0; i < boolFields.length; i++) {
            var fieldName = boolFields[i];
            reportData[fieldName] = (reportData[fieldName] === 'on' || reportData[fieldName] === 'true' || reportData[fieldName] === true);
        }

        // Capturar los parámetros de la tabla (anidando la relación para Hibernate)
        var params = [];
        me.paramStore.each(function(rec) {
            var p = rec.getData();
            var paramId = p.id;
            if (!paramId || paramId === 0 || isNaN(Number(paramId)) || String(paramId).indexOf('ext') !== -1) {
                paramId = null;
            }
            var reportParameter = {
                id: paramId,
                name: p.name,
                description: p.description,
                className: p.parameterClass || 'java.lang.String',
                type: (p.sqlQuery && p.sqlQuery.trim() !== '') ? 'Query' : 'TEXT',
                defaultValue: p.defaultValue,
                data: p.sqlQuery
            };
            params.push({
                required: p.required || false,
                sortOrder: p.sortOrder || 1,
                reportParameter: reportParameter
            });
        });
        reportData.parameters = params;

        // Construir pdfDesignConfig desde los controles del dise\u00f1ador
        try {
            var orientField = me.form.down('[itemId=pdfOrientation]');
            var titleAlignField = me.form.down('[itemId=pdfTitleAlign]');
            var titleFsField = me.form.down('[itemId=pdfTitleFontSize]');
            var dataFsField = me.form.down('[itemId=pdfDataFontSize]');
            var hbgField = me.form.down('[itemId=pdfHeaderBgColor]');
            var hfgField = me.form.down('[itemId=pdfHeaderFgColor]');
            var tcField = me.form.down('[itemId=pdfTitleColor]');
            var logoB64Field = me.form.down('[itemId=pdfLogoBase64]');
            var logoWField = me.form.down('[itemId=pdfLogoWidth]');
            var logoHField = me.form.down('[itemId=pdfLogoHeight]');

            var pdfCfg = {
                landscape: (orientField && orientField.getValue() === 'true') ? true : false,
                titleAlign: titleAlignField ? titleAlignField.getValue() : 'CENTER',
                titleFontSize: titleFsField ? titleFsField.getValue() : 16,
                dataFontSize: dataFsField ? dataFsField.getValue() : 9,
                headerBgColor: hbgField ? hbgField.getValue() : '#00306E',
                headerFgColor: hfgField ? hfgField.getValue() : '#FFFFFF',
                titleColor: tcField ? tcField.getValue() : '#00306E',
                logoBase64: logoB64Field ? logoB64Field.getValue() : '',
                logoWidth: logoWField ? logoWField.getValue() : 80,
                logoHeight: logoHField ? logoHField.getValue() : 40,
                columns: []
            };

            me.pdfColStore.each(function(colRec) {
                pdfCfg.columns.push({
                    colName: colRec.get('colName'),
                    header: colRec.get('header'),
                    align: colRec.get('align'),
                    widthPct: colRec.get('widthPct') || 0
                });
            });

            reportData.pdfDesignConfig = Ext.encode(pdfCfg);
        } catch(ex) {
            reportData.pdfDesignConfig = null;
        }

        // Si es un nuevo registro, limpiar el ID
        if (!reportData.id || reportData.id === '0') {
            delete reportData.id;
        } else {
            reportData.id = parseInt(reportData.id);
        }

        Ext.MessageBox.wait('Guardando reporte...', 'Por favor espere');

        Ext.Ajax.request({
            url: 'admin/reports/save.action',
            method: 'POST',
            jsonData: Ext.encode(reportData),
            success: function(fp) {
                Ext.MessageBox.hide();
                var res = Ext.decode(fp.responseText, true);
                if (res && res.success) {
                    Ext.Msg.alert('\u00c9xito', 'Reporte guardado correctamente');
                    me.reportStore.load({
                        callback: function() {
                            if (res.data && res.data.id) {
                                var recordIndex = me.reportStore.find('id', res.data.id);
                                if (recordIndex !== -1) {
                                    me.grid.getSelectionModel().select(recordIndex);
                                }
                            }
                        }
                    });
                } else {
                    Ext.Msg.alert('Error', res.error || 'Error al guardar el reporte');
                }
            },
            failure: function() {
                Ext.MessageBox.hide();
                Ext.Msg.alert('Error', 'Error de comunicaci\u00f3n con el servidor');
            }
        });
    },
    onValidateReport: function() {
        var me = this;
        var queryField = me.form.down('[name=query]');
        if (!queryField || !queryField.getValue()) {
            Ext.Msg.alert('Aviso', 'Ingrese primero la consulta SQL del reporte para validar.');
            return;
        }

        // Recopilar formatos habilitados
        var fmtList = [];
        var fmtMap = {
            pdfExportEnabled: 'PDF', csvExportEnabled: 'CSV', xlsExportEnabled: 'XLS',
            htmlExportEnabled: 'HTML', rtfExportEnabled: 'RTF', textExportEnabled: 'TXT',
            excelExportEnabled: 'XLSX', imageExportEnabled: 'IMG'
        };
        Ext.Object.each(fmtMap, function(fieldName, label) {
            var chk = me.form.down('[name=' + fieldName + ']');
            if (chk && chk.getValue()) fmtList.push(label);
        });

        Ext.MessageBox.wait('Validando reporte...', 'Por favor espere');
        Ext.Ajax.request({
            url: 'admin/reports/validate.action',
            method: 'POST',
            params: {
                query: queryField.getValue(),
                formats: fmtList.join(',')
            },
            success: function(fp) {
                Ext.MessageBox.hide();
                var res = Ext.decode(fp.responseText, true);
                var msgs = (res && res.messages) ? res.messages.join('<br/>') : 'Sin resultados';
                var title = (res && res.success) ? '\u2705 Validaci\u00f3n Exitosa' : '\u274c Errores de Validaci\u00f3n';
                Ext.Msg.alert(title, msgs);
            },
            failure: function() {
                Ext.MessageBox.hide();
                Ext.Msg.alert('Error', 'Error de comunicaci\u00f3n con el servidor al validar.');
            }
        });
    },

    onDeleteReport: function() {
        var me = this;
        var selected = me.grid.getSelectionModel().getSelection();
        if (selected.length === 0) return;
        var record = selected[0];

        Ext.Msg.confirm('Confirmar eliminaci\u00f3n', '\u00bfEst\u00e1 seguro que desea eliminar el reporte "' + record.get('name') + '"?', function(btn) {
            if (btn === 'yes') {
                Ext.MessageBox.wait('Eliminando reporte...', 'Por favor espere');
                Ext.Ajax.request({
                    url: 'admin/reports/delete.action',
                    method: 'POST',
                    params: { id: record.get('id') },
                    success: function(fp) {
                        Ext.MessageBox.hide();
                        var res = Ext.decode(fp.responseText, true);
                        if (res && res.success) {
                            Ext.Msg.alert('\u00c9xito', 'Reporte eliminado correctamente');
                            me.form.reset();
                            me.form.disable();
                            me.reportStore.load();
                        } else {
                            Ext.Msg.alert('Error', res.error || 'Error al eliminar el reporte');
                        }
                    },
                    failure: function() {
                        Ext.MessageBox.hide();
                        Ext.Msg.alert('Error', 'Error de comunicaci\u00f3n con el servidor');
                    }
                });
            }
        });
    },

    onUploadFile: function() {
        var me = this;
        var win = Ext.create('Ext.window.Window', {
            title: 'Subir Plantilla de Reporte (.jasper o .jrxml)',
            width: 450,
            layout: 'fit',
            modal: true,
            bodyPadding: 15,
            items: [
                {
                    xtype: 'form',
                    layout: 'anchor',
                    defaults: { anchor: '100%' },
                    items: [
                        {
                            xtype: 'filefield',
                            name: 'file',
                            fieldLabel: 'Archivo',
                            labelWidth: 60,
                            msgTarget: 'side',
                            allowBlank: false,
                            buttonText: 'Buscar...'
                        }
                    ],
                    buttons: [
                        {
                            text: 'Subir',
                            handler: function() {
                                var form = this.up('form').getForm();
                                if (form.isValid()) {
                                    form.submit({
                                        url: 'admin/reports/upload.action',
                                        waitMsg: 'Subiendo archivo...',
                                        success: function(fp, o) {
                                            var res = Ext.decode(o.response.responseText, true);
                                            Ext.Msg.alert('\u00c9xito', res.message || 'Archivo subido correctamente');
                                            me.fileStore.load();
                                            win.close();
                                        },
                                        failure: function(fp, o) {
                                            var res = Ext.decode(o.response.responseText, true) || {};
                                            Ext.Msg.alert('Error', res.error || 'Error al subir archivo');
                                        }
                                    });
                                }
                            }
                        },
                        {
                            text: 'Cancelar',
                            handler: function() {
                                win.close();
                            }
                        }
                    ]
                }
            ]
        });
        win.show();
    }
});
