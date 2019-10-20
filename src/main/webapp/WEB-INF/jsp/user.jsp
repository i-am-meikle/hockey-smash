<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>User</title>
</head>
<body>

		<s:if test="trainer">
		<h1>Trainer Profile</h1>
	</s:if>
	<s:else>
		<h1>User Profile</h1>
	</s:else>

	<br />

	<s:form class="form-horizontal" id="edit_user_form" action="user"
		namespace="/private">

		<fieldset>

			<div class="form-group">
				<label for="fullnameId" class="col-md-1 control-label">Name</label>
				<div class="col-sm-8  inputGroupContainer">
					<div class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-user"></i></span> <input name="fullname"
							id="fullnameId" placeholder="Name" class="form-control"
							type="text" value="${fullname}">
					</div>
				</div>
			</div>

			<div class="form-group">
				<label for="nicknameId" class="col-md-1 control-label">Nickname</label>
				<div class="col-sm-8  inputGroupContainer">
					<div class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-user"></i></span> <input name="nickname"
							id="nicknameId" placeholder="Nickname (optional)"
							class="form-control" type="text" value="${nickname}">
					</div>
				</div>
			</div>

			<div class="form-group">
				<label for="emailId" class="col-md-1 control-label">Email</label>
				<div class="col-sm-8  inputGroupContainer">
					<div class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-envelope"></i></span> <input name="email"
							id="emailId" placeholder="Email" class="form-control" type="email"
							value="${email}">
					</div>
				</div>
			</div>

		</fieldset>
		<s:hidden name="key" id="activityKeyId" />
		<s:submit class="btn btn-success btn-primary col-sm-1 col-sm-offset-8"
			method="saveUser" value="Save" />
	</s:form>

	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							$('#edit_user_form')
									.bootstrapValidator(
											{
												// To use feedback icons, ensure that you use Bootstrap v3.1.0 or later
												feedbackIcons : {
													valid : 'glyphicon glyphicon-ok',
													invalid : 'glyphicon glyphicon-remove',
													validating : 'glyphicon glyphicon-refresh'
												},
												fields : {
													fullname : {
														validators : {
															stringLength : {
																min : 6,
															},
															notEmpty : {
																message : 'Please enter your name, min 6 charaters long.'
															}
														}
													},
													nickname : {},
													email : {
														validators : {
															notEmpty : {
																message : 'Please enter your email address'
															},
															emailAddress : {
																message : 'Please enter a valid email address'
															}
														}
													},

													newPassword : {
														validators : {
															stringLength : {
																min : 6,
															},
															identical : {
																field : 'confirmPassword',
																message : 'Confirm your password below - type same password please'
															}
														}
													},
													confirmPassword : {
														validators : {
															stringLength : {
																min : 6,
															},
															identical : {
																field : 'newPassword',
																message : 'The password and its confirm are not the same'
															}
														}
													},

												}
											})

						});
	</script>

</body>
</html>

