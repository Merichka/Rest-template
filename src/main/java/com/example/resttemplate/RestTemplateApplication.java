package com.example.resttemplate;

import com.example.resttemplate.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestTemplateApplication {

    private final static String URL = "http://94.198.50.185:7081/api/users";
    private final static String URL1 = "http://94.198.50.185:7081/api/users/3";
    private static final User USER = new User(3L, "James", "Brown", (byte) 38);
    private static final StringBuilder SECRET_CODE = new StringBuilder();

    public static void main(String[] args) {

        try {
            // получение Set-Cookie
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);
            String setCookies = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
            String jsessionid = setCookies.split(";")[0];

            // 1 часть кода
            HttpHeaders headers = new HttpHeaders();
            ObjectMapper mapper = new ObjectMapper();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add(HttpHeaders.COOKIE, jsessionid);
            HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(USER), headers);
            response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
            SECRET_CODE.append(response.getBody());

            // 2 часть кода
            USER.setName("Thomas");
            USER.setLastName("Shelby");
            entity = new HttpEntity<>(mapper.writeValueAsString(USER), headers);
            response = restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);
            SECRET_CODE.append(response.getBody());

            // 3 часть кода
            entity = new HttpEntity<>(headers);
            response = restTemplate.exchange(URL1, HttpMethod.DELETE, entity, String.class);
            SECRET_CODE.append(response.getBody());

            System.out.println("Code: " + SECRET_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
