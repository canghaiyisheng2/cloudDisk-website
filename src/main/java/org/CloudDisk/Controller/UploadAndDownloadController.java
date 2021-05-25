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
import java.util.Date;

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

    @ResponseBody
    @GetMapping("/getFileList")
    public String getFileList(@RequestParam(value = "p",required=false,defaultValue = "0") int page,
                              @RequestParam(value = "size",required=false,defaultValue = "10") int size,
                              HttpSession session){
        Page<upFile> p = fileService.getFileList(page ,size ,session);
        if (page!=0 && page >= p.getTotalPages())
            return new responseObj("fail","wrong page request").toJson();
        return new responseObj("success", new PageUtil(p).toJson()).toJson();
    }

    @RequestMapping("/downloads")
    public void download(@RequestParam("para") int para, HttpSession session, HttpServletResponse response){
        fileService.download(para, session, response);
    }

    @RequestMapping("/avatar")
    @ResponseBody
    public void avatar(@RequestParam("path")String path, HttpSession session, HttpServletResponse response){
        fileService.avatar(path,session,response);
    }

    @ResponseBody
    @RequestMapping("/withdraw")
    public void withdraw(@RequestParam("para") int para, HttpSession session){
        fileService.DeleteItembyId(para, session);
    }
}
