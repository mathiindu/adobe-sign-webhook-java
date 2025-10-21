
package com.example.webhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

@RestController
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader(value = "X-AdobeSign-ClientId", required = false) String clientId,
            HttpServletRequest request) throws IOException {

        logger.info("Received POST /webhook");
        logRequestDetails(request);

        if (clientId != null && !clientId.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-AdobeSign-ClientId", clientId);
            return ResponseEntity.ok().headers(headers).body("Webhook validated");
        } else {
            return ResponseEntity.badRequest().body("Missing Adobe Sign Client ID");
        }
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> handleGetWebhook() {
        return ResponseEntity.ok("GET /webhook received");
    }

    @GetMapping("/webhook/echosign.webhook.verify")
    public ResponseEntity<String> verifyWebhook(HttpServletRequest request) throws IOException {
        logger.info("Received GET /webhook/echosign.webhook.verify");
        logRequestDetails(request);
        return ResponseEntity.ok("Webhook verification successful");
    }

    private void logRequestDetails(HttpServletRequest request) throws IOException {
        logger.info("Method: {}", request.getMethod());
        logger.info("Request URI: {}", request.getRequestURI());

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            for (String headerName : Collections.list(headerNames)) {
                logger.info("Header: {} = {}", headerName, request.getHeader(headerName));
            }
        }

        StringBuilder body = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line).append("");
        }
        logger.info("Request Body:{}", body.toString());
    }
}
