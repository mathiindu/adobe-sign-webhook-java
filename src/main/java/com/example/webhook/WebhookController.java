package com.example.webhook;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader(value = "X-AdobeSign-ClientId", required = false) String clientId) {

        if (clientId != null && !clientId.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-AdobeSign-ClientId", clientId);
            return ResponseEntity.ok().headers(headers).body("Webhook validated");
        } else {
            return ResponseEntity.badRequest().body("Missing Adobe Sign Client ID");
        }
    }

    @GetMapping("/webhook/echosign.webhook.verify")
    public ResponseEntity<String> verifyWebhook() {
        return ResponseEntity.ok("Webhook verification successful");
    }

}
