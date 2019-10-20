<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Team</title>
</head>
<body>

<s:push value="challengeSummary">

        <h2><s:property value="challenge.name" />
               </h2>
      
      	<div class="row">
		<div class="col-sm-4">
			<div class="input-group">
				<span class="input-group-addon " id="basic-addon1">Start
					Date: </span> <span class="form-control" aria-describedby="basic-addon1">
					<s:property value="challenge.startDate" />
				</span>
			</div>
			<!-- /input-group -->
		</div>
		<!-- /.col-lg-6 -->
		<div class="col-sm-4">
			<div class="input-group">
				<span class="input-group-addon " id="basic-addon2">End Date:
				</span> <span class="form-control" aria-describedby="basic-addon2">
					<s:property value="challenge.endDate" />
				</span>
			</div>
			<!-- /input-group -->
		</div>
		<!-- /.col-lg-6 -->
	</div>
	<!-- /.row -->


	<br />

	<div>
		</g> </svg></a> <a tabindex="0" role="button" data-toggle="popover" data-placement="bottom" data-trigger="focus" title="Total Activities" data-content="The total number of activities."><svg
				xmlns="http://www.w3.org/2000/svg"
				xmlns:xlink="http://www.w3.org/1999/xlink" xml:space="preserve"
				style="shape-rendering:geometricPrecision; text-rendering:geometricPrecision; image-rendering:optimizeQuality; fill-rule:evenodd; clip-rule:evenodd"
				width="100" height="100"> <g id="UrTavla"> <circle
				style="fill:#010101;stroke:#010101;stroke-width:5px;stroke-miterlimit:10;"
				cx="50" cy="50" r="45"> </circle> <text x="50%" y="50%"
				font-size="22" text-anchor="middle" fill="#fff" stroke="#fff"
				stroke-width="2px" dy=".34em"><s:property value="activityCount" /></text> </g> </svg></a> 
				</g> </svg></a> <a tabindex="0" role="button" data-toggle="popover" data-placement="bottom" data-trigger="focus" title="Total <s:property value="challenge.unit" />" data-content="The total number of <s:property value="challenge.unit" />."><svg xmlns="http://www.w3.org/2000/svg"
				xmlns:xlink="http://www.w3.org/1999/xlink" xml:space="preserve"
				style="shape-rendering:geometricPrecision; text-rendering:geometricPrecision; image-rendering:optimizeQuality; fill-rule:evenodd; clip-rule:evenodd"
				width="100" height="100"> <g id="UrTavla"> <circle
				style="fill:#010101;stroke:#010101;stroke-width:5px;stroke-miterlimit:10;"
				cx="50" cy="50" r="45"> </circle> <text x="50%" y="50%"
				font-size="22" text-anchor="middle" fill="#fff" stroke="#fff"
				stroke-width="2px" dy=".34em"><s:property value="totalScore" /></text> </g> </svg></a>

	</div>

	<h3>
		<s:property value="activePlayerCount" />
		active player(s)
	</h3>

	<s:iterator value="playerMap">
	<a tabindex="0" role="button" data-toggle="popover" data-placement="bottom" data-trigger="focus" title="<s:property value="value.nickname"/>" >
		<img title="<s:property value="value.nickname" />"
			alt="<s:property value="value.nickname" />"
			src="<s:property value="%{staticRoot}" />/images/HockeySmash_trans.png"></a>
	</s:iterator>

	<br />
	<br />

	<div>
	<a tabindex="0" role="button" data-toggle="popover" data-placement="bottom" data-trigger="focus" title="Average player activities" data-content="The average number of activities per player">
		 <svg
				xmlns="http://www.w3.org/2000/svg"
				xmlns:xlink="http://www.w3.org/1999/xlink" xml:space="preserve"
				style="shape-rendering:geometricPrecision; text-rendering:geometricPrecision; image-rendering:optimizeQuality; fill-rule:evenodd; clip-rule:evenodd"
				width="100" height="100"> <g id="UrTavla"> <circle
				style="fill:#010101;stroke:#010101;stroke-width:5px;stroke-miterlimit:10;"
				cx="50" cy="50" r="45"> </circle> <text x="50%" y="50%"
				font-size="22" text-anchor="middle" fill="#fff" stroke="#fff"
				stroke-width="2px" dy=".34em"><s:property value="averageActivitiesPerPlayer" /></text>
			</g> </svg></a> <a tabindex="0" role="button" data-toggle="popover" data-placement="bottom" data-trigger="focus" title="Average <s:property value="challenge.unit" /> per player" data-content="The average number of <s:property value="challenge.unit" /> per player"> <svg
				xmlns="http://www.w3.org/2000/svg"
				xmlns:xlink="http://www.w3.org/1999/xlink" xml:space="preserve"
				style="shape-rendering:geometricPrecision; text-rendering:geometricPrecision; image-rendering:optimizeQuality; fill-rule:evenodd; clip-rule:evenodd"
				width="100" height="100"> <g id="UrTavla"> <circle
				style="fill:#010101;stroke:#010101;stroke-width:5px;stroke-miterlimit:10;"
				cx="50" cy="50" r="45"> </circle> <text x="50%" y="50%"
				font-size="22" text-anchor="middle" fill="#fff" stroke="#fff"
				stroke-width="2px" dy=".34em"><s:property value="averageShotsPerPlayer" /></text>
			</g> </svg></a> <a tabindex="0" role="button" data-toggle="popover" data-placement="bottom" data-trigger="focus" title="Average <s:property value="challenge.unit" /> per activity" data-content="The average number of <s:property value="challenge.unit" /> per activity"><svg
				xmlns="http://www.w3.org/2000/svg"
				xmlns:xlink="http://www.w3.org/1999/xlink" xml:space="preserve"
				style="shape-rendering:geometricPrecision; text-rendering:geometricPrecision; image-rendering:optimizeQuality; fill-rule:evenodd; clip-rule:evenodd"
				width="100" height="100"> <g id="UrTavla"> <circle
				style="fill:#010101;stroke:#010101;stroke-width:5px;stroke-miterlimit:10;"
				cx="50" cy="50" r="45"> </circle> <text x="50%" y="50%"
				font-size="22" text-anchor="middle" fill="#fff" stroke="#fff"
				stroke-width="2px" dy=".34em"><s:property value="averageShotsPerActivity" /></text>
			</g> </svg></a>

	</div>

</s:push>

	<br />
	<div class="col-md-8">

			<h2>Latest activities</h2>

			<br />
			<s:iterator value="latestChallengeActivities">

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

	<script type="text/javascript">
		$(function() {
			$('[data-toggle="popover"]').popover()
		})
	
		
	</script>

</body>
</html>

