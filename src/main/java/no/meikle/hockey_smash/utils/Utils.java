package no.meikle.hockey_smash.utils;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.DefaultTableNameResolver;

public class Utils {

    private static AmazonDynamoDB initializeRemoteDb() {

        System.out.println("Setting DB via Default Client");
        return AmazonDynamoDBClientBuilder.defaultClient();
    }

    private static AmazonDynamoDB initializeLocalDb() {

        System.out.println("Setting DB via local endpoint");
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "local"))
                .build();
    }

    public static AmazonDynamoDB initializeDb() {

        if (System.getenv("AWS_ACCESS_KEY_ID") == null) {
            return initializeLocalDb();
        } else {
            return initializeRemoteDb();
        }
    }

    static String tableNameFor(Class<?> aClass) {
        DefaultTableNameResolver nameResolver = DefaultTableNameResolver.INSTANCE;
        String tableName = nameResolver.getTableName(aClass, nameResolver.config());
        return tableName;
    }
}
