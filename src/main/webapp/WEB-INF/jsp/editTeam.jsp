<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Team</title>
</head>
<body>

	<s:if test="name == null">
		<h1>Add Team</h1>
	</s:if>
	<s:else>
		<h1>Edit Team</h1>
	</s:else>

	<s:form class="form-horizontal" id="edit_team_form" action="teams">

		<fieldset>

			<div class="form-group">
				<div class="col-md-8  inputGroupContainer">
					<div class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-pencil"></i></span>
						<s:textfield name="name" placeholder="Name" class="form-control"
							type="text" />
						<div class="input-group-btn">
							<s:submit class="btn btn-success btn-primary" method="saveTeam"
								value="Save" />
						</div>
					</div>
				</div>
			</div>

		</fieldset>

		<s:if test="key != null">
			<s:hidden name="key" />
		</s:if>

	</s:form>

	<br />

	<div class="col-md-8">

		<h2>Team Challenges</h2>

		<s:iterator value="challenges">

			<div class="input-group">

				<s:url var="summaryChallengeUrl" action="challenge-summary" namespace="/private">
					<s:param name="challengeKey">${key}</s:param>
					<s:param name="teamKey">${team}</s:param>
				</s:url>

				<s:a class="form-control" href="%{#summaryChallengeUrl}">
					<s:property value="name" />
				</s:a>

				<s:url var="editChallengeUrl" action="challenges"
					method="editChallenge" namespace="/private">
					<s:param name="selectedChallenge">${key}</s:param>
					<s:param name="teamKey">${team}</s:param>
				</s:url>

				<div class="input-group-btn">

					<s:a class="btn btn-default" role="button"
						href="%{#editChallengeUrl}"><span title="Edit" class="glyphicon glyphicon-edit"></span></s:a>

					<s:url var="toggleActiveUrl" action="teams"
						method="makeActive" namespace="/private">
						<s:param name="selectedChallenge">${key}</s:param>
						<s:param name="teamKey">${team}</s:param>
					</s:url>

					<s:a class="btn btn-default" role="button"
						href="%{#toggleActiveUrl}">
						<s:if test="active">
							<span title="Make Challenge Inactive"
								class="glyphicon glyphicon-star" aria-hidden="true"></span>
						</s:if>
						<s:else>
							<span title="Make Challenge Active"
								class="glyphicon glyphicon-star-empty" aria-hidden="true"></span>
						</s:else>
					</s:a>

				</div>

			</div>

			<br />
		</s:iterator>

		<div>
			<s:url var="addChallengeUrl" action="challenges"
				method="addChallenge" namespace="/private">
				<s:param name="teamKey">${key}</s:param>
			</s:url>
			<s:a class="btn btn-primary" role="button" href="%{#addChallengeUrl}">Add Challenge</s:a>
		</div>
	</div>



</body>
</html>

