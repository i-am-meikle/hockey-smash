package no.meikle.hockey_smash.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import no.meikle.hockey_smash.domain.RememberMe;

public class DAO {

	private AmazonDynamoDB dynamoDB;

	public DAO(AmazonDynamoDB aDynamoDB) {

		this.setDynamoDB(aDynamoDB);
		
	}

	public <T> T load(Class<T> aClass, String hash) {

		DynamoDBMapper mapper = new DynamoDBMapper(this.getDynamoDB());
		return mapper.load(aClass, hash);
	}

	private AmazonDynamoDB getDynamoDB() {
		return dynamoDB;
	}

	private void setDynamoDB(AmazonDynamoDB dynamoDB) {
		this.dynamoDB = dynamoDB;
	}

	public <T> T load(Class<T> aClass, String hash, String range) {
		
		DynamoDBMapper mapper = new DynamoDBMapper(this.getDynamoDB());
		return mapper.load(aClass, hash, range);
	}
	
	public RememberMe resolveRememberMe(String cookieString) {

		String[] array = cookieString.split("!");

		if (array.length != 2) {
			return null;
		}

		String teamKey = array[0];
		String userKey = array[1];
		
		RememberMe rememberMe = this.load(RememberMe.class, teamKey, userKey);
		
		return rememberMe;
	}
	
}
