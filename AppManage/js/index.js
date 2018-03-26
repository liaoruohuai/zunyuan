//var url='http://221.133.244.11:9059/zunyuan/basic';//这里的IP和端口，要根据实际部署机器去改，目前是这种代码里配置，这是一个可以优化的地方，整个项目目前一共两个
 var url='http://127.0.0.1:8080/zunyuan/basic';
function linkPage(pageAddress){
    var pageAddress=pageAddress;
    window.location.href=pageAddress;
};
function goBack(){
    $('.go_back').on('click',function(){
        window.history.go(-1);
    })
};
function dialogFn(messages){
    var d = dialog({
        title: '提示',
        content:messages,
        //Modal: true,
        lock: true,
       ok:function(){}
    });
    d.width(200);
    d.showModal();
};

function dialogFn_SUC(messages,linkpage){
    var d = dialog({
        title: '提示',
        content:messages,
        //Modal: true,
        lock: true,
        ok:function(){
            linkPage(linkpage);
        }
    });
    d.width(200);
    d.showModal();
}

function dialogFn_func(messages,f){
    var d = dialog({
        title: '提示',
        content:messages,
        //Modal: true,
        lock: true,
        ok:function(){
            f()
        },
        cancel:function () {
        }
    });
    d.width(200);
    d.showModal();
}

function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}