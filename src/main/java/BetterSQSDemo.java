import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

/**
 * Created with IntelliJ IDEA. User: aglenn Date: 11/9/13 Time: 6:01 PM
 **/

public class BetterSQSDemo {

    public static void main(String[] args) {

        String accessKey = "put in access key from ~/.aws/config!";
        String secretKey = "add secret key from ~/.aws/config!";
        AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(accessKey, secretKey));

        Region usEast = Region.getRegion(Regions.US_EAST_1);
        sqs.setRegion(usEast);

        System.out.println("Better SQS Demo");

        try {
            // List queues
            System.out.println("Listing all better-queues:");
            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();

            // Send some messages to the best queue ever
            String myQueue = "https://sqs.us-east-1.amazonaws.com/907128454492/develop-sqs-seal-team-6-test";
            int num_messages_to_insert = 2;
            for (int i = 0; i < num_messages_to_insert; i++) {
                String msg = "Message " + RandomStringUtils.randomAlphanumeric(6);
                System.out.println("Sending " + msg + " to " + myQueue);
                sqs.sendMessage(new SendMessageRequest(myQueue, msg));
            }

            // Read all the messages, but only delete 3 of them.
            int numMessagesToDelete = 9;
            int messagesDeleted = 0;
            System.out.println("\nReading all messages from " + myQueue + ".  Processing at most " + numMessagesToDelete + " of them.");

            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueue).withMaxNumberOfMessages(numMessagesToDelete);
            while (true) {
                System.out.println("Polling for new messages");
                List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
                for (Message message : messages) {
                    if (messagesDeleted++ < numMessagesToDelete) {
                        System.out.println("    MessageId:     " + message.getMessageId());
                        System.out.println("    Body:          " + message.getBody());

                        // Delete a message              
                        System.out.println("Deleting this message.\n");
                        sqs.deleteMessage(new DeleteMessageRequest(myQueue, message.getReceiptHandle()));
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException. Weak.");
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, error " + ace.getMessage());
        }
    }
}
