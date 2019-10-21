package no.meikle.hockey_smash.utils;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import no.meikle.hockey_smash.domain.Activity;
import no.meikle.hockey_smash.domain.Challenge;
import no.meikle.hockey_smash.domain.Message;
import no.meikle.hockey_smash.domain.RememberMe;
import no.meikle.hockey_smash.domain.Team;
import no.meikle.hockey_smash.domain.User;

public class AbstractApplicationBuilder {

    protected AmazonDynamoDB dynamoDB;
    private boolean dropFirst = false;

    protected void buildTables() {

        this.generateUserTable();
        this.generateChallengeTable();
        this.generateTeamTable();
        this.generateActivityTable();
        this.generateRememberMeTable();
        this.generateMessageTable();

    }

    protected void generateMessageTable() {
        this.generateTable(Message.class, 1L);
    }

    protected void generateRememberMeTable() {
        this.generateTable(RememberMe.class, 1L);
    }

    protected void generateActivityTable() {
        this.generateTable(Activity.class, 10L);
    }

    protected void generateTeamTable() {
        this.generateTable(Team.class, 1L);
    }

    protected void generateChallengeTable() {
        this.generateTable(Challenge.class, 1L);
    }

    protected void generateUserTable() {
        this.generateTable(User.class, 1L);
    }

    protected void generateTable(Class<?> clazz, Long capacity) {

        if (this.isDropFirst()) {
            dropTable(clazz);
        }

        try {

            DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

            System.out.println("Attempting to create " + clazz.getTypeName() + " table; please wait...");

            CreateTableRequest request = mapper.generateCreateTableRequest(clazz);
            request.setProvisionedThroughput(new ProvisionedThroughput(capacity, capacity));
            CreateTableResult result = dynamoDB.createTable(request);

            System.out.println("Success.  Table status: " + result.getTableDescription().getTableStatus());

        } catch (Exception e) {
            System.err.println("Operation Failed: ");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    protected void dropTable(Class<?> clazz) {

        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        System.out.println("Dropping table " + clazz.getTypeName() + "; please wait...");

        DeleteTableRequest request = mapper.generateDeleteTableRequest(clazz);
        TableUtils.deleteTableIfExists(dynamoDB, request);
    }

    protected void createAdminUser() {

        try {
            System.out.println("Attempting to add Admin user; waiting for table to be available ...");

            TableUtils.waitUntilActive(dynamoDB, Utils.tableNameFor(User.class));

            System.out.println(" finished waiting");

            DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

            // Insert Admin User
            User user = new User();
            user.setAdmin(true);
            user.setTeam(User.ADMIN_STRING);
            user.setEmail("hockey-smash-admin@meikle.dev");
            user.setFullname("Superman");
            user.setPassword("Clark Kent");

            mapper.save(user);

            System.out.println("Success.");

        } catch (Exception e) {
            System.err.println("Unable to add admin user: ");
            System.err.println(e.getMessage());
        }

    }

    public boolean isDropFirst() {
        return dropFirst;
    }

    public void setDropFirst(boolean dropfirst) {
        this.dropFirst = dropfirst;
    }

}
