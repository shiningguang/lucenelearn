<%@taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="<%=request.getContextPath() %>/js/ui/themes/base/jquery.ui.all.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/main.css" type="text/css"></link>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script src="<%=request.getContextPath() %>/js/ui/ui/jquery.ui.core.js"></script>
<script src="<%=request.getContextPath() %>/js/ui/ui/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/js/ui/ui/jquery.ui.mouse.js"></script>
<script src="<%=request.getContextPath() %>/js/ui/ui/jquery.ui.draggable.js"></script>
<script src="<%=request.getContextPath() %>/js/ui/ui/jquery.ui.position.js"></script>
<script src="<%=request.getContextPath() %>/js/ui/ui/jquery.ui.resizable.js"></script>
<script src="<%=request.getContextPath() %>/js/ui/ui/jquery.ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/validate/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/validate/localization/messages_cn.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/msg/inc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/msg/msg.js"></script>
<style type="text/css">
	.msgDialog {
	 display: none;
	}
</style>
<decorator:head />
</head>
<body>
<p style="text-align:right">
<s:if test="#session.loginUser!=null">
Hi,${loginUser.nickname }
<a href="message_addInput.action">添加留言</a>&nbsp;
<a href="<%=request.getContextPath()%>/user/message.do?method=manager">留言管理</a>&nbsp;
<a href="user_updatePasswordInput.action">修改密码</a>&nbsp;
<a href="<%=request.getContextPath()%>/user/user.do?method=updateInput&id=${loginUser.id}">修改个人信息</a>&nbsp;
<a href="user_logout.action">退出系统</a>&nbsp;&nbsp;
</s:if>
<s:else>
<a href="#" id="userLogin">用户登录</a>&nbsp;
<a href="user_addInput.action">用户注册</a>&nbsp;
</s:else>
<a href="#" id="authRefresh">重置权限</a>
</p>
<p>
<a href="message_list.action">留言列表</a>
<s:if test="#session.loginUser.type==3">
<a href="user_list.action">用户列表</a>
<a href="index_show.action">索引管理</a>
</s:if>
</p>
<hr/>
<decorator:body />
<hr/>
<h3 style="text-align:center">CopyRight 2011-2019</h3>
<div id="userLoginDialog" class="msgDialog">
<form id="userLoginForm" action="" method="post">
	<table cellpadding="0" cellspacing="0">
		<Tr><td>用户名:</td><td><input type="text" name="username" class="required" id="loginUsername"/></td></Tr>
		<Tr><td>密码:</td><td><input type="password" name="password" class="required" id="loginPassword"/></td></Tr>
		<tr><td id="loginError" colspan="2"></td></tr>
	</table>
</form>
</div>
</body>
</html>