<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Users</title>
</head>
<body>

	<h1>Users</h1>

	<br />

	<div class="col-md-8">

		<div class="list-group">
			<s:iterator value="allUsers">

				<div class="list-group-item">
					<s:url var="editUserUrl" action="user" namespace="/private">
						<s:param name="userKey">${key}</s:param>
						<s:param name="teamKey">${team}</s:param>
					</s:url>

						<span class="glyphicon glyphicon-user">
					</span> -
					<s:a href="%{#editUserUrl}">
						<s:property value="fullname" /> (<s:property value="nickname" />)
				</s:a>
					<s:url var="toggleTrainerUrl" action="user" method="toggleTrainer" namespace="/private">
						<s:param name="userKey">${key}</s:param>
						<s:param name="teamKey">${team}</s:param>
					</s:url>
				
				<s:a href="%{#toggleTrainerUrl}" class="pull-right">
				<s:if test="!trainer">
				<span title="Make Trainer" class="glyphicon glyphicon-star-empty"></span></s:if>
				<s:else><span title="Make Trainer" class="glyphicon glyphicon-star"></span></s:else></s:a>
				</div>
			</s:iterator>
		</div>

	</div>

</body>

</body>
</html>

