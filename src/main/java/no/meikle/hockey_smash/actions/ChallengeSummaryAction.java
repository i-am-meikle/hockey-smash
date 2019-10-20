package no.meikle.hockey_smash.actions;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.meikle.hockey_smash.ActivityDay;
import no.meikle.hockey_smash.ChallengeSummary;
import no.meikle.hockey_smash.domain.Activity;
import no.meikle.hockey_smash.domain.Challenge;
import no.meikle.hockey_smash.domain.User;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@InterceptorRef("jutulStack")
@Namespace("/private")
@Results({ @Result(location = "/WEB-INF/jsp/challengeSummary.jsp"),
		@Result(name = "challenge-summary", location = "/WEB-INF/jsp/teamChallengeSummary.jsp"), })
@AllowedMethods({ "challengeSummary" })
public class ChallengeSummaryAction extends AbstractJutulAction implements Preparable, ModelDriven<Challenge> {

	public static class UserSummary {

		private int score;
		private User user;
		private Challenge challenge;

		public UserSummary(User aUser, Challenge aChallenge) {

			user = aUser;
			challenge = aChallenge;

		}

		public void addActivity(Activity activity) {

			setScore(getScore() + activity.getActivity());
		}

		public String getFullname() {
			return this.user.getFullname();
		}

		public String getNickname() {
			return this.user.getNickname();
		}

		public int getScore() {
			return score;
		}

		public int getPercentageComplete() {
			return (score * 100) / challenge.getGoal();
		}

		public void setScore(int score) {
			this.score = score;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4860523392842004665L;

	private Challenge challenge;
	private String challengeKey;
	private String teamKey;
	private Collection<UserSummary> players;
	private ChallengeSummary challengeSummary;

	private List<ActivityDay> latestChallengeActivities;

	public Challenge getModel() {

		return challenge;
	}

	public void prepare() throws Exception {

		if (this.getChallengeKey() == null) {
			return;
		}

		challenge = this.load(Challenge.class, this.getTeamKey(), this.getChallengeKey());

	}

	public String execute() {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String startDate = df.format(this.getModel().getStartDate());
		String endDate = df.format(this.getModel().getEndDate());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":start", new AttributeValue().withS(startDate));
		eav.put(":end", new AttributeValue().withS(endDate));
		eav.put(":challengeKey", new AttributeValue().withS(this.getModel().getKey()));

		DynamoDBQueryExpression<Activity> query = new DynamoDBQueryExpression<Activity>()
				.withIndexName("challenge-activities").withConsistentRead(false).withExpressionAttributeValues(eav)
				// #TODO I am pretty sure the between-and is not necessary because a player
				// cannot register a challenge outside of the challenge period.
				.withKeyConditionExpression("challengeKey = :challengeKey AND activityDate BETWEEN :start AND :end");

		List<Activity> list = new DynamoDBMapper(this.dynamoDB()).query(Activity.class, query);

		Map<String, UserSummary> result = new HashMap<String, UserSummary>();

		for (Activity activity : list) {

			String userKey = activity.getUserKey();
			UserSummary summary = result.get(userKey);
			if (summary == null) {
				User user = this.load(User.class, challenge.getTeam(), userKey);
				summary = new UserSummary(user, challenge);
				result.put(userKey, summary);
			}

			summary.addActivity(activity);
		}

		Collection<UserSummary> resultList = result.values();

		this.setPlayers(resultList);

		return SUCCESS;

	}

	public String challengeSummary() {

		challengeSummary = this.generateChallengeSummary(this.getModel());

		return "challenge-summary";
	}

	private ChallengeSummary generateChallengeSummary(Challenge aTeamChallenge) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String startDate = df.format(aTeamChallenge.getStartDate());
		String endDate = df.format(aTeamChallenge.getEndDate());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":start", new AttributeValue().withS(startDate));
		eav.put(":end", new AttributeValue().withS(endDate));
		eav.put(":challengeKey", new AttributeValue().withS(aTeamChallenge.getKey()));

		DynamoDBQueryExpression<Activity> query = new DynamoDBQueryExpression<Activity>()
				.withIndexName("challenge-activities").withConsistentRead(false).withExpressionAttributeValues(eav)
				// #TODO I am pretty sure the between-and is not necessary because a player
				// cannot register a challenge outside of the challenge period.
				.withKeyConditionExpression("challengeKey = :challengeKey AND activityDate BETWEEN :start AND :end")
				.withScanIndexForward(false);

		List<Activity> list = new DynamoDBMapper(this.dynamoDB()).query(Activity.class, query);

		ChallengeSummary summary = new ChallengeSummary(aTeamChallenge, list);

		List<Activity> latest = list.subList(0, Math.min(list.size(), 50));
		
		DateFormat ads = DateFormat.getDateInstance(DateFormat.LONG);

		Map<String, ActivityDay> activityMap = new HashMap<String, ActivityDay>();
		List<ActivityDay> result = new ArrayList<ActivityDay>();

		for (Activity activity : latest) {
			String date = ads.format(activity.getActivityDate());

			ActivityDay activityDay = activityMap.get(date);
			if (activityDay == null) {
				activityDay = new ActivityDay(date);
				activityMap.put(date, activityDay);
				result.add(activityDay);
			}

			activityDay.addActivity(activity);
		}

		latestChallengeActivities = result;
		
		return summary;
	}

	
	public List<ActivityDay> getLatestChallengeActivities() {
		
		return latestChallengeActivities;
	}
	
	public ChallengeSummary getChallengeSummary() {
		return challengeSummary;
	}

	public String getChallengeKey() {
		return challengeKey;
	}

	public void setChallengeKey(String challengeKey) {
		this.challengeKey = challengeKey;
	}

	public Collection<UserSummary> getPlayers() {
		return players;
	}

	public void setPlayers(Collection<UserSummary> players) {
		this.players = players;
	}

	public String getTeamKey() {
		return teamKey;
	}

	public void setTeamKey(String teamKey) {
		this.teamKey = teamKey;
	}

}
