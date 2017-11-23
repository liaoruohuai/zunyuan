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
       ok:function(){}
    });
    d.width(200);
    d.show();
}
