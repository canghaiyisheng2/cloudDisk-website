package org.CloudDisk.Service;

import org.CloudDisk.Dao.FriendDao;
import org.CloudDisk.Dao.FriendRequestDao;
import org.CloudDisk.Dao.UserDao;
import org.CloudDisk.Dao.upFileDao;
import org.CloudDisk.Utils.responseObj;
import org.CloudDisk.pojo.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Service
public class FriendService {

    @Autowired
    UserDao userDao;
    @Autowired
    FriendDao friendDao;
    @Autowired
    FriendRequestDao friendRequestDao;
    @Value("${spring.http.multipart.location}")
    String uploadDir;
    @Resource
    RabbitTemplate rabbitTemplate;

    public String getFriendList(HttpSession session){
        String requestUid = (String) session.getAttribute("uid");
        if (StringUtils.isEmpty(requestUid))
            return new responseObj("fail","未登录").toJson();

        List<User> friends = userDao.findFriendsByUid(requestUid);
        //Correct the format
        List<Map<String,String>> friendsMaps = new ArrayList<>();
        for (User user : friends){
            Map<String,String> userMap = new HashMap<>();
            userMap.put("uuid", user.getUuid());
            userMap.put("usrName", user.getUsrName());
            friendsMaps.add(userMap);
        }
        return new responseObj("success",friendsMaps).toJson();
    }

    @Transactional
    public String sendFriRequest(String userName, HttpSession session){
        String curUid = (String) session.getAttribute("uid");
        if (StringUtils.isEmpty(curUid))
            return new responseObj("fail","未登录").toJson();
        String friendUid = userDao.findOneByName(userName).getUuid();
        if (StringUtils.isEmpty(friendUid))
            return new responseObj("fail","该用户不存在").toJson();
        if (friendDao.count(curUid,friendUid) != 0)
            return new responseObj("fail","该用户已经是你的好友").toJson();

        FriendRequestMap friendRequestMap = new FriendRequestMap();
        friendRequestMap.setMainUid(curUid);
        friendRequestMap.setFriendUid(friendUid);
        friendRequestDao.save(friendRequestMap);

        QueueMsg queueMsg = new QueueMsg("req", new Date().toString(), curUid);
        rabbitTemplate.convertAndSend("inform.msg", friendUid, queueMsg);
        return new responseObj("success","").toJson();
    }

    @Transactional
    public String confirmFriRequest(boolean accept, String friendUid, HttpSession session){
        String curUid = (String) session.getAttribute("uid");
        if (StringUtils.isEmpty(curUid))
            return new responseObj("fail","未登录").toJson();
        if (!userDao.exists(friendUid))
            return new responseObj("fail","该用户不存在").toJson();
        if (friendRequestDao.count(friendUid,curUid) == 0)
            return new responseObj("fail","对方未向你发起过好友申请").toJson();

        friendRequestDao.delete(friendUid, curUid);

        if (accept){
            //正式加为好友
            File chatDir = new File(uploadDir + "chat/" + friendUid + "_" + curUid + ".chat");
            if (!chatDir.exists()) {
                try {
                    chatDir.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return new responseObj("fail","服务器内部错误").toJson();
                }
            }
            FriendMap friendMap = new FriendMap();
            friendMap.setMainUid(friendUid);
            friendMap.setFriendUid(curUid);
            friendDao.save(friendMap);
            FriendMap friendMap1 = friendMap.reverse();
            friendDao.save(friendMap1);
            //向好友发送通知：他的申请已通过
            rabbitTemplate.convertAndSend("inform.msg", friendUid, new QueueMsg("notice", new Date().toString(),
                    "你向" + session.getAttribute("userName") + "发送的好友申请已通过"));
        }

        return new responseObj("success","").toJson();
    }

    @Transactional
    public String deleteFriend(String uid, HttpSession session){
        String curUid = (String) session.getAttribute("uid");
        if (StringUtils.isEmpty(curUid))
            return new responseObj("fail","未登录").toJson();
        friendDao.deleteByUid(curUid,uid);
        friendDao.deleteByUid(uid,curUid);

        //发送已被删除的通知给好友
        rabbitTemplate.convertAndSend("inform.msg", uid, new QueueMsg("notice", new Date().toString(),
                "你的好友" + session.getAttribute("userName") + "已将你从好友列表删除"));
        //删除chat文件
        File chatFile = new File(uploadDir + "chat/" + curUid + "_" + uid + ".chat");
        if (!chatFile.exists())
            chatFile = new File(uploadDir + "chat/" + uid + "_" + curUid + ".chat");
        chatFile.delete();
        return new responseObj("success","").toJson();
    }

    public String getChatList(String uid, HttpSession session){
        String curUid = (String) session.getAttribute("uid");
        if (StringUtils.isEmpty(curUid))
            return new responseObj("fail","未登录").toJson();
        if (friendDao.count(curUid, uid) == 0)
            return new responseObj("fail","他/她不是你的好友").toJson();

        File chatFile = new File(uploadDir + "chat/" + curUid + "_" + uid + ".chat");
        if (!chatFile.exists())
            chatFile = new File(uploadDir + "chat/" + uid + "_" + curUid + ".chat");
        try(
                FileInputStream fis = new FileInputStream(chatFile);
                ObjectInputStream ois = new ObjectInputStream(fis);

            ){
//            ChatItem[] chat = new ChatItem[2];
//            chat[0] = new ChatItem("msg","admin","May 31, 2021 8:23:23 PM","消息");
//            chat[1] = new ChatItem("file","admin","May 31, 2021 8:23:23 PM",upFileDao.findOne(1));
//            oos.writeObject(chat);
            ChatItem[] chatItems = (ChatItem[]) ois.readObject();
            return new responseObj("success",chatItems).toJson();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new responseObj("fail","服务器内部错误").toJson();
        }

    }
}
