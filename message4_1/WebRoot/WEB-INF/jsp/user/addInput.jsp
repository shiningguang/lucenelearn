<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
$(function(){
	$.msg.userAdd();
});

</script>
</head>
<body>
<h3 style="text-align:center">用户注册</h3>
<s:fielderror/>
<s:form action="user_add" method="post" id="myform">
<table align="center" width="500" border="1px">
	<tr>
		<td>用户名：</td>
		<td><s:textfield name="username" id="username"/><s:fielderror fieldName="username"/></td>
	</tr>
	<tr>
		<td>用户密码：</td>
		<td><s:password name="password" cssClass="required" id="password"/><s:fielderror fieldName="password"/></td>
	</tr>
	<tr>
		<td>确认密码：</td>
		<td><s:password name="confirmPwd"/>
		<s:fielderror fieldName="confirmPwd"/></td>
	</tr>
	<tr>
		<td>用户昵称：</td>
		<td><s:textfield name="nickname"/></td>
	</tr>
	<tr>
		<td>电子邮件：</td>
		<td><s:textfield name="email"/></td>
	</tr>
	<tr>
		<td colspan="2" align="center"><input type="submit" value="注册"/><input type="reset" value="重置"/></td>
	</tr>
</table>
</s:form>
</body>
</html>