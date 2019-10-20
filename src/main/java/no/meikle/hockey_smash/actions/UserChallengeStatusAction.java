package no.meikle.hockey_smash.actions;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.opensymphony.xwork2.Preparable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.meikle.hockey_smash.ChallengeMonth;
import no.meikle.hockey_smash.ChallengeWeek;
import no.meikle.hockey_smash.domain.Activity;
import no.meikle.hockey_smash.domain.Challenge;
import no.meikle.hockey_smash.domain.User;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@InterceptorRef("jutulStack")
@Namespace("/private")
@Results({ @Result(name = "success", location = "/WEB-INF/jsp/userChallengeStatus.jsp") })
public class UserChallengeStatusAction extends AbstractJutulAction implements Preparable {


	protected static class ChallengeParticipation {

		private List<ChallengeMonth> months;
		private Challenge challenge;
		private int challengeScore;

		public ChallengeParticipation(Challenge aChallenge, List<ChallengeMonth> theMonths, int aTotal) {
			
			setChallenge(aChallenge);
			setMonths(theMonths);
			setChallengeScore(aTotal);
			
		}

		public List<ChallengeMonth> getMonths() {
			return months;
		}

		public void setMonths(List<ChallengeMonth> months) {
			this.months = months;
		}

		public Challenge getChallenge() {
			return challenge;
		}

		public void setChallenge(Challenge challenge) {
			this.challenge = challenge;
		}
		
		public String getPercentComplete() {
			return String.valueOf((challengeScore * 100) / challenge.getGoal());
		}

		public void setChallengeScore(int challengeScore) {
			this.challengeScore = challengeScore;
		}

		public String getChallengeScore() {
			return String.valueOf(challengeScore);
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4860523392842004665L;

	private User user;
	private String challengeKey;
	private ChallengeParticipation challengeParticipation;

	public void prepare() throws Exception {

		user = this.getLoggedInUser();
		
		Challenge challenge = this.load(Challenge.class, this.getSessionManager().getTeamKey(), this.getChallengeKey());
		
		challengeParticipation = this.createChallengeParticipation(challenge);

	}

	public ChallengeParticipation getChallengeParticipation() {

		return challengeParticipation;
	}

	private ChallengeParticipation createChallengeParticipation(Challenge challenge) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String startDate = df.format(challenge.getStartDate());
		String endDate = df.format(challenge.getEndDate());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":userKey", new AttributeValue(user.getKey()));
		eav.put(":challengeKey", new AttributeValue(this.getChallengeKey()));
		eav.put(":start", new AttributeValue().withS(startDate));
		eav.put(":end", new AttributeValue().withS(endDate));

		DynamoDBQueryExpression<Activity> query = new DynamoDBQueryExpression<Activity>()
				.withIndexName("challenge-activities").withConsistentRead(false)
				.withKeyConditionExpression("challengeKey = :challengeKey and activityDate BETWEEN :start AND :end")
				.withFilterExpression("userKey = :userKey")
				.withExpressionAttributeValues(eav);
		
		List<Activity> list = new DynamoDBMapper(this.dynamoDB()).query(Activity.class, query);

		Map<String, Activity> challengeActivities = new HashMap<String, Activity>();
		int total = 0;
		SimpleDateFormat ads = new SimpleDateFormat("dd/MM/yyyy");

		for (Activity activity : list) {
			challengeActivities.put(ads.format(activity.getActivityDate()), activity);
			total = total + activity.getActivity();
		}

		Calendar calendarStart = new Calendar.Builder().setInstant(challenge.getStartDate()).build();
		calendarStart.setFirstDayOfWeek(Calendar.MONDAY);
		Calendar calendarEnd = new Calendar.Builder().setInstant(challenge.getEndDate()).build();
		calendarEnd.setFirstDayOfWeek(Calendar.MONDAY);

		int startWeek = calendarStart.get(Calendar.WEEK_OF_YEAR);
		int endWeek = calendarEnd.get(Calendar.WEEK_OF_YEAR);

		if (calendarEnd.get(Calendar.YEAR) > calendarStart.get(Calendar.YEAR)) {
			endWeek = endWeek + calendarStart.getWeeksInWeekYear();
		}
		
		List<ChallengeMonth> months = new ArrayList<ChallengeMonth>();
		ChallengeMonth month = new ChallengeMonth(calendarStart.get(Calendar.MONTH));
		months.add(month);

		for (int week = startWeek; week <= endWeek; week++) {

			// Create a new challenge week
			ChallengeWeek cw = new ChallengeWeek(week, calendarStart, calendarEnd);
			cw.initializeChallengeDays(challengeActivities, ads);

			// There is a possibility that the week starts entirely in a new
			// month. In which case, don't add it to the current month.
			Calendar fd = cw.getFirstDay();
			int fdm = fd.get(Calendar.MONTH);
			if (fdm == month.getMonth()) {
				month.addWeek(cw);
				cw.setMonth(month.getMonth());
			}

			// The end date for this week is the lesser of the week end
			// date and the challenge end date. If this date is actually present 
			// in the next month, create a new week for the next month.
			
			Calendar ld = cw.getLastDay();
			if (calendarEnd.compareTo(ld) < 0) {
				ld = calendarEnd;
			}
			
			int i = ld.get(Calendar.MONTH);
			if (i != month.getMonth()) {
				month = new ChallengeMonth(i);
				cw = new ChallengeWeek(week, calendarStart, calendarEnd);
				cw.initializeChallengeDays(challengeActivities, ads);
				month.addWeek(cw);
				cw.setMonth(month.getMonth());
				months.add(month);
			}

		}

		return new ChallengeParticipation(challenge, months, total);
	}

	public String getChallengeKey() {
		return challengeKey;
	}

	public void setChallengeKey(String challengeKey) {
		this.challengeKey = challengeKey;
	}

}
