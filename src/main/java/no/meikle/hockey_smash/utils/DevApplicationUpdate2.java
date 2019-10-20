package no.meikle.hockey_smash.utils;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.util.TableUtils.TableNeverTransitionedToStateException;
import java.util.Arrays;
import java.util.List;
import no.meikle.hockey_smash.domain.Activity;
import no.meikle.hockey_smash.domain.Challenge;
import no.meikle.hockey_smash.domain.Message;
import no.meikle.hockey_smash.domain.Team;
import no.meikle.hockey_smash.domain.User;

public class DevApplicationUpdate2 extends AbstractApplicationBuilder {

	public static void main(String[] args) {

		DevApplicationUpdate2 builder = new DevApplicationUpdate2();
		builder.createMessageTable();

	}

	public void changePassword(String key, String password) {
		this.initializeRemoteDb();
		
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
		User user = mapper.load(User.class, key);
		user.setPassword(password);
		mapper.save(user);
		
	}
	
	private void initializeRemoteDb() {

		dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_1).build();

	}
	
	private void createMessageTable() {

		this.initializeRemoteDb();
		this.setDropFirst(false);
		this.generateTable(Message.class, 1L);
		
	}

	public void recreateActivityTable() {

		//Not to be run in production. Just to enable to add GSI to the local DB.
		//In production this should be done manually from the AWS DunamoDB console.
		
		this.initializeLocalDb();
		this.setDropFirst(true);

		try {

			DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

			if (this.isDropFirst()) {
				System.out.println("Dropping table activity; please wait...");

				DeleteTableRequest request = mapper.generateDeleteTableRequest(Activity.class);
				TableUtils.deleteTableIfExists(dynamoDB, request);
			}

			System.out.println("Attempting to create activity table; please wait...");

			CreateTableRequest request = mapper.generateCreateTableRequest(Activity.class);
			request.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

			GlobalSecondaryIndex teamIndex = new GlobalSecondaryIndex();
			teamIndex.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
			teamIndex.setIndexName("team-activities");
			teamIndex.setKeySchema(Arrays.asList(new KeySchemaElement("teamKey", KeyType.HASH),
					new KeySchemaElement("activityDate", KeyType.RANGE)));
			teamIndex.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

			GlobalSecondaryIndex challengeIndex = new GlobalSecondaryIndex();
			challengeIndex.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
			challengeIndex.setIndexName("challenge-activities");
			challengeIndex.setKeySchema(Arrays.asList(new KeySchemaElement("challengeKey", KeyType.HASH), new KeySchemaElement("activityDate", KeyType.RANGE)));
			challengeIndex.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

			List<GlobalSecondaryIndex> globalSecondaryIndexes = Arrays.asList(teamIndex, challengeIndex);

			request.setGlobalSecondaryIndexes(globalSecondaryIndexes);
			CreateTableResult result = dynamoDB.createTable(request);

			System.out.println("Success.  Table status: " + result.getTableDescription().getTableStatus());

		} catch (Exception e) {
			System.err.println("Operation Failed: ");
			System.err.println(e.getMessage());
		}

	}

	protected void buildTables() {

		this.initializeLocalDb();
		this.setDropFirst(false);

		// Manual Steps performed through the AWS DynamoDB Console BEFORE running script
		// Set team of SuperUser to SUPERMAN
		
		this.migrateTeams();
		this.migrateChallenges();
		this.migrateUsers();
		
		// Manual Steps performed through the AWS DynamoDB Console
		// #Add new column "teamKey" to activity table
		// #Add new global secondary index - team-activities(teamKey, activityDate)
		// #Add new global secondary index - challenges-activities(challengeKey, activityDate)

	}

	public void migrateUsers() {

		this.generateTable(User.class, 1L);
		
		this.waitUntilActive("users-2");
		
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

		DynamoDBScanExpression expression = new DynamoDBScanExpression();
		List<User> list = mapper.scan(User.class, expression);

		list.forEach((u) -> {
			User u2 = new User();
			u2.setKey(u.getKey());
			u2.setFullname(u.getFullname());
			u2.setNickname(u.getNickname());
			u2.setPasswordHash(u.getPasswordHash());
			u2.setEmail(u.getEmail());
			u2.setTeam(u.getTeam());
			u2.setAdmin(u.isAdmin());
			u2.setTrainer(u.isTrainer());
			
			mapper.save(u2);
			
		});
		
	}

	protected void waitUntilActive(String tableName) {
		
		System.out.println("Waiting for table to become active: " + tableName);
		
		try {
			TableUtils.waitUntilActive(dynamoDB, tableName);
		} catch (TableNeverTransitionedToStateException | InterruptedException e) {
		
			System.err.println("Operation Failed: ");
			System.err.println(e.getMessage());
			System.exit(1);
		}
		
		System.out.println("Table Active:" + tableName);
	}

	public void recreateTable(Class<?> clazz, Long capasity) {

		this.initializeLocalDb();
		this.setDropFirst(true);
		this.generateTable(clazz, capasity);

	}

	private void migrateChallenges() {

		this.generateTable(Challenge.class, 1L);
		
		this.waitUntilActive("challenges");
		
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

		DynamoDBScanExpression expression = new DynamoDBScanExpression();
		List<Challenge> list = mapper.scan(Challenge.class, expression);

		list.forEach((c) -> {
			Challenge c2 = new Challenge();
			c2.setActive(true);
			c2.setEndDate(c.getEndDate());
			c2.setGoal(c.getGoal());
			c2.setName(c.getName());
			c2.setStartDate(c.getStartDate());
			c2.setTeam(c.getTeam());
			c2.setKey(c.getKey());

			mapper.save(c2);
		});
	}

	private void migrateTeams() {

		this.generateTable(Team.class, 1L);
		
		this.waitUntilActive("teams");
		
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

		DynamoDBScanExpression expression = new DynamoDBScanExpression();
		List<Team> list = mapper.scan(Team.class, expression);

		list.forEach((t) -> {
			Team t2 = new Team();
			t2.setKey(t.getKey());
			t2.setName(t.getName());
			mapper.save(t2);
		});

	}

	private void initializeLocalDb() {

		dynamoDB = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "local"))
				.build();

	}

}
