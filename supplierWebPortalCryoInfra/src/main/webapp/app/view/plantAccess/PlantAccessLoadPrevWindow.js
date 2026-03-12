Ext.define('SupplierApp.view.plantAccess.PlantAccessLoadPrevWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.plantaccessloadprevwindow',
    title: 'Cargar trabajadores desde solicitud previa',
    modal: true,
    width: 650,
    height: 250,
    layout: 'fit',
    resizable: false,
    initComponent: function() {
        var me = this;

        Ext.apply(me, {
            items: [{
                xtype: 'form',
                bodyPadding: 15,
                border: false,
                defaults: { 
                    anchor: '100%',
                    labelWidth: 180
                },
                items: [
                    { 
                        xtype: 'displayfield',
                        value: '¿Desea cargar trabajadores desde una solicitud anterior?',
                        margin: '0 0 15 0',
                        fieldStyle: 'font-weight: bold; font-size: 13px;'
                    },
                    {
                        xtype: 'radiogroup',
                        itemId: 'choiceGroup',
                        fieldLabel: 'Seleccione una opción',
                        labelAlign: 'left',
                        labelWidth: 150,
                        columns: 2,
                        vertical: false,
                        margin: '0 0 15 0',
                        items: [
                            { boxLabel: 'Sí', name: 'choice', inputValue: 'yes', margin: '0 20 0 0' },
                            { boxLabel: 'No', name: 'choice', inputValue: 'no', checked: true }
                        ],
                        listeners: {
                            change: function(radiogroup, newValue) {
                                var combo = radiogroup.up('form').down('#prevRequestsCombo');
                                if(newValue.choice === 'yes') {
                                    combo.setVisible(true);
                                    combo.setDisabled(false);
                                } else {
                                    combo.setVisible(false);
                                    combo.setDisabled(true);
                                    combo.clearValue();
                                }
                            }
                        }
                    },
                    {
                        xtype: 'combobox',
                        itemId: 'prevRequestsCombo',
                        fieldLabel: 'Solicitud anterior',
                        labelAlign: 'left',
                        displayField: 'nameRequest',
                        valueField: 'id',
                        queryMode: 'local',
                        editable: false,
                        allowBlank: false,
                        emptyText: 'Seleccione una solicitud...',
                        hidden: true,
                        disabled: true,
                        margin: '0 0 10 0',
                        listConfig: {
                            minWidth: 600
                        },
                        tpl: Ext.create('Ext.XTemplate',
                            '<tpl for=".">',
                            '<div class="x-boundlist-item" style="padding:8px;">',
                            '<div style="font-weight:bold; font-size:12px; margin-bottom:3px;">ID: {id} - {nameRequest}</div>',
                            '<div style="font-size:11px; color:#555;">Empresa: {contractorCompany}</div>',
                            '<div style="font-size:10px; color:#888; margin-top:2px;">Fecha: {fechaSolicitudStr}</div>',
                            '</div>',
                            '</tpl>'
                        ),
                        store: Ext.create('Ext.data.Store', {
                            fields: ['id','nameRequest','contractorCompany','fechaSolicitudStr','status'],
                            data: []
                        })
                    }
                ]
            }],
            buttons: [
                { 
                    text: 'Cancelar', 
                    iconCls: 'icon-cancel',
                    handler: function(btn){ 
                        btn.up('window').close(); 
                    } 
                },
                { 
                    text: 'Continuar', 
                    itemId: 'continueBtn',
                    iconCls: 'icon-accept',
                    handler: function(btn){
                        console.log('Continuar button clicked');
                        var win = btn.up('window');
                        var choice = win.down('#choiceGroup').getValue().choice;
                        console.log('Choice:', choice);
                        
                        if(choice === 'no'){
                            win.fireEvent('choiceMade', win, 'no');
                            win.close();
                            return;
                        }
                        
                        var combo = win.down('#prevRequestsCombo');
                        var val = combo.getValue();
                        console.log('Selected request ID:', val);
                        if(!val){
                            Ext.Msg.alert('Aviso','Por favor, seleccione una solicitud previa de la lista.');
                            return;
                        }
                        
                        console.log('Firing events: choiceMade and loadPrevWorkers');
                        win.fireEvent('choiceMade', win, 'yes');
                        win.fireEvent('loadPrevWorkers', win, val);
                        win.close();
                    }
                }
            ]
        });

        me.callParent(arguments);
    },

    loadPrevRequests: function(requests){
        var combo = this.down('#prevRequestsCombo');
        if(combo && combo.getStore()){
            combo.getStore().loadData(requests || []);
        }
    }
});
