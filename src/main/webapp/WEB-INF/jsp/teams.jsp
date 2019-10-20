<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Teams Status</title>
</head>
<body>

	<h1>Teams</h1>


	<div class="col-md-8">

		<s:iterator value="teams">

			<div class="input-group">

				<s:url var="teamSummaryUrl" action="teams" method="teamSummary"
					namespace="/private">
					<s:param name="teamKey">${key}</s:param>
				</s:url>

				<s:a class="form-control" href="%{#teamSummaryUrl}">
					<s:property value="name" />
				</s:a>

				<s:url var="editTeamUrl" action="teams" method="editTeam"
					namespace="/private">
					<s:param name="teamKey">${key}</s:param>
				</s:url>

				<span class="input-group-btn"> <s:a class="btn btn-default"
						role="button" href="%{#editTeamUrl}"><span title="Edit" class="glyphicon glyphicon-edit"></span></s:a>
				</span>
			</div>

			<br />
		</s:iterator>

		<div>
			<s:url var="addTeamUrl" action="teams" method="addTeam"
				namespace="/private">
			</s:url>

			<s:a class="btn btn-primary" role="button" href="%{#addTeamUrl}">Add New Team</s:a>
		</div>
	</div>

</body>

</body>
</html>

