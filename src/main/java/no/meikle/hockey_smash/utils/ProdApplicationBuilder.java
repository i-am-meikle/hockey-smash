package no.meikle.hockey_smash.utils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class ProdApplicationBuilder extends AbstractApplicationBuilder {

	public static void main(String[] args) {

		new ProdApplicationBuilder().buildTables();

	}

	protected void buildTables() {

		this.initializeRemoteDb();

		super.buildTables();

	}



	private void initializeRemoteDb() {

		dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_1).build();

	}
	

}
