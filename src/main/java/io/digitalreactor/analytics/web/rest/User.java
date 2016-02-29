package io.digitalreactor.analytics.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin
public class User {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class UserDoesNotExist extends RuntimeException {
        public UserDoesNotExist(String userName) {
            super("User with name: " + userName + " does not exist.");
        }
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public String getUser(@PathVariable(value = "userName") String userName) {
        if(userName.equals("badMan")){
            throw new UserDoesNotExist(userName);
        }

        return "some information" + userName;
    }

}
