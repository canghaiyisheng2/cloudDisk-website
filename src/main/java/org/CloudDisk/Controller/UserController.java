package org.CloudDisk.Controller;

import org.CloudDisk.Service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Service
public class UserController {

    @Autowired
    UserService userService;

    @Resource
    RabbitTemplate rabbitTemplate;

    @PostMapping("/sendMsg")
    public String sendMsg(@RequestParam("user") String uid,
                          @RequestParam("date") String date,
                          @RequestParam("msg") String msg,
                          HttpSession session){
        return userService.OnetoOne(uid,date,msg,session);
    }

    @PostMapping("/share")
    public String share(@RequestParam("user") String userid,
                        @RequestParam("fileNo") String fileNo,
                        @RequestParam("fileName") String fileName,
                        @RequestParam("token") String token,
                        HttpSession session){

        return userService.Fileshare(userid,fileNo,fileName,token,session);
    }

}
