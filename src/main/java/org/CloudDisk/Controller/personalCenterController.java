package org.CloudDisk.Controller;

import org.CloudDisk.Service.FileService;
import org.CloudDisk.Service.UserService;
import org.CloudDisk.Utils.responseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class personalCenterController {

    @Autowired
    UserService userService;

    @PostMapping("/upavatar")
    @ResponseBody
    public String UpAvatar(@RequestParam("file") MultipartFile multipartFile,
                           HttpSession session){
        return userService.UploadAvatar(multipartFile, session);
    }

    @PostMapping("/modify")
    @ResponseBody
    public String modify(@RequestParam("key") String key,
                         @RequestParam("value") String value,
                         HttpSession session){
        if (key.equals("userName"))
            return userService.modifyUserName(value, session);
        return new responseObj("fail", "错误的参数").toJson();
    }
}
