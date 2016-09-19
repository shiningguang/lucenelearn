/**
 * $.fn就是$.prototype
 * 要为jquery增加方法使用这中方式
 * $.showElement= function(){}这是将showElement作为全局函数，有可能会冲突
 * 所以建议使用的方式是$.prototype
 */
$.fn.showElement = function(attr) {
	$(this).each(function(i){
		if(attr) {
			alert($(this).attr(attr));
		} else {
			alert($(this).html());
		}
	});
	return this;
}
$.msg = {
		msgShow:function() {
			CKEDITOR.replace("comment_content",{
				skin:'office2003',
				toolbar:'Basic'
			});
			$("#addComment").click(function(){
				var userId = $("#userId").val();
				var msgId = $("#messageId").val();
				var content = CKEDITOR.instances.comment_content.getData();
				if(content=="") {
					alert("必须填入评论内容");
					return false;
				}
				$.post("json_commentAdd.action",{userId:userId,messageId:msgId,content:content},function(data){
					if(checkOp(data)) {
						var node = "<tr><td>作者:";
						var obj = data.obj;
						node+=obj.user.nickname;
						node+="</td></tr>";
						node+="<tr><td>"+obj.content+"</td></tr>";
						$("#commentList").prepend(node);
						CKEDITOR.instances.comment_content.setData("");
					} else {
						alert(data.msg);
					}
				},"json");
			});
		},
		msgAdd:function(){
			$("#addForm").validate();
			CKEDITOR.replace("content",{
				skin:'office2003',
				toolbar:[
				         { name: 'document',    items : [ 'Source'] },
				         { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
				         { name: 'paragraph',   items : [ 'Outdent','Indent','-','Blockquote','CreateDiv','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
				         { name: 'links',       items : [ 'Link','Unlink','Anchor' ] },
				         { name: 'insert',      items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak' ] },
				         '/',
				         { name: 'styles',      items : [ 'Styles','Format','Font','FontSize' ] },
				         { name: 'colors',      items : [ 'TextColor','BGColor' ] }
				     ],
				smiley_images:['01.jpg','02.jpg','03.jpg','04.jpg','05.jpg'],
				smiley_path:CKEDITOR.basePath + 'plugins/smiley/taobao_img/'
			});
			var contextPath = $("#contextPath").val();
			var sessionId = $("#sessionId").val();
			var allowDescType = "jpg;png;gif;txt;doc;xls;pdf;rar";
			var allowType = "*.jpg;*.png;*.gif;*.txt;*.doc;*.xls;*.docx;*.xlsx;*.pdf;*.rar";
			$("#attach").uploadify({
				uploader: contextPath+"/js/file/uploadify.swf",//指定文件上传的进度条的位置
				script:"json_uploadAttach.action?jsessionId="+$("#sessionId").val(),//指定用那个服务器端程序处理文件操作
				cancelImg: contextPath+"/js/file/cancel.png",//取消文件上传的图标
				fileDataName:"attach",//文件上传到服务器的名称(使用file中的name)
				method:"post",//通过post方式上传
				queueID:"attachs",//上传队列说存储的位置
				auto:false,//是否选中之后就上传,false表示选中之后不上传
				multi:true,//时候支持多文件上传
				fileDesc:"请选择"+allowDescType,
				fileExt:allowType,
				onComplete:fileComplete,//当一个文件上传完成之后执行该方法
				onAllComplete:bindEvent
			});
			$("#uploadFile").click(function(){
				$("#attach").uploadifyUpload();
			});
			
			function bindEvent() {
				$(".deleteAttach").click(function(){
					var id = this.id;
					var div = $(this).parent("div");
					$.post("json_deleteAttach.action",{id:id},function(data){
						if(checkOp(data)) {
							$(div).remove();
						} else {
							alert(data.msg);
						}
					},"json");
				});
				$(".insertAttach").click(function(){
					var div = $(this).parent("div");
					var type = $(div).attr("type");
					var addNode,address;
					var contextPath = $("#contextPath").val();
					address = contextPath+"/upload/"+$(div).attr("newname");
					if(checkimg(type)) {
						addNode = "<img src='"+address+"'/>";
					} else {
						addNode = "<a href="+address+">"+$(this).prev().html()+"</a>";
					}
					var oEditor = CKEDITOR.instances.content;
					if ( oEditor.mode == 'wysiwyg' )
					{
						// http://docs.cksource.com/ckeditor_api/symbols/CKEDITOR.editor.html#insertText
						oEditor.insertHtml( addNode);
					}
					else
						alert( 'You must be in WYSIWYG mode!' );
				});
			}
			function fileComplete(event, ID, fileObj, response, data) {
				var rel = $.parseJSON(response);
				if(checkOp(rel)) {
					var obj = rel.obj;
					var node = "<div id="+obj.id+" newname="+obj.newName+" type='"+fileObj.type+"'>" +
							"<span>"+obj.oldName+"</span>" +
							"<input type='button' value='插入' class='insertAttach'/>" +
							"<input id="+obj.id+" type='button' value='删除' class='deleteAttach'/>" +
							"<input type='hidden' name='myatt' value='"+obj.id+"'/></div>" ;
					$("#alreadyAttachs").append(node);
				} else {
					alert(rel.msg);
				}
			}
			
		},
		userAdd:function(){
			$("#myform").validate({
				rules:{
					username:{
						required:true,
						userExist:true
					},
					confirmPwd:{
						equalTo:"#password"
					},
					email:{
						email:true
					}
				},
				messages:{
					confirmPwd:"两次输入的密码必须一致"
				}
			});
		},
		userList:function(){
			$("table tbody tr:even").addClass("even");
			$("table tbody tr:odd").addClass("odd");
			$("table tbody tr").bind("mouseenter mouseleave",function(){
				$(this).toggleClass("hover");
			});
			$("#all").click(function(){
				if($(this).attr("checked")) {
					$("input[name='users']").attr("checked","checked");
				} else {
					$("input[name='users']").removeAttr("checked");
				}
			});
			//1、获取要修改用户的表格
			var oldUser,oldHtml,value,ot,dic;
			$("table tbody tr[id!='pages'] td").dblclick(function(){
				//2、根据表格上面的属性edit来判断是否要响应 
				if($(this).attr("edit")||$(this).attr("edit")=="") {
					if(oldUser) {
						$(oldUser).html(oldHtml);
					}
					//保存原有对象
					oldUser = this;oldHtml = $(this).html();
					var uid = $(this).parent("tr").attr("uid");
					var btn = "<input type='button' uid='"+uid+"' value='编辑' class='userEdit' utype='"+$(this).attr("type")+"'/>";
					if($(this).attr("text")) {
						ot = "text";
						var txt = "<input size='10' type='text' value='"+$(this).attr("edit")+"'/>";
						$(this).html(txt+btn);
					} else if($(this).attr("select")){
						ot = "select";
						dic = $(this).attr("select");
						$(this).html(dic2select(dic,$(this).attr("edit"))+btn);
					}
				} else {
					if(oldUser) {
						$(oldUser).html(oldHtml);
					}
				}
				$(".userEdit").click(function(){
					var val = $(this).prev().val();
					var type = $(this).attr("utype");
					var id = $(this).attr("uid");
					//判断值是否有变变动，如果没有就不修改
					var ov = $(this).parent("td").attr("edit");
					if(ov==val) {
						$(oldUser).html(oldHtml);
						return false;
					}
					//通过上一级的td可以获取这个节点的类型
					if(ot=='text')
						value = val;
					else if(ot=='select') {
						value = getValue(dic,val);
					}
					$.post("json_updateUser.action",{id:id,utype:type,uvalue:val},function(data){
						if(checkOp(data)) {
							//修改成功
							$(oldUser).html(value);
							$(oldUser).attr("edit",value);
							oldUser = null;
							oldHtml = null;
							value = null;
						} else {
							alert(data.msg);
						}
					},"json");
				});
			});
		},
		userUpdatePwd:function(){
			$("#myform").validate({
				rules:{
					confirmPwd:{
						required:true,
						equalTo:"#password"
					}
				},
				messages: {
					confirmPwd:"两次输入的密码必须一致"
				}
			});
		}
}

$.msg.dic = {
		userType:[{'key':'1','value':'普通用户'},{'key':'3','value':'超级管理员'}],
		userStatus:[{'key':'0','value':'启用'},{'key':'1','value':'停用'}],
		userHidden:[{'key':'0','value':'未禁止'},{'key':'1','value':'禁止'}]
};

function getValue(type,value) {
	var node = $.msg.dic[type];
	for(var i=0;i<node.length;i++) {
		if(node[i].key==value) return node[i].value;
	}
}

function dic2select(dic,value) {
	var ds = $.msg.dic[dic];
	var s ="<select>";
	for(var i=0;i<ds.length;i++) {
		if(value&&value==ds[i].key) {
			s+="<option value='"+ds[i].key+"' selected>"+ds[i].value+"</option>";
		} else {
			s+="<option value='"+ds[i].key+"'>"+ds[i].value+"</option>";
		}
	}
	s+="</select>";
	return s;
}
