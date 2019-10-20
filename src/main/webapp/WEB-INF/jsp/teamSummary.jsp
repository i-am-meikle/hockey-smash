<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Team</title>
</head>
<body>

	<h1>
		<s:property value="name" />
		<small><s:if test="sessionManager.trainer">
				<s:url var="editTeamUrl" action="teams" method="editTeam"
					namespace="/private">
					<s:param name="teamKey">${key}</s:param>
				</s:url>

				<span class="label"> <s:a title="Edit Team"
						class="glyphicon glyphicon-edit" href="%{#editTeamUrl}"></s:a>
				</span>

				<span class="label"> <s:a title="Edit Team"
						class="glyphicon glyphicon-comment" href="#" data-toggle="modal" data-target="#messageModal"></s:a>
				</span>

			</s:if></small>
	</h1>

	<br />
	<div class="col-md-8">

		<s:iterator value="teamMessages"> 
		<div class="panel panel-default">

				<div class="panel-body"><s:property escapeHtml="true" value="message"/>
				<s:if test="sessionManager.trainer">
				<button title="Edit Message" type="button" class="btn-link pull-right glyphicon glyphicon-edit" data-toggle="modal"
					data-target="#messageModal" data-start="<s:property value="startDate" />" data-end="<s:property value="endDate" />"
					data-message="<s:property value="message" />" data-message-key="<s:property value="messageKey" />"/></button> 
				</s:if>
				</div>
			
		</div>
		
		</s:iterator>

		<s:iterator value="activeChallenges">

			<s:url var="summaryChallengeUrl" action="challenge-summary"
				method="challengeSummary" namespace="/private">
				<s:param name="challengeKey">${key}</s:param>
				<s:param name="teamKey">${team}</s:param>
			</s:url>

			<div class="list-group">
				<div class="list-group-item">
					<span class="list-group-item-text"> <span class="glyphicon"
						aria-hidden="true"><img alt=""
							src="<s:property value="%{staticRoot}" />/images/HockeySmash_trans.png"></span>
						<s:a href="%{#summaryChallengeUrl}">
							<s:property value="name" />
						</s:a>
					</span>
				</div>
			</div>

		</s:iterator>

		
		<!-- Message Modal -->

	<div class="modal fade" id="messageModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<s:form class="form-horizontal" id="message_form" action="message" namespace="/private">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Enter Message</h4>
					</div>
					<div class="modal-body">

						<fieldset>

								<div class="form-group">
									<div class="col-md-12  inputGroupContainer">
										<div class="input-group" id="datetimepickerStart">
											<span class="input-group-addon"><i
												class="glyphicon glyphicon-calendar"></i></span>
											<s:textfield name="startDate" id="startDateId"
												placeholder="Start Date (dd/mm/yyyy)" class="form-control" />
										</div>
									</div>
								</div>

								<div class="form-group">
									<div class="col-md-12  inputGroupContainer">
										<div class="input-group" id="datetimepickerEnd">
											<span class="input-group-addon"><i
												class="glyphicon glyphicon-calendar"></i></span>
											<s:textfield name="endDate" id="endDateId"
												placeholder="End Date (dd/mm/yyyy)" class="form-control" />
										</div>
									</div>
								</div>

							<div class="form-group">
								<div class="col-md-12  inputGroupContainer">
									<div class="input-group">
										<span class="input-group-addon"><i
											class="glyphicon glyphicon-envelope"></i></span> <textarea name="message" 
											id="messageId" placeholder="Message" class="form-control" rows="5"></textarea>
									</div>
								</div>
							</div>

						</fieldset>

					</div>

						<input type="hidden" name="messageKey" id="messageKeyId" />
						<input type="hidden" name="targetKey" value="${teamKey}" />
						<input type="hidden" name="createdBy" value="${loggedInUser.key}"/>
						<input type="hidden" name="redirectUrl" value="${redirectUrl}"/>

					<div class="modal-footer">
						<s:submit class="btn btn-primary btn-default btn-success"
							method="addMessage" value="Add" id="addButtonId"/>
						<s:submit class="btn btn-default btn-success"
							method="deleteMessage" value="Delete" id="deleteButtonId"/>
						<button type="button" class="btn" data-dismiss="modal">Close</button>

					</div>
				</s:form>
			</div>
		</div>
	</div>


	<script type="text/javascript">
	
	$('#messageModal').on('show.bs.modal', function(event) {
		// Button that triggered the modal
		var btn = $(event.relatedTarget); 
		
		// Extract info from data-* attributes
		var sd = btn.data('start'); 
		$('#startDateId').val(sd);

		var ed = btn.data("end");
		$('#endDateId').val(ed);

		var message = btn.data("message");
		$('#messageId').val(message);
		
		var key = btn.data("message-key");
		$('#messageKeyId').val(key);
		
		if(key == null) {
			$('#addButtonId').val("Add");
			$('#deleteButtonId').addClass("hidden");
		} else {
			$('#addButtonId').val("Edit");
			$('#deleteButtonId').removeClass("hidden");
		}
	});
	
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
	});
	
		$(document)
				.ready(
						function() {
							$('#message_form')
									.bootstrapValidator(
											{
												// To use feedback icons, ensure that you use Bootstrap v3.1.0 or later
												feedbackIcons : {
													valid : 'glyphicon glyphicon-ok',
													invalid : 'glyphicon glyphicon-remove',
													validating : 'glyphicon glyphicon-refresh'
												},
												fields : {
													startDate : {
														validators : {
															notEmpty : {
																message : 'Please enter a valid date.'
															}
														}
													},
													endDate : {
														validators : {
															notEmpty : {
																message : 'Please enter a valid date.'
															}
														}
													},
													message : {
														validators : {
															notEmpty : {
																message : 'Please enter a message'
															}
														}
													},
												}
											})

						});
	</script>

	<!-- Message Modal End -->

	</div>

</body>
</html>

