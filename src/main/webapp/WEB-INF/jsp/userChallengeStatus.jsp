<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Challenge Status</title>
</head>
<body>

	<s:push value="challengeParticipation">

		<h2>
			<s:property value="challenge.name" />
		</h2>

		<br />
		<div>
			<p>
				<s:property value="challengeScore" />
				of
				<s:property value="challenge.goal" />
				Completed !
			</p>
		</div>

		<div class="progress">
			<div class="progress-bar" role="progressbar"
				aria-valuenow="<s:property value="percentComplete"/>"
				aria-valuemin="0" aria-valuemax="100"
				style="min-width: 2em; width: <s:property value="percentComplete"/>%;">
				<s:property value="percentComplete" />
				%
			</div>
		</div>
		
	<s:iterator value="months">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">
					<s:property value="monthString" />
				</h3>
			</div>
			<div class="panel-body tablePanel">
				<div class="table-responsive">
					<table class="table table-bordered table-condensed challengeTable">
						<tr>
							<th class="visible-md visible-lg">Monday</th>
							<th class="visible-sm">Mon</th>
							
							<th class="visible-md visible-lg">Tuesday</th>
							<th class="visible-sm">Tue</th>
							
							<th class="visible-md visible-lg">Wednesday</th>
							<th class="visible-sm">Wed</th>
							
							<th class="visible-md visible-lg">Thursday</th>
							<th class="visible-sm">Thu</th>
							
							<th class="visible-md visible-lg">Friday</th>
							<th class="visible-sm">Fri</th>
							
							<th class="visible-md visible-lg">Saturday</th>
							<th class="visible-sm">Sat</th>
							
							<th class="visible-md visible-lg">Sunday</th>
							<th class="visible-sm">Sun</th>
						</tr>
						<s:iterator value="challengeWeeks" status="weekStatus">
							<tr class="hidden-xs">
								<s:iterator value="challengeDays" status="dayStatus">
									<s:if test="isValid">
										<td class="col-md-1 activeDay">
											<button type="button" class="btn btn-link"
												<s:if test="!isActive">disabled="disabled"</s:if>
												data-date="<s:property value="date" />"
												<s:if test="hasActivity"> 
													data-score="<s:property value="activity.activity" />" 
													data-key="<s:property value="activity.key" />" 
												</s:if>
												data-toggle="modal" data-target="#activityModal">
												<s:property value="dayNumber" />
												<s:if test="hasActivity">
													<span class="badge badge-success"><s:property
															value="activity.activity" /></span>
												</s:if>
											</button>
										</td>
									</s:if>
									<s:else>
										<td class="col-md-1 danger"></td>
									</s:else>
								</s:iterator>
							</tr>
							<s:iterator value="challengeDays" status="dayStatus">
									<s:if test="isValid">
							<tr class="visible-xs"><td class="dayName"><s:property value="dayName" /></td>
										<td class="col-md-1 activeDay">
											<button type="button" class="btn btn-link"
												<s:if test="!isActive">disabled="disabled"</s:if>
												data-date="<s:property value="date" />"
												<s:if test="hasActivity"> 
													data-score="<s:property value="activity.activity" />" 
													data-key="<s:property value="activity.key" />" 
												</s:if>
												data-toggle="modal" data-target="#activityModal">
												<s:property value="dayNumber" />
												<s:if test="hasActivity">
													<span class="badge badge-success"><s:property
															value="activity.activity" /></span>
												</s:if>
											</button>
										</td>
									</s:if>
								</s:iterator>
						</s:iterator>
					</table>
				</div>
			</div>
		</div>
	</s:iterator>


	<!-- Activity Modal -->
	<div class="modal fade" id="activityModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<s:form class="form-horizontal" id="activity_form"
					action="register-activity">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Register your <s:property value="challenge.unit" />
							!</h4>
					</div>
					<div class="modal-body">

						<fieldset>

							<div class="form-group">
								<div class="col-md-10  inputGroupContainer">
									<div class="input-group" id="activityDate">
										<span class="input-group-addon"><i
											class="glyphicon glyphicon-calendar"></i></span><input
											name="activityDate" placeholder="Activity Date"
											id="activityDateId" class="form-control" readonly>
									</div>
								</div>
							</div>

							<div class="form-group">
								<div class="col-md-10  inputGroupContainer">
									<div class="input-group">
										<span class="input-group-addon"><i
											class="glyphicon glyphicon-ok-sign"></i></span> <input
											name="activity" placeholder="<s:property value="challenge.unit" />" id="activityScoreId"
											class="form-control" type="number">
									</div>
								</div>
							</div>

						</fieldset>

						<s:hidden name="key" id="activityKeyId" />
						<s:hidden name="challengeKey"/>
					</div>
					<div class="modal-footer">
						<s:submit class="btn btn-primary btn-default btn-success"
							value="Send in !" />
						<button type="button" class="btn" data-dismiss="modal">Close</button>

					</div>
				</s:form>
			</div>
		</div>
	</div>

</s:push>

	<script type="text/javascript">
		$('#activityModal').on('show.bs.modal', function(event) {
			// Button that triggered the modal
			var btn = $(event.relatedTarget); 
			
			// Extract info from data-* attributes
			var score = btn.data('score'); 
			$('#activityScoreId').val(score);

			var date = btn.data("date");
			$('#activityDateId').val(date);

			var key = btn.data("key");
			$('#activityKeyId').val(key);
		})
	</script>
</html>
