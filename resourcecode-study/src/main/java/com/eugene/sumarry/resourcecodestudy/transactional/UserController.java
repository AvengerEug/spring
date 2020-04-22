package com.eugene.sumarry.resourcecodestudy.transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {


    @Autowired
    UserService userService;

}
