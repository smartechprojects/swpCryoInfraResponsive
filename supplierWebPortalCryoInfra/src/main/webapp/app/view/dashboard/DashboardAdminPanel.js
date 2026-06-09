Ext.define('SupplierApp.view.dashboard.DashboardAdminPanel', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.dashboardAdminPanel',
    title: 'Administrador de Dashboards y KPIs',
    iconCls: 'x-fa fa-cogs',
    cls: 'custom-admin-tabs',
    bodyPadding: 5,

    initComponent: function() {
        var me = this;

        // Inyectar estilos CSS para mejorar la legibilidad y accesibilidad de los tabs
        if (!Ext.util.CSS.getRule('custom-admin-tabs')) {
            Ext.util.CSS.createStyleSheet(
                '.custom-admin-tabs .x-tab-bar, .custom-admin-tabs .x-tab-bar-default { background-color: #f8fafc !important; background-image: none !important; padding: 6px 6px 0 6px !important; border-bottom: 1px solid #e2e8f0 !important; } ' +
                '.custom-admin-tabs .x-tab, .custom-admin-tabs .x-tab-default { background-color: #e2e8f0 !important; background-image: none !important; border: 1px solid #cbd5e1 !important; border-bottom: none !important; margin-right: 4px !important; border-radius: 4px 4px 0 0 !important; opacity: 1 !important; box-shadow: none !important; } ' +
                '.custom-admin-tabs .x-tab-inner, .custom-admin-tabs .x-tab-inner-default { color: #475569 !important; font-size: 13px !important; font-family: Poppins-Regular, sans-serif !important; font-weight: 500 !important; opacity: 1 !important; } ' +
                '.custom-admin-tabs .x-tab-icon-el, .custom-admin-tabs .x-tab-icon-el-default { color: #475569 !important; } ' +
                '.custom-admin-tabs .x-tab-active, .custom-admin-tabs .x-tab-active-default { background-color: #00306E !important; background-image: none !important; border: 1px solid #00306E !important; border-bottom: none !important; opacity: 1 !important; } ' +
                '.custom-admin-tabs .x-tab-active .x-tab-inner, .custom-admin-tabs .x-tab-active-default .x-tab-inner-default { color: #ffffff !important; font-weight: bold !important; opacity: 1 !important; } ' +
                '.custom-admin-tabs .x-tab-active .x-tab-icon-el, .custom-admin-tabs .x-tab-active-default .x-tab-icon-el-default { color: #ffffff !important; } ' +
                '.custom-admin-tabs .x-tab-over, .custom-admin-tabs .x-tab-over-default { background-color: #cbd5e1 !important; background-image: none !important; border-color: #94a3b8 !important; opacity: 1 !important; } ' +
                '.custom-admin-tabs .x-tab-over .x-tab-inner, .custom-admin-tabs .x-tab-over-default .x-tab-inner-default { color: #1e293b !important; }',
                'custom-admin-tabs'
            );
        }

        // Modelos Locales
        Ext.define('AdminKpiModel', {
            extend: 'Ext.data.Model',
            fields: ['id', 'name', 'description', 'sqlQuery', 'kpiType', 'iconName', 'subtextTemplate', 'sortOrder', 'isActive', 'role', 'color']
        });

        Ext.define('AdminChartModel', {
            extend: 'Ext.data.Model',
            fields: ['id', 'name', 'description', 'chartQuery', 'chartType', 'xAxisLabel', 'yAxisLabel', 'showLegend', 'showTitle', 'role', 'colors']
        });

        Ext.define('AdminTableModel', {
            extend: 'Ext.data.Model',
            fields: ['id', 'name', 'description', 'sqlQuery', 'columnHeaders', 'columnKeys', 'columnFormats', 'sortOrder', 'isActive', 'role']
        });

        // Stores
        me.kpiStore = Ext.create('Ext.data.Store', {
            model: 'AdminKpiModel',
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: 'admin/dashboard/kpis/list.action',
                reader: { type: 'json', rootProperty: 'data' }
            }
        });

        me.chartStore = Ext.create('Ext.data.Store', {
            model: 'AdminChartModel',
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: 'admin/dashboard/charts/list.action',
                reader: { type: 'json', rootProperty: 'data' }
            }
        });

        me.tableStore = Ext.create('Ext.data.Store', {
            model: 'AdminTableModel',
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: 'admin/dashboard/tables/list.action',
                reader: { type: 'json', rootProperty: 'data' }
            }
        });

        me.layoutWidgetStore = Ext.create('Ext.data.Store', {
            fields: ['id', 'type', 'refId', 'name', 'colSpan', 'sortOrder']
        });

        me.iconStore = getUDCStore('KPI_ICONS', '', '', '');
        me.iconStore.load();

        // TABS DE COMPONENTES POR ROLES
        var kpisTab = me.createKpisTab();
        var chartsTab = me.createChartsTab();
        var tablesTab = me.createTablesTab();
        var layoutTab = me.createLayoutTab();

        Ext.apply(me, {
            items: [kpisTab, chartsTab, tablesTab, layoutTab]
        });

        me.callParent(arguments);
    },

    // --- TAB CREATORS ---
    createKpisTab: function() {
        var me = this;
        var form = Ext.create('Ext.form.Panel', {
            region: 'center',
            title: 'Detalle de KPI',
            bodyPadding: 15,
            disabled: true,
            scrollable: true,
            defaults: { anchor: '100%', margin: '0 0 12 0', labelAlign: 'top' },
            buttons: [
                {
                    text: 'Probar / Preview',
                    handler: function() {
                        var f = this.up('form');
                        var vals = f.getValues();
                        if (!vals.sqlQuery) {
                            Ext.Msg.alert('Error', 'Debe definir una consulta SQL.');
                            return;
                        }
                        Ext.Ajax.request({
                            url: 'admin/dashboard/kpis/preview.action',
                            method: 'POST',
                            params: {
                                sqlQuery: vals.sqlQuery,
                                kpiType: vals.kpiType,
                                subtextTemplate: vals.subtextTemplate
                            },
                            success: function(resp) {
                                var r = Ext.decode(resp.responseText, true);
                                if (r && r.success) {
                                    var color = vals.color || '#00306E';
                                    var icon = vals.iconName || 'fa-info-circle';
                                    
                                    // Función local para aclarar el color (gradiente)
                                    var lightenColor = function(hex, percent) {
                                        if (!hex || hex.indexOf('#') !== 0) return '#0050B3';
                                        try {
                                            var num = parseInt(hex.replace("#",""), 16),
                                                amt = Math.round(2.55 * percent),
                                                R = (num >> 16) + amt,
                                                G = (num >> 8 & 0x00FF) + amt,
                                                B = (num & 0x0000FF) + amt;
                                            return "#" + (0x1000000 + (R<255?R<0?0:R:255)*0x10000 + (G<255?G<0?0:G:255)*0x100 + (B<255?B<0?0:B:255)).toString(16).slice(1);
                                        } catch (e) {
                                            return '#0050B3';
                                        }
                                    };
                                    
                                    var bgGradient = 'linear-gradient(135deg, ' + color + ' 0%, ' + lightenColor(color, 20) + ' 100%)';
                                    
                                    var win = Ext.create('Ext.window.Window', {
                                        title: 'Vista Previa de KPI: ' + (vals.name || 'Sin Nombre'),
                                        width: 320,
                                        height: 180,
                                        modal: true,
                                        layout: 'fit',
                                        bodyPadding: 15,
                                        style: 'background-color: #ffffff;',
                                        items: [
                                            {
                                                xtype: 'panel',
                                                border: false,
                                                style: {
                                                    borderRadius: '8px',
                                                    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                                                    background: bgGradient
                                                },
                                                bodyStyle: {
                                                    background: 'transparent',
                                                    display: 'flex',
                                                    alignItems: 'center'
                                                },
                                                html: '<div style="display:flex; align-items:center; width:100%; color:#fff; font-family:Poppins-Regular, sans-serif; padding:10px;">' +
                                                      '  <div style="font-size:32px; margin-right:15px; opacity:0.8;"><i class="fa ' + icon + '"></i></div>' +
                                                      '  <div style="flex:1;">' +
                                                      '    <div style="font-size:11px; font-weight:300; opacity:0.9; text-transform:uppercase; letter-spacing:1px;">' + (vals.name || 'KPI') + '</div>' +
                                                      '    <div style="font-size:24px; font-weight:700; margin:2px 0;">' + r.value + '</div>' +
                                                      '    <div style="font-size:10px; opacity:0.8; font-weight:300;">' + r.subtext + '</div>' +
                                                      '  </div>' +
                                                      '</div>'
                                            }
                                        ]
                                    });
                                    win.show();
                                } else {
                                    Ext.Msg.alert('Error de Preview', r.error || 'Error al ejecutar SQL');
                                }
                            }
                        });
                    }
                },
                {
                    text: 'Guardar',
                    formBind: true,
                    cls: 'buttonStyle',
                    handler: function() {
                        var f = this.up('form');
                        if (!f.isValid()) return;
                        var vals = f.getValues();
                        var data = Ext.clone(vals);
                        data.isActive = (vals.isActive === 'on' || vals.isActive === 'true' || vals.isActive === true);
                        data.sortOrder = parseInt(vals.sortOrder || 1);
                        if (!data.id) delete data.id;

                        // Multi-rol: unir los roles seleccionados con comas
                        var roleField = f.down('[name=role]');
                        if (roleField) {
                            var roleVal = roleField.getValue();
                            if (Ext.isArray(roleVal)) {
                                data.role = roleVal.join(',');
                            } else {
                                data.role = roleVal || '';
                            }
                        }

                        Ext.Ajax.request({
                            url: 'admin/dashboard/kpis/save.action',
                            method: 'POST',
                            jsonData: Ext.encode(data),
                            success: function() {
                                Ext.Msg.alert('\u00c9xito', 'KPI guardado correctamente');
                                me.kpiStore.load();
                            }
                        });
                    }
                },
                {
                    text: 'Eliminar',
                    cls: 'deleteStyle',
                    style: 'background:#cc0000; color:white; border-color:#cc0000;',
                    handler: function() {
                        var grid = this.up('panel').up('panel').down('grid');
                        var sel = grid.getSelectionModel().getSelection();
                        if (sel.length === 0) return;
                        Ext.Msg.confirm('Eliminar', '\u00bfEliminar este KPI?', function(btn) {
                            if (btn === 'yes') {
                                Ext.Ajax.request({
                                    url: 'admin/dashboard/kpis/delete.action',
                                    method: 'POST',
                                    params: { id: sel[0].get('id') },
                                    success: function() {
                                        me.kpiStore.load();
                                        form.reset();
                                        form.disable();
                                    }
                                });
                            }
                        });
                    }
                }
            ],
            items: [
                { xtype: 'hiddenfield', name: 'id' },
                { xtype: 'textfield', name: 'name', fieldLabel: 'Nombre KPI', allowBlank: false },
                { xtype: 'textfield', name: 'description', fieldLabel: 'Descripci\u00f3n' },
                {
                    xtype: 'tagfield',
                    name: 'role',
                    fieldLabel: 'Rol(es) Autorizado(s)',
                    store: getUDCStore('ROLES', '', '', ''),
                    displayField: 'strValue1',
                    valueField: 'strValue1',
                    triggerAction: 'all',
                    allowBlank: false,
                    forceSelection: true,
                    emptyText: 'Seleccionar Rol(es)...',
                    multiSelect: true
                },
                {
                    xtype: 'combobox',
                    name: 'kpiType',
                    fieldLabel: 'Tipo de Formato',
                    store: ['NUMBER', 'CURRENCY', 'PERCENTAGE', 'TEXT'],
                    allowBlank: false
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: 'Icono FontAwesome',
                    layout: 'hbox',
                    defaults: { margin: '0 10 0 0' },
                    items: [
                        {
                            xtype: 'combobox',
                            name: 'iconName',
                            itemId: 'iconComboField',
                            flex: 1,
                            store: me.iconStore,
                            displayField: 'strValue2',
                            valueField: 'udcKey',
                            triggerAction: 'all',
                            queryMode: 'local',
                            emptyText: 'Seleccionar Icono...',
                            forceSelection: false,
                            tpl: Ext.create('Ext.XTemplate',
                                '<ul class="x-list-plain"><tpl for=".">',
                                    '<li role="option" class="x-boundlist-item" style="padding: 6px 10px; border-bottom: 1px solid #f0f0f0; display: flex; align-items: center;">',
                                        '<span style="font-size: 18px; width: 30px; display: inline-block; text-align: center;"><i class="fa {udcKey}"></i></span>',
                                        '<span style="font-size: 13px; font-weight: 500; color: #333;">{strValue2} ({udcKey})</span>',
                                    '</li>',
                                '</tpl></ul>'
                            ),
                            listeners: {
                                change: function(combo, newVal) {
                                    var preview = combo.up('fieldcontainer').down('#iconPreviewCmp');
                                    if (newVal) {
                                        preview.setHtml('<div style="font-size: 20px; color: #00306E;"><i class="fa ' + newVal + '"></i></div>');
                                    } else {
                                        preview.setHtml('<div style="font-size: 20px; color: #ccc;"><i class="fa fa-info-circle"></i></div>');
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'component',
                            itemId: 'iconPreviewCmp',
                            width: 40,
                            style: 'display: flex; align-items: center; justify-content: center; background: #f5f5f5; border: 1px solid #ccc; border-radius: 4px; height: 32px;',
                            html: '<div style="font-size: 20px; color: #ccc;"><i class="fa fa-info-circle"></i></div>'
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: 'Color de Fondo (HEX)',
                    layout: 'hbox',
                    defaults: { margin: '0 10 0 0' },
                    items: [
                        {
                            xtype: 'textfield',
                            name: 'color',
                            itemId: 'colorTextField',
                            flex: 1,
                            value: '#00306E',
                            emptyText: 'e.g. #00306E',
                            regex: /^#[0-9A-F]{6}$/i,
                            regexText: 'Formato HEX inválido (debe comenzar con # seguido de 6 caracteres hexadecimales)',
                            listeners: {
                                change: function(field, newVal) {
                                    var picker = field.up('fieldcontainer').down('#colorPickerField');
                                    if (newVal && /^#[0-9A-F]{6}$/i.test(newVal)) {
                                        picker.setValue(newVal);
                                    } else if (!newVal) {
                                        picker.setValue('#00306E');
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'textfield',
                            inputType: 'color',
                            itemId: 'colorPickerField',
                            value: '#00306E',
                            width: 50,
                            height: 32,
                            submitValue: false,
                            style: 'padding: 0; border: none; cursor: pointer; background: transparent;',
                            listeners: {
                                change: function(picker, newVal) {
                                    var textfield = picker.up('fieldcontainer').down('#colorTextField');
                                    if (textfield.getValue() !== newVal) {
                                        textfield.setValue(newVal);
                                    }
                                }
                            }
                        }
                    ]
                },
                { xtype: 'textfield', name: 'subtextTemplate', fieldLabel: 'Plantilla de Subtexto', emptyText: 'e.g. Total anterior {prev_value}' },
                { xtype: 'numberfield', name: 'sortOrder', fieldLabel: 'Orden', value: 1 },
                { xtype: 'checkbox', name: 'isActive', boxLabel: 'Activo', checked: true },
                {
                    xtype: 'textarea',
                    name: 'sqlQuery',
                    fieldLabel: 'Consulta SQL',
                    height: 100,
                    allowBlank: false,
                    emptyText: 'SELECT val as value FROM table_name ...'
                }
            ]
        });

        var grid = Ext.create('Ext.grid.Panel', {
            region: 'west',
            width: 360,
            split: true,
            store: me.kpiStore,
            columns: [
                { text: 'Nombre KPI', dataIndex: 'name', width: 140 },
                {
                    text: 'Rol(es)',
                    dataIndex: 'role',
                    width: 140,
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
                { text: 'Formato', dataIndex: 'kpiType', width: 80 }
            ],
            tbar: [
                {
                    text: 'Nuevo KPI',
                    iconCls: 'x-fa fa-plus',
                    handler: function() {
                        grid.getSelectionModel().deselectAll();
                        form.reset();
                        form.enable();
                        var roleField = form.down('[name=role]');
                        if (roleField) roleField.setValue([]);
                    }
                }
            ],
            listeners: {
                selectionchange: function(sm, sel) {
                    if (sel.length > 0) {
                        form.enable();
                        form.loadRecord(sel[0]);
                        // Multi-rol: splitear el string de roles en un array
                        var roleField = form.down('[name=role]');
                        if (roleField) {
                            var rolesStr = sel[0].get('role');
                            if (rolesStr) {
                                roleField.setValue(rolesStr.split(','));
                            } else {
                                roleField.setValue([]);
                            }
                        }
                    } else {
                        form.disable();
                        form.reset();
                        var roleField = form.down('[name=role]');
                        if (roleField) roleField.setValue([]);
                    }
                }
            }
        });

        return {
            title: 'KPIs',
            iconCls: 'x-fa fa-key',
            xtype: 'panel',
            layout: 'border',
            items: [grid, form]
        };
    },

    createChartsTab: function() {
        var me = this;
        var form = Ext.create('Ext.form.Panel', {
            region: 'center',
            title: 'Detalle del Gr\u00e1fico',
            bodyPadding: 15,
            disabled: true,
            scrollable: true,
            defaults: { anchor: '100%', margin: '0 0 12 0', labelAlign: 'top' },
            buttons: [
                {
                    text: 'Probar / Preview',
                    iconCls: 'x-fa fa-eye',
                    handler: function() {
                        var f = this.up('form');
                        var vals = f.getValues();
                        if (!vals.chartQuery) {
                            Ext.Msg.alert('Error', 'Debe definir una consulta SQL.');
                            return;
                        }
                        
                        Ext.MessageBox.wait('Ejecutando SQL y cargando gr\u00e1fico...', 'Espere por favor');
                        
                        Ext.Ajax.request({
                            url: 'admin/dashboard/charts/preview.action',
                            method: 'POST',
                            params: {
                                chartQuery: vals.chartQuery,
                                chartTypeStr: vals.chartTypeStr,
                                xAxisLabel: vals.xAxisLabel || '',
                                yAxisLabel: vals.yAxisLabel || '',
                                showLegend: (vals.showLegend === 'on' || vals.showLegend === 'true' || vals.showLegend === true),
                                showTitle: (vals.showTitle === 'on' || vals.showTitle === 'true' || vals.showTitle === true),
                                colors: vals.colors || '',
                                description: vals.description || ''
                            },
                            success: function(resp) {
                                Ext.MessageBox.hide();
                                var r = Ext.decode(resp.responseText, true);
                                if (r && r.success && r.data) {
                                    var chartData = r.data;
                                    var labels = chartData.labels || [];
                                    var datasets = chartData.datasets || [];
                                    var currentColorsJsonStr = f.down('[name=colors]').getValue() || '';
                                    
                                    var colorMap = {};
                                    if (currentColorsJsonStr && currentColorsJsonStr.indexOf('{') === 0) {
                                        try {
                                            colorMap = Ext.decode(currentColorsJsonStr);
                                        } catch(e) {}
                                    }
                                    
                                    var keys = me.getColorKeys(labels, datasets, vals.chartTypeStr);
                                    me.renderColorPickers(f, keys, colorMap);
                                    
                                    var latestColors = f.down('[name=colors]').getValue() || '{}';
                                    var latestColorMap = {};
                                    try {
                                        latestColorMap = Ext.decode(latestColors);
                                    } catch(e) {}

                                    var canvasId = 'chart-preview-canvas-' + Ext.id();
                                    var win = Ext.create('Ext.window.Window', {
                                        title: 'Vista Previa de Gr\u00e1fico: ' + vals.name,
                                        width: 600,
                                        height: 400,
                                        modal: true,
                                        layout: 'fit',
                                        bodyPadding: 15,
                                        style: 'background-color: #ffffff;',
                                        html: '<div style="width:100%; height:100%;"><canvas id="' + canvasId + '"></canvas></div>',
                                        listeners: {
                                            afterrender: function() {
                                                Ext.defer(function() {
                                                    var canvas = document.getElementById(canvasId);
                                                    if (!canvas) return;
                                                    
                                                    var chartType = 'bar';
                                                    if (chartData.chartType === 'LINE') chartType = 'line';
                                                    else if (chartData.chartType === 'PIE') chartType = 'pie';
                                                    
                                                    var defaultPalette = [
                                                        '#00306E', '#0050B3', '#1890FF', '#52C41A', '#FAAD14', '#F5222D', '#722ED1', '#13C2C2', '#EB2F96', '#2F54EB'
                                                    ];
                                                    
                                                    var chartJsDatasets = [];
                                                    if (datasets.length === 1 || chartType === 'pie') {
                                                        var bgColors = [];
                                                        var borderColors = [];
                                                        for (var l = 0; l < labels.length; l++) {
                                                            var label = labels[l];
                                                            var col = latestColorMap[label] || defaultPalette[l % defaultPalette.length];
                                                            bgColors.push(col);
                                                            borderColors.push(col);
                                                        }
                                                        
                                                        chartJsDatasets.push({
                                                            label: datasets[0] ? (datasets[0].label || 'Cantidad') : 'Cantidad',
                                                            data: datasets[0] ? (datasets[0].data || []) : [],
                                                            backgroundColor: bgColors,
                                                            borderColor: chartType === 'pie' ? '#ffffff' : borderColors,
                                                            borderWidth: 1.5,
                                                            fill: chartType === 'line' ? 'origin' : false,
                                                            tension: 0.3
                                                        });
                                                    } else {
                                                        for (var d = 0; d < datasets.length; d++) {
                                                            var dsLabel = datasets[d].label || ('Serie ' + (d + 1));
                                                            var col = latestColorMap[dsLabel] || defaultPalette[d % defaultPalette.length];
                                                            
                                                            chartJsDatasets.push({
                                                                label: dsLabel,
                                                                data: datasets[d].data || [],
                                                                backgroundColor: col,
                                                                borderColor: col,
                                                                borderWidth: 1.5,
                                                                fill: chartType === 'line' ? 'origin' : false,
                                                                tension: 0.3
                                                            });
                                                        }
                                                    }
                                                    
                                                    try {
                                                        new Chart(canvas.getContext('2d'), {
                                                            type: chartType,
                                                            data: {
                                                                labels: labels,
                                                                datasets: chartJsDatasets
                                                            },
                                                            options: {
                                                                responsive: true,
                                                                maintainAspectRatio: false,
                                                                plugins: {
                                                                    legend: { display: chartData.showLegend !== false, position: 'bottom' },
                                                                    title: { display: chartData.showTitle === true && chartData.description, text: chartData.description }
                                                                },
                                                                scales: chartType === 'pie' ? {} : {
                                                                    x: { title: { display: !!chartData.xAxisLabel, text: chartData.xAxisLabel } },
                                                                    y: { beginAtZero: true, title: { display: !!chartData.yAxisLabel, text: chartData.yAxisLabel } }
                                                                }
                                                            }
                                                        });
                                                    } catch (e) {
                                                        console.error(e);
                                                    }
                                                }, 100);
                                            }
                                        }
                                    });
                                    win.show();
                                } else {
                                    Ext.Msg.alert('Error de Preview', r.error || 'Error al ejecutar consulta SQL');
                                }
                            },
                            failure: function() {
                                Ext.MessageBox.hide();
                                Ext.Msg.alert('Error', 'Fallo de red al conectar al servidor');
                            }
                        });
                    }
                },
                {
                    text: 'Guardar',
                    iconCls: 'x-fa fa-save',
                    formBind: true,
                    cls: 'buttonStyle',
                    handler: function() {
                        var f = this.up('form');
                        if (!f.isValid()) return;
                        var vals = f.getValues();
                        var data = Ext.clone(vals);
                        data.showLegend = (vals.showLegend === 'on' || vals.showLegend === 'true' || vals.showLegend === true);
                        data.showTitle = (vals.showTitle === 'on' || vals.showTitle === 'true' || vals.showTitle === true);
                        data.width = 400;
                        data.height = 300;
                        data.chartType = 1;
                        if (vals.chartTypeStr === "LINE") data.chartType = 2;
                        if (vals.chartTypeStr === "PIE") data.chartType = 3;
                        
                        if (!data.id) delete data.id;

                        // Multi-rol: unir los roles seleccionados con comas
                        var roleField = f.down('[name=role]');
                        if (roleField) {
                            var roleVal = roleField.getValue();
                            if (Ext.isArray(roleVal)) {
                                data.role = roleVal.join(',');
                            } else {
                                data.role = roleVal || '';
                            }
                        }

                        Ext.Ajax.request({
                            url: 'admin/dashboard/charts/save.action',
                            method: 'POST',
                            jsonData: Ext.encode(data),
                            success: function() {
                                Ext.Msg.alert('\u00c9xito', 'Gr\u00e1fico guardado correctamente');
                                me.chartStore.load();
                            }
                        });
                    }
                },
                {
                    text: 'Eliminar',
                    iconCls: 'x-fa fa-trash',
                    cls: 'deleteStyle',
                    style: 'background:#cc0000; color:white; border-color:#cc0000;',
                    handler: function() {
                        var grid = this.up('panel').up('panel').down('grid');
                        var sel = grid.getSelectionModel().getSelection();
                        if (sel.length === 0) return;
                        Ext.Msg.confirm('Eliminar', '\u00bfEliminar este gr\u00e1fico?', function(btn) {
                            if (btn === 'yes') {
                                Ext.Ajax.request({
                                    url: 'admin/dashboard/charts/delete.action',
                                    method: 'POST',
                                    params: { id: sel[0].get('id') },
                                    success: function() {
                                        me.chartStore.load();
                                        form.reset();
                                        form.disable();
                                    }
                                });
                            }
                        });
                    }
                }
            ],
            items: [
                { xtype: 'hiddenfield', name: 'id' },
                { xtype: 'textfield', name: 'name', fieldLabel: 'Nombre Gr\u00e1fico', allowBlank: false },
                { xtype: 'textfield', name: 'description', fieldLabel: 'Descripci\u00f3n' },
                {
                    xtype: 'tagfield',
                    name: 'role',
                    fieldLabel: 'Rol(es) Autorizado(s)',
                    store: getUDCStore('ROLES', '', '', ''),
                    displayField: 'strValue1',
                    valueField: 'strValue1',
                    triggerAction: 'all',
                    allowBlank: false,
                    forceSelection: true,
                    emptyText: 'Seleccionar Rol(es)...',
                    multiSelect: true
                },
                {
                    xtype: 'combobox',
                    name: 'chartTypeStr',
                    fieldLabel: 'Tipo de Gr\u00e1fico',
                    store: ['BAR', 'LINE', 'PIE'],
                    value: 'BAR',
                    allowBlank: false
                },
                { xtype: 'textfield', name: 'xAxisLabel', fieldLabel: 'Etiqueta Eje X' },
                { xtype: 'textfield', name: 'yAxisLabel', fieldLabel: 'Etiqueta Eje Y' },
                { xtype: 'checkbox', name: 'showTitle', boxLabel: 'Mostrar T\u00edtulo', checked: true },
                { xtype: 'checkbox', name: 'showLegend', boxLabel: 'Mostrar Leyenda', checked: true },
                {
                    xtype: 'hiddenfield',
                    name: 'colors'
                },
                {
                    xtype: 'fieldset',
                    title: 'Configuraci\u00f3n de Colores por Categor\u00eda/Serie',
                    itemId: 'chartColorsFieldSet',
                    layout: 'anchor',
                    items: [
                        {
                            xtype: 'container',
                            itemId: 'chartColorsContainer',
                            layout: 'anchor',
                            defaults: { anchor: '100%' }
                        }
                    ]
                },
                {
                    xtype: 'textarea',
                    name: 'chartQuery',
                    fieldLabel: 'Consulta SQL',
                    height: 100,
                    allowBlank: false,
                    emptyText: 'La primera columna es la etiqueta (label), las siguientes deben ser num\u00e9ricas.'
                }
            ]
        });

        var grid = Ext.create('Ext.grid.Panel', {
            region: 'west',
            width: 360,
            split: true,
            store: me.chartStore,
            columns: [
                { text: 'Gr\u00e1fico', dataIndex: 'name', width: 140 },
                {
                    text: 'Rol(es)',
                    dataIndex: 'role',
                    width: 140,
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
                { text: 'Tipo', dataIndex: 'chartType', width: 80, renderer: function(v) { return v === 3 ? 'PIE' : (v === 2 ? 'LINE' : 'BAR'); } }
            ],
            tbar: [
                {
                    text: 'Nuevo Gr\u00e1fico',
                    iconCls: 'x-fa fa-plus',
                    handler: function() {
                        grid.getSelectionModel().deselectAll();
                        form.reset();
                        form.enable();
                        var container = form.down('#chartColorsContainer');
                        if (container) container.removeAll(true);
                        var roleField = form.down('[name=role]');
                        if (roleField) roleField.setValue([]);
                    }
                }
            ],
            listeners: {
                itemdblclick: function(grid, rec) {
                    form.loadRecord(rec);
                },
                selectionchange: function(sm, sel) {
                    if (sel.length > 0) {
                        form.enable();
                        var rec = sel[0];
                        form.loadRecord(rec);
                        var typeStr = 'BAR';
                        if (rec.get('chartType') === 2) typeStr = 'LINE';
                        if (rec.get('chartType') === 3) typeStr = 'PIE';
                        form.down('[name=chartTypeStr]').setValue(typeStr);
                        
                        me.loadChartColorPickers(form, rec);

                        // Multi-rol: splitear el string de roles en un array
                        var roleField = form.down('[name=role]');
                        if (roleField) {
                            var rolesStr = rec.get('role');
                            if (rolesStr) {
                                roleField.setValue(rolesStr.split(','));
                            } else {
                                roleField.setValue([]);
                            }
                        }
                    } else {
                        form.disable();
                        form.reset();
                        var container = form.down('#chartColorsContainer');
                        if (container) container.removeAll(true);
                        var roleField = form.down('[name=role]');
                        if (roleField) roleField.setValue([]);
                    }
                }
            }
        });

        return {
            title: 'Gr\u00e1ficos',
            iconCls: 'x-fa fa-line-chart',
            xtype: 'panel',
            layout: 'border',
            items: [grid, form]
        };
    },

    createTablesTab: function() {
        var me = this;
        var form = Ext.create('Ext.form.Panel', {
            region: 'center',
            title: 'Detalle de Tabla',
            bodyPadding: 15,
            disabled: true,
            scrollable: true,
            defaults: { anchor: '100%', margin: '0 0 12 0', labelAlign: 'top' },
            buttons: [
                {
                    text: 'Probar / Preview',
                    handler: function() {
                        var f = this.up('form');
                        var vals = f.getValues();
                        if (!vals.sqlQuery) {
                            Ext.Msg.alert('Error', 'Debe definir una consulta SQL.');
                            return;
                        }
                        if (!vals.columnHeaders || !vals.columnKeys || !vals.columnFormats) {
                            Ext.Msg.alert('Error', 'Debe definir Encabezados, Campos BD y Formatos.');
                            return;
                        }
                        
                        Ext.MessageBox.wait('Ejecutando SQL y cargando tabla...', 'Espere por favor');
                        
                        Ext.Ajax.request({
                            url: 'admin/dashboard/tables/preview.action',
                            method: 'POST',
                            params: {
                                sqlQuery: vals.sqlQuery,
                                columnHeaders: vals.columnHeaders,
                                columnKeys: vals.columnKeys,
                                columnFormats: vals.columnFormats,
                                description: vals.description || ''
                            },
                            success: function(resp) {
                                Ext.MessageBox.hide();
                                var r = Ext.decode(resp.responseText, true);
                                if (r && r.success && r.data) {
                                    var tableData = r.data;
                                    var headers = tableData.headers || [];
                                    var keys = tableData.keys || [];
                                    var rows = tableData.rows || [];
                                    
                                    var columns = [];
                                    for (var i = 0; i < headers.length; i++) {
                                        columns.push({
                                            text: headers[i],
                                            dataIndex: keys[i],
                                            flex: 1
                                        });
                                    }
                                    
                                    var store = Ext.create('Ext.data.Store', {
                                        fields: keys,
                                        data: rows
                                    });
                                    
                                    var win = Ext.create('Ext.window.Window', {
                                        title: 'Vista Previa de Tabla: ' + vals.name,
                                        width: 700,
                                        height: 400,
                                        modal: true,
                                        layout: 'fit',
                                        items: [
                                            {
                                                xtype: 'grid',
                                                store: store,
                                                columns: columns,
                                                forceFit: true,
                                                viewConfig: { stripeRows: true }
                                            }
                                        ]
                                    });
                                    win.show();
                                } else {
                                    Ext.Msg.alert('Error de Preview', r.error || 'Error al ejecutar consulta SQL');
                                }
                            },
                            failure: function() {
                                Ext.MessageBox.hide();
                                Ext.Msg.alert('Error', 'Fallo de red al conectar al servidor');
                            }
                        });
                    }
                },
                {
                    text: 'Guardar',
                    formBind: true,
                    cls: 'buttonStyle',
                    handler: function() {
                        var f = this.up('form');
                        if (!f.isValid()) return;
                        var vals = f.getValues();
                        var data = Ext.clone(vals);
                        data.isActive = (vals.isActive === 'on' || vals.isActive === 'true' || vals.isActive === true);
                        data.sortOrder = parseInt(vals.sortOrder || 1);
                        if (!data.id) delete data.id;

                        // Multi-rol: unir los roles seleccionados con comas
                        var roleField = f.down('[name=role]');
                        if (roleField) {
                            var roleVal = roleField.getValue();
                            if (Ext.isArray(roleVal)) {
                                data.role = roleVal.join(',');
                            } else {
                                data.role = roleVal || '';
                            }
                        }

                        Ext.Ajax.request({
                            url: 'admin/dashboard/tables/save.action',
                            method: 'POST',
                            jsonData: Ext.encode(data),
                            success: function() {
                                Ext.Msg.alert('\u00c9xito', 'Tabla guardada correctamente');
                                me.tableStore.load();
                            }
                        });
                    }
                },
                {
                    text: 'Eliminar',
                    cls: 'deleteStyle',
                    style: 'background:#cc0000; color:white; border-color:#cc0000;',
                    handler: function() {
                        var grid = this.up('panel').up('panel').down('grid');
                        var sel = grid.getSelectionModel().getSelection();
                        if (sel.length === 0) return;
                        Ext.Msg.confirm('Eliminar', '\u00bfEliminar esta tabla?', function(btn) {
                            if (btn === 'yes') {
                                Ext.Ajax.request({
                                    url: 'admin/dashboard/tables/delete.action',
                                    method: 'POST',
                                    params: { id: sel[0].get('id') },
                                    success: function() {
                                        me.tableStore.load();
                                        form.reset();
                                        form.disable();
                                    }
                                });
                            }
                        });
                    }
                }
            ],
            items: [
                { xtype: 'hiddenfield', name: 'id' },
                { xtype: 'textfield', name: 'name', fieldLabel: 'Nombre Tabla', allowBlank: false },
                { xtype: 'textfield', name: 'description', fieldLabel: 'Descripci\u00f3n' },
                {
                    xtype: 'tagfield',
                    name: 'role',
                    fieldLabel: 'Rol(es) Autorizado(s)',
                    store: getUDCStore('ROLES', '', '', ''),
                    displayField: 'strValue1',
                    valueField: 'strValue1',
                    triggerAction: 'all',
                    allowBlank: false,
                    forceSelection: true,
                    emptyText: 'Seleccionar Rol(es)...',
                    multiSelect: true
                },
                { xtype: 'hiddenfield', name: 'columnHeaders' },
                { xtype: 'hiddenfield', name: 'columnKeys' },
                { xtype: 'hiddenfield', name: 'columnFormats' },
                {
                    xtype: 'fieldset',
                    title: 'Configuraci\u00f3n de Columnas',
                    itemId: 'tableColumnsFieldSet',
                    layout: 'anchor',
                    items: [
                        {
                            xtype: 'button',
                            text: 'Analizar Columnas de Query',
                            iconCls: 'x-fa fa-search',
                            margin: '0 0 10 0',
                            handler: function(btn) {
                                var formPanel = btn.up('form');
                                var sqlQuery = formPanel.down('[name=sqlQuery]').getValue();
                                if (!sqlQuery) {
                                    Ext.Msg.alert('Error', 'Ingrese una consulta SQL primero');
                                    return;
                                }
                                Ext.MessageBox.wait('Analizando query...', 'Por favor espere');
                                Ext.Ajax.request({
                                    url: 'admin/dashboard/tables/analyze.action',
                                    method: 'POST',
                                    params: { sqlQuery: sqlQuery },
                                    success: function(resp) {
                                        Ext.MessageBox.hide();
                                        var res = Ext.decode(resp.responseText, true);
                                        if (res && res.success && res.columns) {
                                            var keys = [];
                                            var headers = [];
                                            var formats = [];
                                            for (var i = 0; i < res.columns.length; i++) {
                                                keys.push(res.columns[i].key);
                                                headers.push(res.columns[i].key);
                                                formats.push(res.columns[i].type || 'TEXT');
                                            }
                                            formPanel.down('[name=columnKeys]').setValue(keys.join(','));
                                            formPanel.down('[name=columnHeaders]').setValue(headers.join(','));
                                            formPanel.down('[name=columnFormats]').setValue(formats.join(','));
                                            me.renderTableColumnConfigs(formPanel, keys, headers, formats);
                                        } else {
                                            Ext.Msg.alert('Error', res.error || 'No se pudo analizar la consulta');
                                        }
                                    },
                                    failure: function() {
                                        Ext.MessageBox.hide();
                                        Ext.Msg.alert('Error', 'Error de red al analizar consulta');
                                    }
                                });
                            }
                        },
                        {
                            xtype: 'container',
                            itemId: 'columnsContainer',
                            layout: 'anchor',
                            defaults: { anchor: '100%' }
                        }
                    ]
                },
                { xtype: 'numberfield', name: 'sortOrder', fieldLabel: 'Orden', value: 1 },
                { xtype: 'checkbox', name: 'isActive', boxLabel: 'Activa', checked: true },
                {
                    xtype: 'textarea',
                    name: 'sqlQuery',
                    fieldLabel: 'Consulta SQL',
                    height: 100,
                    allowBlank: false,
                    emptyText: 'SELECT num_fact, rfc, total FROM invoices LIMIT 10'
                }
            ]
        });

        var grid = Ext.create('Ext.grid.Panel', {
            region: 'west',
            width: 360,
            split: true,
            store: me.tableStore,
            columns: [
                { text: 'Tabla', dataIndex: 'name', width: 140 },
                {
                    text: 'Rol(es)',
                    dataIndex: 'role',
                    width: 140,
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
            tbar: [
                {
                    text: 'Nueva Tabla',
                    iconCls: 'x-fa fa-plus',
                    handler: function() {
                        grid.getSelectionModel().deselectAll();
                        form.reset();
                        form.enable();
                        var colContainer = form.down('#columnsContainer');
                        if (colContainer) colContainer.removeAll(true);
                        var roleField = form.down('[name=role]');
                        if (roleField) roleField.setValue([]);
                    }
                }
            ],
            listeners: {
                selectionchange: function(sm, sel) {
                    if (sel.length > 0) {
                        form.enable();
                        var rec = sel[0];
                        form.loadRecord(rec);
                        var keysStr = rec.get('columnKeys') || '';
                        var headersStr = rec.get('columnHeaders') || '';
                        var formatsStr = rec.get('columnFormats') || '';
                        var keys = keysStr ? keysStr.split(',').map(function(s){return s.trim();}) : [];
                        var headers = headersStr ? headersStr.split(',').map(function(s){return s.trim();}) : [];
                        var formats = formatsStr ? formatsStr.split(',').map(function(s){return s.trim();}) : [];
                        
                        for (var i = 0; i < keys.length; i++) {
                            if (i >= headers.length) headers.push(keys[i]);
                            if (i >= formats.length) formats.push('TEXT');
                        }
                        me.renderTableColumnConfigs(form, keys, headers, formats);

                        // Multi-rol: splitear el string de roles en un array
                        var roleField = form.down('[name=role]');
                        if (roleField) {
                            var rolesStr = rec.get('role');
                            if (rolesStr) {
                                roleField.setValue(rolesStr.split(','));
                            } else {
                                roleField.setValue([]);
                            }
                        }
                    } else {
                        form.disable();
                        form.reset();
                        var colContainer = form.down('#columnsContainer');
                        if (colContainer) colContainer.removeAll(true);
                        var roleField = form.down('[name=role]');
                        if (roleField) roleField.setValue([]);
                    }
                }
            }
        });

        return {
            title: 'Tablas',
            iconCls: 'x-fa fa-table',
            xtype: 'panel',
            layout: 'border',
            items: [grid, form]
        };
    },

    createLayoutTab: function() {
        var me = this;
        var activeRole = null;

        var widgetGrid = Ext.create('Ext.grid.Panel', {
            region: 'center',
            title: 'Elementos en Pantalla',
            store: me.layoutWidgetStore,
            disabled: true,
            tbar: [
                {
                    text: 'Agregar Componente',
                    iconCls: 'x-fa fa-plus',
                    handler: function() {
                        if (!activeRole) return;
                        me.showAddWidgetWindow();
                    }
                },
                {
                    text: 'Subir Orden',
                    iconCls: 'x-fa fa-arrow-up',
                    handler: function() {
                        var sel = widgetGrid.getSelectionModel().getSelection();
                        if (sel.length === 0) return;
                        var idx = me.layoutWidgetStore.indexOf(sel[0]);
                        if (idx > 0) {
                            me.layoutWidgetStore.remove(sel[0], true);
                            me.layoutWidgetStore.insert(idx - 1, sel[0]);
                            widgetGrid.getSelectionModel().select(idx - 1);
                        }
                    }
                },
                {
                    text: 'Bajar Orden',
                    iconCls: 'x-fa fa-arrow-down',
                    handler: function() {
                        var sel = widgetGrid.getSelectionModel().getSelection();
                        if (sel.length === 0) return;
                        var idx = me.layoutWidgetStore.indexOf(sel[0]);
                        if (idx < me.layoutWidgetStore.getCount() - 1) {
                            me.layoutWidgetStore.remove(sel[0], true);
                            me.layoutWidgetStore.insert(idx + 1, sel[0]);
                            widgetGrid.getSelectionModel().select(idx + 1);
                        }
                    }
                },
                {
                    text: 'Eliminar del Dashboard',
                    iconCls: 'x-fa fa-trash',
                    handler: function() {
                        var sel = widgetGrid.getSelectionModel().getSelection();
                        if (sel.length > 0) {
                            me.layoutWidgetStore.remove(sel[0]);
                        }
                    }
                },
                '->',
                {
                    text: 'Guardar Dise\u00f1o',
                    iconCls: 'x-fa fa-save',
                    cls: 'buttonStyle',
                    handler: function() {
                        if (!activeRole) return;
                        var widgets = [];
                        me.layoutWidgetStore.each(function(rec) {
                            widgets.push({
                                type: rec.get('type'),
                                refId: rec.get('refId'),
                                colSpan: rec.get('colSpan')
                            });
                        });
                        Ext.MessageBox.wait('Guardando dise\u00f1o...', 'Espere un momento');
                        Ext.Ajax.request({
                            url: 'dashboard/layout/save.action',
                            method: 'POST',
                            params: { role: activeRole },
                            jsonData: Ext.encode(widgets),
                            success: function(resp) {
                                Ext.MessageBox.hide();
                                var r = Ext.decode(resp.responseText, true);
                                if (r && r.success) {
                                    Ext.Msg.alert('\u00c9xito', 'Dise\u00f1o del Dashboard guardado');
                                } else {
                                    Ext.Msg.alert('Error', r.error || 'Error al guardar el layout');
                                }
                            },
                            failure: function() {
                                Ext.MessageBox.hide();
                                Ext.Msg.alert('Error', 'Fallo de red');
                            }
                        });
                    }
                }
            ],
            listeners: {
                itemdblclick: function(view, record) {
                    me.showEditColSpanWindow(record);
                }
            },
            columns: [
                { text: 'Tipo Componente', dataIndex: 'type', width: 120 },
                { text: 'Referencia / Nombre', dataIndex: 'name', width: 250 },
                {
                    text: 'Columnas (1 a 4)',
                    dataIndex: 'colSpan',
                    width: 150,
                    renderer: function(v) {
                        return v + ' (' + (v * 25) + '% Ancho)';
                    }
                }
            ]
        });

        var roleStore = getUDCStore('ROLES', '', '', '');
        roleStore.load();

        var roleGrid = Ext.create('Ext.grid.Panel', {
            region: 'west',
            width: 250,
            split: true,
            title: 'Roles de Usuario',
            store: roleStore,
            columns: [
                { text: 'Rol', dataIndex: 'strValue1', width: 220 }
            ],
            listeners: {
                selectionchange: function(sm, sel) {
                    if (sel.length > 0) {
                        activeRole = sel[0].get('strValue1');
                        me.activeRole = activeRole; // Guardar en la instancia
                        widgetGrid.enable();
                        // Cargar widgets actuales del Rol (usamos activeRole como tenantId temporal en el GET)
                        Ext.Ajax.request({
                            url: 'dashboard/metrics.action',
                            method: 'GET',
                            params: { tenantId: activeRole },
                            success: function(resp) {
                                var r = Ext.decode(resp.responseText, true);
                                me.layoutWidgetStore.removeAll();
                                if (r && r.widgets) {
                                    for (var i = 0; i < r.widgets.length; i++) {
                                        var w = r.widgets[i];
                                        var name = "";
                                        if (w.kpi) name = w.kpi.name;
                                        else if (w.chart) name = w.chart.name;
                                        else if (w.table) name = w.table.name;

                                        me.layoutWidgetStore.add({
                                            id: w.id,
                                            type: w.type,
                                            refId: w.refId,
                                            name: name,
                                            colSpan: w.colSpan,
                                            sortOrder: w.sortOrder
                                        });
                                    }
                                }
                            }
                        });
                    } else {
                        activeRole = null;
                        widgetGrid.disable();
                        me.layoutWidgetStore.removeAll();
                    }
                }
            }
        });

        return {
            title: 'Dise\u00f1o de Dashboard',
            iconCls: 'x-fa fa-th-large',
            xtype: 'panel',
            layout: 'border',
            items: [roleGrid, widgetGrid]
        };
    },

    showAddWidgetWindow: function() {
        var me = this;
        var selectedType = 'KPI';

        var comboItem = Ext.create('Ext.form.ComboBox', {
            fieldLabel: 'Componente a agregar',
            anchor: '100%',
            displayField: 'name',
            valueField: 'id',
            queryMode: 'local',
            allowBlank: false,
            forceSelection: true
        });

        var loadComboStore = function(type) {
            var store;
            if (type === 'KPI') {
                store = me.kpiStore;
            } else if (type === 'CHART') {
                store = me.chartStore;
            } else if (type === 'TABLE') {
                store = me.tableStore;
            }
            
            if (store) {
                store.clearFilter(true);
                if (me.activeRole) {
                    store.filterBy(function(rec) {
                        var roleVal = rec.get('role');
                        if (!roleVal) return false;
                        var roles = roleVal.split(',').map(function(r){return r.trim();});
                        return Ext.Array.contains(roles, me.activeRole);
                    });
                }
                comboItem.setStore(store);
            }
            comboItem.setValue(null);
        };

        loadComboStore('KPI');

        var win = Ext.create('Ext.window.Window', {
            title: 'Agregar Componente al Dashboard',
            width: 400,
            modal: true,
            bodyPadding: 15,
            layout: 'anchor',
            listeners: {
                destroy: function() {
                    me.kpiStore.clearFilter();
                    me.chartStore.clearFilter();
                    me.tableStore.clearFilter();
                },
                close: function() {
                    me.kpiStore.clearFilter();
                    me.chartStore.clearFilter();
                    me.tableStore.clearFilter();
                }
            },
            items: [
                {
                    xtype: 'combobox',
                    fieldLabel: 'Tipo Componente',
                    store: ['KPI', 'CHART', 'TABLE'],
                    value: 'KPI',
                    anchor: '100%',
                    margin: '0 0 10 0',
                    listeners: {
                        change: function(cb, val) {
                            selectedType = val;
                            loadComboStore(val);
                        }
                    }
                },
                comboItem,
                {
                    xtype: 'combobox',
                    itemId: 'colSpanField',
                    fieldLabel: 'Ancho Columnas',
                    store: [
                        [1, '1 (25% Ancho)'],
                        [2, '2 (50% Ancho)'],
                        [3, '3 (75% Ancho)'],
                        [4, '4 (100% Ancho)']
                    ],
                    value: 2,
                    anchor: '100%',
                    margin: '10 0 0 0'
                }
            ],
            buttons: [
                {
                    text: 'Agregar',
                    handler: function() {
                        var refId = comboItem.getValue();
                        var colSpan = win.down('#colSpanField').getValue();
                        if (!refId) return;

                        var record = comboItem.getStore().findRecord('id', refId);
                        var name = record ? record.get('name') : 'Componente';

                        me.layoutWidgetStore.add({
                            type: selectedType,
                            refId: refId,
                            name: name,
                            colSpan: colSpan,
                            sortOrder: me.layoutWidgetStore.getCount() + 1
                        });
                        win.close();
                    }
                },
                {
                    text: 'Cancelar',
                    handler: function() {
                        win.close();
                    }
                }
            ]
        });
        win.show();
    },

    showEditColSpanWindow: function(record) {
        var me = this;
        var win = Ext.create('Ext.window.Window', {
            title: 'Editar Ancho de Columnas',
            width: 320,
            modal: true,
            bodyPadding: 15,
            layout: 'anchor',
            items: [
                {
                    xtype: 'displayfield',
                    fieldLabel: 'Componente',
                    value: record.get('name')
                },
                {
                    xtype: 'combobox',
                    itemId: 'colSpanField',
                    fieldLabel: 'Ancho Columnas',
                    store: [
                        [1, '1 (25% Ancho)'],
                        [2, '2 (50% Ancho)'],
                        [3, '3 (75% Ancho)'],
                        [4, '4 (100% Ancho)']
                    ],
                    value: record.get('colSpan') || 2,
                    anchor: '100%',
                    forceSelection: true,
                    editable: false
                }
            ],
            buttons: [
                {
                    text: 'Aceptar',
                    handler: function() {
                        var newVal = win.down('#colSpanField').getValue();
                        record.set('colSpan', newVal);
                        win.close();
                    }
                },
                {
                    text: 'Cancelar',
                    handler: function() {
                        win.close();
                    }
                }
            ]
        });
        win.show();
    },

    getColorKeys: function(labels, datasets, chartTypeStr) {
        if (datasets.length === 1 || chartTypeStr === 'PIE') {
            return labels;
        } else {
            var keys = [];
            for (var i = 0; i < datasets.length; i++) {
                keys.push(datasets[i].label || ('Serie ' + (i + 1)));
            }
            return keys;
        }
    },

    renderColorPickers: function(formPanel, keys, colorMap) {
        var me = this;
        var container = formPanel.down('#chartColorsContainer');
        if (!container) return;
        
        container.removeAll(true);
        
        if (!keys || keys.length === 0) {
            container.add({
                xtype: 'label',
                text: 'Ingrese un Query SQL v\u00e1lido para detectar las categor\u00edas y seleccionar sus colores.',
                style: 'color: #777; font-style: italic; display: block; margin: 10px 0;'
            });
            return;
        }
        
        var defaultPalette = [
            '#00306E', '#0050B3', '#1890FF', '#52C41A', '#FAAD14', '#F5222D', '#722ED1', '#13C2C2', '#EB2F96', '#2F54EB'
        ];
        
        for (var i = 0; i < keys.length; i++) {
            var key = keys[i];
            var defaultColor = colorMap[key] || defaultPalette[i % defaultPalette.length];
            
            container.add({
                xtype: 'container',
                layout: 'hbox',
                margin: '5 0',
                defaults: { margin: '0 10 0 0' },
                items: [
                    {
                        xtype: 'displayfield',
                        value: '<b>' + key + '</b>',
                        width: 150,
                        fieldStyle: 'text-overflow: ellipsis; overflow: hidden; white-space: nowrap;'
                    },
                    {
                        xtype: 'textfield',
                        value: defaultColor,
                        flex: 1,
                        emptyText: 'e.g. #00306E',
                        regex: /^#[0-9A-F]{6}$/i,
                        regexText: 'Formato HEX inv\u00e1lido',
                        categoryKey: key,
                        listeners: {
                            change: function(field, newVal) {
                                var picker = field.up('container').down('textfield[inputType=color]');
                                if (newVal && /^#[0-9A-F]{6}$/i.test(newVal)) {
                                    picker.setValue(newVal);
                                }
                                me.serializeChartColors(formPanel);
                            }
                        }
                    },
                    {
                        xtype: 'textfield',
                        inputType: 'color',
                        value: defaultColor,
                        width: 50,
                        height: 32,
                        submitValue: false,
                        style: 'padding: 0; border: none; cursor: pointer; background: transparent;',
                        listeners: {
                            change: function(picker, newVal) {
                                var txt = picker.up('container').down('textfield[categoryKey]');
                                if (txt.getValue() !== newVal) {
                                    txt.setValue(newVal);
                                }
                                me.serializeChartColors(formPanel);
                            }
                        }
                    }
                ]
            });
        }
        
        container.updateLayout();
        me.serializeChartColors(formPanel);
    },

    serializeChartColors: function(formPanel) {
        var container = formPanel.down('#chartColorsContainer');
        if (!container) return;
        
        var colorMap = {};
        container.items.each(function(row) {
            if (row.xtype === 'container') {
                var txt = row.down('textfield[categoryKey]');
                if (txt) {
                    var key = txt.categoryKey;
                    var val = txt.getValue() || '#00306E';
                    colorMap[key] = val;
                }
            }
        });
        
        formPanel.down('[name=colors]').setValue(Ext.encode(colorMap));
    },

    loadChartColorPickers: function(formPanel, record) {
        var me = this;
        var sql = record.get('chartQuery');
        var colorsJsonStr = record.get('colors') || '';
        
        var colorMap = {};
        if (colorsJsonStr && colorsJsonStr.indexOf('{') === 0) {
            try {
                colorMap = Ext.decode(colorsJsonStr);
            } catch(e) {
                console.error("Error decoding chart colors JSON", e);
            }
        }
        
        var container = formPanel.down('#chartColorsContainer');
        if (container) container.removeAll(true);
        
        if (!sql) return;
        
        Ext.Ajax.request({
            url: 'admin/dashboard/charts/preview.action',
            method: 'POST',
            params: {
                chartQuery: sql,
                chartTypeStr: formPanel.down('[name=chartTypeStr]').getValue() || 'BAR',
                xAxisLabel: '',
                yAxisLabel: '',
                showLegend: true,
                showTitle: false,
                colors: '',
                description: ''
            },
            success: function(resp) {
                var r = Ext.decode(resp.responseText, true);
                if (r && r.success && r.data) {
                    var labels = r.data.labels || [];
                    var datasets = r.data.datasets || [];
                    var keys = me.getColorKeys(labels, datasets, formPanel.down('[name=chartTypeStr]').getValue());
                    me.renderColorPickers(formPanel, keys, colorMap);
                }
            }
        });
    },

    renderTableColumnConfigs: function(formPanel, keys, headers, formats) {
        var me = this;
        var container = formPanel.down('#columnsContainer');
        if (!container) return;
        
        container.removeAll(true);
        
        if (!keys || keys.length === 0) {
            container.add({
                xtype: 'label',
                text: 'No hay columnas configuradas. Ingrese un Query SQL y presione "Analizar Columnas de Query".',
                style: 'color: #777; font-style: italic; display: block; margin: 10px 0;'
            });
            return;
        }
        
        for (var i = 0; i < keys.length; i++) {
            var key = keys[i];
            var header = headers[i] || key;
            var format = formats[i] || 'TEXT';
            
            container.add({
                xtype: 'container',
                layout: 'hbox',
                margin: '5 0',
                defaults: { margin: '0 5 0 0' },
                items: [
                    {
                        xtype: 'displayfield',
                        value: '<b>' + key + '</b>',
                        width: 150,
                        fieldStyle: 'text-overflow: ellipsis; overflow: hidden; white-space: nowrap;'
                    },
                    {
                        xtype: 'textfield',
                        value: header,
                        flex: 1,
                        emptyText: 'Cabecera (e.g. Total)',
                        allowBlank: false,
                        dbKey: key,
                        listeners: {
                            change: function() {
                                me.serializeTableColumns(formPanel);
                            }
                        }
                    },
                    {
                        xtype: 'combobox',
                        value: format,
                        width: 130,
                        store: ['TEXT', 'NUMBER', 'CURRENCY', 'PERCENTAGE'],
                        allowBlank: false,
                        dbKey: key,
                        listeners: {
                            change: function() {
                                me.serializeTableColumns(formPanel);
                            }
                        }
                    }
                ]
            });
        }
        
        container.updateLayout();
    },

    serializeTableColumns: function(formPanel) {
        var container = formPanel.down('#columnsContainer');
        if (!container) return;
        
        var keys = [];
        var headers = [];
        var formats = [];
        
        container.items.each(function(row) {
            if (row.xtype === 'container') {
                var txt = row.down('textfield[dbKey]');
                var combo = row.down('combobox[dbKey]');
                if (txt && combo) {
                    keys.push(txt.dbKey);
                    headers.push(txt.getValue() || txt.dbKey);
                    formats.push(combo.getValue() || 'TEXT');
                }
            }
        });
        
        formPanel.down('[name=columnKeys]').setValue(keys.join(','));
        formPanel.down('[name=columnHeaders]').setValue(headers.join(','));
        formPanel.down('[name=columnFormats]').setValue(formats.join(','));
    }
});
