package com.cdsxt.web.cntroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("shop")
public class LoginController {

    @RequestMapping(value = {"", "login"}, method = RequestMethod.GET)
    public String login() {
        return "front/login";
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "front/index";
    }
}
