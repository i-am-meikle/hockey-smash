package no.meikle.hockey_smash;

import java.util.ArrayList;
import java.util.List;
import no.meikle.hockey_smash.domain.Activity;

public class ActivityDay {

	private String activityDate;
	private List<Activity> activities = new ArrayList<>();

	public ActivityDay(String anActivityDate) {

		this.setActivityDate(anActivityDate);
	}

	public void addActivity(Activity activity) {

		activities.add(activity);

	}

	public String getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}

	public List<Activity> getActivities() {
		return activities;
	}

}
