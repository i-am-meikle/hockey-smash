<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Challenge Details</title>
</head>
<body>

	<s:if test="name == null">
		<h2>Add Challenge to 
	</s:if>
	<s:else>
		<h2>Edit Challenge in 
	</s:else> <s:property value="loadedTeam.name"/> </h2>

	<s:form class="form-horizontal" id="edit_challenge_form"
		action="challenges">

		<fieldset>

			<div class="form-group">
				<div class="col-md-8  inputGroupContainer">
					<div class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-pencil"></i></span>
						<s:textfield name="name" placeholder="Name" class="form-control"
							type="text" />
					</div>
				</div>
			</div>

			<div class="form-group">
				<div class="col-md-8  inputGroupContainer">
					<div class="input-group" id="datetimepickerStart">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-calendar"></i></span>
						<s:textfield name="startDate" placeholder="Start Date (dd/mm/yyyy)"
							class="form-control" />
					</div>
				</div>
			</div>

			<div class="form-group">
				<div class="col-md-8  inputGroupContainer">
					<div class="input-group" id="datetimepickerEnd">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-calendar"></i></span>
						<s:textfield name="endDate" placeholder="End Date (dd/mm/yyyy)"
							class="form-control" />
					</div>
				</div>
			</div>

			<div class="form-group">
				<div class="col-md-8  inputGroupContainer">
					<div class="input-group" id="goalId">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-star-empty"></i></span>
						<s:textfield name="goal" placeholder="Goal"
							class="form-control" type="number" />
					</div>
				</div>
			</div>

			<div class="form-group">
				<div class="col-md-8  inputGroupContainer">
					<div class="input-group" id="verbId">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-star"></i></span>
						<s:textfield name="unit" placeholder="Unit" class="form-control"/>
					</div>
				</div>
			</div>
			
			<div class="form-group">
				<div class="col-md-8  inputGroupContainer">
					<div class="input-group" id="taglineId">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-pencil"></i></span>
						<s:textfield name="tagline" placeholder="Tagline" class="form-control"/>
					</div>
				</div>
			</div>

		</fieldset>

		<s:if test="key != null">
			<s:hidden name="key" />
		</s:if>
		<s:hidden name="teamKey" />
		<s:hidden name="active" />
		<s:submit class="btn btn-success btn-primary" method="saveChallenge"
			value="Save" /> <s:submit class="btn btn-default" method="cancel"
			value="Cancel" />

	</s:form>

	<script type="text/javascript">
		$(function() {
			$('#datetimepickerStart').datetimepicker({
				format : 'DD/MM/YYYY'
			});
			$('#datetimepickerEnd').datetimepicker({
				format : 'DD/MM/YYYY',
				useCurrent : false
			//Important! See issue #1075
			});
			$("#datetimepickerStart").on("dp.change", function(e) {
				$('#datetimepickerEnd').data("DateTimePicker").minDate(e.date);
			});
			$("#datetimepickerEnd").on(
					"dp.change",
					function(e) {
						$('#datetimepickerStart').data("DateTimePicker")
								.maxDate(e.date);
					});
		})
	</script>


</body>
</html>

