$(document).ready(function () {
    if (detectOS() == 'Linux') {
        var tab = "\r\n";
    } else {
        var tab = "<br/>";
    }
    tab = encodeURI(tab);

    $("#all").click(function () {
        location.href = "query";
    });
    $("#jobcn").click(function () {
        location.href = "query?path=JobcnX";
    });
    $("#jobcn").click(function () {
        location.href = "query?path=JobcnX";
    });
    $("#jobcn_p").click(function () {
        location.href = "query?path=JobcnX,trunk";
    });
    $("#jobcn_c").click(function () {
        location.href = "query?path=JobcnX,Employer";
    });
    $("#jobcn_m").click(function () {
        location.href = "query?path=JobcnX,weihu";
    });
    $("#jobcn_u").click(function () {
        location.href = "query?path=JobcnX,util";
    });

    $("#biz").click(function () {
        location.href = "query?path=BizOpp";
    });
    $("#biz_f").click(function () {
        location.href = "query?path=BizOpp,ForeGround";
    });
    $("#biz_c").click(function () {
        location.href = "query?path=BizOpp,MerchantCenter";
    });
    $("#biz_m").click(function () {
        location.href = "query?path=BizOpp,Maintenance";
    });
    $("#biz_common").click(function () {
        location.href = "query?path=BizOpp,Common";
    });

    $("#logout").click(function () {
        location.href = "doLogout";
    });

    $("#search").click(function () {
        var start = $("#start").val();
        var end = $("#end").val();
        var path = $("#path").val();
        location.href = "query?start=" + start + "&end=" + end + "&path=" + path;
    });

    //选中事件
    $("table").on('click', '.check_row', function () {
        var $this = $(this);
        var checkBox = $this.find('input[type="checkbox"]');
        if (checkBox.is(":checked")) {
            checkBox.attr("checked", false);
        } else {
            checkBox.attr("checked", true);
            ;
        }
    });

    //复制
    $("table").on('click', '.copy', function () {
        var $this = $(this);
        var title = $this.closest('tr').find(".msg").text();
        var context = title + '\n' + $this.closest('tr').find(".path").text();
        //TODO 复制
        alert(context);
    });

    //发送邮件
    $("table").on('click', '.send', function (event) {
        var $this = $(this);
        var email = '8999@jobcn.com';
        var title = $this.closest('tr').find(".msg").text();
        var context = title + tab + $this.closest('tr').find(".path").text();
        context = context.replace(/\n/g, tab)
        var url = "mailto:" + email + "?subject=" + title + "&body=" + context;
        $this[0].href = url;
    });


    //批量发送邮件
    $("table").on('click', '#send_all', function () {
        var checkBoxs = $("#table_content").find('input[type="checkbox"]');
        var titles = [];
        var contexts = [];
        checkBoxs.each(function (e) {
            var $this = $(this);
            if ($this.is(":checked")) {
                var title = $this.closest("tr").find(".msg").text();
                var context = title + tab + $this.closest("tr").find(".path").text();
                context = context.replace(/\n/g, tab);
                titles.push(title);
                contexts.push(context);
            }
        });
        if (titles.length == 0) {
            alert("没有选中");
            return;
        }
        var $this = $(this);
        var email = '8999@jobcn.com';
        var url = "mailto:" + email + "?subject=" + titles.join(';') + "&body=" + contexts.join(tab);
        $this[0].href = url;
    });


    function detectOS() {
        var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
        if (isMac) return "Win";
        var isMac = (navigator.platform == "Mac68K") || (navigator.platform == "MacPPC") || (navigator.platform == "Macintosh") || (navigator.platform == "MacIntel");
        if (isMac) return "Mac";
        var isUnix = (navigator.platform == "X11") && !isWin && !isMac;
        if (isUnix) return "Unix";
        var isLinux = (String(navigator.platform).indexOf("Linux") > -1);
        if (isLinux) return "Linux";
        return "other";
    }

})