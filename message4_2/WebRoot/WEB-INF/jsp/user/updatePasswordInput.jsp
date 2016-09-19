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
	$.msg.userUpdatePwd();
});

</script>
</head>
<body>
<h3 style="text-align:center">密码修改</h3>
<s:fielderror/>
<s:form action="user_updatePassword" method="post" id="myform">
<table align="center" width="500" border="1px">
	<tr>
		<td>用户名：</td>
		<td><s:property value="#session.loginUser.username"/>
		[${session.loginUser.nickname }]
			<s:hidden value="%{#session.loginUser.id}" name="id"/>
		</td>
	</tr>
	<tr>
		<td>原始密码:</td>
		<td><s:password name="oldPwd" cssClass="required"/></td>
	</tr>
	<tr>
		<td>新密码：</td>
		<td><s:password name="password" cssClass="required" id="password"/></td>
	</tr>
	<tr>
		<td>确认密码：</td>
		<td><s:password name="confirmPwd"/>
		<s:fielderror fieldName="confirmPwd"/></td>
	</tr>
	<tr>
		<td colspan="2" align="center"><input type="submit" value="修改密码"/><input type="reset" value="重置"/></td>
	</tr>
</table>
</s:form>
</body>
</html>