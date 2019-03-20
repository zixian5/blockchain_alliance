package com.blockchain.alliance.controller;

import com.blockchain.alliance.entity.User;
import com.blockchain.alliance.repository.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public String login(HttpServletRequest request, @RequestParam(required = true) String username, @RequestParam(required = true) String password)
    {
        User u = userRepository.findByUsername(username);

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("data",null);

        if(u == null)
        {
            map.put("code",404);
        }
        else if(!u.getPassword().equals(password))
        {
            map.put("code","403");
        }
        else{
            request.getSession().putValue("id",u.getId());
            map.put("code","200");
        }
        return new Gson().toJson(map);
    }
}
