package org.CloudDisk.Controller;

import org.CloudDisk.Service.FileService;
import org.CloudDisk.Service.UserService;
import org.CloudDisk.Utils.responseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class personalCenterController {

    @Autowired
    UserService userService;
    @Autowired
    FileService fileService;


    @PostMapping("/modify")
    @ResponseBody
    public String modify(@RequestParam("type") String type,
                         @RequestParam("value") String value,
                         HttpSession session){
        if (type.equals("username"))
            return userService.modifyUserName(value, session);
        else if (type.equals("password"))
            return userService.modifyPassword(value, session);
        return new responseObj("fail", "错误的参数").toJson();
    }
}
