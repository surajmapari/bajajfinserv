package com.johndoe.bajajfinserv;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.johndoe.bajajfinserv.dto.SolutionRequest;
import com.johndoe.bajajfinserv.dto.WebhookRequest;
import com.johndoe.bajajfinserv.dto.WebhookResponse;

@SpringBootApplication
public class BajajfinservApplication {

	private static final String YOUR_NAME = "Suraj Mapari"; // e.g., "Jane Doe"
    private static final String YOUR_REG_NO = "1262240448"; // e.g., "REG12347" or "REG12348"
    private static final String YOUR_EMAIL = "suraj.mapari@mitwpu.edu.in";


    public static void main(String[] args) {
        SpringApplication.run(BajajfinservApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            System.out.println("Application starting...");

            // --- Step 1: Generate Webhook ---
            String generateWebhookUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            WebhookRequest webhookRequestPayload = new WebhookRequest("SURAJ MAPARI", "1262240448", "suraj.mapari@mitwpu.edu.in");

            HttpHeaders headersStep1 = new HttpHeaders();
            headersStep1.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<WebhookRequest> requestEntityStep1 = new HttpEntity<>(webhookRequestPayload, headersStep1);

            WebhookResponse webhookResponse = null;
            try {
                System.out.println("Sending request to generate webhook...");
                ResponseEntity<WebhookResponse> responseStep1 = restTemplate.exchange(
                        generateWebhookUrl,
                        HttpMethod.POST,
                        requestEntityStep1,
                        WebhookResponse.class
                );
                webhookResponse = responseStep1.getBody();
                if (webhookResponse != null && webhookResponse.getWebhook() != null && webhookResponse.getAccessToken() != null) {
                    System.out.println("Received Webhook URL: " + webhookResponse.getWebhook());
                    System.out.println("Received Access Token: " + webhookResponse.getAccessToken());
                } else {
                    System.err.println("Failed to get webhook URL or access token. Response: " + (responseStep1.getBody() != null ? responseStep1.getBody().toString() : "null body"));
                    System.err.println("Raw response: " + responseStep1.toString());
                    return; // Exit if we don't get what we need
                }
            } catch (Exception e) {
                System.err.println("Error during webhook generation: " + e.getMessage());
                e.printStackTrace();
                return;
            }


            String yourSolvedSqlQuery = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, (SELECT COUNT(e2.EMP_ID) FROM EMPLOYEE e2 WHERE e2.DEPARTMENT = e1.DEPARTMENT AND e2.DOB > e1.DOB) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID ORDER BY e1.EMP_ID DESC;";

            if (webhookResponse == null || webhookResponse.getWebhook() == null || webhookResponse.getAccessToken() == null) {
                System.err.println("Cannot submit solution, webhook details are missing.");
                return;
            }

            String submissionUrl = webhookResponse.getWebhook();
            String accessToken = webhookResponse.getAccessToken();

            SolutionRequest solutionPayload = new SolutionRequest(yourSolvedSqlQuery);

            HttpHeaders headersStep2 = new HttpHeaders();
            headersStep2.setContentType(MediaType.APPLICATION_JSON);
            headersStep2.set("Authorization", accessToken); 
                            
            HttpEntity<SolutionRequest> requestEntityStep2 = new HttpEntity<>(solutionPayload, headersStep2);

            try {
                System.out.println("Submitting SQL solution to: " + submissionUrl);
                ResponseEntity<String> responseStep2 = restTemplate.exchange(
                        submissionUrl,
                        HttpMethod.POST,
                        requestEntityStep2,
                        String.class 
                );
                System.out.println("Submission response status: " + responseStep2.getStatusCode());
                System.out.println("Submission response body: " + responseStep2.getBody());
            } catch (Exception e) {
                System.err.println("Error during solution submission: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("Application finished.");
        };
    }

}
