package org.CloudDisk.Service;

import org.CloudDisk.Dao.UserDao;
import org.CloudDisk.pojo.User;
import org.CloudDisk.Utils.responseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class LoginAndSignupService {
	@Autowired
	UserDao userDao;

	public String checkLogin(HttpSession session, String name, String passwd){
		User user = userDao.findOneByName(name);
		if (user == null) return new responseObj("fail", "该用户名不存在").toJson();
		if (passwd.equals(user.getPasswd())){
			session.setAttribute("userName", user.getUsrName());
			session.setAttribute("uid", user.getUuid());
			return user.toJson();
		}
		else
			return new responseObj("fail", "用户名或密码错误").toJson();
	}

	public String CheckLoginStatus(HttpSession session){
		if(session.getAttribute("userName") == null) {
			return new responseObj("fail","").toJson();
		}
		else {
			String name = (String)session.getAttribute("userName");
			User user = userDao.findOneByName(name);
			return new responseObj("success",user.toJson()).toJson();
		}
	}

	public String signupCheck(HttpServletRequest request, String name,String passwd){
		if (userDao.findOneByName(name)!=null)
			return new responseObj("fail","用户名已存在").toJson();
		else{
			User user = new User();
			user.setUsrName(name);
			user.setPasswd(passwd);
			user.setAuth("user");
			userDao.save(user);
			request.getSession().setAttribute("userName", user.getUsrName());
			request.getSession().setAttribute("uid", user.getUuid());
			return user.toJson();
		}
	}

	public String exit(HttpSession session){
		if(session.getAttribute("uid") != null) {
			session.removeAttribute("userName");
			session.removeAttribute("uid");
			return new responseObj("success","").toJson();
		}
		else {
			return new responseObj("fail","").toJson();
		}
	}
}