<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Challenge Status</title>
</head>
<body>

	<h2>
		<s:property value="name" />
	</h2>

	<h3>Goal: <s:property value="goal" /> <s:property value="unit" /></h3>
	<br />

	<s:iterator value="players">

		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">
					<s:property value="nickname" /> (<s:property value="fullname" />)
				</h3>
			</div>
			<div class="panel-body">
				<div>
					Current total:
					<s:property value="score" />
				</div>
				<div class="progress">
					<div class="progress-bar" role="progressbar"
						aria-valuenow="<s:property value="percentageComplete"/>"
						aria-valuemin="0" aria-valuemax="100"
						style="min-width: 2em; width: <s:property value="percentageComplete"/>%;">
						<s:property value="percentageComplete" />%
					</div>
				</div>

			</div>
		</div>
	</s:iterator>
	
</html>
