package no.meikle.hockey_smash.actions;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.meikle.hockey_smash.AlphanumComparator;
import no.meikle.hockey_smash.AlphanumComparator.Extractor;
import no.meikle.hockey_smash.domain.Challenge;
import no.meikle.hockey_smash.domain.Team;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@InterceptorRef("jutulStack")
@Namespace("/private")
@Results({ @Result(name = "success", location = "/WEB-INF/jsp/teams.jsp"),
		@Result(name = "editTeam", location = "/WEB-INF/jsp/editTeam.jsp"),
		@Result(name = "team-summary", location = "/WEB-INF/jsp/teamSummary.jsp"),
		@Result(name = "myTeam", type = "redirectAction", location = "teams", params = { "method", "teamSummary",
				"namespace", "/private", "teamKey", "${sessionManager.teamKey}" }),
		@Result(name = "redirect-editTeam", type = "redirectAction", location = "teams", params = { "method",
				"editTeam", "teamKey", "${teamKey}" }),
		@Result(name = "teams", type = "redirectAction", location = "teams", params = { "namespace", "/private" }) })
@AllowedMethods({ "addTeam", "editTeam", "saveTeam", "makeActive", "teamSummary" })
public class TeamsAction extends AbstractJutulAction implements Preparable, ModelDriven<Team> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4860523392842004665L;

	private Team team;
	private String teamKey;
	private String selectedChallenge;

	public void setTeamKey(String aTeam) {

		this.teamKey = aTeam;
	}

	public String getTeamKey() {

		return this.teamKey;
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

	public List<Challenge> getChallenges() {

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":teamKey", new AttributeValue(this.getTeamKey()));

		DynamoDBQueryExpression<Challenge> queryExpression = new DynamoDBQueryExpression<Challenge>()
				.withKeyConditionExpression("team = :teamKey").withExpressionAttributeValues(eav);

		DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDB());
		PaginatedQueryList<Challenge> list = mapper.query(Challenge.class, queryExpression);

		List<Challenge> result = new ArrayList<Challenge>(list);

		result.sort((c1, c2) -> c1.getStartDate().compareTo(c2.getStartDate()));

		return result;
	}
	
	public String addTeam() throws Exception {

		// Do nothing

		return "editTeam";

	}

	public String editTeam() throws Exception {

		if (this.getTeamKey() == null) {
			this.addActionError("Team identifier not provided");
			return SUCCESS;
		}

		team = this.load(Team.class, this.getTeamKey());

		return "editTeam";

	}

	public String saveTeam() throws Exception {

		this.save(this.getModel());
		// There is a possibility that the team name in the session needs to be refreshed
		this.getSessionManager().updateTeamName(this.getModel());

		if (this.getLoggedInUser().isAdmin()) {
			return "teams";
		} else {
			return "myTeam";
		}
	}

	public String makeActive() throws Exception {

		Challenge challenge = this.load(Challenge.class, this.getTeamKey(), this.getSelectedChallenge());

		challenge.setActive(!challenge.isActive());

		this.save(challenge);

		return "redirect-editTeam";

	}

	public String teamSummary() throws Exception {

		team = this.load(Team.class, this.getTeamKey());

		return "team-summary";

	}

	public List<Challenge> getActiveChallenges() {

		return this.getActiveChallenges(this.getTeamKey());

	}
	
	public Team getModel() {

		return team;
	}

	public void prepare() throws Exception {

		team = new Team();
	}

	public String getSelectedChallenge() {
		return selectedChallenge;
	}

	public void setSelectedChallenge(String selectedChallenge) {
		this.selectedChallenge = selectedChallenge;
	}

}
