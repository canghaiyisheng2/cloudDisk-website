package org.CloudDisk.Service;

import org.CloudDisk.Dao.UserDao;
import org.CloudDisk.Utils.responseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    UserDao userDao;
    @Value("${spring.http.multipart.location}")
    String uploadDir;

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
