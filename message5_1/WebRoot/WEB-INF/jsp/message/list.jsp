<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>留言列表</title>
</head>
<body>
<h3 style="text-align:center">留言列表</h3>
<table align="center" width="900" cellpadding="0" cellspacing="0" class="bc">
	<tr><td colspan="4">
	<form action="index_search.action" method="post">
		输入关键字:<input type="text" size="20" name="con" value="${con }"/>
		<input type="submit" value="搜索"/>
		</form>
	</td></tr>
	<tr>
	<td width="500">标题</td><td>发布时间</td><td>作者</td><td>评论数量</td>
	</tr>
	<s:if test="#pages.datas==null">
	<tr><td colspan="4">还没有任何留言信息</td></tr>
	</s:if>
	<s:else>
		<s:iterator value="#pages.datas">
			<tr>
			<td><a href="message_show.action?id=${id }">${title }</a></td>
			<td>
				<s:date name="createDate" format="yyyy-MM-dd HH:ss"/>
			</td>
			<td>
				<s:if test="#session.loginUser!=null">
					<a href="user_show.action?id=${user.id }">${user.nickname }</a>
				</s:if>
				<s:else>
					${user.nickname }			
				</s:else>
			</td>
			<td>0</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="4">
				<jsp:include page="/inc/pager.jsp">
					<jsp:param value="${pages.totalRecord }" name="totalRecord"/>
					<jsp:param value="message_list.action" name="url"/>
				</jsp:include>
			</td>
		</tr>
	</s:else>
</table>
</body>
</html>