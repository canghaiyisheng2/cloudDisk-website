package org.CloudDisk.Controller;

import org.CloudDisk.Service.LoginAndSignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
public class LoginAndSignupController {
    @Autowired
    LoginAndSignupService lsService;

    @PostMapping("/login")
    @ResponseBody
    public String login(HttpSession session, @RequestBody HashMap<String, String> map){
        String name = map.get("name");
        String passwd = map.get("password");
        return lsService.checkLogin(session, name, passwd);
    }

    @GetMapping("/checklogin")
    @ResponseBody
    public String checklogin(HttpSession session){
        return lsService.CheckLoginStatus(session);
    }

    @GetMapping("/ExitLogin")
    @ResponseBody
    public String exitLogin(HttpSession session){
        return lsService.exit(session);
    }

    @PostMapping("/signup")
    @ResponseBody
    public String signup(HttpSession session, @RequestBody HashMap<String,String> map){
        String name = map.get("name");
        String passwd = map.get("password");
        System.out.println(name + ' ' + passwd);
        return lsService.signupCheck(session, name, passwd);
    }
}
