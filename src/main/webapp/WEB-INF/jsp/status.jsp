<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Hockey Smash</title>
<s:head />
</head>
<body>


	<h1>Hockey Smash</h1>

	<br />

	<s:if test="!sessionManager.loggedIn">
		<p>Welcome to Hockey Smash. Login to see how it is going, or check
			out the videos below to see how it is done!</p>
	</s:if> <s:else>
	<s:iterator value="teamMessages"> 
		<div class="panel panel-default">

				<div class="panel-body"><s:property escapeHtml="true" value="message"/>
				</div>
			
		</div>
		
		</s:iterator>
	</s:else>

	<br />
	
	
	<iframe width="560" height="315" src="https://www.youtube.com/embed/Mq2qm5sdQkE" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
	<iframe width="560" height="315" src="https://www.youtube.com/embed/ujaePXhR4qY" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
	<iframe width="560" height="315" src="https://www.youtube.com/embed/Z38QfMBqHss" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
	
	<iframe width="560" height="315" src="https://www.youtube.com/embed/AwyxWKwETfM" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
	<iframe width="560" height="315" src="https://www.youtube.com/embed/km1yy-3APOw" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
	<iframe width="560" height="315" src="https://www.youtube.com/embed/MC4bVV-NWIU" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
	<iframe width="560" height="315" src="https://www.youtube.com/embed/nVQNjWsNSG4" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
		
	<s:if test="sessionManager.loggedIn">

		<div class="col-md-8">

			<h2>Latest activities</h2>

			<br />
			<s:iterator value="latestActivities">

				<div class="list-group">
					<div class="list-group-item">
						<h4 class="list-group-item-heading">
							<s:property value="activityDate" />
						</h4>
						<s:iterator value="activities">
							<div class="list-group-item bg-success">
								<span class="list-group-item-text text-success"> <span
									class="glyphicon" aria-hidden="true"><img alt=""
										src="<s:property value="%{staticRoot}" />/images/HockeySmash_trans.png"></span>
									<s:property value="tagline" /> 
								</span>
							</div>
						</s:iterator>
					</div>
				</div>
			</s:iterator>

		</div>

	</s:if>

	<!-- Register Modal -->
	<div class="modal fade" id="registerModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<s:form class="form-horizontal" id="register_form" action="login" namespace="/">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Please register</h4>
					</div>
					<div class="modal-body">

						<fieldset>

							<div class="form-group">
								<div class="col-md-12  inputGroupContainer">
									<div class="input-group">
										<span class="input-group-addon"><i
											class="glyphicon glyphicon-user"></i></span> <input name="fullname"
											placeholder="Players Name" class="form-control" type="text">
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-12  inputGroupContainer">
									<div class="input-group">
										<span class="input-group-addon"><i
											class="glyphicon glyphicon-envelope"></i></span> <input name="email"
											placeholder="Email" class="form-control" type="email">
									</div>
								</div>
							</div>

							<div class="form-group">
								<div class="col-md-12  inputGroupContainer">
									<div class="input-group">
										<span class="input-group-addon"><i
											class="glyphicon glyphicon-record"></i></span> <s:select list="teams" listKey="key" listValue="name"
									id="challenge" class="form-control" placeholder="Team"
									name="team"></s:select>
									</div>
								</div>
							</div>

							<div class="form-group">
								<div class="col-md-12  inputGroupContainer">
									<div class="input-group">
										<span class="input-group-addon"><i
											class="glyphicon glyphicon-pencil"></i></span> <input name="password"
											placeholder="Password" class="form-control" type="password">
									</div>
								</div>
							</div>

							<div class="form-group">
								<div class="col-md-12  inputGroupContainer">
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


					</div>
					<div class="modal-footer">
						<s:submit class="btn btn-primary btn-default btn-success"
							method="register" value="Register" />
						<button type="button" class="btn" data-dismiss="modal">Close</button>

					</div>
				</s:form>
			</div>
		</div>
	</div>


	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							$('#register_form')
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
																message : 'Please enter the players name, min 6 charaters long.'
															}
														}
													},
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
													team : {
														validators : {
															notEmpty : {
																message : 'Please select your team'
															},
														}
													},
													password : {
														validators : {
															notEmpty : {
																message : 'Please enter a password'
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
																message : 'Please enter a password'
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
