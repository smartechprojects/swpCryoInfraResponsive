Ext.define('SupplierApp.view.dashboard.DashboardViewPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.dashboardViewPanel',
    layout: 'fit',
    title: 'Dashboards y M\u00e9tricas',
    iconCls: 'x-fa fa-pie-chart',

    initComponent: function() {
        var me = this;

        // Cargar Chart.js desde CDN si no está cargado
        if (typeof Chart === 'undefined') {
            var script = document.createElement('script');
            script.src = 'https://cdn.jsdelivr.net/npm/chart.js';
            script.type = 'text/javascript';
            document.head.appendChild(script);
        }

        // Contenedor dinámico de widgets con diseño responsivo
        me.dashboardContainer = Ext.create('Ext.container.Container', {
            scrollable: true,
            padding: 15,
            layout: {
                type: 'column'
            },
            defaults: {
                margin: '0 10 15 10'
            }
        });

        // Barra superior
        me.tbar = [
            {
                text: 'Recargar',
                iconCls: 'x-fa fa-refresh',
                handler: function() {
                    me.loadDashboard();
                }
            }
        ];

        if (typeof role !== 'undefined' && role === 'ROLE_ADMIN') {
            var roleStore = getUDCStore('ROLES', '', '', '');
            roleStore.load();

            var roleCombo = Ext.create('Ext.form.ComboBox', {
                itemId: 'roleCombo',
                fieldLabel: 'Rol',
                labelWidth: 30,
                width: 200,
                margin: '0 10 0 10',
                store: roleStore,
                displayField: 'strValue1',
                valueField: 'strValue1',
                triggerAction: 'all',
                queryMode: 'local',
                value: 'ROLE_ADMIN',
                listeners: {
                    change: function(combo, newVal) {
                        var supplierField = me.down('#supplierField');
                        if (supplierField) {
                            if (newVal === 'ROLE_SUPPLIER') {
                                supplierField.show();
                            } else {
                                supplierField.hide();
                                supplierField.setValue(null);
                            }
                        }
                        if (me.rendered) {
                            me.loadDashboard();
                        }
                    }
                }
            });

            var supplierCombo = Ext.create('Ext.form.ComboBox', {
                itemId: 'supplierField',
                fieldLabel: 'Proveedor',
                labelWidth: 70,
                width: 320,
                margin: '0 10 0 10',
                hidden: true,
                store: Ext.create('Ext.data.Store', {
                    fields: ['addressNumber', 'name'],
                    autoLoad: true,
                    proxy: {
                        type: 'ajax',
                        url: 'dashboard/suppliers/list.action',
                        reader: {
                            type: 'json',
                            rootProperty: 'data'
                        }
                    }
                }),
                displayField: 'name',
                valueField: 'addressNumber',
                anyMatch: true,
                typeAhead: true,
                forceSelection: false,
                queryMode: 'local',
                emptyText: 'Escriba o seleccione...',
                tpl: Ext.create('Ext.XTemplate',
                    '<ul class="x-list-plain"><tpl for=".">',
                        '<li role="option" class="x-boundlist-item" style="padding: 6px 10px; border-bottom: 1px solid #f0f0f0;">',
                            '<div style="font-size: 13px; font-weight: bold; color: #00306E;">{addressNumber}</div>',
                            '<div style="font-size: 12px; color: #555;">{name}</div>',
                        '</li>',
                    '</tpl></ul>'
                ),
                listeners: {
                    change: function() {
                        if (me.rendered) {
                            me.loadDashboard();
                        }
                    }
                }
            });

            me.tbar.push('->');
            me.tbar.push(roleCombo);
            me.tbar.push(supplierCombo);
        }

        Ext.apply(me, {
            items: [me.dashboardContainer],
            listeners: {
                afterrender: function() {
                    me.loadDashboard();
                }
            }
        });

        me.callParent(arguments);
    },

    loadDashboard: function() {
        var me = this;
        var container = me.dashboardContainer;

        if (me.activeDashboardRequest) {
            Ext.Ajax.abort(me.activeDashboardRequest);
            me.activeDashboardRequest = null;
        }

        me.mask('Cargando Dashboard...');
        container.removeAll();

        var params = {};
        if (typeof role !== 'undefined' && role === 'ROLE_ADMIN') {
            var rCombo = me.down('#roleCombo');
            var sCombo = me.down('#supplierField');
            if (rCombo) {
                params.tenantId = rCombo.getValue();
            }
            if (sCombo) {
                params.supplierNumber = sCombo.getValue();
            }
        }

        me.activeDashboardRequest = Ext.Ajax.request({
            url: 'dashboard/metrics.action',
            method: 'GET',
            params: params,
            success: function(resp) {
                me.activeDashboardRequest = null;
                me.unmask();
                var res = Ext.decode(resp.responseText, true);
                if (res && res.widgets) {
                    me.renderWidgets(res.widgets);
                } else {
                    container.add({
                        xtype: 'label',
                        text: 'No se encontraron widgets o configuraciones en este dashboard.',
                        margin: 20
                    });
                }
            },
            failure: function(response) {
                if (response.aborted) {
                    return;
                }
                me.activeDashboardRequest = null;
                me.unmask();
                Ext.Msg.alert('Error', 'No se pudo comunicar con el servidor para cargar el dashboard.');
            }
        });
    },

    renderWidgets: function(widgets) {
        var me = this;
        var container = me.dashboardContainer;
        container.removeAll(true);

        for (var i = 0; i < widgets.length; i++) {
            var w = widgets[i];
            var colSpan = w.colSpan || 2; // Columnas: 1 a 4. 2 es 50%
            var widthPct = (colSpan / 4); // Porcentaje de ancho

            if (w.type === 'KPI' && w.kpi) {
                var kpiComp = me.createKpiCard(w.kpi, widthPct);
                container.add(kpiComp);
            } else if (w.type === 'TABLE' && w.table) {
                var tableComp = me.createTableGrid(w.table, widthPct);
                container.add(tableComp);
            } else if (w.type === 'CHART' && w.chart) {
                var chartComp = me.createChart(w.chart, widthPct);
                container.add(chartComp);
            }
        }
    },

    createKpiCard: function(kpi, widthPct) {
        var me = this;
        var icon = kpi.icon || 'fa-info-circle';
        
        // Obtener el color guardado o el valor por defecto
        var color = kpi.color || '#00306E';
        var textColor = kpi.textColor || '#ffffff';
        
        // Generar gradiente dinámico en base al color elegido
        var bgGradient = 'linear-gradient(135deg, ' + color + ' 0%, ' + me.lightenColor(color, 20) + ' 100%)';

        return Ext.create('Ext.panel.Panel', {
            columnWidth: widthPct,
            height: 110,
            bodyPadding: 15,
            border: false,
            style: {
                borderRadius: '8px',
                boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                background: bgGradient
            },
            bodyStyle: {
                background: 'transparent',
                display: 'flex',
                alignItems: 'center',
                color: textColor + ' !important'
            },
            html: '<div style="display:flex; align-items:center; width:100%; color:' + textColor + ' !important; font-family:Poppins-Regular, sans-serif;">' +
                  '  <div style="font-size:32px; margin-right:15px; opacity:0.8; color:' + textColor + ' !important;"><i class="fa ' + icon + '"></i></div>' +
                  '  <div style="flex:1; color:' + textColor + ' !important;">' +
                  '    <div style="font-size:11px; font-weight:300; opacity:0.9; text-transform:uppercase; letter-spacing:1px; color:' + textColor + ' !important;">' + kpi.name + '</div>' +
                  '    <div style="font-size:24px; font-weight:700; margin:2px 0; color:' + textColor + ' !important;">' + kpi.value + '</div>' +
                  '    <div style="font-size:10px; opacity:0.8; font-weight:300; color:' + textColor + ' !important;">' + kpi.subtext + '</div>' +
                  '  </div>' +
                  '</div>'
        });
    },

    lightenColor: function(hex, percent) {
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
    },

    createTableGrid: function(table, widthPct) {
        var fields = table.keys;
        var rows = table.rows;

        var columns = [];
        for (var i = 0; i < table.headers.length; i++) {
            columns.push({
                text: table.headers[i],
                dataIndex: table.keys[i],
                flex: 1
            });
        }

        var store = Ext.create('Ext.data.Store', {
            fields: fields,
            data: rows
        });

        return Ext.create('Ext.grid.Panel', {
            columnWidth: widthPct,
            height: 280,
            title: table.name,
            iconCls: 'x-fa fa-table',
            store: store,
            columns: columns,
            forceFit: true,
            style: {
                borderRadius: '8px',
                boxShadow: '0 4px 6px rgba(0, 0, 0, 0.05)',
                border: '1px solid #e0e0e0'
            },
            viewConfig: {
                stripeRows: true
            }
        });
    },

    createChart: function(chart, widthPct) {
        var me = this;
        
        var datasets = chart.datasets || [];
        var labels = chart.labels || [];
        var canvasId = 'chart-canvas-' + Ext.id();

        // Si Chart.js no está cargado, usar fallback de tabla
        if (typeof Chart === 'undefined') {
            var chartStoreFields = ['label'];
            for (var d = 0; d < datasets.length; d++) {
                chartStoreFields.push('value_' + d);
            }

            var storeData = [];
            for (var l = 0; l < labels.length; l++) {
                var rowObj = { label: labels[l] };
                for (var d = 0; d < datasets.length; d++) {
                    rowObj['value_' + d] = datasets[d].data[l] || 0;
                }
                storeData.push(rowObj);
            }

            var chartStore = Ext.create('Ext.data.Store', {
                fields: chartStoreFields,
                data: storeData
            });

            var fbColumns = [{ text: chart.xAxisLabel || 'Etiqueta', dataIndex: 'label', flex: 1 }];
            for (var d = 0; d < datasets.length; d++) {
                fbColumns.push({
                    text: datasets[d].label,
                    dataIndex: 'value_' + d,
                    flex: 1,
                    renderer: Ext.util.Format.numberRenderer('0,0.00')
                });
            }

            return Ext.create('Ext.grid.Panel', {
                columnWidth: widthPct,
                height: 280,
                title: chart.name + ' (Cargando Gr\u00e1fico...)',
                iconCls: 'x-fa fa-bar-chart',
                store: chartStore,
                columns: fbColumns,
                forceFit: true,
                style: {
                    borderRadius: '8px',
                    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.05)',
                    border: '1px solid #e0e0e0'
                }
            });
        }

        // Crear contenedor con Canvas
        var iconClass = 'x-fa fa-bar-chart';
        var typeStr = chart.chartType; // int or string
        var chartType = 'bar';
        if (typeStr === 2 || typeStr === 'LINE' || typeStr === 2) {
            chartType = 'line';
            iconClass = 'x-fa fa-line-chart';
        } else if (typeStr === 3 || typeStr === 'PIE' || typeStr === 3) {
            chartType = 'pie';
            iconClass = 'x-fa fa-pie-chart';
        }

        return Ext.create('Ext.panel.Panel', {
            columnWidth: widthPct,
            height: 280,
            title: chart.name,
            iconCls: iconClass,
            style: {
                borderRadius: '8px',
                boxShadow: '0 4px 6px rgba(0, 0, 0, 0.05)',
                border: '1px solid #e0e0e0',
                backgroundColor: '#ffffff'
            },
            html: '<div style="padding:15px; width:100%; height:230px; box-sizing:border-box; background:#ffffff;">' +
                  '  <canvas id="' + canvasId + '" style="width:100%; height:100%;"></canvas>' +
                  '</div>',
            listeners: {
                afterrender: function(panel) {
                    Ext.defer(function() {
                        var canvas = document.getElementById(canvasId);
                        if (!canvas) return;

                        var defaultPalette = [
                            '#00306E', '#0050B3', '#1890FF', '#52C41A', '#FAAD14', '#F5222D', '#722ED1', '#13C2C2', '#EB2F96', '#2F54EB'
                        ];
                        
                        var colorMap = {};
                        if (chart.colors && chart.colors.indexOf('{') === 0) {
                            try {
                                colorMap = Ext.decode(chart.colors);
                            } catch(e) {}
                        }

                        var chartJsDatasets = [];
                        if (datasets.length === 1 || chartType === 'pie') {
                            // Colorear cada categoría por separado
                            var bgColors = [];
                            var borderColors = [];
                            for (var l = 0; l < labels.length; l++) {
                                var label = labels[l];
                                var col = colorMap[label] || defaultPalette[l % defaultPalette.length];
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
                            // Colorear cada serie por separado
                            for (var d = 0; d < datasets.length; d++) {
                                var dsLabel = datasets[d].label || ('Serie ' + (d + 1));
                                var col = colorMap[dsLabel] || defaultPalette[d % defaultPalette.length];
                                
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
                                        legend: {
                                            display: chart.showLegend !== false,
                                            position: 'bottom',
                                            labels: {
                                                boxWidth: 12,
                                                font: { size: 11, family: 'Poppins-Regular, sans-serif' }
                                            }
                                        },
                                        title: {
                                            display: chart.showTitle === true && chart.description,
                                            text: chart.description || '',
                                            font: { size: 12, family: 'Poppins-Regular, sans-serif' }
                                        }
                                    },
                                    scales: chartType === 'pie' ? {} : {
                                        x: {
                                            title: {
                                                display: !!chart.xAxisLabel,
                                                text: chart.xAxisLabel || '',
                                                font: { size: 11, weight: 'bold' }
                                            },
                                            grid: { display: false }
                                        },
                                        y: {
                                            beginAtZero: true,
                                            title: {
                                                display: !!chart.yAxisLabel,
                                                text: chart.yAxisLabel || '',
                                                font: { size: 11, weight: 'bold' }
                                            }
                                        }
                                    }
                                }
                            });
                        } catch (err) {
                            console.error('Error al inicializar Chart.js:', err);
                        }
                    }, 100);
                }
            }
        });
    }
});
