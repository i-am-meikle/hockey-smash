package no.meikle.hockey_smash.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Date;

@DynamoDBTable(tableName = "messages")
public class Message {

	@DynamoDBHashKey
	private String targetKey;
	@DynamoDBRangeKey
	@DynamoDBAutoGeneratedKey()
	private String messageKey;
	private Date startDate;
	private Date endDate;
	private String message;
	private String createdBy;
	private boolean acknowledged = false;

	public String getTargetKey() {
		return targetKey;
	}

	public void setTargetKey(String targetKey) {
		this.targetKey = targetKey;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public boolean isAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public void preWrite() {

		// If a generated field is set to am empty string by HTTP parameters,
		// then DynamoDB will not generate a key and barf instead.
		// So if the messageKey is a zero length String, reset it to null.

		if (this.messageKey != null && this.messageKey.length() == 0) {
			this.messageKey= null;
		}

	}

}
