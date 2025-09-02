Ext.define('Ext.ux.tab.Tab', {
    extend: 'Ext.tab.Tab',
    alias: 'widget.ux.menutab',
    requires: [
        'Ext.button.Split'
      ],

      constructor: function() {
        this.callParent(arguments);
        this.onClick = Ext.button.Split.prototype.onClick;
      },

      onRender: function() {
        this.callParent(arguments);
        this.btnWrap.insertSibling({
          tag: 'a',
          cls: 'arrow-inside-tab',
          href: '#'
        }, 'after');
        this.btnWrap.addCls(['pdr10']);  //padding-right: 10px; to make some place for arrow
      }
    });