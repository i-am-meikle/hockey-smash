package no.meikle.hockey_smash.interceptor;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import javax.servlet.http.Cookie;
import no.meikle.hockey_smash.SessionManager;
import no.meikle.hockey_smash.dao.DAO;
import no.meikle.hockey_smash.domain.RememberMe;
import no.meikle.hockey_smash.domain.Team;
import no.meikle.hockey_smash.domain.User;
import org.apache.struts2.ServletActionContext;

public class RememberMeInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -4234569487326806733L;

	private String cookieName;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		SessionManager sm = new SessionManager(ActionContext.getContext().getSession());

		if (!sm.isLoggedIn()) {

			Cookie[] cookies = ServletActionContext.getRequest().getCookies();

			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(this.getCookieName())) {
					this.autoLogin(cookie.getValue(), sm);
				}
			}
		}

		return invocation.invoke();

	}

	private void autoLogin(String cookieString, SessionManager sm) {

		if (cookieString == null) {
			return;
		}

		DAO dao = new DAO((AmazonDynamoDB) ServletActionContext.getServletContext().getAttribute("dynamoDB"));

		RememberMe rememberMe = dao.resolveRememberMe(cookieString);

		if (rememberMe == null) {
			return;
		}
		
		User user = dao.load(User.class, rememberMe.getTeamKey(), rememberMe.getUserKey());

		if (user == null) {
			return;
		}

		if (user.getPasswordHash().equals(rememberMe.getPasswordHash())) {

			Team team = null;

			if (user.getTeam() != null) {
				team = dao.load(Team.class, user.getTeam());
			}

			sm.setLoggedInUser(user, team);

		}

	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

}
