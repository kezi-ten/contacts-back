package com.contacts;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
public class TempleController {

    @RequestMapping("/")
    public String home() {
        return "Home Page";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello from server!";
    }
}
