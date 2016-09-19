<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示用户信息</title>
</head>
<body>
<jsp:include page="/inc/error.jsp"/>
<h3 style="text-align:center">显示用户信息</h3>
<table align="center" width="500" border="1px">
	<tr>
		<td>用户名：</td><td>${user.username }</td>
	</tr>
	<tr>
		<td>用户昵称：</td><td>${user.nickname }</td>
	</tr>
	<tr>
		<td>电子邮件：</td>
		<td><a href="mailto:${user.email }">${user.email }</a></td>
	</tr>
	<tr><td>用户类型：</td>
	<td>
		<c:if test="${user.type eq 1 }">注册用户</c:if><c:if test="${user.type eq 3 }">超级管理员</c:if>
	</td>
	</tr>
	<tr>
		<td>用户状态：</td>
		<td><c:if test="${user.status eq 0 }">已启用</c:if><c:if test="${user.status eq 1 }"><span style="color:red">停用</span></c:if></td>
	</tr>
	<tr>
		<td>是否禁止留言：</td>
		<td><c:if test="${user.hiddenMsg eq 0 }">正常</c:if><c:if test="${user.hiddenMsg eq 1 }"><span style="color:red">禁止</span></c:if></td>
	</tr>
	<tr>
		<td>用户创建时间：</td>
		<td><fmt:formatDate value="${user.createDate }" pattern="yyyy-MM-dd HH:ss"/></td>
	</tr>
</table>
</body>
</html>