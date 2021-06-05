package org.CloudDisk.Controller;

import com.sun.deploy.net.HttpResponse;
import com.sun.xml.internal.ws.server.ServerRtException;
import org.CloudDisk.Service.FileService;
import org.CloudDisk.Utils.PageUtil;
import org.CloudDisk.Utils.responseObj;
import org.CloudDisk.pojo.upFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class UploadAndDownloadController {
    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    @ResponseBody
    public String upload(@RequestParam("file")MultipartFile multipartFile,
                         @RequestParam("date")Date date,
                         @RequestParam("msg")String msg,
                         HttpSession session){
        return fileService.UploadFile(multipartFile,date,msg,session);
    }


    @RequestMapping("/download")
    public void download(@RequestParam("no") int fno,
                         @RequestParam(value = "token", required = false, defaultValue = "")String token,
                         HttpSession session, HttpServletResponse response){
        fileService.download(fno, token, session, response);
    }


    @PostMapping("/upavatar")
    @ResponseBody
    public String UpAvatar(@RequestParam("file") MultipartFile multipartFile,
                           HttpSession session){
        return fileService.UploadAvatar(multipartFile, session);
    }

    @RequestMapping("/avatar")
    @ResponseBody
    public void avatar(@RequestParam("user") String userName,
                       HttpServletResponse response){
        fileService.avatar(userName ,response);
    }

}
