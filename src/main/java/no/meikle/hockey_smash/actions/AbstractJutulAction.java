package no.meikle.hockey_smash.actions;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.meikle.hockey_smash.SessionManager;
import no.meikle.hockey_smash.dao.DAO;
import no.meikle.hockey_smash.domain.Challenge;
import no.meikle.hockey_smash.domain.Message;
import no.meikle.hockey_smash.domain.User;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJutulAction extends ActionSupport implements ApplicationAware, SessionAware {

	private static final long serialVersionUID = -6850343291311588759L;
	public static final String SMASH_RESULT = "smash";

	private Map<String, Object> applicaionMap;
	protected SessionManager sessionManager;
	private Logger log = LoggerFactory.getLogger(AbstractJutulAction.class);

	public AbstractJutulAction() {
		super();
	}

	public String getRedirectUrl() {
		
		StringBuilder path = new StringBuilder();
		path.append(ServletActionContext.getRequest().getAttribute(javax.servlet.RequestDispatcher.FORWARD_SERVLET_PATH));
		
		String queryString = ServletActionContext.getRequest().getQueryString();
		if (queryString != null && !queryString.isEmpty()) {
			path.append("?");
			path.append(queryString);
			}
		
		
		return path.toString();
	}
	
	public List<Message> getTeamMessages() {

		if (this.getSessionManager().getTeamKey() == null || this.getSessionManager().getTeamKey().equals(User.ADMIN_STRING)) {
			return Collections.emptyList();
		}
		
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":teamKey", new AttributeValue(this.getSessionManager().getTeamKey()));

		DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
				.withKeyConditionExpression("targetKey = :teamKey").withExpressionAttributeValues(eav);

		DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDB());
		PaginatedQueryList<Message> list = mapper.query(Message.class, queryExpression);

		return list;
	}
	
	protected Logger getLogger() {
		return log;
	}
	
	public User getLoggedInUser() {

		return this.load(User.class, this.getSessionManager().getTeamKey(), this.getSessionManager().getLoggedInUserKey());

	}

	public void setApplication(Map<String, Object> application) {

		this.applicaionMap = application;

	}

	protected AmazonDynamoDB dynamoDB() {

		return (AmazonDynamoDB) applicaionMap.get("dynamoDB");
	}

	protected void save(Object object) {
		DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDB());
		mapper.save(object);
	}

	protected <T> T load(Class<T> aClass, String key) {

		DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDB());
		return mapper.load(aClass, key);
	}

	protected <T> T load(Class<T> aClass, String key, Object rangeKey) {

		DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDB());
		return mapper.load(aClass, key, rangeKey);
	}

	protected void delete(Object object) {

		DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDB());
		mapper.delete(object);

	}

	public List<Challenge> getActiveChallenges() {

		return this.getActiveChallenges(this.getSessionManager().getTeamKey());

	}

	public List<Challenge> getActiveChallenges(String teamKey) {

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":teamKey", new AttributeValue(teamKey));
		eav.put(":isActive", new AttributeValue().withN("1"));

		DynamoDBQueryExpression<Challenge> queryExpression = new DynamoDBQueryExpression<Challenge>()
				.withKeyConditionExpression("team = :teamKey").withExpressionAttributeValues(eav)
				.withFilterExpression("active = :isActive");

		DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDB());
		PaginatedQueryList<Challenge> list = mapper.query(Challenge.class, queryExpression);

		List<Challenge> result = new ArrayList<Challenge>(list);

		result.sort((c1, c2) -> c1.getStartDate().compareTo(c2.getStartDate()));

		return result;
	}
	
	

	public void setSession(Map<String, Object> aSession) {

		this.sessionManager = new SessionManager(aSession);

	}

	public DAO getDAO() {
		return new DAO(this.dynamoDB());
	}
	
	public SessionManager getSessionManager() {

		return this.sessionManager;
	}

	public String getStaticRoot() {

		return ServletActionContext.getRequest().getContextPath();
	}

}