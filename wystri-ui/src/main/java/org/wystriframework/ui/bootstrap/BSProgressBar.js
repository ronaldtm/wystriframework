(function(){
    
Wicket.WUPB.prototype.setPercent = function(progressPercent) {
    Wicket.$(this.barid).style.width = progressPercent + '%';
    if ((progressPercent <= 0) || (progressPercent >= 100))
        $('#' + this.statusid).closest('.progress').hide();
    else
        $('#' + this.statusid).closest('.progress').show();
};

var originalInitialize = Wicket.WUPB.prototype.initialize;
Wicket.WUPB.prototype.initialize = function(formid, statusid, barid, url, fileid, initialStatus) {
    (originalInitialize.bind(this))(formid, statusid, barid, url, fileid, initialStatus);
    
    var wupb = this;
    $('#' + fileid).on('beforesubmit', function() { wupb.start(); });
};

//var originalStart = Wicket.WUPB.prototype.start;
//Wicket.WUPB.prototype.start = function() {
//    (originalStart.bind(this))();
//    $('#' + statusid).parent().show();
//};

})();
