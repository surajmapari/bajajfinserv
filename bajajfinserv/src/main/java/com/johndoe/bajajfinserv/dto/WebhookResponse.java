package com.johndoe.bajajfinserv.dto;

// import lombok.Data;
// @Data
public class WebhookResponse {
    private String webhook;
        private String accessToken;
    
        // Option 2: If NOT using Lombok, add getters and setters:
        public String getWebhook() {
            return webhook;
        }
    
        public void setWebhook(String webhook) {
            this.webhook = webhook;
        }
    
        public String getAccessToken() {
            return accessToken;
        }
    
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
        
}
