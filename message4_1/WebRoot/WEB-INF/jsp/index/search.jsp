<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>留言检索</title>
</head>
<body>
<table align="center" width="900" cellpadding="0" cellspacing="0" class="bc">
	<tr><td>
	<form action="index_search.action" method="post">
		输入关键字:<input type="text" size="20" name="con" value="${con }"/>
		共搜索出${pages.totalRecord }条记录
		<input type="submit" value="搜索"/>
		</form>
	</td></tr>
	<s:iterator value="#pages.datas">
		<tr>
			<td>
				<a href="message_show.action?id=${msgId }">${title }</a><br/>
				<hr/>
				${summary }
			</td>
		</tr>
	</s:iterator>
	<tr>
		<td>
			<jsp:include page="/inc/pager.jsp">
				<jsp:param value="${pages.totalRecord }" name="totalRecord"/>
				<jsp:param value="index_search.action" name="url"/>
			</jsp:include>
		</td>
	</tr>
</table>
</body>
</html>