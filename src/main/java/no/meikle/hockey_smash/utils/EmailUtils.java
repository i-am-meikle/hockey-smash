package no.meikle.hockey_smash.utils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import java.util.List;
import no.meikle.hockey_smash.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtils {

	static final String REGISTRATION = "hockey-smash-registration@meikle.dev";
	static final String PASSWORD_RESET = "hockey-smash-password@meikle.dev";
	static final String ADMIN = "hockey-smash-admin@meikle.dev";
	
	private Logger log = LoggerFactory.getLogger(EmailUtils.class);

	private Logger getLogger() {
		return log;
	}
	
	public void sendWelcomeEmail(User user, List<String> trainerEmails) {

		// Construct an object to contain the recipient address.
		Destination destination = new Destination().withToAddresses(new String[] { user.getEmail() })
				.withBccAddresses(ADMIN).withCcAddresses(trainerEmails);

		// Create the subject and body of the message.
		Content subject = new Content().withData("Welcome to Hockey Smash !");
		Content textBody = new Content().withData(
				"Welcome " + user.getFullname() + " to Hockey Smash !\n\nClick the link below to start playing.\n\n"
						+ "https://hockey-smash.meikle.dev/ \n\nEnjoy the game !\n" + "The Hockey Smash Team !");
		Body body = new Body().withText(textBody);

		// Create a message with the specified subject and body.
		Message message = new Message().withSubject(subject).withBody(body);

		// Assemble the email.
		SendEmailRequest request = new SendEmailRequest().withSource(REGISTRATION).withDestination(destination)
				.withMessage(message);

		this.sendEmail(request);
	}

	protected void sendEmail(SendEmailRequest request) {
		
		try {
			this.getLogger().info("Attempting to send an email through Amazon SES.");

			AmazonSimpleEmailService client;
			String region = System.getenv("HOCKEY_SMASH_EMAIL_REGION");
			if (region == null || region.isEmpty()) {
				this.getLogger().info("No Email Region set, using AWS_REGION: " + Regions.getCurrentRegion());
				client = AmazonSimpleEmailServiceClientBuilder.defaultClient();
			} else {
				this.getLogger().info("Email Region set to: " + region);
				AmazonSimpleEmailServiceClientBuilder clientBuilder = AmazonSimpleEmailServiceClientBuilder.standard();
				clientBuilder.setRegion(region);
				client = clientBuilder.build();
			}

			// Send the email.
			client.sendEmail(request);
			this.getLogger().info("Email sent!");
		} catch (Exception ex) {
			this.getLogger().info("The email was not sent.");
			this.getLogger().debug("Error message: " + ex.getMessage());
		}
	}

	public void sendPasswordResetEmail(User user) {

		// Construct an object to contain the recipient address.
		Destination destination = new Destination().withToAddresses(new String[] { user.getEmail() })
				.withBccAddresses(ADMIN);

		// Create the subject and body of the message.
		Content subject = new Content().withData("Hockey Smash - password reset request!");
		Content textBody = new Content().withData(
				"Hi " + user.getFullname() + ", \n\nSomeone has requested to reset your Hockey Smash password.\n\n"
						+ "Your username is: " + user.getFullname()
						+ "\n\nPlease click the link below to reset your password.\n\n"
						+ "https://hockey-smash.meikle.dev/login!changePasswordRequest.jutul?key=" + user.getKey()
						+ "&team=" + user.getTeam() + "\n\n"
						+ "If you did not request a password reset, please ignore and delete this email.\n\n"
						+ "Enjoy the game !\n" + "The Hockey Smash Team !");
		Body body = new Body().withText(textBody);

		// Create a message with the specified subject and body.
		Message message = new Message().withSubject(subject).withBody(body);

		// Assemble the email.
		SendEmailRequest request = new SendEmailRequest().withSource(PASSWORD_RESET).withDestination(destination)
				.withMessage(message);

		this.sendEmail(request);

	}
}
