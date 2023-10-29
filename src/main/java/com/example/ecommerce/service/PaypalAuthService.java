package com.example.ecommerce.service;

import com.example.ecommerce.utils.Define;
import com.example.ecommerce.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


@Service
public class PaypalAuthService {
    private final String paypalUrlToken = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
    private final String paypalUrlCheckout = "https://api-m.sandbox.paypal.com/v2/checkout/orders";
    private final String payoutUrlPayout = "https://api-m.sandbox.paypal.com/v1/payments/payouts";

    public String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        // Create the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create the request body
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(paypalUrlToken)
                .queryParam("grant_type", "client_credentials");

        // Set up basic authentication
        headers.setBasicAuth(Define.CLIENT_ID, Define.PRIVATE_KEY);

        // Create the HTTP entity with headers and body
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Send the POST request to PayPal
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class
        );

        // Extract the access token from the response
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String response = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, String> map = objectMapper.readValue(response, Map.class);
                return map.get("access_token");
            } catch (JsonProcessingException e) {
                return e.getMessage();
            }
        } else {
            // Handle error here
            return "Error occurred";
        }
    }

    public Boolean transferMoneyToSeller1(String money ,String mailUsername, String token){
        String requestBody = "{\"intent\":\"CAPTURE\",\"purchase_units\":[{\"amount\":{\"value\":\""+money+"\",\"currency_code\":\"USD\"},\"payee\":{\"email_address\":\""+mailUsername+"\"}}]}";

        // Tạo tiêu đề (headers) cho yêu cầu
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        // Tạo yêu cầu POST
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Gửi yêu cầu và nhận phản hồi
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(paypalUrlCheckout, HttpMethod.POST, requestEntity, String.class);

        // Trích xuất dữ liệu phản hồi từ ResponseEntity
        String responseBody = responseEntity.getBody();
        if (responseEntity.getStatusCode() == HttpStatus.OK){
            return true;
        }
        return false;
    }

    public Boolean transferMoneyToSeller(String money ,String mailUsername, String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        String requestBody = "{\n" +
                "  \"sender_batch_header\": {\n" +
                "    \"sender_batch_id\": \"Payouts"+ Utils.getCurrentDateAndMinute()+"\",\n" +
                "    \"email_subject\": \"You have a payout!\",\n" +
                "    \"email_message\": \"You have received a payout! Thanks for using our service!\"\n" +
                "  },\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"recipient_type\": \"EMAIL\",\n" +
                "      \"amount\": {\n" +
                "        \"value\": \""+money+"\",\n" +
                "        \"currency\": \"USD\"\n" +
                "      },\n" +
                "      \"note\": \"Thanks for your patronage!\",\n" +
                "      \"sender_item_id\": \"201403140001\",\n" +
                "      \"receiver\": \""+mailUsername+"\",\n" +
                "      \"recipient_wallet\": \"PAYPAL\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(payoutUrlPayout, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK){
            return true;
        } else {
            return false;
        }
    }
}
