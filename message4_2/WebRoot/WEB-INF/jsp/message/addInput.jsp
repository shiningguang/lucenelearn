<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>留言添加页面</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/file/jquery.uploadify.v2.1.4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/file/swfobject.js"></script>

<script type="text/javascript">
$(function(){
	$.msg.msgAdd();
});

</script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/js/file/uploadify.css" type="text/css"></link></head>
<body>
<input type="hidden" id="contextPath" value="<%=request.getContextPath() %>"/>
<input type="hidden" id="sessionId" value="<%=session.getId() %>"/>
<form id="addForm" action="message_add.action" method="post" enctype="multipart/form-data">
<input type="hidden" name="userId" value="${loginUser.id }">
<table width="1000" align="center" border="1">
<tr>
<td colspan="2"><span style="color:#f00">${loginUser.nickname }</span>添加留言</td>
</tr>
<tr>
	<td width="100">留言标题</td>
	<td>
		<input type="text" name="title" size="50" value="${title}" class="required">
	</td>
</tr>
<tr>
	<td>留言附件</td>
	<td>
		<div id="attachs"></div>
		<input type="file" id="attach" name="attach"/>
		<input type="button" id="uploadFile" value="上传文件"/>
	</td>
</tr>
<tr>
	<td>已上传附件</td>
	<td>
	<div id="alreadyAttachs"></div>
	</td>
</tr>
<tr>
	<td width="100">留言内容</td>
	<td>
		<textarea rows="20" cols="50" name="content" id="content" class="required">
			${content }
		</textarea>
	</td>
</tr>
<tr>
	<td colspan="2" align="center">
		<input type="submit" value="添加留言"/>
		<input type="reset"/>
	</td>
</tr>
</table>
</form>
</body>
</html>