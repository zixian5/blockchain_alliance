package com.blockchain.alliance.controller;

import com.blockchain.alliance.entity.User;
import com.blockchain.alliance.repository.UserRepository;
import com.blockchain.alliance.response.ResponseCode;
import com.blockchain.alliance.service.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String login(HttpServletRequest request, @RequestBody(required = false)String json)
    {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        System.out.println(jsonObject);
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        System.out.println("----------------------------"+username+"----------------------");
        User u = userRepository.findByUsername(username);

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("data",null);

        if(u == null)
        {
            map.put("code",404);

        }
        else if(!u.getPassword().equals(password))
        {
            map.put("code",403);
        }
        else{
            request.getSession().putValue("id",u.getId());
            map.put("code",200);
        }
     //   map.put("data",username);
     //   System.out.println("----------------------------"+username+"----------------------");
        return new Gson().toJson(map);
    }
    @RequestMapping("/getVerificationCode")
    @ResponseBody
    public String getVerificationCode(String email,HttpServletRequest request)
    {
        Map<String,Object> map = new LinkedHashMap<>();
        try {
            String code  = userService.sendVerificationCode(email);
            request.getSession().putValue("code",code);
            map.put("code",ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", ResponseCode.ERROE.getCode());
        }
        return new Gson().toJson(map);
    }

    @PostMapping(value = "/signUp", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String signUp(HttpServletRequest request,@RequestBody(required = false) String json )
    {
        System.out.println("-----------------"+json+"--------------");
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        String name = jsonObject.get("name").getAsString();
        Integer age = jsonObject.get("age").getAsInt();
        String address = jsonObject.get("address").getAsString();
        String email = jsonObject.get("email").getAsString();
        String verificationCode = jsonObject.get("verificationCode").getAsString();

        Map<String,Object> map = new HashMap<>();
        String code = (String) request.getSession().getAttribute("code");
        if(code == null || !code.equals(verificationCode))
        {
            map.put("code",403);
            return new Gson().toJson(map);
        }
        if(userRepository.findBYEmail(email) != null)
        {
            map.put("code",409);
            return  new Gson().toJson(map);
        }
        User user = new User();
        user.setAddress(address);
        user.setAge(age);
        user.setName(name);
        user.setPassword(password);
        user.setType(0);
        user.setUsername(username);
        user.setEmail(email);
        userRepository.saveAndFlush(user);
        map.put("code",ResponseCode.SUCCESS.getCode());
        return new Gson().toJson(map);
    }
}
