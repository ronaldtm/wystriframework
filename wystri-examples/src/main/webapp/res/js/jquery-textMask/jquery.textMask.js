(function($) {
  const vanillaTextMask = window['vanillaTextMask'];

  const TextMaskKey = 'jQueryTextMask';
  let methods = {
    init    : function(options) {
      let $this = $(this);
      if ($this.data(TextMaskKey))
          $this.textMask('destroy');
      let opts = $.extend({}, $.fn.textMask.defaults, options || {}, { inputElement: this });
      let controller = vanillaTextMask.maskInput(opts);
      $this.data(TextMaskKey, controller);
    },
    destroy : function() {
      let $this = $(this);
      let ps = $this.data(TextMaskKey);
      $this.removeData(TextMaskKey);
      ps.destroy();
    },
  };

  $.fn.textMask = function(methodOrOptions) {
    let args = arguments;
    if ('TextMask' == methodOrOptions) {
      return this.data(TextMaskKey);
    } else if (methods[methodOrOptions]) {
      return this.each(function() {
        methods[methodOrOptions].apply(this, Array.prototype.slice.call(args, 1));
      });
    } else if (typeof methodOrOptions === 'object' || !methodOrOptions) {
      return this.each(function() {
        methods.init.apply(this, args); // Default to "init"
      });
    } else {
      $.error('Method ' + methodOrOptions + ' does not exist on jQuery.textMask');
    }
  };

  $.fn.textMask.defaults = {};
})(window['jQuery']);
