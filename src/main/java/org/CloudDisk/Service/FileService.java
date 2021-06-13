package org.CloudDisk.Service;

import com.google.gson.Gson;
import javafx.util.Pair;
import org.CloudDisk.Dao.DirDao;
import org.CloudDisk.Dao.RecycleBinDao;
import org.CloudDisk.Dao.UserDao;
import org.CloudDisk.Dao.upFileDao;
import org.CloudDisk.Utils.responseObj;
import org.CloudDisk.pojo.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Service
public class FileService {

    @Autowired
    upFileDao upFileDao;
    @Autowired
    UserDao userDao;
    @Autowired
    DirDao dirDao;
    @Autowired
    RecycleBinDao recycleBinDao;
    @Value("${spring.http.multipart.location}")
    String uploadDir;

    public String UploadFile(MultipartFile multipartFile, Date date, String msg,  HttpSession session) {

        String filename = multipartFile.getOriginalFilename(); //获取上传文件原来的名称
        String fileUserUid = (String) session.getAttribute("uid"); //上传文件用户
        String filePath = null;

        //upload file related message to database
        upFile file = new upFile();
        file.setDate(date);
        file.setMsg(msg);
        file.setName(filename);
        file.setUsr(fileUserUid);
        file.setSize(FileUtils.byteCountToDisplaySize(multipartFile.getSize()));
        file.setPath(userDao.getUserPathByUid(fileUserUid));
        file.setToken(UUID.randomUUID().toString()); //利用UUID生成下载token
        file = upFileDao.save(file);

        if (multipartFile.isEmpty()) {
            return  new responseObj("fail", "未选择文件").toJson();
        }
        filePath = "upload/" + fileUserUid + "/";
        File temp = new File(uploadDir + filePath);
        if (!temp.exists()) {
            temp.mkdirs();
        }

        File localFile = new File(filePath + file.getNo() + "." + FilenameUtils.getExtension(filename));
        try {
            multipartFile.transferTo(localFile); //把上传的文件保存至本地
        } catch (IOException e) {
            e.printStackTrace();
            return new responseObj("fail", "服务器内部错误，上传失败！").toJson();
        }
        return new responseObj("success","").toJson();
    }

    @Transactional
    public String NewDir(String name, int path, HttpSession session){
        String user = (String) session.getAttribute("uid");
        //验证文件夹归属
        Dir dir = dirDao.findOne(path);
        if (dir == null || !dir.getUsr().equals(user))
            return new responseObj("fail","参数错误").toJson();
        //新建文件夹
        Dir newDir = new Dir();
        newDir.setName(name);
        newDir.setPath(path);
        newDir.setUsr(user);
        newDir.setDate(new Date());
        dirDao.save(newDir);
        return new responseObj("success","").toJson();
    }

    public String getDirList(HttpSession session){
        String user = (String) session.getAttribute("uid");
        List<Dir> dirs = dirDao.findAllByUser(user);
        return new responseObj("success",dirs).toJson();
    }

    public String getItemList(int path, HttpSession session){
        String uid = (String) session.getAttribute("uid");
        String userName = (String) session.getAttribute("userName");
        if(!StringUtils.isEmpty(uid)){
            List items;
            if (isAdmin(uid)){
                //管理员返回所有文件
                items = upFileDao.findAll();
                List<User> id_nameList = userDao.findAll();
                Map<String, String> map = new HashMap<>();
                for (User one : id_nameList){
                    map.put(one.getUuid(),one.getUsrName());
                }
                for(Object i : items){
                    ((upFile) i).setUsr(map.get(((upFile) i).getUsr()));
                }
            }
            else{
                if (path < 0){
                    //回收站
                    items = recycleBinDao.findAllByUser(uid);
                    for(Object i : items){
                        ((RecycleItem) i).setUsr(userName);
                    }
                    return new responseObj("success",items).toJson();
                }

                //判断path是否为对应的用户上传的目录号
                if (path == 0 || (!(userDao.findOne(uid).getDirNo() == path) && !(dirDao.findOne(path).getUsr().equals(uid))))
                    return new responseObj("fail", "请别偷看别人的目录！").toJson();

                //用户返回用户上传的文件与目录
                items = dirDao.findByPath(path);
                for(Object i : items){
                    ((Dir) i).setUsr(userName);
                }
                List<upFile> files = upFileDao.findByPath(path);
                for (upFile f : files){
                    f.setUsr(userName);
                }
                items.addAll(files);
            }
            return new responseObj("success",items).toJson();
        }
        return new responseObj("fail", "该功能需要先登录！").toJson();
    }

    @Transactional
    public String moveItem(String type, int no, int srcPath, int desPath, HttpSession session){
        String user = (String) session.getAttribute("uid");
        if (type.equals("file")){
            upFile item = upFileDao.findOne(no);
            if (item == null || !user.equals(item.getUsr()) || item.getPath()!=srcPath) return new responseObj("fail","参数错误").toJson();
            upFileDao.updatePathByNo(no, desPath);
        }
        else if (type.equals("dir")){
            Dir item = dirDao.findOne(no);
            if (item == null || !user.equals(item.getUsr()) || item.getPath()!=srcPath) return new responseObj("fail","参数错误").toJson();
            dirDao.updatePathByNo(no, desPath);
        }
        return new responseObj("success","").toJson();
    }

    public void download(int fno, String token, HttpSession session, HttpServletResponse response) {
        upFile upFile = upFileDao.getOne(fno);
        String curUser = (String)session.getAttribute("uid");
        //该功能需要先登录
        if (StringUtils.isEmpty(curUser))
            return;
        //没有下载权限
        if (!upFile.getUsr().equals(curUser) && !upFile.getToken().equals(token) && !curUser.equals("001"))
            return;
        FileInputStream fis = null;
        ServletOutputStream out = null;
        try {
            String filePath = uploadDir + upFile.getFilePath();
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName="+ URLEncoder.encode(upFile.getName(),"UTF-8"));
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
            //内部服务器错误,下载失败
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
        System.out.println("success");
    }

    public void avatar(String userName, HttpServletResponse response){
        User user = userDao.findOneByName(userName);
        FileInputStream fis = null;
        ServletOutputStream out = null;
        try {
            String filePath = "";
            if (user == null || user.getAvatar()==null)
                filePath = ResourceUtils.getFile("classpath:static/default.png").getAbsolutePath();
            else{
                filePath = uploadDir + "avatar/" + user.getUuid() + user.getAvatar();
            }
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

    @Transactional
    public String UploadAvatar(MultipartFile multipartFile, HttpSession session) {
        String fileUser = (String) session.getAttribute("uid"); //上传文件用户
        if (StringUtils.isEmpty(fileUser))
            return new responseObj("fail", "未登录").toJson();
        String fileName = multipartFile.getOriginalFilename();
        String filePath = "avatar/";
        File temp = new File(uploadDir + filePath);
        if (!temp.exists()) {
            temp.mkdirs();
        }

        String contentType = multipartFile.getContentType();
        if (!("image/jpeg".equals(contentType) || "image/jpg".equals(contentType) || "image/png".equals(contentType)))
            return new responseObj("fail", "错误的文件类型").toJson();

        User user = userDao.findOne(fileUser);
        String suffix = "." + FilenameUtils.getExtension(fileName);
        File localFile;
        //此时已有头像时删除原头像
        if (!StringUtils.isEmpty(user.getAvatar())) {
            localFile = new File(filePath + fileUser + user.getAvatar());
            localFile.delete();
        }
        //添加新头像数据
        localFile = new File(filePath + fileUser + suffix);
        try {
            multipartFile.transferTo(localFile); //把上传的文件保存至本地
            userDao.updateAvatar(fileUser, suffix);
        } catch (Exception e) {
            e.printStackTrace();
            return new responseObj("fail", "服务器内部错误，上传失败！").toJson();
        }
        return new responseObj("success", "").toJson();
    }

    @Transactional
    public String MoveToBin(int no, String type, HttpSession session){
        String user = (String) session.getAttribute("uid");
        if (type.equals("file")){
            upFile item = upFileDao.findOne(no);
            if (item == null || !user.equals(item.getUsr())) return new responseObj("fail","参数错误").toJson();
            recycleBinDao.save(new RecycleItem(item));
            upFileDao.delete(no);
        }
        else if (type.equals("dir")){
            Dir item = dirDao.findOne(no);
            if (item == null || !user.equals(item.getUsr())) return new responseObj("fail","参数错误").toJson();
            recycleBinDao.save(new RecycleItem(item));
            dirDao.delete(no);
        }
        else return new responseObj("fail","参数错误").toJson();
        return new responseObj("success","").toJson();
    }

    @Transactional
    public String RestoreFromBin(int no, String type, HttpSession session){
        String user = (String) session.getAttribute("uid");
        RecycleItem item = recycleBinDao.findOne(new RecycleItemPK(type,no));
        if (item == null || !user.equals(item.getUsr()))
            return new responseObj("fail","参数错误").toJson();
        if (type.equals("file"))
            upFileDao.save(new upFile(item));
        else if (type.equals("dir"))
            dirDao.save(new Dir(item));
        else
            return new responseObj("fail","参数错误").toJson();
        recycleBinDao.delete(new RecycleItemPK(type, no));
        return new responseObj("success","").toJson();
    }

    public void DeleteItembyId(int id ,HttpSession session){
        upFile upFile = upFileDao.getOne(id);
        if (!upFile.getUsr().equals(session.getAttribute("uid")))
            return;
        String filePath = null;
        filePath = uploadDir+upFile.getFilePath();
        File file = new File(filePath);
        file.delete();
        upFileDao.delete(id);
    }

    public boolean isAdmin(String uid){
        User user = userDao.findOne(uid);
        return user.getAuth().equals("admin");
    }
}
