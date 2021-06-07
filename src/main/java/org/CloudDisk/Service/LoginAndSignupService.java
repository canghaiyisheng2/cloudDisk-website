package org.CloudDisk.Service;

import org.CloudDisk.Config.RabbitmqConfig;
import org.CloudDisk.Dao.DirDao;
import org.CloudDisk.Dao.UserDao;
import org.CloudDisk.Utils.QueueMsg;
import org.CloudDisk.pojo.Dir;
import org.CloudDisk.pojo.User;
import org.CloudDisk.Utils.responseObj;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class LoginAndSignupService {
	@Autowired
	UserDao userDao;
	@Autowired
	DirDao dirDao;
	@Autowired
	RabbitmqConfig rabbitmqConfig;
	@Resource
	RabbitTemplate rabbitTemplate;
	@Autowired
	RabbitAdmin rabbitAdmin;

	public String checkLogin(HttpSession session, String name, String passwd){
		User user = userDao.findOneByName(name);
		if (user == null) return new responseObj("fail", "该用户名不存在").toJson();
		if (passwd.equals(user.getPasswd())){
			session.setAttribute("userName", user.getUsrName());
			session.setAttribute("uid", user.getUuid());
			return new responseObj("success","").toJson();
		}
		else
			return new responseObj("fail", "用户名或密码错误").toJson();
	}

	public String CheckLoginStatus(HttpSession session){
		if(session.getAttribute("userName") == null) {
			return new responseObj("fail","未登录").toJson();
		}
		else {
			String name = (String)session.getAttribute("userName");
			User user = userDao.findOneByName(name);
			return new responseObj("success",user.toJson()).toJson();
		}
	}

	@Transactional
	public String signupCheck(HttpSession session, String name,String passwd){
		if (name.equals("") || passwd.equals(""))
			return new responseObj("fail","用户名或密码不得为空").toJson();
		if (userDao.findOneByName(name)!=null)
			return new responseObj("fail","用户名已存在").toJson();
		else{
			//save new user
			User user = new User();
			user.setUsrName(name);
			user.setPasswd(passwd);
			user.setAuth("user");
			userDao.save(user);
			session.setAttribute("userName", user.getUsrName());
			session.setAttribute("uid", user.getUuid());

			//save new user's root dir
			Dir newDir = new Dir();
			newDir.setName("root");
			newDir.setUsr("");
			newDir.setPath(0);
			newDir.setUsr(user.getUuid());
			newDir.setDate(new Date());
			dirDao.save(newDir);

			//update dusr in table dirinfo
			userDao.updateDirNoById(user.getUuid(),newDir.getNo());

			//注册用户的消息队列
			Queue queue = new Queue(user.getUuid(), true);
			rabbitAdmin.declareQueue(queue);
			rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(rabbitmqConfig.MsgExchange()).with(user.getUuid()).noargs());

			//发送欢迎消息
			QueueMsg queueMsg = new QueueMsg("notice",new Date().toString(),"Welcome to our system!");
			rabbitTemplate.convertAndSend("inform.msg",user.getUuid(),queueMsg);

			return new responseObj("success","").toJson();
		}
	}

	public String exit(HttpSession session){
		if(session.getAttribute("uid") != null) {
			session.removeAttribute("userName");
			session.removeAttribute("uid");
			return new responseObj("success","").toJson();
		}
		else {
			return new responseObj("fail","未登录").toJson();
		}
	}
}