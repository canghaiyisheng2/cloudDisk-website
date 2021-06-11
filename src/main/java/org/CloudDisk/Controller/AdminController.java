package org.CloudDisk.Controller;

import org.CloudDisk.Dao.UserDao;
import org.CloudDisk.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/getUserList")
    public String getUserList(HttpSession session) {return userService.getUserList(session);}

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("user") String uid,
                             HttpSession session){
        return userService.deleteUser(uid, session);
    }
    @PostMapping("auth")
    public String riseUserAuth(@RequestParam("user") String uid,
                               HttpSession session){
        return userService.riseUserAuth(uid, session);
    }


}
