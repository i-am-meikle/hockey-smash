<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Change Password</title>
</head>
<body>

	<h1>Change Password</h1>
	<br />

	<h2>Please enter a new password for: <s:property value="fullname"></s:property></h2>

	<s:form class="form-horizontal" id="data_form" action="login">

		<fieldset>

			<div class="form-group">
				<div class="col-md-8  inputGroupContainer">
					<div class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-pencil"></i></span> <input name="password"
							placeholder="New Password" class="form-control" type="password">
					</div>
				</div>
			</div>

			<div class="form-group">
				<div class="col-md-8  inputGroupContainer">
					<div class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-pencil"></i></span> <input
							name=confirm_password placeholder="Confirm Password"
							class="form-control" type="password" /> <span
							class="glyphicon form-control-feedback"></span> <span
							class="help-block with-errors"></span>
					</div>
				</div>
			</div>

		</fieldset>
		<s:hidden name="key"  />
		<s:hidden name="team"  />
		
		<s:submit class="btn btn-success btn-primary col-sm-2 col-sm-offset-6"
			method="changePassword" value="Change Password" />
	</s:form>

	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							$('#data_form')
									.bootstrapValidator(
											{
												// To use feedback icons, ensure that you use Bootstrap v3.1.0 or later
												feedbackIcons : {
													valid : 'glyphicon glyphicon-ok',
													invalid : 'glyphicon glyphicon-remove',
													validating : 'glyphicon glyphicon-refresh'
												},
												fields : {
													password : {
														validators : {
															notEmpty : {
																message : 'Please enter a new password'
															},
															stringLength : {
																min : 6,
															},
															identical : {
																field : 'confirm_password',
																message : 'Confirm your password below - type same password please'
															}
														}
													},
													confirm_password : {
														validators : {
															notEmpty : {
																message : 'Please enter a new password'
															},
															stringLength : {
																min : 6,
															},
															identical : {
																field : 'password',
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

