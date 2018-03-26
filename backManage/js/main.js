require.config({
  baseUrl : '../../vendors',
	 shim : {   
       // 'jquery-ui' : ['jquery'],
               'bs' : ['jquery'],
         'validate' : ['jquery'],
      'validate-zh' : ['jquery','validate'],
       'datepicker' : ['jquery',],
    'datepicker-zh' : ['jquery','datepicker'],
           'custom' : ['jquery'],
              'bbx' : ['bs'],
           'upload' : ['jquery'],
           'upload' : ['jquery'],
        'ue-config' : ['validate-zh','bbx','upload','datepicker-zh'],
               'ue' : ['ue-config'],
            'pager' : ['jquery'],
     },
	paths : {
           'jquery' : 'jquery/dist/jquery-1.11.3.min',
        //'jquery-ui' : 'jquery-ui/jquery-ui.min',  
               'bs' : 'bootstrap/dist/js/bootstrap.min',
         'validate' : 'validator/jquery.validate.min',
      'validate-zh' : 'validator/messages_cn',
       'datepicker' : 'bootstrap-datepicker/bootstrap-datepicker.min', 
    'datepicker-zh' : 'bootstrap-datepicker/bootstrap-datepicker.zh-CN.min',
           'custom' : '../js/custom',
              'bbx' : 'bootbox/bootbox.min',
        //'ue-config' : 'http://10.249.181.194:8080/happyshop/uEditor/ueditor.config',
        'ue-config' : 'uEditor/ueditor.config',
              // 'ue' : 'http://10.249.181.194:8080/happyshop/uEditor/ueditor.all.min',
                'ue' : 'uEditor/ueditor.all.min',
           'upload' : 'ajaxUpload/ajaxfileupload',
            'pager' : 'jquery-pager/jquery.page'
	}
});
require(['jquery','bbx','validate-zh','custom','ue','pager'],function($,bootbox){
    $(function(){

        //获取工程名，具体是否需要，还需要看上线之后是否保留
        var src = $('[data-main]')[0].src;
        var basePath = src.substring(0,src.indexOf('/vendors')) + '/back'


        //取数据的基础url
        var dataUrl = decodeURIComponent(window.name);
        console.log(dataUrl);
        if( dataUrl === '' ||dataUrl === 'null' ||dataUrl === 'undefined' ){
            alert('请先登录');
            location.replace('../../login.html');
        }


         //bootbox默认参数
         bootbox.setDefaults({
          size:'small',
          locale: 'zh_CN'
        });
        //创建分页
        function readyForPager(context,data,pageRoot){
            console.log(pageRoot);
            createPager(data,pageRoot,function(pageNumber){
                requestData(context.dataPath + '?' + $.param($.extend(data.condition,{page: pageNumber})))
                    .then(function(data){
                        context.showData(data);
                    });
            });
        }
        function createPager(data,pageRoot,callback){
            pageRoot = pageRoot || '.pagePageCode';
            $(pageRoot).newPage({
                totalPages: data.totalPages,
                currentPage: 1,
                data: {},
                callback:function(pageNumber){
                    callback(pageNumber-1);
                }
            });
        }

        //请求失败时执行，暂时只做简单处理，同样需要进行session判断
        function requestError(xhr,status,error) {
            bootbox.alert('请求失败');
        }

        //用于后退
        function backward(data) {
            if( data.code == 'success' ) {
                history.back(); 
            } else {
                bootbox.alert(data.message);
            }
        }
        //用于文本过多时省略显示
        function textEllipsis(selector) {
            var arr = selector.split(",");
            for(var i = 0; i < arr.length ; i++) {
                $(arr[i]).each(function(){
                    $(this).attr('title', $(this).text());
                    if(20 < $(this).text().length) {
                      $(this).html($(this).text().substring(0,20) + "...");
                    }
                });
            }
        }

        /**
         * [用于表单提交]
         * @param  {args.form} jquery对象 [需要提交的表单元素]
         * @param  {args.otherValidate} function [补充一些验证插件无法验证的验证规则]
         * @param  {args.redirectUrl} boolean [基本不用，留空]
         * @param  {args.submitUrl} string [表单的提交地址]
         * @param  {args.getFormData} function [需要提交的表单数据]
         */
        function formSubmit(args) {
            //参数默认值
            var arg = {
                form: $('#submit-form'),
                otherValidate: function() { return true; },
                redirectUrl: false,//这个参数基本不用
                submitUrl: '',
                getFormData: function() {
                    return this.form.serialize();
                }
            };
            arg = $.extend(arg,args);

            arg.form.validate({
                submitHandler : function(){

                    //执行附加验证，若返回false则不进行表单提交
                    if(!arg.otherValidate()) {
                        //在阻止提交前可进行部分其他操作
                        
                        return;
                    };
                    bootbox.confirm({
                      message: "确定要提交数据吗？",
                      callback: function(tag){
                        if(tag){
                          $.ajax({
                              type: "POST",
                              url: arg.submitUrl,
                              dataType: "json",
                              data: arg.getFormData()
                          })
                          .then(function(data){
                              backward(data);
                          })
                          .fail(requestError);
                        }
                      }
                    });
                }
            });

        }
      //获取localstorage
      function readSession() {
        return localStorage.getItem('X-Session');
      }

    	//ajax配置
        $.ajaxSetup({cache: false,headers: {Accept: '*/*'} ,xhrFields:{withCredentials:true} });

    	$(document)
    	.ajaxStart(function(){
			//过渡动画
    		$('.loading-modal').addClass('load');
    	})
    	.ajaxStop(function(){
    		$('.loading-modal').removeClass('load');
    	});

      //调用路由
      $(window).on('hashchange',function(e){
          var tplPath =  location.hash.substring(1);
          if(router[tplPath]) {
              router[tplPath].handle(tplPath);
          }
      });

      //加载模板
      function showTpl(tplPath) {
         return $.get(basePath + tplPath)
              .then(function(data){
                //bootbox.alert(data);
              cleanAndShow(data);
              })
              .fail(requestError);
      }

      //模板填充前清除数据
      function cleanAndShow(html,container) {
      var container = container? $(container) : $('.real-content');
          var html = $($.parseHTML(html)).filter(function(){
            return this.nodeType === 1;
          })
          container.empty().html(html);
      }

      //请求数据
      function requestData(url) {
        return $.ajax({
            dataType:'JSON',
            url: url,
            type: 'get'
          })
          //.then(badSession);
      }

      //删除数据
      function deleteData(option){
          console.log(option);
        if(option.deletable==='false'){
          bootbox.alert('不可删除');
          return;
        }
        var data = {}; 
        data[option.param.key] = option.param.value;
        return $.ajax({
            url: option.url,
            type: 'post',
            data: data
          })
          //.then(badSession)
          .then(function(){
            $(window).trigger('hashchange');
          })
          .fail(requestError);      
      }


      var showData,searchPath;
      //初始化页面
      function initPage(options) {
          showData = options.showData;
          searchPath = options.dataPath;
          showTpl(options.tplPath)
          .then(function(){
            //拿数据
            if(!options.dataPath){
              return false;
            }
              return requestData(options.dataPath)
              .then(function(data,status,xhr){
                  sessionStorage.pagerData = JSON.stringify(data);
                  return options.showData(data,status,xhr);
              })
              .fail(requestError);
          })
          .then(function(){
              options.bindEvent();
              if(options.submitParam) {
                formSubmit(options.submitParam);
              }
          });
      }

      //获取数据的接口地址 有接口地址时再行解注
      function getDataPath(url,queryObject){
        if(!url){
            return false;
        }
        if (queryObject){
          return dataUrl + url + '?' + $.param(queryObject);
        } else {
          return dataUrl + url;
        }
      }
      var router = {
          //机构信息
          '/agency_information/agency_information/index.html':{
              dataPath: '/org/show',
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                        otherValidate: function(){

                          return true;
                        },
                        //submitUrl: '',
                        getFormData: function() {
                            return this.form.serialize();
                        }
                      }
                  });
              },
              showData: function(data) {
                  //这里填写每个页面如何处理拿到的数据
                    console.log(data);
                  var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                  var content=data.data.org.content;
                  var totalRows=data.data.org.totalRows;
                  var html='';
                  for(var i=0;i<content.length;i++){
                      html+='<tr>';
                      html+='<td  class="column-title">'+(i+1)+'</td>';
                      html+='<td  class="column-title">'+content[i].orgNumber+'</td>';
                      html+=' <td  class="column-title">'+content[i].orgName+'</td>';
                      if(content[i].supOrgNumber == '100000'){
                        html+='<td  class="column-title">尊远本部</td>';
                      }else{
                        html+='<td  class="column-title">'+content[i].supOrgNumber+'</td>';
                      }
                      html+='<td  class="column-title">';
                      html+=' <a href= "#/agency_information/agency_information/amend_agency_information.html" class="fa fa-lg fa-pencil-square-o" data-role="edit" data-edit-id="'+content[i].id+'" title="编辑"></a>';
                      html+='<a href= "javascript:void(0);" data-delete-path="/org/delete" data-delete-id="'+content[i].orgNumber+'" data-role="delete" data-key="orgNumber" class="fa fa-lg fa-trash-o" title="删除"></a>';
                      html+=' </td>';
                      html+=' </tr>';
                  }
                  $('tbody.text-center').html(html);
                  $('.page_number').html('共'+totalRows+'条');
              },
            bindEvent: function() {
                //这里填写每个页面需要绑定的一些事件
                var _this = this;
                var data = JSON.parse(sessionStorage.pagerData);
                readyForPager(_this,data.data.org);
                $('#search-form').data({ context: _this, key: 'org' });
            }
        },
          //机构信息新增
          '/agency_information/agency_information/new_agency_information.html': {
              //每个页面对应的数据获取地址
              dataPath: '',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          submitUrl:dataUrl+'/org/add',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
              }
          },
          //机构信息修改
          '/agency_information/agency_information/amend_agency_information.html': {
              //每个页面对应的数据获取地址
              dataPath: '/org/index',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath,{id:sessionStorage.getItem('editId')}),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          submitUrl: dataUrl + '/org/orgUpdate',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                  console.log(data);
                  var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                  var id=sessionStorage.getItem('editId');
                  var orgName=data.data.OrgEntity.orgName;
                  var orgNumber=data.data.OrgEntity.orgNumber;
                  var supOrgNumber=data.data.OrgEntity.supOrgNumber;
                  $('#id').val(id);
                  $('#agency_number').val(orgNumber);
                  $('#agency_name').val(orgName);
                  $('#fore_agency').val(supOrgNumber);
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件

              }
          },
          //销售网点
          '/sell_point/sell_point/index.html':{
              dataPath: '/salesNetwork/show',
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          //submitUrl: '',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                  //这里填写每个页面如何处理拿到的数据
                  var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                  var content=data.data.organizationUser.content;
                  var totalRows=data.data.organizationUser.totalRows;
                  var html='';
                  for(var i=0;i<content.length;i++){
                      html+='<tr>';
                      html+='<td  class="column-title">'+(i+1)+'</td>';
                      html+='<td  class="column-title">'+content[i].netName+'</td>';
                      html+=' <td  class="column-title">'+content[i].netNumber+'</td>';
                      html+='<td  class="column-title">'+content[i].netAddress+'</td>';
                      html+='<td  class="column-title">'+content[i].orgEntity.orgName+'</td>';
                      html+='<td  class="column-title">';
                      html+=' <a href= "#/sell_point/sell_point/amend_sell_point.html" class="fa fa-lg fa-pencil-square-o" data-role="edit" data-edit-id="'+content[i].id+'" title="编辑"></a>';
                      html+='<a href= "javascript:void(0);" data-delete-path="/salesNetwork/delete" data-delete-id="'+content[i].netNumber+'" data-role="delete" data-key="id" class="fa fa-lg fa-trash-o" title="删除"></a>';
                      html+=' </td>';
                      html+=' </tr>';
                  }
                  $('tbody.text-center').html(html);
                  $('.page_number').html('共'+totalRows+'条');
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.organizationUser);
                  $('#search-form').data({ context: _this, key: 'organizationUser' });
              }
          },
          //销售网点新增
          '/sell_point/sell_point/new_sell_point.html': {
              //每个页面对应的数据获取地址
              dataPath: '/org/show',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){
                              return true;
                          },
                          submitUrl:dataUrl+'/salesNetwork/add',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
               var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                        var content=data.data.org.content;
                        var html ='<select id="orgNumber" property="orgs" class="form-control col-md-7 col-xs-12 user-read" name="orgNumber">';
                        for(var i=0;i<content.length;i++){
                          html+=" <option value="+content[i].orgNumber+">"+content[i].orgName+"</option>"
                        }
                        html+="</select>";
                        $('#orgs_opts').html(html);
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
              }
          },
          //机构信息修改
          '/sell_point/sell_point/amend_sell_point.html': {
              //每个页面对应的数据获取地址
              dataPath: '/salesNetwork/index',
              //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath,{id:sessionStorage.getItem('editId')}),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          submitUrl: dataUrl + '/salesNetwork/update',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                  var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                  var netAddress=data.data.SalesNetwork.netAddress;
                  var netName=data.data.SalesNetwork.netName;
                  var netNumber=data.data.SalesNetwork.netNumber;
                  var orgNumber=data.data.SalesNetwork.orgNumber;
                  var id=sessionStorage.getItem('editId');
                  $('#id').val(id);
                  $('#agency_number').val(netName);
                  $('#agency_name').val(netNumber);
                  $('#fore_agency').val(netAddress);
                  $('#sell_point').val(orgNumber);
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件

              }
          },
          //销售人员信息
          '/sell_person_infor/sell_person_infor/index.html':{
              dataPath: '/login/show',
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          //submitUrl: '',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                  //这里填写每个页面如何处理拿到的数据
                  console.log(data);
                  var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                  var content=data.data.sales.content;
                  var totalRows=data.data.sales.totalRows;
                  var html='';
                    for(var i=0;i<content.length;i++){
                        var salerId=content[i].salerId;
                        var netName=content[i].salesNetwork.netName;
                        var salerName=content[i].salerName;
                        var salerPhone=content[i].salerPhone;
                        html+='<tr>';
                        html+='<td  class="column-title">'+(i+1)+'</td>';
                        html+='<td  class="column-title">'+salerId+'</td>';
                        html+='<td  class="column-title">'+salerName+'</td>';
                        html+='<td  class="column-title">'+salerPhone+'</td>';
                        html+='<td  class="column-title">'+netName+'</td>';
                        html+='<td  class="column-title">';
                        html+=' <a href= "#/sell_person_infor/sell_person_infor/amend_sell_person.html" class="fa fa-lg fa-pencil-square-o" data-role="edit" data-edit-id="'+content[i].salerId+'" title="编辑"></a>';
                        html+='<a href= "javascript:void(0);" data-delete-path="/saler/delete" data-delete-id="'+content[i].salerId+'" data-role="delete" data-key="salerId" class="fa fa-lg fa-trash-o" title="删除"></a>';
                        html+='</td>';
                        html+=  ' </tr>';
                    }
                  $('tbody.text-center').html(html);
                  $('.page_number').html('共'+totalRows+'条');
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.sales);
                  $('#search-form').data({ context: _this, key: 'sales' });

                  $('#export').on('click',function(){
                      window.open(dataUrl +'/downLoad/saler');
                  });
              }
          },
          //销售人员新增
           '/sell_person_infor/sell_person_infor/new_sell_person.html': {
            //每个页面对应的数据获取地址
             dataPath: '/salesNetwork/show',
             //页面加载之后需要做的事
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){
                              return true;
                          },
                          submitUrl:dataUrl+'/saler/add',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                 var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                        var content=data.data.organizationUser.content;
                        var html ='<select id="net_number" property="orgs" class="form-control col-md-7 col-xs-12 user-read" name="netNumber">';
                        for(var i=0;i<content.length;i++){
                          html+=" <option value="+content[i].netNumber+">"+content[i].netName+"</option>"
                        }
                        html+="</select>";
                        $('#sales_opts').html(html);
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
              }
          },
          //销售人员修改
              '/sell_person_infor/sell_person_infor/amend_sell_person.html': {
                  //每个页面对应的数据获取地址
                  dataPath: '/saler/index',
                  //页面加载之后需要做的事
                  handle: function (tplPath) {
                      initPage({
                          tplPath: tplPath,
                          showData: this.showData,
                          dataPath: getDataPath(this.dataPath,{id:sessionStorage.getItem('editId')}),
                          bindEvent: this.bindEvent,
                          submitParam: {
                              otherValidate: function(){
                                  return true;
                              },
                              submitUrl: dataUrl + '/saler/update',
                              getFormData: function() {
                                  return this.form.serialize();
                              }
                          }
                      });
                  },
                  showData: function(data) {
                      var code=data.code;
                      if(code!='success'){
                          return false;
                      }

                      var salerName=data.data.SalesPerson.salerName;
                      var salerId=data.data.SalesPerson.salerId;
                      var salerPhone=data.data.SalesPerson.salerPhone;
                      var net_number=data.data.SalesPerson.netNumber;
                      var id=sessionStorage.getItem('editId');
                      $('#salerId').val(salerId);
                      $('#salerName').val(netName);
                      $('#salerPhone').val(salerPhone);
                      $('#net_number').val(net_number);

                  },
                  bindEvent: function() {
                      //这里填写每个页面需要绑定的一些事件

                  }
              },

          //商品信息
          '/product_information/product_information/index.html':{
              dataPath: '/product/show',
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          //submitUrl: '',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                  //这里填写每个页面如何处理拿到的数据
                  var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                  var content=data.data.organizationUser.content;
                  var totalRows=data.data.organizationUser.totalRows;
                  var html='';
                  for(var i=0;i<content.length;i++){
                      html+='<tr>';
                      html+='<td  class="column-title">'+(i+1)+'</td>';
                      html+='<td  class="column-title">'+content[i].orgNumber+'</td>';
                      html+=' <td  class="column-title">'+content[i].productNumber+'</td>';
                      html+='<td  class="column-title">'+content[i].productName+'</td>';
                      html+='<td  class="column-title">'+content[i].productPrice+'</td>';
                      html+='<td  class="column-title">'+content[i].productAmount+'</td>';
                      html+='<td  class="column-title">'+content[i].couponPoolNo+'</td>';
                      html+='<td  class="column-title">'+content[i].origProductAmount+'</td>';
                      html+='<td  class="column-title">';
                      html+=' <a href= "#/product_information/product_information/amend_product_information.html" class="fa fa-lg fa-pencil-square-o" data-role="edit" data-edit-id="'+content[i].id+'" title="编辑"></a>';
                      html+='<a href= "javascript:void(0);" data-delete-path="/product/delete" data-delete-id="'+content[i].productNumber+'" data-role="delete" data-key="productNumber" class="fa fa-lg fa-trash-o" title="删除"></a>';
                      html+=' </td>';
                      html+=' </tr>';
                  }
                  $('tbody.text-center').html(html);
                  $('.page_number').html('共'+totalRows+'条');
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.organizationUser);
                  $('#search-form').data({ context: _this, key: 'organizationUser' });
              }
          },
          //商品信息新增
          '/product_information/product_information/new_product_information.html':{
              dataPath: '',
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          submitUrl: dataUrl+'/product/add',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                  //这里填写每个页面如何处理拿到的数据
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件

              }
          },
          //商品信息修改
          '/product_information/product_information/amend_product_information.html':{
              dataPath: '/product/index',
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath,{id:sessionStorage.getItem('editId')}),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          submitUrl: dataUrl+'/product/productUpdate',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                  //这里填写每个页面如何处理拿到的数据
                    console.log(data);
                  var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                  var content=data.data.Product;
                  var couponPoolNo=content.couponPoolNo;
                  var orgNumber=content.orgNumber;
                  var origProductAmount=content.origProductAmount;
                  var productAmount=content.productAmount;
                  var productName=content.productName;
                  var productNumber=content.productNumber;
                  var productPrice=content.productPrice;
                  var id=sessionStorage.getItem('editId');
                  $('#id').val(id);
                  $('#agency_number').val(orgNumber);
                  $('#agency_name').val(productNumber);
                  $('#fore_agency').val(productName);
                  $('#product_price').val(productPrice);
                  $('#product_number').val(productAmount);
                  $('#couponPoolNo').val(couponPoolNo);
                  $('#origProductAmount').val(origProductAmount);
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件

              }
          },
        //订单信息
          '/order_goods_information/order_goods_information/index.html':{
              dataPath: '/order/show',
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          //submitUrl: '',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                  //这里填写每个页面如何处理拿到的数据
                  console.log(data);
                  var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                  var content=data.data.order.content;
                  var totalRows=data.data.order.totalRows;
                  var html='';
                  for(var i=0;i<content.length;i++){
                      html+='<tr>';
                      html+='<td  class="column-title">'+(i+1)+'</td>';
                      html+=' <td  class="column-title">'+content[i].orderId+'</td>';
                      html+='<td  class="column-title">'+content[i].orderType+'</td>';
                      html+='<td  class="column-title">'+content[i].tradeTime+'</td>';
                      html+='<td  class="column-title">'+content[i].tradeStatus+'</td>';
                      html+='<td  class="column-title">'+content[i].saleCode+'</td>';
                      html+=' <td  class="column-title">'+content[i].origOrderId+'</td>';
                      if(content[i].shopId){
                          html+=' <td  class="column-title">'+content[i].shopId+'</td>';
                      }else{
                          html+=' <td  class="column-title"> </td>';
                      }
                      if(content[i].orgNumber){
                          html+=' <td  class="column-title">'+content[i].orgNumber+'</td>';
                      }else{
                          html+=' <td  class="column-title"> </td>';
                      }
                      html+=' </tr>';
                  }
                  $('tbody.text-center').html(html);
                  $('.page_number').html('共'+totalRows+'条');
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件
                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.order);
                  $('#search-form').data({ context: _this, key: 'order' });

                  $('#export').on('click',function(){
                      window.open(dataUrl +'/downLoad/order');
                  });
              }
          },
        //申请信息
          '/apply_information/apply_information/index.html':{
              dataPath: '/apply/show',
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          //submitUrl: '',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                  //这里填写每个页面如何处理拿到的数据
                  console.log(data);
                  var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                  var content=data.data.apply.content;
                  var totalRows=data.data.apply.totalRows;
                  var html='';
                  for(var i=0;i<content.length;i++){
                      html+='<tr>';
                      html+='<td  class="column-title">'+(i+1)+'</td>';
                      html+=' <td  class="column-title">'+content[i].name+'</td>';
                      html+='<td  class="column-title">'+content[i].mobile+'</td>';
                      html+='<td  class="column-title">'+content[i].idNum+'</td>';
                      html+='<td  class="column-title">'+content[i].applyDate+'</td>';
                      if(content[i].applyStatus == '0'){
                          html+=' <td  class="column-title">申请已受理</td>';
                      }else if(content[i].applyStatus == '1'){
                          html+=' <td  class="column-title">申请已成功</td>';
                      }else{
                          html+=' <td  class="column-title">申请失败</td>';
                      }
                      if(content[i].applyType == '0'){
                          html+=' <td  class="column-title">自助申请</td>';
                      }else{
                          html+=' <td  class="column-title">销售推荐</td>';
                      }
                      html+='<td  class="column-title">'+content[i].saler.salerName+'</td>';
                      html+=' </tr>';
                  }
                  $('tbody.text-center').html(html);
                  $('.page_number').html('共'+totalRows+'条');
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件

                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.apply);
                  $('#search-form').data({ context: _this, key: 'apply' });

                  $('#export').on('click',function(){
                   //   window.open(dataUrl +'/downLoad/apply');
                      window.location.href = dataUrl + '/downLoad/apply?' + $("#search-form").serialize();
                  });
              }
          },
       //卡券信息
          '/coupon_information/coupon_information/index.html':{
              dataPath: '/coupon/show',
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          //submitUrl: '',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                  //这里填写每个页面如何处理拿到的数据
                  console.log(data);
                  var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                  var content=data.data.coupon.content;
                  var totalRows=data.data.coupon.totalRows;
                  var html='';
                  for(var i=0;i<content.length;i++){
                      html+='<tr>';
                      html+='<td  class="column-title">'+(i+1)+'</td>';
                      html+=' <td  class="column-title">'+content[i].couponType+'</td>';
                      html+='<td  class="column-title">'+content[i].couponInfo+'</td>';
                      html+='<td  class="column-title">'+content[i].couponValidDate+'</td>';
                      html+='<td  class="column-title">'+content[i].couponDesp+'</td>';
                      html+='<td  class="column-title">'+content[i].grantMember+'</td>';
                      html+='<td  class="column-title">'+content[i].grantTime+'</td>';
                      if(content[i].couponStatus == '0'){
                          html+=' <td  class="column-title">未发放</td>';
                      }else{
                          html+=' <td  class="column-title">已发放</td>';
                      }


                      html+=' </tr>';
                  }
                  $('tbody.text-center').html(html);
                  $('.page_number').html('共'+totalRows+'条');
              },
              bindEvent: function() {
              }
          },

        //会员信息
          '/member_information/member_information/index.html':{
              dataPath: '/member/show',
              handle: function (tplPath) {
                  initPage({
                      tplPath: tplPath,
                      showData: this.showData,
                      dataPath: getDataPath(this.dataPath),
                      bindEvent: this.bindEvent,
                      submitParam: {
                          otherValidate: function(){

                              return true;
                          },
                          //submitUrl: '',
                          getFormData: function() {
                              return this.form.serialize();
                          }
                      }
                  });
              },
              showData: function(data) {
                  //这里填写每个页面如何处理拿到的数据
                  console.log(data);
                  var code=data.code;
                  if(code!='success'){
                      return false;
                  }
                  var content=data.data.member.content;
                  var totalRows=data.data.member.totalRows;
                  var html='';
                  for(var i=0;i<content.length;i++){
                      html+='<tr>';
                      html+='<td  class="column-title">'+(i+1)+'</td>';
                      html+=' <td  class="column-title">'+content[i].memberId+'</td>';
                      html+='<td  class="column-title">'+content[i].memberName+'</td>';
                      html+='<td  class="column-title">'+content[i].memberPhone+'</td>';
                      html+='<td  class="column-title">'+content[i].memberCertNo+'</td>';
                      html+='<td  class="column-title">'+content[i].memberLevel+'</td>';
                      html+='<td  class="column-title">'+content[i].memberPoint+'</td>';
                      html+='<td  class="column-title">'+content[i].lastLoginTime+'</td>';
                      html+=' </tr>';
                  }
                  $('tbody.text-center').html(html);
                  $('.page_number').html('共'+totalRows+'条');
              },
              bindEvent: function() {
                  //这里填写每个页面需要绑定的一些事件

                  var _this = this;
                  var data = JSON.parse(sessionStorage.pagerData);
                  readyForPager(_this,data.data.member);
                  $('#search-form').data({ context: _this, key: 'member' });
              }
          },


      };
        //填充select
      function loadSelectOPtion(option){
          var method = option.method || 'append',
          optionStr = '';
          for(var i = 0,len = option.data.length; i < len; i++){
              optionStr += '<option value="'+ option.data[i][option.value] +'">' + option.data[i][option.text] + '</option>';
          }
          $(option.selector)[method](optionStr);
      }

      window.load_checkbox = function(data,param_nam,param_id,param_name,selector){
            var opts = '';
            for(var i = 1,len = data.length; i <= len; i++){
                opts += '<label class="col-xs-4"><input class="user-read" type="checkbox" name="'+ param_nam +'"  value="' + data[i-1][param_id] + '" required style="margin-right:2px;">' + data[i-1][param_name] + '</label>';
                if(i % 5 == 0) {
                    opts += '<br>';
                }
            }
            $(selector).append(opts);
        };


      function datePicker(){
         $('.datepicker').datepicker({
                  format:'yyyy-mm-dd',
                  todayHighlight: true,
                  autoclose:true,
                  language:'zh-CN'
              });
      }

      function setInput(dataInput){
        if($.isArray(dataInput)){
          $.each(dataInput,function(k,v){
             $('input').not('select,[type="checkbox"],[type="radio"]').each(function(){
              this.value = v[this.name];
            });
          });
        } else if($.isPlainObject(dataInput)) {
          $('input').not('select,[type="checkbox"],[type="radio"]').each(function(){
              this.value = dataInput[this.name];
          });
        }
      }

      window.cadleo_data = function(dataPath){
          !!location.hash.substring(1) && $.get(dataUrl + encodeURI(dataPath), function(data) {
             data.code == 0 && !showData(data) || (data.message && bootbox.alert(data.message)||bootbox.alert('请求出错，请重试'));
          });
      };

      /**
       * [fillInput 填充checkbox radio]
       * @param  {options.arr} 数组 [需要循环的数据的集合]
       * @param  {options.type} 类型 [input的类型]
       * @param  {options.arr[i].name} 类型 [input的name]
       * @param  {options.arr[i].value} string [input的value值]
       * @param  {options.arr[i].text} string [label中的文本]
       * @param  {options.required} boolean [是否必选，默认为false]
       * @param  {options.selector} string [需要填充的父容器的选择器]
       */
      fillInput = function(options){
          var opts = '';
          for(var i = 0,len = options.arr.length; i < len; i++){
              opts += '<label><input type="'+ options.type +'" name="'+ options.arr[i].name +'" value="' + options.arr[i].value + (options.required?'" required>':'" >') + options.arr[i].text + '</label>';
              if(i % 5 == 0) {
                  opts += '<br>';
              }
          }
          $(options.selector).append(opts);
      };

		  //编辑
	    $(document).on('click', '[data-role="edit"]', function(e){
          sessionStorage.setItem('editId', $(this).attr('data-edit-id'));
	    	  sessionStorage.setItem('typeName', $(this).attr('data-type-name'));
		  });

      //菜单点击加载页面
      $(document).on('click', '[data-hash="href"]', function(e){
        $(this).parent('li').siblings().removeClass('active current-page');
        if($(this).attr('href')==location.hash){
          $(window).trigger('hashchange');
        }
      });

      //返回按钮
      $(document).on('click', '[data-history="back"]', function(e){
        history.back();
        e.preventDefault();
      });

      //删除
  		$(document).on('click', '[data-role="delete"]', function(){
  			var _this = $(this),key = _this.attr('data-key');
        bootbox.confirm({
          message: '确认要删除这条数据吗?',
          callback: function(arg){
            if(arg){
        			deleteData({url: dataUrl + _this.attr('data-delete-path')+'?netNumber='+_this.attr('data-delete-id'),param: {key: key,value: _this.attr('data-delete-id')}});
            }
          }
        })
  		});

		  //详情页
   	  $(document).on('click','[data-role="detail"]',function(e){
        var _this = $(this);
        sessionStorage.setItem('detailId', _this.attr('data-detail-id'));
        _this.attr('data-detail-path') && $.ajax({
                                            url: _this.attr('data-detail-path'),
                                            type: 'GET',
                                        })
                                        .then(function(data){
                                            //$('#wj-main-content').html(data);
                                        })
                                        .fail(requestError);

   	  });
      

    //checkbox全选单选 
      function initTableCheckbox() {  
          var thr = $('#check-all');  
          var tbr = $('.check-single');   
          var checkAll = thr.find('input');  
            checkAll.click(function(event){  
              /*将所有行的选中状态设成全选框的选中状态*/  
              tbr.find('input').prop('checked',$(this).prop('checked'));  
              /*阻止向上冒泡，以防再次触发点击操作*/  
              event.stopPropagation();  
           });  
          /*点击全选框所在单元格时也触发全选框的点击操作*/  
          thr.click(function(){  
              $(this).find('input').click();  
          });
         
          tbr.find('input').click(function(event){  
              checkAll.prop('checked',tbr.find('input:checked').length == tbr.length ? true : false);  
              /*阻止向上冒泡，以防再次触发点击操作*/  
              event.stopPropagation();  
          });  
          /*点击每一行时也触发该行的选中操作*/  
          tbr.click(function(){  
              $(this).find('input').click();  
          }); 
      }  
       
      //checkbox全选单选结束


    	//搜索
    	$(document).on('submit','#search-form',function(){
            var _this = $(this),
                context = _this.data('context');
            requestData(context.dataPath + '?' + _this.serialize())
                .then(function(data){
                    if(data.code == 'success'){
                        context.showData(data);
                        readyForPager(context,data.data[_this.data('key')]);
                    }
                })
                .fail(requestError);
            return false;//阻止表单提交
    	});


    	$(document).on('change','#fileToUpload',function(){
    		var fileName = '';
    		if($(this).val().lastIndexOf('\\') !=-1){
    			fileName = $(this).val().substring($(this).val().lastIndexOf('\\')+1);
    		} else {
    			fileName = $(this).val().substring($(this).val().lastIndexOf('/')+1);
    		}
    		$(this).parent().siblings('.file-path').text('您选择的文件为：'+fileName).attr('title',fileName);
    	});

    	$(document).on('click','.upload-btn',function() {
    		var parent = $(this).parent();
    		if(!$(this).siblings().find('#fileToUpload').val()){
    			parent.find('.file-path').text('请先选择图片');
    			return ;
    		}
    		var val = $(this).siblings().find('#fileToUpload').val();
    		if(!/^(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$/.test(val.substring(val.lastIndexOf('.')))){
    			$(this).siblings().find('#fileToUpload').val('');
    			$(this).siblings('.file-path').text('只能上传后缀名为".jpg"，".jpeg"，".png"，".gif"，".bmp"的文件');
    			return ;
    		}
    		var path = $(this).siblings('.file-path');
        	$.ajaxFileUpload({
     	    	 url : dataUrl+'/upload.htmls',
     	    	 secureuri : false,
     	    	 fileElementId : 'fileToUpload',
     	    	 parentNode: parent,
     	    	 dataType : 'json',
     	    	 success : function(data, status) {
              //parent.parent('.view-img-pane').attr('src', dataUrl+'/showPic.htmls?pdfUrl='+data.picUrl).removeClass('collapse');
     	    		//parent.find().find('.view-img-shop ').attr('src', dataUrl+'/showPic.htmls?pdfUrl='+ data.data).removeClass('collapse');
              //parent.parent().find('#submit-file').val(data.picUrl);
     	    	 },
     	    	 error : function(xhr, status, error) {
     	    		path.text('服务器错误，上传出错，请重试');
     	    	 }
       		});
        	path.text('');
        });
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

        //登出
    	$(document).on('click','#un-login',function(){
            var str = dataUrl + $(this).data('path');
            $.ajax({
                 type: 'get',
                 url:dataUrl+'/destory.htmls',
                 dataType: 'html',
            })
            .then(function(data){
              if(!$.isPlainObject(data)){
                var data = $.parseJSON(data);
              }
              if(data.code == 'E10013'){
            	  location.href = '/happy_shop_web/login.html' || '/';
              }
            })
            .fail(requestError);
    	});

      // $.ajax({
      //     type: 'get',
      //     url: basePath + '/init.htmls',
      // })
      // .then(function(data){
      //     if(data.code == '0'){

      //     } else {
      //         location.href = basePath || '/';
      //     }
      // })
      // .fail(requestError);

      //F5刷新时加载数据
      setTimeout(function(){
      	$(window).trigger('hashchange');
      });

      function light_cad(lightCad,time){
          lightCad.css({left:-680}).animate({left:'120%'},time,function(){
            lightCad.css({left:-680});
            light_cad(lightCad,time);
          });
      }
      if(document.body.style.transition===undefined){
        var lightCad = $('<div class="light-cad"></div>').appendTo('.welcome-content').css({height:$('html')[0].offsetHeight});
        lightCad.css({opacity: 0}).animate({left:'120%'},300,function(){
          lightCad.css({opacity: 1});
          light_cad(lightCad,2000);
        });
      }

	});
});

