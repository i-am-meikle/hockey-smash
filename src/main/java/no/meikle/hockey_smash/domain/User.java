package no.meikle.hockey_smash.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Optional;
import org.apache.commons.codec.digest.DigestUtils;

@DynamoDBTable(tableName = "users")
public class User {

	public static final String ADMIN_STRING = "SUPERMAN";

	static public String HashPassword(String aPassword) {

		return aPassword == null ? null : DigestUtils.sha256Hex(aPassword);
	}

	@DynamoDBRangeKey
	@DynamoDBAutoGeneratedKey
	private String key;
	private String fullname;
	private String nickname;
	private String passwordHash;
	private String email;
	@DynamoDBHashKey
	private String team;
	private boolean isAdmin;
	private boolean isTrainer;

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String aPassword) {

		this.passwordHash = aPassword;
	}

	public void setPassword(String aPassword) {

		this.passwordHash = HashPassword(aPassword);
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String aName) {

		Optional.ofNullable(aName).map(name -> this.fullname = name.trim());
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getNickname() {

		if (nickname == null || nickname.isEmpty()) {
			return this.getFullname();
		}

		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean isTrainer() {
		return isTrainer;
	}

	public void setTrainer(boolean isTrainer) {
		this.isTrainer = isTrainer;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

}
