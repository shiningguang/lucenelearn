<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3 style="text-align:center">用户登录</h3>
<s:fielderror/>
<s:form action="user_login" method="post">
<table align="center" width="500" border="1px">
	<tr>
		<td>用户名：</td><td><s:textfield name="username"/></td>
	</tr>
	<tr>
		<td>用户密码：</td><td><s:password name="password"/></td>
	</tr>
	<tr>
		<td colspan="2" align="center"><input type="submit" value="登录"/><input type="reset" value="重置"/></td>
	</tr>
</table>
</s:form>
</body>
</html>