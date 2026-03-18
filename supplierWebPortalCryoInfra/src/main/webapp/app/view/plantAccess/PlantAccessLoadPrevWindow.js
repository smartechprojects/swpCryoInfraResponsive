Ext.define('SupplierApp.view.plantAccess.PlantAccessLoadPrevWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.plantaccessloadprevwindow',
    title: SuppAppMsg.plantAccess104,
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
                        value: SuppAppMsg.plantAccess105,
                        margin: '0 0 15 0',
                        fieldStyle: 'font-weight: bold; font-size: 13px;'
                    },
                    {
                        xtype: 'radiogroup',
                        itemId: 'choiceGroup',
                        fieldLabel: SuppAppMsg.plantAccess106,
                        labelAlign: 'left',
                        labelWidth: 150,
                        columns: 2,
                        vertical: false,
                        margin: '0 0 15 0',
                        items: [
                            { boxLabel: SuppAppMsg.plantAccess107, name: 'choice', inputValue: 'yes', margin: '0 20 0 0' },
                            { boxLabel: SuppAppMsg.plantAccess108, name: 'choice', inputValue: 'no', checked: true }
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
                        fieldLabel: SuppAppMsg.plantAccess109,
                        labelAlign: 'left',
                        displayField: 'nameRequest',
                        valueField: 'id',
                        queryMode: 'local',
                        editable: false,
                        allowBlank: false,
                        emptyText: SuppAppMsg.plantAccess110,
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
                    text: SuppAppMsg.approvalCancel, 
                    iconCls: 'icon-cancel',
                    cls: 'buttonStyle',
                    handler: function(btn){ 
                        btn.up('window').close(); 
                    } 
                },
                { 
                    text: SuppAppMsg.plantAccess111, 
                    itemId: 'continueBtn',
                    iconCls: 'icon-accept',
                    cls: 'buttonStyle',
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
                            //Ext.Msg.alert(SuppAppMsg.plantAccess112,SuppAppMsg.plantAccess113);
                            Ext.MessageBox.alert({ maxWidth: 700, minWidth: 650, title: SuppAppMsg.plantAccess112, msg: SuppAppMsg.plantAccess113});
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
