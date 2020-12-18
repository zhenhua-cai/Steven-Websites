package net.stevencai.stevenweb.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("contact")
public class ContactController {

    @GetMapping
    public String contactMe(){
        return "contact";
    }

    @PostMapping("sendEmail")
    @ResponseStatus(HttpStatus.OK)
    public void sendEmail(){

    }
}
