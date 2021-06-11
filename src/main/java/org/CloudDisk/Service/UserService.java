package org.CloudDisk.Service;

import org.CloudDisk.Dao.UserDao;
import org.CloudDisk.Utils.responseObj;
import org.CloudDisk.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserDao userDao;
    @Value("${spring.http.multipart.location}")
    String uploadDir;


    public String getUserList(HttpSession session) {
        String requestid = (String) session.getAttribute("uid");
        if (StringUtils.isEmpty(requestid))
            return new responseObj("fail","未登录").toJson();
        List<User> users = userDao.findallUsers();
        List<Map<String,String>> usersMaps = new ArrayList<>();
        for(User user : users){
            Map<String, String> userMap = new HashMap<>();
            userMap.put("uuid", user.getUuid());
            userMap.put("usrName", user.getUsername());
            userMap.put("auth", user.getAuth());
            usersMaps.add(userMap);
        }
        return new responseObj("success", usersMaps).toJson();
    }



    @Transactional
    public String deleteUser(String uid, HttpSession session) {
        String curuid = (String) session.getAttribute("uid");
        if(StringUtils.isEmpty(curuid))
            return new responseObj("fail", "未登录").toJson();
        User user = userDao.findOneById(uid);
        Map<String, String> userMap = new HashMap<>();
        if(user.getAuth() == "user"){
            userDao.deleteByUid(uid);
            userMap.put("uuid", user.getUuid());
            userMap.put("usrName", user.getUsername());
            userMap.put("auth", user.getAuth());
            return new responseObj("success", userMap).toJson();
        }
        return new responseObj("fail","").toJson();
    }

    @Transactional
    public String riseUserAuth(String uid, HttpSession session) {
        String curuid = (String) session.getAttribute("uid");
        if(StringUtils.isEmpty(curuid))
            return new responseObj("fail", "未登录").toJson();
        userDao.riseAuth(uid);
        return new responseObj("success", "").toJson();
    }



    @Transactional
    public String modifyUserName(String newName ,HttpSession session){
        String userUid = (String) session.getAttribute("uid");
        if (StringUtils.isEmpty(userUid))
            return new responseObj("fail", "未登录").toJson();
        try {
            userDao.updateUserName(userUid, newName);
            session.setAttribute("userName", newName);
        }catch (Exception e){
            return new responseObj("fail", e.getMessage()).toJson();
        }

        return new responseObj("success","").toJson();
    }

    @Transactional
    public String modifyPassword(String newPassword, HttpSession session){
        String userUid = (String) session.getAttribute("uid");
        if (StringUtils.isEmpty(userUid))
            return new responseObj("fail", "未登录").toJson();
        try {
            userDao.updatePassword(userUid, newPassword);
        }catch (Exception e){
            return new responseObj("fail", "服务器内部错误").toJson();
        }

        return new responseObj("success","").toJson();
    }
}
