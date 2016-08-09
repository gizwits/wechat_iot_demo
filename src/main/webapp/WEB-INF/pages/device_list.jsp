<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <title>设备信息</title>
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=3, minimum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="<%=basePath%>css/bootstrap.min.css">
    <link rel="stylesheet" href="<%=basePath%>css/common.css">
    <link rel="stylesheet" href="<%=basePath%>css/scape.css">
</head>
<body>
<div class="container-fluid">
    <div class="row padding-20"></div>
</div>
</body>
<script src="<%=basePath%>js/jquery-2.2.4.min.js"></script>
<script src="<%=basePath%>js/gizwits_ws_0.1.1.min.js"></script>
<script type="text/javascript">

    function gizwits_init() {
        var gizwitsws,
            apiHost = "api.gizwits.com",
            commType = "attrs_v4",
            wechatOpenId = "${OpenId}",
            gizwitsAppId = "${gizwitsAppId}";


        gizwitsws = new GizwitsWS(apiHost, wechatOpenId, gizwitsAppId, commType);

        gizwitsws.onInit = onInit;

        //=========================================================
        // callback functions
        //=========================================================
        function onInit(devices) {

            $('.row').append("<div class='list-group'></div>");

            if (devices.length == 0)
            {
                $('.list-group').append('<a href="#" class=""list-group-item">没有绑定的列表</a>');
                return;
            }

            function formString(device) {
                return  '<a class="list-group-item" id="'+device.did+'" ' +
                        'href="<%=basePath%>device_detail?did='+device.did+'&' +
                        'openid=${OpenId}">' +
                        '<span class="badge">'+(device.is_online?'在线':'离线')+'</span>' +
                        (device.dev_alias? device.dev_alias+': ':'') + device.mac +
                        '</a>';
            }

            for (var i = 0; i < devices.length; i++) {
                $('.list-group').append(formString(devices[i]));
            }
        }

        gizwitsws.init();

    }
    gizwits_init();

</script>
</html>
