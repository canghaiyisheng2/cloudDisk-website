package org.CloudDisk.Controller;

import org.CloudDisk.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class ItemController {

    @Autowired
    FileService fileService;

    @ResponseBody
    @GetMapping("/getFileList")
    public String getFileList(@RequestParam("path") int path,
                              HttpSession session){
        return fileService.getItemList(path, session);
    }
}
