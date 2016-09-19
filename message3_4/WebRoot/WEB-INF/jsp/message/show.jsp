<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示留言信息</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/ckeditor/ckeditor.js"></script>
<script type="text/javascript">
$(function(){
	$.msg.msgShow();
});
</script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body>
<table class="bc" align="center" width="900" cellpadding="0" cellspacing="0">
	<tr>
	<td style="font-weight:bolder;">
	<s:property value="message.title"/>
	</td>
	</tr>
	<tr>
	<td>发布人员:${message.user.nickname },发布时间:<fmt:formatDate value="${message.createDate }" pattern="yyyy-MM-dd HH:ss"/></td>
	</tr>
	<tr>
	<td>
	${message.content }
	</td>
	</tr>
	<c:if test="${not empty loginUser and loginUser.id==message.user.id }">
	<tr>
		<td><A href="user/message_updateInput.action?id=${id }">编辑</A></td>
	</tr>
	</c:if>
	<tr>
	<td>留言评论</td>
	</tr>
	<s:if test="#comments.totalRecord<=0">
		<tr><td>还没有评论</td></tr>
	</s:if>
	<s:else>
	<tr><td>
		<table align="center" width="900" cellpadding="0" cellspacing="0" id="commentList">
		<c:forEach items="${ comments.datas}" var="comment" varStatus="s">
			<tr><td>
				作者:${comment.user.nickname }
				(<fmt:formatDate value="${comment.createDate }" pattern="yy-MM-dd HH:ss"/>)
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<c:if test="${(not empty loginUser) and (loginUser.type==3 or comment.user.id==loginUser.id)}">
					<a href="user/message.do?method=deleteComment&id=${comment.id }&msgId=${message.id}">删除评论</a>
				</c:if>
			</td></tr>
			<tr>
				<td>
					${comment.content }
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td>
			<jsp:include page="/inc/pager.jsp">
				<jsp:param value="${comments.totalRecord }" name="totalRecord"/>
				<jsp:param value="message_show.action" name="url"/>
				<jsp:param value="id" name="params"/>
			</jsp:include>
			</td>
		</tr>
		</table>
	</td></tr>
	</s:else>
</table>
<c:if test="${not empty loginUser }">
<input type="hidden" id="userId" value="${loginUser.id }">
<input type="hidden" id="messageId" value="${message.id }">
<table align="center" width="900" border="1">
	<tr>
	<td>评论</td>
	<td>
	<textarea rows="10" cols="30" id="comment_content" name="content"></textarea>
	</td>
	</tr>
	<tr>
	<td colspan="2" align="center"><input type="button" value="发布评论" id="addComment"/></td>
	
	</tr>
</table>
</c:if>
</body>
</html>