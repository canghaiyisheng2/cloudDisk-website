package org.CloudDisk.Service;

import org.CloudDisk.Dao.FriendDao;
import org.CloudDisk.Dao.UserDao;
import org.CloudDisk.Utils.QueueMsg;
import org.CloudDisk.Utils.responseObj;
import org.CloudDisk.pojo.ChatItem;
import org.CloudDisk.pojo.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Service
public class UserService {

    @Autowired
    FriendDao friendDao;
    @Autowired
    UserDao userDao;
    @Value("${spring.http.multipart.location}")
    String uploadDir;
    @Resource
    RabbitTemplate rabbitTemplate;


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

    public String MsgBoardcast(String msg, HttpSession session){
        String userUid = (String) session.getAttribute("uid");
        if(StringUtils.isEmpty(userUid))
            return new responseObj("fail", "未登录").toJson();
        QueueMsg queueMsg = new QueueMsg("notice",new Date().toString(),msg);
        rabbitTemplate.convertAndSend("inform.msg", "",queueMsg);
        return new responseObj("success","").toJson();
    }

    public String OnetoOne(String uid, String date, String msg, HttpSession session) {
        String curUid = (String) session.getAttribute("uid");
        String curName = (String) session.getAttribute("userName");
        if (StringUtils.isEmpty(curUid))
            return new responseObj("fail","未登录").toJson();
        if (friendDao.count(curUid, uid) == 0)
            return new responseObj("fail","他/她不是你的好友").toJson();

        File chatFile = new File(uploadDir + "chat/" + curUid + "_" + uid + ".chat");
        if (!chatFile.exists())
            chatFile = new File(uploadDir + "chat/" + uid + "_" + curUid + ".chat");
        try(
                FileOutputStream fos = new FileOutputStream(chatFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                ){
            ChatItem chatItem = new ChatItem( "msg", curName, date, msg);
            oos.writeObject(chatItem);
            Map<String, String> map = new HashMap<>();
            map.put( "src", curName);
            map.put( "msg", msg);
            QueueMsg queueMsg = new QueueMsg( "msg", date, map);
            rabbitTemplate.convertAndSend("inform.msg", uid, queueMsg);
        } catch (IOException e) {
            e.printStackTrace();
            return new responseObj("fail","服务器内部错误").toJson();
        }
        return new responseObj("success","").toJson();
    }

    public String Fileshare(String userid, String fileNo, String fileName, String token, HttpSession session) {

        String curUid = (String) session.getAttribute("uid");
        String CurName = (String) session.getAttribute("userName");
        if (StringUtils.isEmpty(curUid))
            return new responseObj("fail","未登录").toJson();
        if (friendDao.count(curUid, userid) == 0)
            return new responseObj("fail","他/她不是你的好友").toJson();

        Map<String, String> map = new HashMap<>();
        map.put("src", CurName);
        map.put("fileNo", fileNo);
        map.put("fileName", fileName);
        map.put("token", token);
        QueueMsg queueMsg = new QueueMsg("msg", new Date().toString(), map);
        rabbitTemplate.convertAndSend("inform.msg", userid, queueMsg);
        return new responseObj("success", "").toJson();

    }

}
