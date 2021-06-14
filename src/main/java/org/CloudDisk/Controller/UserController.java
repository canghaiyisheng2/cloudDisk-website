package org.CloudDisk.Controller;

import org.CloudDisk.Service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/sendMsg")
    public String sendMsg(@RequestParam("user") String uid,
                          @RequestParam("msg") String msg,
                          HttpSession session){
        return userService.OnetoOne(uid,msg,session);
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
