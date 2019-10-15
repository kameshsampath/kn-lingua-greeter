package com.redhat.developers.demo;

import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LinguaGreeterController {

    Logger logger = Logger.getLogger(LinguaGreeterController.class);

    @PostMapping
    public String translateGreeting(@RequestBody String payload) {
        logger.debug("Received body:"+payload);
        return payload;
    }
}