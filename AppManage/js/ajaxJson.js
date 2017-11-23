 /*
 * e:'POST'
 * f:请求参数
 */
 $.ajaxSetup({cache: false,headers: {Accept: '*/*'} ,xhrFields:{withCredentials:true} });
 function postJSON(url,data,e,f){
    $.ajax({
    	url: url,
        type: 'post',
        dataType: 'json',
        //contentType: 'application/json',
        data: data,
        success: function (data, status, xhr) {
           e(data);
        },
        error: function (xhr, errorType, error) {
           f();
        }
    });
}
function getJSON(url,e,f){
    $.ajax({
    	url: url,
        type:'get' ,
        dataType:  'json',
        //contentType: 'application/json',
        success: function (data, status, xhr) {
           e(data);
        },
        error: function (xhr, errorType, error) {
           f();
        }
    });
}
