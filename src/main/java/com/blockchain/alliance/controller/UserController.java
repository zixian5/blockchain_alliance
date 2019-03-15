package com.blockchain.alliance.controller;

import com.blockchain.alliance.entity.User;
import com.blockchain.alliance.repository.UserRepository;
import com.blockchain.alliance.response.LoginResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam(required = true) String telephone , @RequestParam(required = true) String password)
    {
        User u = userRepository.findByTelephone(telephone);

        LoginResponse response = new LoginResponse();
        if(u == null)
        {
            response.setResult(false);
        }else{
           // LoginResponse response = new LoginResponse();
            response.setResult(true);
            response.setType(u.getType());
        }

        return new Gson().toJson(response);
    }
}
