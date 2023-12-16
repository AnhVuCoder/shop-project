package com.myshop.controller;

import com.myshop.service.UserService;
import com.myshop.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRestController {
    @Autowired
    private UserService userService;
    @PostMapping("users/check_email")
    public String checkEmailDuplicated(@RequestParam(name="id", defaultValue = "0") Integer id,
            @RequestParam(name = "email") String email){
        return userService.checkDuplicatedEmail(id,email) ?"OK":"Duplicated";
    }
}
