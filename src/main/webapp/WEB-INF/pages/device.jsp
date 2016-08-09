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
    <div class="row padding-20">
        <div class="col-xs-3 btn btn-block btn-default">温度: <span id="temp"></span></div>
        <div class="col-xs-3 btn btn-block btn-default">湿度: <span id="humi"></span></div>
    </div>
</div>
<div class="container-fluid" id="led">

    <div class="row padding-20">
        <div><span id="lab_r" class="white">0</span></div>
        <input id="led_r" type="range" max="255" min="0" step="1" value="0">
    </div>

    <div class="row padding-20">
        <div><span id="lab_g" class="white">0</span></div>
        <input id="led_g" type="range" max="255" min="0" step="1" value="0">
    </div>

    <div class="row padding-20">
        <div><span id="lab_b" class="white">0</span></div>
        <input id="led_b" type="range" max="255" min="0" step="1" value="0">
    </div>

    <div class="row padding-20">
        <div class="btn btn-default btn-block" id="led_off">关灯</div>
    </div>
</div>
<div class="container-fluid">

    <div class="row padding-20">
        <div class="col-xs-12">
            <div class="btn btn-block btn-default"> 电机速度: <span id="eng_n">0</span></div>
            <div class="margin-top-20"></div>
            <input id="engine" type="range" min="-5" max="5" value="0" step="1">
        </div>
    </div>
</div>
</body>
<script src="<%=basePath%>js/jquery-2.2.4.min.js"></script>
<script src="<%=basePath%>js/gizwits_ws_0.1.1.min.js"></script>
<script type="text/javascript">
    var gizwitsws,
            did = "${did}";

    function base_init() {
        var red = 0,
                green = 0,
                blue = 0;

        renew();

        $('#led_r').on("change", function () {
            var val = $(this).val();
            red = parseInt(val);
            $('#lab_r').text(red);
            var attrs = '{"LED_R": ' + val + '}';
            gizwitsws.write(did, JSON.parse(attrs));
            renew();
        });

        $('#led_g').on("change", function () {
            var val = $(this).val();
            green = parseInt(val);
            $('#lab_g').text(green);
            var attrs = '{"LED_G": ' + val + '}';
            gizwitsws.write(did, JSON.parse(attrs));
            renew();
        });

        $('#led_b').on("change", function () {
            var val = $(this).val();
            blue = parseInt(val);
            $('#lab_b').text(blue);
            var attrs = '{"LED_B": ' + val + '}';
            gizwitsws.write(did, JSON.parse(attrs));
            renew();
        });

        $('#led_off').on('click', function () {
            red = green = blue = 0;
            $('#led_r, #led_g, #led_b').val(0);
            $('#lab_r, #lab_g, #lab_b').text(0);
            var attrs = '{"LED_R": 0, "LED_G": 0, "LED_B": 0}';
            gizwitsws.write(did, JSON.parse(attrs));
            renew();
        });

        $('#engine').on("change", function () {
            var val = $(this).val();
            $('#eng_n').text(val);
            var attrs = '{"Motor_Speed": ' + val + '}';
            gizwitsws.write(did, JSON.parse(attrs));
        });

        function renew() {
            var rgb = "#" + to16(red) + "" + to16(green) + "" + to16(blue);
            console.log('rgb', rgb);
            $('#led').css("background-color", rgb);
        }

        function to16(num) {
            if (!num) num = 0;
            var result = num.toString(16);
            result = result.length < 2 ? "0" + result : result;
            return result;
        }
    }

    function gizwits_init() {
        var apiHost = "api.gizwits.com",
                commType = "attrs_v4",
                wechatOpenId = "${OpenId}",
                gizwitsAppId = "${gizwitsAppId}";


        gizwitsws = new GizwitsWS(apiHost, wechatOpenId, gizwitsAppId, commType);

        gizwitsws.onInit = onInit;
        gizwitsws.onConnected = onConnected;
        gizwitsws.onOnlineStatusChanged = onOnlineStatusChanged;
        gizwitsws.onReceivedRaw = onReceivedRaw;
        gizwitsws.onReceivedAttrs = onReceivedAttrs;
        gizwitsws.onError = onError;

        gizwitsws.init();
        function onInit(devices) {
            console.log("init成功了！");
            if (devices.length == 0) {
                console.log("没有绑定的设备");
            }
            else {
                for (var i = 0; i < devices.length; i++) {
                    console.log("==================================================");
                    console.log("已绑定设备，did=" + devices[i].did);
                    console.log("已绑定设备，mac=" + devices[i].mac);
                    console.log("已绑定设备，product_key=" + devices[i].product_key);
                    console.log("已绑定设备，is_online=" + devices[i].is_online);
                    console.log("已绑定设备, dev_alias=" + devices[i].dev_alias);
                    console.log("已绑定设备，remark=" + devices[i].remark);
                }
                gizwitsws.connect(did);
            }
        }

        function onConnected() {
            console.log("websocket连接成功!");
            gizwitsws.read(did);
        }

        function onReceivedRaw(value) {
            console.log("设备Raw，did=" + value.did);
            var str = "";
            for (var i = 0; i < value.raw.length; i++) {
                str = str + value.raw[i] + " ";
            }
            console.log("设备Raw，raw=" + str);
        }

        var hasnot_init = true;

        function onReceivedAttrs(value) {
            for (var key in value.attrs) {
                console.log(key, ":", value.attrs[key]);
                if (key == 'Temperature') {
                    $('#temp').text(value.attrs[key]);
                } else if (key == "Humidity") {
                    $('#humi').text(value.attrs[key]);
                }
            }
            if (hasnot_init) {
                base_init();
                hasnot_init = false;
            }
        }

        function onOnlineStatusChanged(value) {
            alert("设备上下线通知，did=" + value.did);
            alert("设备上下线通知，is_online=" + value.is_online);
        }

        function onError(value) {
            console.log("error!!!!!::::", value.toString());
        }
    }

    gizwits_init();
</script>
</html>
