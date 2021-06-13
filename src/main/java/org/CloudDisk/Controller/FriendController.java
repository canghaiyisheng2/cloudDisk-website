package org.CloudDisk.Controller;

import org.CloudDisk.Service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class FriendController {

    @Autowired
    FriendService friendService;

    @GetMapping("/getFriList")
    public String getFriendList(HttpSession session){
        return friendService.getFriendList(session);
    }

    @PostMapping("/reqFri")
    public String AskForFriend(@RequestParam("user") String userName,
                               HttpSession session){
        return friendService.sendFriRequest(userName, session);
    }

    @PostMapping("/confirm")
    public String confirmFriReq(@RequestParam("accept")boolean isAccept,
                                @RequestParam("user")String uid,
                                HttpSession session){
        return friendService.confirmFriRequest(isAccept, uid, session);
    }

    @PostMapping("/delFri")
    public String deleteFriend(@RequestParam("user") String uid,
                               HttpSession session){
        return friendService.deleteFriend(uid, session);
    }

    @PostMapping("/getChatList")
    public String getCahtList(@RequestParam("user") String uid,
                               HttpSession session){
        return friendService.getChatList(uid, session);
    }
}
