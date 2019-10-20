package no.meikle.hockey_smash.utils;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper.FailedBatch;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import no.meikle.hockey_smash.domain.Activity;
import no.meikle.hockey_smash.domain.Challenge;
import no.meikle.hockey_smash.domain.Message;
import no.meikle.hockey_smash.domain.RememberMe;
import no.meikle.hockey_smash.domain.Team;
import no.meikle.hockey_smash.domain.User;
import no.meikle.hockey_smash.domain.old.Challenge2;
import no.meikle.hockey_smash.domain.old.Team2;
import no.meikle.hockey_smash.domain.old.User2;

public class HockeySmashBackup {

    private static final Long DEFAULT_CAPASITY = 1L;
    private File location;
    private AmazonDynamoDB dynamoDB;
    private AmazonDynamoDBClientBuilder builder;

    public HockeySmashBackup(File aLocation, String target) {

        this.location = aLocation;

        if (target.compareToIgnoreCase("local") == 0) {
            this.initializeLocalDb();
        } else if (target.compareToIgnoreCase("remote") == 0) {
            this.initializeRemoteDb();
        }

    }

    private void initializeRemoteDb() {

        builder = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_1);
        dynamoDB = builder.build();

    }

    public static void main(String[] args) throws IOException {

        if (args.length < 1 || args.length > 3) {
            System.out.println("Invalid number of arguments: Usage: HockeySmash backup|restore <location> <target>");
            System.exit(1);
        }

        String operation = args[0];
        String locationString = args[1];
        String target = args[2];

        File location = new File(locationString);

        if (!location.exists()) {
            boolean mkdirs = location.mkdirs();
            if (!mkdirs) {
                System.out.println("Provided location does not exist and could not be created.");
                System.exit(1);
            }
        }

        if (operation.compareToIgnoreCase("backup") == 0) {
            new HockeySmashBackup(location, target).backup();
        } else if (operation.compareToIgnoreCase("restore") == 0) {
            new HockeySmashBackup(location, target).restore();
        } else if (operation.compareToIgnoreCase("migrate") == 0) {
            new HockeySmashBackup(location, target).migrate();
        }

    }

    private void restore() throws IOException {
        this.restoreFromOld();
    }

    private void restoreFromOld() throws IOException {

        System.out.println("Restoring database: " + builder.getRegion());

//        this.restore(User.class, User2.class);
//        this.restore(Team.class, Team2.class);
//        this.restore(Message.class);
        // Only regenerate the RememberMe table, do not reload values.
        // This will cause a re-login
//        this.generateTable(RememberMe.class);
//        this.restore(Challenge.class, Challenge2.class);
//        this.recreateActivityTable();
        this.restore(Activity.class, false, 5L);
    }

    private void restoreNew() throws IOException {

        System.out.println("Restoring database: " + builder.getRegion());

        this.restore(User.class);
        this.restore(Team.class);
        this.restore(Message.class);
        // Only regenerate the RememberMe table, do not reload values.
        // This will cause a re-login
        this.generateTable(RememberMe.class);
        this.restore(Challenge.class);
        this.recreateActivityTable();
        this.restore(Activity.class, false, 5L);

    }

    private void migrate() throws IOException {

        System.out.println("Migrating database: " + builder.getRegion());

        this.generateTable(User.class);
        this.migrate("users-2", old -> {
            User user = new User();
            user.setFullname(getString(old.get("fullname")));
            user.setTrainer(getBoolean(old.get("trainer")));
            user.setEmail(getString(old.get("email")));
            user.setNickname(getString(old.get("nickname")));
            user.setAdmin(getBoolean(old.get("admin")));
            user.setTeam(getString(old.get("team")));
            user.setPasswordHash(getString(old.get("passwordHash")));
            user.setKey(getString(old.get("key")));
            return user;
        });
        this.generateTable(Team.class);
        this.migrate("teams-2", old -> {
            Team team = new Team();
            team.setKey(getString(old.get("key")));
            team.setName(getString(old.get("name")));
            return team;
        });
        // Only regenerate the RememberMe table, do not reload values.
        // This will cause a re-login
        this.generateTable(RememberMe.class);

        this.generateTable(Challenge.class);
        this.migrate("challenges-2", old -> {
            Challenge c = new Challenge();
            c.setUnit(getString(old.get("unit")));
            c.setActive(getBoolean(old.get("active")));
            c.setTeam(getString(old.get("team")));
            c.setEndDate(getDate(old.get("endDate")));
            c.setStartDate(getDate(old.get("startDate")));
            c.setKey(getString(old.get("key")));
            c.setName(getString(old.get("name")));
            c.setGoal(getInt(old.get("goal")));
            c.setTagline(getString(old.get("tagline")));
            return c;
        });
        this.restore(Message.class);
        this.recreateActivityTable();
        this.restore(Activity.class, false, 5L);
    }

    private static int getInt(JsonNode node) {
        return node.get("n").asInt();
    }

    private static boolean getBoolean(JsonNode node) {
        return getInt(node) == 1;
    }

    private static String getString(JsonNode node) {
        return node.get("s").asText();
    }

    private static Date getDate(JsonNode node) {
        return Date.from(OffsetDateTime.parse(node.get("s").asText()).toInstant());
    }

    private <T> void migrate(String source, Function<JsonNode, T> mapper) throws IOException {

        String target = location.getAbsolutePath() + File.separator + source;

        FileInputStream in = new FileInputStream(target);
        ObjectMapper serializer = new ObjectMapper();
        JsonNode root = serializer.readTree(in);

        List<T> objects = new ArrayList<>();
        root.forEach(e -> mapper.andThen(objects::add).apply(e));

        DynamoDBMapper db = new DynamoDBMapper(dynamoDB);
        List<FailedBatch> result = db.batchSave(objects);

        if (result.isEmpty()) {
            System.out.println(objects.size() + " objects restored");
        } else {
            System.out.println("Restore failed: activities");
            result.stream().forEach(f -> f.getException().printStackTrace());

        }

    }

    private void migrateActivityTable() throws IOException {

        System.out.println("Migrating Activity Table");

        String activityTarget = location.getAbsolutePath() + File.separator + "activities";

        FileInputStream in = new FileInputStream(activityTarget);
        ObjectMapper serializer = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

        List<Activity> objects;

        try {
            objects = serializer.readValue(in,
                    serializer.getTypeFactory().constructCollectionType(List.class, Activity.class));
        } finally {
            in.close();
        }

        // Load in all the teams.
        String teamsTarget = location.getAbsolutePath() + File.separator + "users";

        in = new FileInputStream(teamsTarget);
        serializer = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<User> users;

        try {

            users = serializer.readValue(in,
                    serializer.getTypeFactory().constructCollectionType(List.class, User.class));
        } finally {
            in.close();
        }

        Map<String, String> teamMap = users.stream().collect(Collectors.toMap(User::getKey, User::getTeam));

        objects.stream().forEach(u -> {
            u.setTeamKey(teamMap.get(u.getUserKey()));
            u.setKey(java.util.UUID.randomUUID().toString());
        });

        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        List<FailedBatch> result = mapper.batchSave(objects);

        if (result.isEmpty()) {
            System.out.println(objects.size() + " objects restored");
        } else {
            System.out.println("Restore failed: activities");
            result.stream().forEach(f -> f.getException().printStackTrace());

        }
    }

    public void recreateActivityTable() {

        // Not to be run in production. Just to enable to add GSI to the local DB.
        // In production this should be done manually from the AWS DunamoDB console.

        try {

            this.dropTable(Activity.class);

            DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

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
            challengeIndex.setKeySchema(Arrays.asList(new KeySchemaElement("challengeKey", KeyType.HASH),
                    new KeySchemaElement("activityDate", KeyType.RANGE)));
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

    protected void generateTable(Class<?> clazz) {
        this.generateTable(clazz, DEFAULT_CAPASITY);
    }

    protected void generateTable(Class<?> clazz, Long capacity) {

        try {

            this.dropTable(clazz);

            DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

            String tableName = Utils.tableNameFor(clazz);

            System.out.println("Attempting to create " + tableName + " table; please wait...");

            CreateTableRequest request = mapper.generateCreateTableRequest(clazz);
            request.setProvisionedThroughput(new ProvisionedThroughput(capacity, capacity));
            CreateTableResult result = dynamoDB.createTable(request);

            System.out.println("Success.  Table status: " + result.getTableDescription().getTableStatus());

            TableUtils.waitUntilActive(dynamoDB, tableName);

            System.out.println("Table " + tableName + " Ready");

        } catch (Exception e) {
            System.err.println("Operation Failed: ");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private void dropTable(Class aClass) {

        try {

            String tableName = Utils.tableNameFor(aClass);

            DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

            System.out.println("Dropping table " + tableName + "; please wait");

            DeleteTableRequest dropRequest = mapper.generateDeleteTableRequest(aClass);
            TableUtils.deleteTableIfExists(dynamoDB, dropRequest);

            this.waitUntilNotExists(aClass);
            System.out.println("Table " + tableName + " gone.");

        } catch (Exception e) {
            System.err.println("Operation Failed: ");
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    private void waitUntilNotExists(Class aClass) {

        String tableName = Utils.tableNameFor(aClass);

        boolean available = true;

        while (available) {

            try {
                dynamoDB.describeTable(new DescribeTableRequest(tableName)).getTable();
            } catch (ResourceNotFoundException ex) {
                available = false;
            }

            try {
                Thread.sleep(1000);
                System.out.print(".");
            } catch (InterruptedException e) {
                System.err.println("Sleep Interrupted !");
                e.printStackTrace();
            }

        }

    }

    private void restore(Class<?> aClass) throws IOException {
        this.restore(aClass, aClass);
    }

    private void restore(Class<?> aClass, Class<?> from) throws IOException {
        this.restore(aClass, Utils.tableNameFor(from));
    }

    private void restore(Class<?> aClass, String from) throws IOException {
        this.restore(aClass, true, from);
    }

    private void restore(Class<?> aClass, boolean drop, String from) throws IOException {
        this.restore(aClass, drop, from, DEFAULT_CAPASITY);
    }

    private void restore(Class<?> aClass, boolean drop, Long capasity) throws IOException {
        this.restore(aClass, drop, Utils.tableNameFor(aClass), capasity);
    }

    private void restore(Class<?> aClass, boolean drop, String from, Long capasity) throws IOException {

        if (drop) {
            this.dropTable(aClass);
            this.generateTable(aClass, capasity);
        }

        String to = Utils.tableNameFor(aClass);

        System.out.println("Restoring Table: " + to + " from: " + from);

        String target = location.getAbsolutePath() + File.separator + from;

        FileInputStream in = new FileInputStream(target);
        ObjectMapper serializer = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

        List<Object> objects;

        try {
            objects = serializer.readValue(in,
                    serializer.getTypeFactory().constructCollectionType(List.class, aClass));
        } finally {
            in.close();
        }

        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        List<FailedBatch> result = mapper.batchSave(objects);

        if (result.isEmpty()) {
            System.out.println(objects.size() + " objects restored");
        } else {
            System.out.println("Restore failed: " + to);
            result.stream().forEach(f -> f.getException().printStackTrace());

        }
    }

    private void backup() throws IOException {

       this.backupOld();
    }

    private void backupOld() throws IOException {

        System.out.println("Backing up database: " + builder.getRegion());

        this.backup(User2.class);
        this.backup(Team2.class);
        this.backup(Challenge2.class);
        this.backup(Activity.class);
        this.backup(Message.class);

        System.out.println("Backup finished " + builder.getRegion());
    }

    private void backupNew() throws IOException {

        System.out.println("Backing up database: " + builder.getRegion());

        this.backup(User.class);
        this.backup(Team.class);
        this.backup(Challenge.class);
        this.backup(Activity.class);
        this.backup(Message.class);

        System.out.println("Backup finished " + builder.getRegion());
    }

    private void backup(Class<?> aClass) throws IOException {

        String tableName = Utils.tableNameFor(aClass);

        System.out.println("Backing up table: " + tableName);

        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        PaginatedScanList<?> result = mapper.scan(aClass, new DynamoDBScanExpression());

        String target = location.getAbsolutePath() + File.separator + tableName;

        FileOutputStream out = new FileOutputStream(target);
        ObjectMapper serializer = new ObjectMapper();

        try {
            serializer.writeValue(out, result.toArray());
        } finally {
            out.close();
        }

        System.out.println(result.size() + " records backed up from " + tableName);
    }

    private void initializeLocalDb() {

        builder = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "local"));

        dynamoDB = builder.build();

    }

}
