var CHECK_RESULT;
$.validator.addMethod("userExist",function(value){
	$.ajax({
		type: "POST",
	    url: "json_checkUser.action",
	    data: {username:value},
	    success: checkuser,
	    dataType:"json",
	    async:false//一定要设置成同步模式
	});
	return CHECK_RESULT;
},"<span style='color:red'>用户名已经存在</span>");
function checkuser(data) {
	CHECK_RESULT = (data.result==1);
}

function checkimg(type) {
	var imgs = ["jp?g","gif","bmp","png"];
	type = type.toLowerCase();
	for(var i=0;i<imgs.length;i++) {
		if(type.match(imgs[i]))return true;
	}
	return false;
}

function checkOp(data) {
	if(data.result==1) return true
	else return false;
}

$(function(){
	var loginForm = $("#userLoginForm").validate();
	var loginDialog;
	$("#userLogin").click(function(){
		loginDialog = $("#userLoginDialog").dialog({
			title:"用户登录",
			modal:true,
			resizable:false,
			width:450,
			buttons:{
				"用户登录":function(){
					if(loginForm.checkForm()){
						var username = $("#loginUsername").val();
						var password = $("#loginPassword").val();
						$.post("json_login.action",{username:username,password:password},checklogin,"json");
					} else {
						loginForm.form();
					}
				}
			},
			close:function(){
				$("#loginUsername").val("");
				$("#loginPassword").val("");
			}
		});
	});
	function checklogin(data) {
		if(checkOp(data)) {
			loginDialog.dialog("close");
			window.location.reload(true);
		} else {
			$("#loginError").html(data.msg);
		}
	}
	$("#authRefresh").click(function(){
		$.getJSON("json_authRefresh.action",null,function(data){
			alert(data.msg);
		});
	});
});