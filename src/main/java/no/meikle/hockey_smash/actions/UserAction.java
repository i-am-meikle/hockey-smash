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
import java.util.List;
import no.meikle.hockey_smash.AlphanumComparator;
import no.meikle.hockey_smash.AlphanumComparator.Extractor;
import no.meikle.hockey_smash.domain.Team;
import no.meikle.hockey_smash.domain.User;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@InterceptorRef("jutulStack")
@Action("user")
@Namespace("/private")
@Results({ @Result(location = "/WEB-INF/jsp/user.jsp"), @Result(name = "users", location = "/WEB-INF/jsp/allUsers.jsp"),
		@Result(name = "redirect", type = "redirectAction", location = "user", params = { "method", "users" }),
		@Result(name = "redirect-user", type = "redirectAction", location = "user", params = { "userKey",
				"${userKey}" }) })
@AllowedMethods({ "saveUser", "users", "toggleTrainer" })
public class UserAction extends AbstractJutulAction implements ModelDriven<User>, Preparable {

	private static final long serialVersionUID = 4860523392842004665L;
	private User user;
	private String userKey;
	private String teamKey;

	public String execute() throws Exception {

		String loggedInUserKey = this.getSessionManager().getLoggedInUserKey();

		if (this.getUserKey() != null && (!loggedInUserKey.equals(this.getUserKey()))
				&& !(this.getSessionManager().isAdminUser() || this.getSessionManager().isTrainer())) {
			this.addActionError("You are not authorised to perform that function");
			return SMASH_RESULT;
		}

		return SUCCESS;
	}

	public String toggleTrainer() throws Exception {

		if (user.isTrainer()) {
			user.setTrainer(false);
		} else {
			user.setTrainer(true);
		}

		this.save(user);

		return "redirect";
	}

	public String saveUser() throws Exception {

		this.save(user);
		Team team = this.load(Team.class, user.getTeam());
		this.getSessionManager().setLoggedInUser(user, team);

		return SUCCESS;
	}

	public String users() throws Exception {

		return "users";
	}

	public User getModel() {

		return user;
	}

	public void prepare() throws Exception {

		if (this.getUserKey() == null || this.getUserKey().isEmpty()) {
			user = this.getLoggedInUser();
		} else if (this.getTeamKey() == null || this.getTeamKey().isEmpty()) {
			user = this.load(User.class, this.getSessionManager().getTeamKey(), this.getUserKey());
		} else {
			user = this.load(User.class, this.getTeamKey(), this.getUserKey());
		}

	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public List<User> getAllUsers() {

		if (this.getLoggedInUser().isTrainer()) {
			return this.getAllUsersFor(this.getSessionManager().getTeamKey());
		} else {
			return this.getAllUsersInSystem();
		}
	}

	private List<User> getAllUsersFor(String teamKey) {

		DynamoDBQueryExpression<User> query = new DynamoDBQueryExpression<User>()
				.withKeyConditionExpression("team = :val1")
				.addExpressionAttributeValuesEntry(":val1", new AttributeValue(teamKey));

		DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDB());
		PaginatedQueryList<User> list = mapper.query(User.class, query);

		return this.sort(list);

	}

	private List<User> getAllUsersInSystem() {

		DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDB());
		PaginatedScanList<User> list = mapper.scan(User.class, new DynamoDBScanExpression());

		return this.sort(list);

	}

	private List<User> sort(List<User> list) {

		List<User> result = new ArrayList<User>(list);

		result.sort(new AlphanumComparator<User>(new Extractor<User>() {

			public String getString(User anObject) {
				return anObject.getFullname();
			}
		}));

		return result;
	}

	public String getTeamKey() {
		return teamKey;
	}

	public void setTeamKey(String teamKey) {
		this.teamKey = teamKey;
	}

}
