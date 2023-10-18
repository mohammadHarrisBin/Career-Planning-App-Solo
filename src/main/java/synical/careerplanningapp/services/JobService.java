package synical.careerplanningapp.services;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import synical.careerplanningapp.lib.DBUtil;
import synical.careerplanningapp.lib.Function;

import java.time.LocalDateTime;
import java.util.Arrays;

import static synical.careerplanningapp.lib.Function.error;
import static synical.careerplanningapp.lib.Function.print;

public class JobService {
    private static final MongoCollection<Document> collection = DBUtil.getCollection("job_opportunity");

    public static boolean register(String title, String description, String companyName, String addressLine1, String addressLine2, double salaryMin, double salaryMax, String[] qualifications, String[] skills, String responsibilities,
                                   String education, String applicationDateline, int applicationProcessTime, boolean requiresTravel, boolean allowsRemoteWork, String languageRequirements, String physicalRequirements, String createdBy) {

        // get timestamp
        long timestamp = Function.getCurrentTimestamp();

        // check for any existing job
        Document query = new Document("title", title);
        Document document = DBUtil.getDocument(collection, query);

        // no same job title exist
        if (document == null) {
            Document jobDocument = new Document(
                    "title", title)
                    .append("description", description)
                    .append("companyName", companyName)
                    .append("location", new Document("addressLine1", addressLine1)
                            .append("addressLine2", addressLine2))
                    .append("salary", new Document("salaryMin", salaryMin)
                            .append("salaryMax", salaryMax))
                    .append("qualifications", Arrays.asList(qualifications))
                    .append("skills", Arrays.asList(skills))
                    .append("responsibilities", responsibilities)
                    .append("education", education)
                    .append("dateline", applicationDateline)
                    .append("processTime", applicationProcessTime)
                    .append("requiresTravel", requiresTravel)
                    .append("allowsRemoteWork", allowsRemoteWork)
                    .append("languageRequirements", languageRequirements)
                    .append("physicalRequirements", physicalRequirements)
                    .append("createdBy", createdBy)
                    .append("createdAt", timestamp);

            boolean result = DBUtil.insertOne(collection, jobDocument);

            // check if insert is successful
            if (result) {
                print("Successfully registered new job opportunity!");
                return true;
            } else {
                error("Failed to register new job opportunity.");
            }
        } else {
            error("Job title '" + title + "' already exist. Try another job title.");
        }

        return false;
    }

    public static void viewJobOpportunities() {
    }
}
