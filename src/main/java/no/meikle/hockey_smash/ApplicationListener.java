package no.meikle.hockey_smash;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.AwsRegionProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application Lifecycle Listener implementation class ApplicationListener
 */
public class ApplicationListener implements ServletContextListener {

    private AmazonDynamoDB dynamoDB;
    private Logger log = LoggerFactory.getLogger(ApplicationListener.class);

    /**
     * Default constructor.
     */
    public ApplicationListener() {
        // Nothing
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {
        dynamoDB.shutdown();
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {

        boolean localDb = Boolean.parseBoolean(System.getenv("HOCKEY_SMASH_LOCAL_DB"));

        if (localDb) {
            log.info("HOCKEY_SMASH_LOCAL_DB set. Using Local DB.");
            dynamoDB = this.getLocalDynamoDB();
        } else {
            log.info("Setting DB via AWS_REGION: " + Regions.getCurrentRegion());
            dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        }

        log.info("Setting dynamoDB: {}", dynamoDB);
        event.getServletContext().setAttribute("dynamoDB", dynamoDB);
    }

    private AmazonDynamoDB getLocalDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://dynamodb:8000", "local"))
                .build();
    }

}
