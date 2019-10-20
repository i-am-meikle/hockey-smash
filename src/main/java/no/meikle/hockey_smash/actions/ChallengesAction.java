package no.meikle.hockey_smash.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import no.meikle.hockey_smash.domain.Challenge;
import no.meikle.hockey_smash.domain.Team;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@InterceptorRef("jutulStack")
@Namespace("/private")
@Results({
		@Result(name = "team", type = "redirectAction", location = "teams", params = { "method", "editTeam",
				"teamKey", "${teamKey}" }),
		@Result(name = "success", location = "/WEB-INF/jsp/editChallenge.jsp")})
@AllowedMethods({ "editChallenge", "addChallenge", "saveChallenge", "cancel" })
public class ChallengesAction extends AbstractJutulAction implements Preparable, ModelDriven<Challenge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4860523392842004665L;

	private Challenge challenge;
	private String selectedChallenge;
	private String teamKey;
	private Team team;

	public void setSelectedChallenge(String selectedChallenge) {

		this.selectedChallenge = selectedChallenge;
	}

	public String getSelectedChallenge() {

		return this.selectedChallenge;
	}

	public String addChallenge() throws Exception {

		this.getModel().setActive(true);
		return SUCCESS;

	}

	public String cancel() throws Exception {
		// Do nothing
		return "team";
	}

	public String editChallenge() throws Exception {

		if (this.getSelectedChallenge() == null) {
			this.addActionError("No Challenge selected");
			return "team";
		}

		challenge = this.load(Challenge.class, this.getTeamKey(), this.getSelectedChallenge());

		return SUCCESS;

	}

	public String saveChallenge() throws Exception {

		this.getModel().setTeam(this.getTeamKey());
		this.save(this.getModel());

		return "team";

	}

	public Challenge getModel() {

		return challenge;
	}

	public void prepare() throws Exception {

		// Load the target team - needed so that we can say which team we are
		// adding the challenge to
		this.team = this.load(Team.class, this.getTeamKey());

		// Always create a new challenge. DynamoDB works in a way that if you
		// save a new object with the same @key, it does an update.
		// This saves us a read request.
		// This works because we pass @key and @teamKey as hidden parameters in
		// the form.
		this.challenge = new Challenge();
	}

	public String getTeamKey() {
		return teamKey;
	}

	public void setTeamKey(String teamKey) {
		this.teamKey = teamKey;
	}

	public Team getLoadedTeam() {
		return team;
	}

}
