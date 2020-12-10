package net.stevencai.stevenweb.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/","/home"})
public class HomeController {
    @GetMapping
    public String home(){
        return "index";
    }
    @GetMapping("about")
    public String aboutMe(){
        return "about";
    }
    @GetMapping("contact")
    public String contactMe(){
        return "contact";
    }

}
