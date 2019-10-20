package no.meikle.hockey_smash.actions;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.meikle.hockey_smash.ActivityDay;
import no.meikle.hockey_smash.AlphanumComparator;
import no.meikle.hockey_smash.AlphanumComparator.Extractor;
import no.meikle.hockey_smash.domain.Activity;
import no.meikle.hockey_smash.domain.Team;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@InterceptorRef("jutulStack")
@Action("smash")
@Namespace("/")
@Results({ @Result(location = "/WEB-INF/jsp/status.jsp") })
public class StatusAction extends AbstractJutulAction {

	private static final long serialVersionUID = 4860523392842004665L;

	public Collection<ActivityDay> getLatestActivities() {

		if (this.getSessionManager().getTeamKey() == null) {
			return Collections.emptyList();
		}

		DynamoDBQueryExpression<Activity> query = new DynamoDBQueryExpression<>();
		query.withLimit(50).withIndexName("team-activities")
				.addExpressionAttributeValuesEntry(":teamKey",
						new AttributeValue(this.getSessionManager().getTeamKey()))
				.withKeyConditionExpression("teamKey = :teamKey").withConsistentRead(false)
				.withScanIndexForward(false);

		QueryResultPage<Activity> queryResult = new DynamoDBMapper(this.dynamoDB()).queryPage(Activity.class, query);

		DateFormat ads = DateFormat.getDateInstance(DateFormat.LONG);

		Map<String, ActivityDay> activityMap = new HashMap<String, ActivityDay>();
		List<ActivityDay> result = new ArrayList<ActivityDay>();

		for (Activity activity : queryResult.getResults()) {
			String date = ads.format(activity.getActivityDate());

			ActivityDay activityDay = activityMap.get(date);
			if (activityDay == null) {
				activityDay = new ActivityDay(date);
				activityMap.put(date, activityDay);
				result.add(activityDay);
			}

			activityDay.addActivity(activity);
		}

		return result;

	}

	public List<Team> getTeams() {
 
		DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDB());
		PaginatedScanList<Team> list = mapper.scan(Team.class, new DynamoDBScanExpression());

		List<Team> result = new ArrayList<Team>(list);

		result.sort(new AlphanumComparator<Team>(new Extractor<Team>() {

			public String getString(Team anObject) {
				return anObject.getName();
			}
		}));

		return result;

	}

	public String execute() throws Exception {
		return SUCCESS;
	}
}
