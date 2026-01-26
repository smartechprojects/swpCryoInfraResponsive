Ext.onReady(function() {
    // Configurar locale español para fechas
    Ext.Date.monthNames = [
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    ];
    
    Ext.Date.dayNames = [
        "Domingo", "Lunes", "Martes", "Miércoles", 
        "Jueves", "Viernes", "Sábado"
    ];
    
    Ext.Date.monthNumbers = {
        "Ene": 0, "Feb": 1, "Mar": 2, "Abr": 3, "May": 4, "Jun": 5,
        "Jul": 6, "Ago": 7, "Sep": 8, "Oct": 9, "Nov": 10, "Dic": 11
    };
    
    // Formato de AM/PM en español
    Ext.Date.formatCodes.a = "(this.getHours() < 12 ? 'a.m.' : 'p.m.')";
    Ext.Date.formatCodes.A = "(this.getHours() < 12 ? 'A.M.' : 'P.M.')";
    
    // Nombres cortos de meses (opcional pero recomendado)
    Ext.Date.getShortMonthName = function(month) {
        return Ext.Date.monthNames[month].substring(0, 3);
    };
    
    // Nombres cortos de días (opcional)
    Ext.Date.getShortDayName = function(day) {
        return Ext.Date.dayNames[day].substring(0, 3);
    };
    
    // Configurar el formato por defecto de fecha
    Ext.Date.defaultFormat = 'd/m/Y';
});

Ext.application({
    name: 'SupplierApp',
    extend: 'SupplierApp.Application',
    requires: [
        'SupplierApp.view.main.Main',
        'Ext.plugin.Responsive'
    ]
});
