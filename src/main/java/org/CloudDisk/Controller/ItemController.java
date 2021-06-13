package org.CloudDisk.Controller;

import org.CloudDisk.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class ItemController {

    @Autowired
    FileService fileService;

    @PostMapping("/getFileList")
    public String getFileList(@RequestParam("path") int path,
                              HttpSession session){
        return fileService.getItemList(path, session);
    }

    @PostMapping("/bin")
    public String moveToBin(@RequestParam("no")int no,
                            @RequestParam("type")String type,
                            HttpSession session){
        return fileService.MoveToBin(no, type, session);
    }

    @PostMapping("/restore")
    public String Restore(@RequestParam("no")int no,
                          @RequestParam("type")String type,
                          HttpSession session){
        return fileService.RestoreFromBin(no, type, session);
    }

    @PostMapping("/newdir")
    public String newDir(@RequestParam("name")String dirName,
                         @RequestParam("path")int path,
                         HttpSession session){
        return fileService.NewDir(dirName, path, session);
    }

    @GetMapping("/getDirList")
    public String getDirList(HttpSession session){
        return fileService.getDirList(session);
    }

    @PostMapping("/move")
    public String move(@RequestParam("type")String type,
                       @RequestParam("no")int no,
                       @RequestParam("srcPath")int srcPath,
                       @RequestParam("desPath")int desPath,
                       HttpSession session){
        return fileService.moveItem(type, no, srcPath, desPath, session);
    }
}
