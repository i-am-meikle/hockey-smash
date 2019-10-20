package no.meikle.hockey_smash.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.Date;
import no.meikle.hockey_smash.domain.Activity;
import no.meikle.hockey_smash.domain.Challenge;
import no.meikle.hockey_smash.domain.User;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@InterceptorRef("jutulStack")
@Namespace("/private")
@Results({ @Result(name = "success", type = "redirectAction", location = "user-challenge-status", params = {
		"namespace", "/private", "challengeKey", "${challengeKey}" }) })
public class RegisterActivityAction extends AbstractJutulAction implements Preparable, ModelDriven<Activity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4860523392842004665L;

	private Activity activity;
	private User user;
	private Challenge challenge;
	private String challengeKey;
	private String key;
	private Date activityDate;

	public Activity getModel() {

		return activity;
	}

	public void prepare() throws Exception {

		user = this.getLoggedInUser();
		challenge = this.load(Challenge.class, user.getTeam(), this.getChallengeKey());

		// Check if the activity already exists, if so, update it,
		// if not, create a new one.

		if (this.getKey() != null && !this.getKey().isEmpty()) {
			activity = this.load(Activity.class, user.getKey(), this.getKey());
		}

		if (activity == null) {
			activity = new Activity();
		}
	}

	@Override
	public String execute() throws Exception {

		if (this.getActivity().getActivity() <= 0) {
			if (this.getActivity().getUserKey() != null) {
				this.delete(activity);
			}
		} else {
			this.getActivity().setUserKey(user.getKey());
			this.getActivity().setNickname(user.getNickname());
			this.getActivity().setChallengeKey(challenge.getKey());
			this.getActivity().setChallengeName(challenge.getName());
			this.getActivity().setTeamKey(user.getTeam());
			this.getActivity().setTagline(this.generateTagline());
			this.getActivity().preWrite();
			this.save(activity);
		}

		return SUCCESS;
	}

	private String generateTagline() {

		if(challenge.getTagline() == null) {
			return null;
		}
		
		String tagline = challenge.getTagline();
		tagline = tagline.replace("%nickname", user.getNickname());
		tagline = tagline.replace("%challenge", challenge.getName());
		tagline = tagline.replace("%activity", String.valueOf(activity.getActivity()));
		tagline = tagline.replace("%unit", challenge.getUnit());
		
		return tagline;
	}

	public Activity getActivity() {
		return activity;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date anActivityDate) {
		this.activityDate = anActivityDate;
	}

	public String getChallengeKey() {
		return challengeKey;
	}

	public void setChallengeKey(String challengeKey) {
		this.challengeKey = challengeKey;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
