function GizwitsWS(apiHost, wechatOpenId, gizwitsAppId, commType, SocketType) {
    this.onInit = undefined;
    this.onConnected = undefined;
    this.onOnlineStatusChanged = undefined;
    this.onReceivedRaw = undefined;
    this.onReceivedAttrs = undefined;
    this.onError = undefined;
    this._openId = wechatOpenId;
    this._appId = gizwitsAppId;
    this._commType = commType;
    if (SocketType == undefined) {
        this._socketType = "ssl_socket"
    } else {
        this._socketType = SocketType
    }
    this._apiHost = apiHost;
    this._websocket = {};
    this._heartbeatTimerId = {};
    this._wsUrl = "{0}/ws/app/v1";
    this._userId = undefined;
    this._userToken = undefined;
    this._bindingDevices = undefined
};GizwitsWS.prototype.init = function () {
    var me = this;
    me._getUserToken()
};
GizwitsWS.prototype.connect = function (did) {
    var me = this;
    if (me._bindingDevices == undefined) {
        me._sendError("Please call 'init()' firstly.");
        return
    }
    var device = me._bindingDevices[did];
    if (device == null) {
        me._sendError("Device is not bound.");
        return
    }
    device.subscribed = true;
    var wsInfo = me._getWebsocketConnInfo(device);
    if (me._websocket[wsInfo] == null) {
        me._connectWS(wsInfo)
    } else {
        me.onConnected()
    }
};
GizwitsWS.prototype.send = function (did, data) {
    var me = this;
    if (me._bindingDevices == undefined) {
        me._sendError("Please call 'init()' firstly.");
        return
    }
    var device = me._bindingDevices[did];
    if (device == null) {
        me._sendError("Device is not bound.");
        return
    }
    var wsInfo = me._getWebsocketConnInfo(device);
    return me._sendJson(wsInfo, {cmd: "c2s_raw", data: {did: did, raw: data}})
};
GizwitsWS.prototype.read = function (did) {
    var me = this;
    if (me._bindingDevices == undefined) {
        me._sendError("Please call 'init()' firstly.");
        return
    }
    var device = me._bindingDevices[did];
    if (device == null) {
        me._sendError("Device is not bound.");
        return
    }
    var wsInfo = me._getWebsocketConnInfo(device);
    return me._sendJson(wsInfo, {cmd: "c2s_read", data: {did: did}})
};
GizwitsWS.prototype.write = function (did, attrs) {
    var me = this;
    if (me._bindingDevices == undefined) {
        me._sendError("Please call 'init()' firstly.");
        return
    }
    var device = me._bindingDevices[did];
    if (device == null) {
        me._sendError("Device is not bound.");
        return
    }
    var wsInfo = me._getWebsocketConnInfo(device);
    return me._sendJson(wsInfo, {cmd: "c2s_write", data: {did: did, attrs: attrs}})
};
GizwitsWS.prototype._getUserToken = function () {
    var me = this;
    var url = "https://{0}/app/users".format(me._apiHost);
    $.ajax(url, {
        type: "POST",
        contentType: "application/json",
        headers: {"X-Gizwits-Application-Id": me._appId},
        dataType: "json",
        data: "{\"phone_id\":\"" + me._openId + "\",\"lang\":\"en\"}"
    }).done(function (result) {
        me._userId = result.uid;
        me._userToken = result.token;
        var limit = 20;
        var skip = 0;
        me._bindingDevices = {};
        me._getBindingList(limit, skip)
    }).fail(function (evt) {
        me._sendError("Init error when getting user token.")
    })
};
GizwitsWS.prototype._getBindingList = function (limit, skip) {
    var me = this;
    var url = "https://{0}/app/bindings".format(me._apiHost);
    var query = "?show_disabled=0&limit=" + limit + "&skip=" + skip;
    $.ajax(url + query, {
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        headers: {"X-Gizwits-Application-Id": me._appId, "X-Gizwits-User-token": me._userToken}
    }).done(function (result) {
        for (var i in result.devices) {
            var device = result.devices[i];
            device.subscribed = false;
            var did = device.did;
            me._bindingDevices[did] = device
        }
        if (result.devices.length == limit) {
            me._getBindingList(limit, skip + limit)
        } else {
            me._returnDeviceList()
        }
    }).fail(function (evt) {
        me._bindingDevices = undefined;
        me._sendError("Init error when getting binding devices.")
    })
};
GizwitsWS.prototype._connectWS = function (wsInfo) {
    var me = this;
    var websocket = new WebSocket(me._wsUrl.format(wsInfo));
    websocket.onopen = function (evt) {
        me._onWSOpen(evt, wsInfo)
    };
    websocket.onclose = function (evt) {
        me._onWSClose(evt, wsInfo)
    };
    websocket.onmessage = function (evt) {
        me._onWSMessage(evt, wsInfo)
    };
    websocket.onerror = function (evt) {
        me._onWSError(evt, wsInfo)
    };
    me._websocket[wsInfo] = websocket
};
GizwitsWS.prototype._onWSOpen = function (evt, wsInfo) {
    var me = this;
    var json = {
        cmd: "login_req",
        data: {
            appid: me._appId,
            uid: me._userId,
            token: me._userToken,
            p0_type: me._commType,
            heartbeat_interval: 180000
        }
    };
    me._sendJson(wsInfo, json)
};
GizwitsWS.prototype._onWSClose = function (evt, wsInfo) {
    var me = this;
    me._stopPing(wsInfo);
    me._connectWS(wsInfo)
};
GizwitsWS.prototype._onWSMessage = function (evt, wsInfo) {
    var me = this;
    var res = JSON.parse(evt.data);
    switch (res.cmd) {
        case"pong":
            break;
        case"login_res":
            if (res.data.success == true) {
                me._startPing(wsInfo);
                if (me.onConnected) {
                    me.onConnected()
                }
            } else {
                me._sendError("Connect error.")
            }
            break;
        case"s2c_online_status":
            var device = me._getBindingDevice(res.data.did);
            if (me.onOnlineStatusChanged && device && device.subscribed) {
                me.onOnlineStatusChanged({did: device.did, is_online: res.data.online})
            }
            break;
        case"s2c_raw":
            var device = me._getBindingDevice(res.data.did);
            if (me.onReceivedRaw && device && device.subscribed) {
                me.onReceivedRaw({did: device.did, raw: res.data.raw})
            }
            break;
        case"s2c_noti":
            var device = me._getBindingDevice(res.data.did);
            if (me.onReceivedAttrs && device && device.subscribed) {
                me.onReceivedAttrs({did: device.did, attrs: res.data.attrs})
            }
            break;
        case"s2c_invalid_msg":
            me._sendError(res.data.msg);
            break
    }
};
GizwitsWS.prototype._onWSError = function (evt, wsInfo) {
    var me = this;
    me._sendError("Websocket on error")
};
GizwitsWS.prototype._startPing = function (wsInfo) {
    var me = this;
    me._heartbeatTimerId[wsInfo] = window.setInterval(function () {
        me._sendJson(wsInfo, {cmd: "ping"})
    }, 60000)
};
GizwitsWS.prototype._stopPing = function (wsInfo) {
    var me = this;
    window.clearInterval(me._heartbeatTimerId[wsInfo])
};
GizwitsWS.prototype._sendJson = function (wsInfo, json) {
    var me = this;
    var data = JSON.stringify(json);
    return me._sendData(wsInfo, data)
};
GizwitsWS.prototype._sendData = function (wsInfo, data) {
    var me = this;
    var websocket = me._websocket[wsInfo];
    if (websocket.readyState == websocket.OPEN) {
        websocket.send(data);
        return true
    } else {
        console.log("Send data error, websocket is not connected.");
        return false
    }
};
GizwitsWS.prototype._sendError = function (msg) {
    if (this.onError) {
        this.onError(msg)
    }
};
GizwitsWS.prototype._returnDeviceList = function () {
    var me = this;
    if (me.onInit) {
        var devices = [];
        var i = 0;
        for (var key in me._bindingDevices) {
            devices[i] = {
                "did": me._bindingDevices[key].did,
                "mac": me._bindingDevices[key].mac,
                "product_key": me._bindingDevices[key].product_key,
                "is_online": me._bindingDevices[key].is_online,
                "dev_alias": me._bindingDevices[key].dev_alias,
                "remark": me._bindingDevices[key].remark
            };
            i++
        }
        me.onInit(devices)
    }
};
GizwitsWS.prototype._getBindingDevice = function (did) {
    var me = this;
    return me._bindingDevices[did]
};
GizwitsWS.prototype._getWebsocketConnInfo = function (device) {
    var me = this;
    var host = device.host;
    var pre = "ws://";
    var port = device.ws_port.toString();
    if (me._socketType == "ssl_socket") {
        pre = "wss://";
        port = device.wss_port.toString()
    }
    return pre + host + ":" + port
};
String.prototype.format = function () {
    var args = arguments;
    return this.replace(/\{(\d+)\}/g, function (m, i) {
        return args[i]
    })
};