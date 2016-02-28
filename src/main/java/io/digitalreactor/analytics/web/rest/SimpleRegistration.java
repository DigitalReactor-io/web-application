package io.digitalreactor.analytics.web.rest;

import io.digitalreactor.analytics.web.dao.RegistrationSessionDao;
import io.digitalreactor.analytics.web.dao.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * CORS support in Spring Framework https://spring.io/blog/2015/06/08/cors-support-in-spring-framework
 */
@RestController
@RequestMapping("/registration")
@CrossOrigin
public class SimpleRegistration {

    private final String REGISTRATION_SESSION = "registration_session";
    private UserDao userDao;
    private RegistrationSessionDao registrationSessionDao;

    public SimpleRegistration(UserDao userDao, RegistrationSessionDao registrationSessionDao) {
        this.userDao = userDao;
        this.registrationSessionDao = registrationSessionDao;
    }

    @RequestMapping(
            value = "/credentials",
            method = RequestMethod.POST
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void getCredentials(
            @RequestHeader(value = "name") String userName,
            @RequestHeader(value = "password") String password,
            HttpServletResponse response

    ) {
        String session = registrationSessionDao.makeSession(userName);
        response.addCookie(new Cookie(REGISTRATION_SESSION, session));
        userDao.add(userName, password);
    }

    @RequestMapping(
            value = "/token",
            method = RequestMethod.GET
    )
    public void getTokenByGrandCode(@RequestParam(value = "code") String code) {

    }

}
