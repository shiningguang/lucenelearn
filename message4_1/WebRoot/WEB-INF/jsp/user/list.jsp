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
	$.msg.userList();
});
</script>
</head>
<body>
<form action="user_list.action" method="post">
<h3 style="text-align:center">输入用户名或昵称:<input type="text" name="username" id="username" value="${username }"/>
	<input type="submit" value="查询"/></h3>
</form>

<table  class="bc" width="800px" align="center" cellpadding="0" cellspacing="0">
	<thead>
		<tr>
		<td>用户名</td><td>用户昵称</td><td>用户类型</td><td>用户状态</td><td>是否禁止</td>
		<td>电子邮件</td>
		<td>全选<input type="checkbox" id="all"/></td>
		</tr>
	</thead>
	<s:iterator value="#pages.datas" >
		<tr uid="${id }">
		<td>${username }</td>
		<td edit="${nickname }" text='nickname' type='nickname'>${nickname }</td>
		<td <s:if test="username!='admin'">edit="${type }" select="userType" type='type'</s:if>>
			<s:if test="type eq 1">普通用户</s:if>
			<s:elseif test="type eq 3">超级管理员</s:elseif>
		</td>
		<td <s:if test="username!='admin'">edit="${status }" select="userStatus" type='status'</s:if>>
			<s:if test="status eq 0">启用</s:if>
			<s:else><span class="emphasis">停用</span></s:else>
		</td>
		<td <s:if test="username!='admin'">edit="${hiddenMsg }" select="userHidden" type='hiddenMsg'</s:if>>
			<s:if test="hiddenMsg eq 0 ">未禁止</s:if>
			<s:else><span class="emphasis">禁止</span></s:else>
		</td>
		<td edit="${email }" text="email" type='email'>${email }</td>
		<td>
			<input type="checkbox" value="${id }" name="users"/>
		</td>
		</tr>
	</s:iterator>
	<tr id="pages">
		<td colspan="7">
		<!-- jsp: -->
		<jsp:include page="/inc/pager.jsp">
			<jsp:param value="${pages.totalRecord }" name="totalRecord"/>
			<jsp:param value="user_list.action" name="url"/>
			<jsp:param value="username" name="params"/>
		</jsp:include>
		</td>
	</tr>
</table>
</body>
</html>