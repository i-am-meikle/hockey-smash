<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Reset Password</title>
</head>
<body>

		<h2>Reset Password</h2>
	<br />

<h3>Please enter the fullname, nickname or eMail address you registered with.</h3>

	<br />	
	
	<s:form class="form-horizontal" id="data_form" action="login" >

		<fieldset>

			<div class="form-group">
				<div class="col-sm-8  inputGroupContainer">
					<div class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-user"></i></span> <input name="fullname"
							id="fullnameId" placeholder="Full Name" class="form-control"
							type="text" value="${fullname}">
					</div>
				</div>
			</div>
			
		</fieldset>

		<s:submit class="btn btn-success btn-primary col-sm-1 col-sm-offset-7"
			method="sendResetEmail" value="Reset" />
	</s:form>

</body>
</html>

