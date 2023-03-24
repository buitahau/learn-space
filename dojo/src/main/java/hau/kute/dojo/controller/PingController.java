package hau.kute.dojo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(("/api/v1"))
public class PingController {

    private static final Logger log = LoggerFactory.getLogger(PingController.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/ping")
    public String ping() {
        log.info("ping");
        return restTemplate.getForObject("http://localhost:8080/dojo/api/v1/pong", String.class);
    }

    @GetMapping("/pong")
    public String pong() {
        log.info("pong");
        return "pong";
    }
}
