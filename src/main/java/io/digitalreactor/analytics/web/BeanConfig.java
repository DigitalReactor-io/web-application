package io.digitalreactor.analytics.web;

import io.digitalreactor.analytics.web.dao.RegistrationSessionDao;
import io.digitalreactor.analytics.web.dao.UserDao;
import io.digitalreactor.analytics.web.rest.SimpleRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class BeanConfig {
    @Bean
    public RegistrationSessionDao registrationSessionDao() {
        return new RegistrationSessionDao();
    }

    @Bean
    public UserDao userDao() {
        return new UserDao();
    }

    @Bean
    @Autowired
    public SimpleRegistration simpleRegistration(UserDao userDao, RegistrationSessionDao registrationSessionDao) {
        Logger logger = LoggerFactory.getLogger("registration.newUser");
        return new SimpleRegistration(userDao, registrationSessionDao, logger);
    }
}
