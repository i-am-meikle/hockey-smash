package no.meikle.hockey_smash;

import java.util.HashMap;
import java.util.List;
import no.meikle.hockey_smash.domain.Activity;
import no.meikle.hockey_smash.domain.Challenge;

public class ChallengeSummary {

	public static class PlayerActivitySummary {

		private int activityCount = 0;
		private int totalActivity = 0;
		private String nickname;

		public int getActivityCount() {
			return activityCount;
		}

		public int getTotalActivity() {
			return totalActivity;
		}

		public String getNickname() {
			return nickname;
		}

		public String getUserKey() {
			return userKey;
		}

		private String userKey;

		public PlayerActivitySummary(Activity activity) {
			userKey = activity.getUserKey();
			nickname = activity.getNickname();
		}

		public void addActivity(Activity activity) {

			activityCount++;
			totalActivity = totalActivity + activity.getActivity();

		}

	}

	private HashMap<String, PlayerActivitySummary> playerMap;
	private int activityCount = 0;
	private int totalScore = 0;
	private Challenge challenge;

	public HashMap<String, PlayerActivitySummary> getPlayerMap() {
		return playerMap;
	}

	public Challenge getChallenge() {
		return challenge;
	}

	public int getActivityCount() {
		return activityCount;
	}

	public int getActivePlayerCount() {
		return playerMap.size();
	}

	public int getTotalScore() {
		return totalScore;
	}

	public int getAverageShotsPerActivity() {
		return totalScore / activityCount;
	}

	public int getAverageActivitiesPerPlayer() {

		return activityCount / this.getActivePlayerCount();
	}

	public int getAverageShotsPerPlayer() {

		return this.getTotalScore() / this.getActivePlayerCount();
	}

	public ChallengeSummary(Challenge aChallenge, List<Activity> list) {

		challenge = aChallenge;
		activityCount = list.size();

		playerMap = new HashMap<String, PlayerActivitySummary>();

		for (Activity activity : list) {

			String userKey = activity.getUserKey();

			PlayerActivitySummary pac = playerMap.get(userKey);
			if (pac == null) {
				pac = new PlayerActivitySummary(activity);
				playerMap.put(userKey, pac);
			}

			pac.addActivity(activity);
			totalScore = totalScore + activity.getActivity();

		}

		
	}

}
