package no.meikle.hockey_smash.utils;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TableAnalysis {

    private final List<String> commandLineArgs;
    protected AmazonDynamoDB dynamoDB;

    public TableAnalysis(String[] args) {
        this.commandLineArgs = Arrays.asList(args);
    }

    public static void main(String[] args) {

        new TableAnalysis(args).analyseTables();
    }

    protected void analyseTables() {

        this.initializeDb();
        this.listTables();
    }

    private void listTables() {

		String table = this.tableToList();
		if (table == null) {
			this.analyseAllTables();
		} else {
			this.analyseTable(table);
		}
	}

    private String tableToList() {

        int tableIndex = commandLineArgs.indexOf("-t");

        if (tableIndex == -1) {
            return null;
        } else {
            return commandLineArgs.get(tableIndex + 1);
        }

    }

    protected void initializeDb() {

        dynamoDB = Utils.initializeDb();

    };

    private void analyseAllTables() {

        System.out.println("Listing tables");

        for (String table : dynamoDB.listTables().getTableNames()) {
            this.analyseTable(table);
        }

    }

    protected void analyseTable(String table) {

        System.out.println("TABLE: " + table);

        ScanResult result = dynamoDB.scan(new ScanRequest(table));

        System.out.printf("COUNT: %d \n", result.getCount());

        if (this.isVerbose()) {
            for (Map<String, AttributeValue> item : result.getItems()) {
                System.out.println("---------------------------------------------");
                for (Entry<String, AttributeValue> attribute : item.entrySet()) {
                    System.out.println(attribute);
                }
            }
        }
    }

    private boolean isVerbose() {
        return commandLineArgs.contains("-v");
    }

}
