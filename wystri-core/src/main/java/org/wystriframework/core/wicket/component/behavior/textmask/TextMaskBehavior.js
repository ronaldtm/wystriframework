let TextMaskBehavior = window.TextMaskBehavior || {};

TextMaskBehavior.cpfCnpj = function($query) {
    $query.textMask({
        guide : false,
        mask : function(s) {
            var n = s.replace(/[^0-9]/g,'');
            return (n.length <= 11)
            ? [ /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '-', /\d/, /\d/ ]
            : [ /\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/ ];
        }
    });
};

TextMaskBehavior.cpf = function($query) {
    $query.textMask({
        guide : false,
        mask : [ /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '-', /\d/, /\d/ ]
    });
};

TextMaskBehavior.cnpj = function($query) {
    $query.textMask({
        guide : false,
        mask : [ /\d/, /\d/, '.', /\d/, /\d/, /\d/, '.', /\d/, /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/ ]
    });
};

TextMaskBehavior.ddmmyyyy = function($query, opts) {
    $query.textMask({
        guide : false,
        mask: [ /\d/, /\d/, '/', /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/],
        pipe : createAutoCorrectedDatePipe.default('dd/mm/yyyy', opts || { minYear: 1, maxYear:9999 })
    });
};

TextMaskBehavior.money = function($query, options) {
    var defaults = {
            prefix : '',
            includeThousandsSeparator : true,
            thousandsSeparatorSymbol : '.',
            decimalSymbol : ',',
            allowDecimal : true,
            decimalLimit : 2,
            allowNegative : false,
            allowLeadingZeroes : true,
            integerLimit : 999999999
        };
    var opts = $.extend(defaults, options || {});
    $query.textMask({
        guide : false,
        mask : createNumberMask.default(opts)
    });
};

TextMaskBehavior.conta1DV = function($query, opts) {
    $query.textMask({
        guide : false,
        mask: function(s) {
            var n = s.replace(/[^0-9]/g,'');
            if (n.length < 3) {
                return [ /\d/, '-', /\d/ ];
            }
            var mask = [];
            for (var i=0; i<n.length-1; i++) {
                mask.push(/\d/);
            }
            mask.push('-');
            mask.push(/\d/);
            return mask;
        }
    });
};