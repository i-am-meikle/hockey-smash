package no.meikle.hockey_smash.actions;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import no.meikle.hockey_smash.domain.RememberMe;
import no.meikle.hockey_smash.domain.Team;
import no.meikle.hockey_smash.domain.User;
import no.meikle.hockey_smash.utils.EmailUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@InterceptorRef("jutulStack")
@Namespace("/")
@Results({ @Result(name = "success", type = "redirectAction", location = "smash", params = { "namespace", "/" }),
		@Result(name = "myTeam", type = "redirectAction", location = "teams", params = { "method", "teamSummary",
				"namespace", "/private", "teamKey", "${sessionManager.teamKey}" }),
		@Result(name = "teams", type = "redirectAction", location = "teams", params = { "namespace", "/private" }),
		@Result(name = "reset-password", location = "/WEB-INF/jsp/resetPassword.jsp"),
		@Result(name = "login-failed", location = "/WEB-INF/jsp/loginFailed.jsp"),
		@Result(name = "change-password", location = "/WEB-INF/jsp/changePassword.jsp") })
@AllowedMethods({ "login", "register", "logout", "resetPassword", "sendResetEmail", "changePassword", "changePasswordRequest"})
public class LoginAction extends AbstractJutulAction implements Preparable, ModelDriven<User> {

	private static final long serialVersionUID = 6630163872980963896L;

	private User user;

	public String login() throws Exception {

		User aUser = this.getUserFromEmail(this.getUser().getEmail());

		if (aUser == null) {
			this.addActionError("Username or password is incorrect");
			return "login-failed";
		}

		String passwordHash = aUser.getPasswordHash();

		if (this.getUser().getPasswordHash().equals(passwordHash)) {
			this.setLoggedIn(aUser);
		} else {
			this.addActionError("Username or password is incorrect");
			return "login-failed";
		}

		return this.redirect(aUser);
	}

	public String sendResetEmail() {

		// This looks a bit strange, but the model here is just a placeholder for
		// variables, so when a user enters a fullname or password the JSP page will
		// always map it to the @fullname instance variable on the model.

		String reset = this.getModel().getFullname();

		DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
				.withFilterExpression("fullname = :val1 or email = :val1 or nickname = :val1")
				.addExpressionAttributeValuesEntry(":val1", new AttributeValue(reset));

		List<User> list = new DynamoDBMapper(this.dynamoDB()).scan(User.class, queryExpression);

		if (list.size() == 0) {
			this.addActionError("No user found which matched those details !");
			return "login-failed";
		}

		// There could be multiple users with the same email address since a
		// parent might register multiple children on the same email.
		// Therefore send an email to each user found.

		list.stream().forEach(aUser -> new EmailUtils().sendPasswordResetEmail(aUser));

		this.addActionMessage("Email sent !");

		return SUCCESS;

	}

	public String resetPassword() {

		return "reset-password";

	}

	public String changePasswordRequest() {
		
		this.user = this.load(User.class, this.getModel().getTeam(), this.getModel().getKey());
		
		return "change-password";
	}
	
	public String changePassword() {

		User aUser = this.load(User.class, this.getModel().getTeam(), this.getModel().getKey());

		if (aUser == null) {
			this.addActionError("Password change failed - No user found !");
			return SUCCESS;
		}

		aUser.setPasswordHash(this.getModel().getPasswordHash());
		this.save(aUser);

		this.addActionMessage("Password Changed - Please login.");

		return SUCCESS;

	}

	private User getUserFromEmail(String aName) {

		DynamoDBScanExpression queryExpression = new DynamoDBScanExpression().withFilterExpression("email = :val1")
				.addExpressionAttributeValuesEntry(":val1", new AttributeValue(aName));

		List<User> list = new DynamoDBMapper(this.dynamoDB()).scan(User.class, queryExpression);

		if (list.size() == 0) {
			return null;
		}

		// There should not be two users with the same fullname.
		return list.get(0);
	}

	private User getUser() {
		return user;
	}

	public String register() throws Exception {

		// Check if a user already exists with that Fullname

		User aUser = this.getUserFromEmail(this.getUser().getEmail());

		if (aUser != null) {
			this.addActionError("Sorry, but a user is already registered with the email: " + this.getUser().getFullname()
					+ ". Please use another email or if this email is yours, reset your password using the reset password function.");
			return SUCCESS;
		}

		this.save(this.getUser());

		this.setLoggedIn(this.getUser());

		new EmailUtils().sendWelcomeEmail(this.getUser(), this.getTrainersEmail());

		return this.redirect(this.getUser());

	}

	private List<String> getTrainersEmail() {

		DynamoDBQueryExpression<User> query = new DynamoDBQueryExpression<User>()
				.withKeyConditionExpression("team = :teamKey")
				.addExpressionAttributeValuesEntry(":teamKey", new AttributeValue(this.getUser().getTeam()))
				.addExpressionAttributeValuesEntry(":isTrainer", new AttributeValue().withN("1"))
				.withFilterExpression("trainer = :isTrainer");

		PaginatedQueryList<User> list = new DynamoDBMapper(this.dynamoDB()).query(User.class, query);

		return list.stream().map(u -> u.getEmail()).collect(Collectors.toList());

	}

	private String redirect(User aUser) {

		if (aUser.isAdmin()) {
			return "teams";
		} else if (aUser.isTrainer()) {
			return "myTeam";
		} else
			return SMASH_RESULT;

	}

	private void setLoggedIn(User aUser) {

		Team team = null;

		if (aUser.getTeam() != null) {
			team = this.load(Team.class, aUser.getTeam());
		}

		if (team == null && aUser.isAdmin()) {
			team = Team.ADMIN_TEAM;
		}

		this.getSessionManager().setLoggedInUser(aUser, team);

		RememberMe rememberMe = new RememberMe(aUser);
		this.save(rememberMe);

		Cookie newCookie = new Cookie("hockey-smash", rememberMe.getCookieValue());
		newCookie.setMaxAge(60 * 60 * 24 * 365);

		ServletActionContext.getResponse().addCookie(newCookie);

	}

	public String logout() throws Exception {

		this.getSessionManager().invalidateSession();

		// delete the rememberMe info & cookie.

		Cookie[] cookies = ServletActionContext.getRequest().getCookies();

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("hockey-smash")) {
				RememberMe rememberMe = this.getDAO().resolveRememberMe(cookie.getValue());
				if (rememberMe != null) {
					this.delete(rememberMe);
				}
				cookie.setMaxAge(0);
				ServletActionContext.getResponse().addCookie(cookie);
			}
		}

		return SUCCESS;
	}

	public User getModel() {

		return user;
	}

	public void prepare() throws Exception {

		user = new User();

	}
}
