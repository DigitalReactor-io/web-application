package io.digitalreactor.analytics.web.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.digitalreactor.analytics.web.dao.RegistrationSessionDao;
import io.digitalreactor.analytics.web.dao.UserDao;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * CORS support in Spring Framework https://spring.io/blog/2015/06/08/cors-support-in-spring-framework
 */
@RestController
@RequestMapping("/registration")
@CrossOrigin
public class SimpleRegistration {

    private final String REGISTRATION_SESSION = "registration_session";
    private Logger logger;
    private UserDao userDao;
    private RegistrationSessionDao registrationSessionDao;

    public SimpleRegistration(UserDao userDao, RegistrationSessionDao registrationSessionDao, Logger logger) {
        this.userDao = userDao;
        this.registrationSessionDao = registrationSessionDao;
        this.logger = logger;
    }

    @RequestMapping(
            value = "/credentials",
            method = RequestMethod.POST
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void getCredentials(
            @RequestHeader(value = "email") String email,
            @RequestHeader(value = "password") String password,
            HttpServletResponse response

    ) {
        String session = registrationSessionDao.makeSession(email);
        Cookie cookie = new Cookie(REGISTRATION_SESSION, session);
        cookie.setMaxAge(1000);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        userDao.add(email, password);
        logger.info("New user was created with name {}", email);
    }

    @RequestMapping(
            value = "/counters",
            method = RequestMethod.GET
    )
    public HashMap<String, String> counters(@CookieValue(REGISTRATION_SESSION) String registrationSession) {

        String userName = registrationSessionDao.getUserName(registrationSession);
        String token = userDao.getTokens(userName).get(0);
        RestTemplate restTemplate = new RestTemplate();
        String restTemplateForObject = restTemplate.getForObject(
                "https://api-metrika.yandex.ru/counters.json?oauth_token=" + token,
                String.class
        );

        logger.info(restTemplateForObject);

        return null;
    }

    /**
     * https://tech.yandex.ru/direct/doc/dg/examples/auth-token-sample-docpage/
     */
    @RequestMapping(
            value = "/token",
            method = RequestMethod.POST
    )
    public void getTokenByGrandCode(
            @RequestHeader(value = "code") String code,
            @CookieValue(REGISTRATION_SESSION) String registrationSession
    ) {
        String email = registrationSessionDao.getUserName(registrationSession);

        String applicationAuth = "Basic YjI2ZTMyNGQ1YTEzNDE2OGIwOTBiM2YyM2U3N2EwZTc6Yjk3MGJjMWIzOGI3NDE5YWEyN2Y4YjhjM2Q1ZDEzZTA=";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", applicationAuth);
        String json = "grant_type=authorization_code&code="+code+"&client_id=b26e324d5a134168b090b3f23e77a0e7&client_secret=b970bc1b38b7419aa27f8b8c3d5d13e0";
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://oauth.yandex.ru/token", HttpMethod.POST, requestEntity, String.class);

        String body = responseEntity.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            String token = jsonNode.get("access_token").asText();
            userDao.addToken(email, token);
            logger.info("Token was got for user {}", email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
