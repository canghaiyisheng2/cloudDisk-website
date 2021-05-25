package org.CloudDisk.Service;

import org.CloudDisk.Dao.UserDao;
import org.CloudDisk.Dao.upFileDao;
import org.CloudDisk.Utils.responseObj;
import org.CloudDisk.pojo.User;
import org.CloudDisk.pojo.upFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileService {

    @Autowired
    upFileDao upFileDao;
    @Autowired
    UserDao userDao;
    @Value("${spring.http.multipart.location}")
    String uploadDir;

    public String UploadFile(MultipartFile multipartFile, Date date, String msg, HttpSession session) {

        String filename = multipartFile.getOriginalFilename(); //获取上传文件原来的名称
        String fileUserUid = (String) session.getAttribute("uid"); //上传文件用户
        String filePath = null;

        //file related message upload to database
        upFile file = new upFile();
        file.setDate(date);
        file.setMsg(msg);
        file.setName(filename);
        file.setUsr(fileUserUid);
        file = upFileDao.save(file);

        if (multipartFile.isEmpty()) {
            return "未选择文件";
        }
        filePath = "upload/" + fileUserUid + "/";
        File temp = new File(uploadDir + filePath);
        System.out.println(uploadDir);
        if (!temp.exists()) {
            temp.mkdirs();
        }

        File localFile = new File(filePath + file.getNo() + "." + FilenameUtils.getExtension(filename));
        try {
            multipartFile.transferTo(localFile); //把上传的文件保存至本地
            System.out.println(multipartFile.getOriginalFilename() + " 上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return new responseObj("fail", "上传失败").toJson();
        }
        return new responseObj("success", String.valueOf(file.getNo())).toJson();
    }

    public Page<upFile> getFileList(int page, int size ,HttpSession session){
        String uid = (String) session.getAttribute("uid");
        String userName = (String) session.getAttribute("userName");
        if(!StringUtils.isEmpty(uid)){
            PageRequest pageable= new PageRequest(page, size);
            Page<upFile> p;

            if (isAdmin(uid)){
                p=upFileDao.findAll(pageable);
                List<User> users = userDao.findAll();
                Map<String, String> map = new HashMap<>();
                for (User user:users){
                    map.put(user.getUuid(), user.getUsrName());
                }
                for(upFile f : p.getContent()){
                    f.setUsr(map.get(f.getUsr()));
                }
            }
            else{
                p = upFileDao.findAll(uid,pageable);
                for(upFile f : p.getContent()){
                    f.setUsr(userName);
                }
            }
            return p;
        }
        return null;
    }

    public void download(int para, HttpSession session, HttpServletResponse response) {
        upFile upFile = upFileDao.getOne(para);
        String curUser = (String)session.getAttribute("uid");
        if (!curUser.equals("001") && !upFile.getUsr().equals(curUser))
            return;
        FileInputStream fis = null;
        ServletOutputStream out = null;
        try {
            String filePath = uploadDir + upFile.getPath();
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName="+ upFile.getName());
            File file = new File(filePath);
            fis = new FileInputStream(file);
            out = response.getOutputStream();

            byte[] buffer = new byte[512];
            int b = 0;
            while(b!=-1){
                b = fis.read(buffer);
                if(b != -1){
                    out.write(buffer,0,b);//写到输出流(out)中
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(fis!=null){
                    fis.close();
                }
                if(out!=null){
                    out.close();
                    out.flush();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void avatar(String path, HttpSession session, HttpServletResponse response){
        String curUser = (String)session.getAttribute("uid");
        FileInputStream fis = null;
        ServletOutputStream out = null;
        try {
            String filePath = uploadDir + "avatar/" + path;
            File file = new File(filePath);
            fis = new FileInputStream(file);
            out = response.getOutputStream();

            byte[] buffer = new byte[512];
            int b = 0;
            while(b!=-1){
                b = fis.read(buffer);
                if(b != -1){
                    out.write(buffer,0,b);//写到输出流(out)中
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(fis!=null){
                    fis.close();
                }
                if(out!=null){
                    out.close();
                    out.flush();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void DeleteItembyId(int id ,HttpSession session){
        upFile upFile = upFileDao.getOne(id);
        if (!upFile.getUsr().equals(session.getAttribute("uid")))
            return;
        String filePath = null;
        try {
            filePath = ResourceUtils.getURL("classpath:").getPath()+upFile.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File file = new File(filePath);
        file.delete();
        upFileDao.delete(id);
    }

    public boolean isAdmin(String uid){
        User user = userDao.findOne(uid);
        return user.getAuth().equals("admin");
    }
}
