<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="Jutul Hockey - Hockey Smash">
<meta name="author" content="Meikle & Sons and that fabulous Mr Thomas">
<meta name="robots" content="noindex">
<link rel="icon"
	href="<s:property value="%{staticRoot}" />/images/HockeySmash.ico">

<title><decorator:title default="Jutul Apps" /></title>

<!-- Bootstrap core CSS -->
<link href="<s:property value="%{staticRoot}" />/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Bootstrap Validator CSS -->
<link
	href="<s:property value="%{staticRoot}" />/css/bootstrapValidator.min.css"
	rel="stylesheet">
<!-- Bootstrap Datepicker CSS -->
<link
	href="<s:property value="%{staticRoot}" />/css/bootstrap-datetimepicker.min.css"
	rel="stylesheet" />

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<link
	href="<s:property value="%{staticRoot}" />/css/ie10-viewport-bug-workaround.css"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="<s:property value="%{staticRoot}" />/css/jumbotron.css"
	rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

<!-- Bootstrap core JavaScript
    ================================================== -->
<script type="text/javascript"
	src="<s:property value="%{staticRoot}" />/js/jquery.min.js"></script>
<!-- Moment.js for date handling and required for the datepicker -->
<script type="text/javascript"
	src="<s:property value="%{staticRoot}" />/js/moment-with-locales.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script type="text/javascript"
	src="<s:property value="%{staticRoot}" />/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<s:property value="%{staticRoot}" />/js/bootstrapValidator.min.js"></script>
<script type="text/javascript"
	src="<s:property value="%{staticRoot}" />/js/bootstrap-datetimepicker.min.js"></script>

<decorator:head />
</head>

<body>

	<nav class="navbar navbar-inverse navbar-fixed-top ">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<s:a action="smash" namespace="/" class="navbar-brand">Jutul Hockey</s:a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">

			<s:if test="sessionManager.loggedIn">

				<div class="navbar-right">
					<ul class="nav navbar-nav">
						<li><s:a action="user" namespace="/private">
								<s:property value="sessionManager.loggedInNickname" />
							</s:a></li>

						<s:if
							test="sessionManager.hasTeam && ! sessionManager.isAdminUser">
							<s:url var="teamUrl" action="teams" namespace="/private"
								method="teamSummary">
								<s:param name="teamKey">
									<s:property value="sessionManager.teamKey" />
								</s:param>
							</s:url>

							<li><s:a href="%{#teamUrl}">
									<s:property value="sessionManager.teamName" />
								</s:a></li>
						</s:if>

						<s:if test="! sessionManager.isAdminUser">
							<s:if test="sessionManager.trainer">

								<s:iterator value="activeChallenges">

									<s:url var="challengeSummaryUrl" action="challenge-summary"
										namespace="/private">
										<s:param name="challengeKey">
											<s:property value="key" />
										</s:param>
										<s:param name="teamKey">
											<s:property value="team" />
										</s:param>
									</s:url>
									<li><s:a href="%{#challengeSummaryUrl}">
											<s:property value="name" />
										</s:a></li>
								</s:iterator>

							</s:if>
							<s:else>
								<s:iterator value="activeChallenges">

									<s:url var="challengeSummaryUrl" action="user-challenge-status"
										namespace="/private">
										<s:param name="challengeKey">
											<s:property value="key" />
										</s:param>
									</s:url>
									<li><s:a href="%{#challengeSummaryUrl}">
											<s:property value="name" />
										</s:a></li>
								</s:iterator>
							</s:else>

						</s:if>

						<s:if test="sessionManager.adminUser">
							<li><s:a action="teams" namespace="/private">Teams</s:a></li>
							<li><s:a action="user" method="users" namespace="/private">Users</s:a></li>
						</s:if>
						<s:if test="sessionManager.trainer">
							<li><s:a action="user" method="users" namespace="/private">Members</s:a></li>
						</s:if>
						<li><s:a action="login" method="logout" namespace="/">Logout</s:a></li>
					</ul>
				</div>
			</s:if>
			<s:else>
				<s:form class="navbar-form navbar-right" action="login"
					namespace="/">
					<div class="form-group">
						<input type="email" placeholder="email" class="form-control"
							name="email">
					</div>
					<div class="form-group">
						<input type="password" placeholder="password" class="form-control"
							name="password">
					</div>
					<s:submit class="btn btn-primary btn-success" method="login"
						value="Login" />

					<button type="button" class="btn btn-primary" data-toggle="modal"
						data-target="#registerModal">Register</button>
				</s:form>
			</s:else>
		</div>
		<!--/.navbar-collapse -->
	</div>
	</nav>

	<div class="jumbotron">
		<div class="container">
				<s:actionerror />
				<s:actionmessage />
			<div>
				<a href="http://www.jutul.net/Ishockey.html" class="pull-right"
					target="_blank_"> <img alt="Jutul Hockey"
					src="<s:property value="%{staticRoot}" />/images/jutul_hockey_100.png">
				</a>
				<decorator:body />
			</div>
		</div>
	</div>

	<div class="container">

		<footer>
		<p>&copy; 2019 Meikle &amp; Sons and that fabulous Mr Thomas</p>
		</footer>
	</div>
	<!-- /container -->

</body>
</html>




