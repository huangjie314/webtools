$(document).ready(function () {


    $("#jobcn").click(function () {
        location.href = "/query?path=JobcnX";
    });
    $("#jobcn").click(function () {
        location.href = "/query?path=JobcnX";
    });
    $("#jobcn_p").click(function () {
        location.href = "/query?path=JobcnX,trunk";
    });
    $("#jobcn_c").click(function () {
        location.href = "/query?path=JobcnX,Employer";
    });
    $("#jobcn_m").click(function () {
        location.href = "/query?path=JobcnX,weihu";
    });
    $("#jobcn_u").click(function () {
        location.href = "/query?path=JobcnX,util";
    });

    $("#biz").click(function () {
        location.href = "/query?path=BizOpp";
    });
    $("#biz_f").click(function () {
        location.href = "/query?path=BizOpp,ForeGround";
    });
    $("#biz_c").click(function () {
        location.href = "/query?path=BizOpp,MerchantCenter";
    });
    $("#biz_m").click(function () {
        location.href = "/query?path=BizOpp,Maintenance";
    });
    $("#biz_common").click(function () {
        location.href = "/query?path=BizOpp,Common";
    });

    $("table").on('click', '.copy', function () {
        $("#info").trigger('select');
        document.execCommand("copy");
        alert('ok');
    });

//·¢ËÍÓÊ¼þ
    $("table").on('click', '.send', function () {
        var $this = $(this);
        var email = '8999@jobcn.com';
        var title = $this.closest('tr').find(".msg").text();
        var email_text = title+'<br/>'+$this.closest('tr').find(".path").text();
        var url = "mailto:" + email + "?subject=" + title + "&body=" + email_text;
        var aTarget = $this.find("a");
        aTarget[0].href = url;
        aTarget.trigger("click");
    });

})