package no.meikle.hockey_smash.utils;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
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
import java.util.Arrays;
import java.util.List;
import no.meikle.hockey_smash.domain.Activity;
import no.meikle.hockey_smash.domain.Challenge;
import no.meikle.hockey_smash.domain.Team;
import no.meikle.hockey_smash.domain.User;

public class DevApplicationBuilder extends AbstractApplicationBuilder  {

	public static void main(String[] args) {

		DevApplicationBuilder builder = new DevApplicationBuilder();
		builder.initializeLocalDb();
		builder.dropOldTables();
		builder.buildTables();
		builder.createAdminUser();

	}

	private void dropOldTables() {

		this.dropTable(User.class);
		this.dropTable(Challenge.class);
		this.dropTable(Team.class);

	}

	protected void buildTables() {

		this.setDropFirst(true); 
		super.buildTables();

	}

	@Override
	protected void generateActivityTable() {

		//Not to be run in production. Just to enable to add GSI to the local DB.
		//In production this should be done manually from the AWS DunamoDB console.

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

	private void initializeLocalDb() {

		dynamoDB = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "local"))
				.build();

	}

}
