package org.CloudDisk.Service;

import org.CloudDisk.Dao.UserDao;
import org.CloudDisk.Utils.responseObj;
import org.CloudDisk.pojo.User;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Transactional
    public String modifyUserName(String newName ,HttpSession session){
        String oldName = (String) session.getAttribute("userName");
        try {
            userDao.updateUserName(oldName, newName);
            session.setAttribute("userName", newName);
        }catch (Exception e){
            return new responseObj("fail", e.getMessage()).toJson();
        }

        return new responseObj("success","").toJson();
    }

    @Transactional
    public String UploadAvatar(MultipartFile multipartFile, HttpSession session) {
        String fileUser = (String) session.getAttribute("userName"); //上传文件用户
        String fileName = multipartFile.getOriginalFilename();
        String filePath = null;
        try {
            filePath = ResourceUtils.getURL("classpath:").getPath() + "static/avatar/";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File temp = new File(filePath);
        if (!temp.exists()) {
            temp.mkdirs();
        }

        String contentType = multipartFile.getContentType();
        if (!("image/jpeg".equals(contentType) || "image/jpg".equals(contentType) || "image/png".equals(contentType)))
            return new responseObj("fail", "错误的文件类型").toJson();

        User user = userDao.findOneByName(fileUser);
        String suffix = "." + FilenameUtils.getExtension(fileName);
        File localFile;
        if (!StringUtils.isEmpty(user.getAvatar())) {
            localFile = new File(filePath + user.getUuid() + user.getAvatar());
            localFile.delete();
        }
        localFile = new File(filePath + user.getUuid() + suffix);
        try {
            multipartFile.transferTo(localFile); //把上传的文件保存至本地
            userDao.updateAvatar(fileUser, suffix);
            System.out.println(fileName + " 上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new responseObj("fail", "上传失败").toJson();
        }
        return new responseObj("success", "").toJson();
    }

    public boolean isAdmin(String uid){
        User user = userDao.findOne(uid);
        return user.getAuth().equals("admin");
    }
}
