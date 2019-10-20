package no.meikle.hockey_smash;

import java.util.Map;
import no.meikle.hockey_smash.domain.Team;
import no.meikle.hockey_smash.domain.User;
import org.apache.struts2.dispatcher.SessionMap;

public class SessionManager {

	public static final String TEAM_NAME = "teamName";
	public static final String TEAM_KEY = "teamKey";
	public static final String USER_KEY = "userKey";
	public static final String NICKNAME = "nickname";
	public static final String FULL_NAME = "fullname";
	public static final String IS_ADMIN_USER = "isAdminUser";
	public static final String IS_TRAINER = "isTrainer";

	protected Map<String, Object> session;

	public SessionManager(Map<String, Object> aSession) {

		this.session = aSession;

	}

	public String getLoggedInUserKey() {

		return (String) session.get(USER_KEY);

	}

	public String getLoggedInNickname() {

		return (String) session.get(NICKNAME);

	}

	public boolean isLoggedIn() {

		return getLoggedInUserKey() != null;
	}

	public boolean getHasTeam() {

		return this.session.get(TEAM_KEY) != null;

	}

	public String getTeamName() {

		return (String) this.session.get(TEAM_NAME);

	}

	public String getTeamKey() {

		return (String) this.session.get(TEAM_KEY);

	}
	
	public boolean isAdminUser() {

		return (Boolean) this.session.get(IS_ADMIN_USER) == true;

	}

	public boolean isTrainer() {

		return (Boolean) this.session.get(IS_TRAINER) == true;

	}
	
	public void setLoggedInUser(User aUser, Team aTeam) {

		this.session.put(FULL_NAME, aUser.getFullname());
		this.session.put(NICKNAME, aUser.getNickname());
		this.session.put(USER_KEY, aUser.getKey());
		this.session.put(IS_ADMIN_USER, aUser.isAdmin());
		this.session.put(IS_TRAINER, aUser.isTrainer());

		if (aTeam != null) {
			this.session.put(TEAM_NAME, aTeam.getName());
			this.session.put(TEAM_KEY, aTeam.getKey());
		}

	}

	public void invalidateSession() {

		this.session.remove(USER_KEY);
		((SessionMap<String, Object>) this.session).invalidate();

	}

	public void updateTeamName(Team team) {
		
		if (team != null && team.getKey().equals(this.session.get(TEAM_KEY))) {
			this.session.put(TEAM_NAME, team.getName());
		}
		
	}

}
