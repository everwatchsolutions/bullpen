window.Handlebars.registerHelper('select', function (value, options) {
    var $el = $('<select />').html(options.fn(this));
    $el.find('[value=' + value + ']').attr({'selected': 'selected'});
    return $el.html();
});
window.Handlebars.registerHelper('selectText', function (value, options) {
    var $el = $('<select />').html(options.fn(this));
    $el.find('option:contains("' + value + '")').attr({'selected': 'selected'});
    return $el.html();
});