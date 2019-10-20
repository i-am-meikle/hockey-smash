<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Login Failed</title>
</head>
<body>

	<h2>Login Failed</h2>
	<br />

	<s:url var="resetPassswordUrl" action="login" method="resetPassword">
		<s:param name="fullname">
			<s:property value="fullname" />
		</s:param>
	</s:url>

	<s:a class="btn btn-primary btn-success" action="status">Return to home</s:a>
	<s:a class="btn btn-primary btn-primary" href="%{#resetPassswordUrl}">Reset Password</s:a>
</body>
</html>

