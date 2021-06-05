package org.CloudDisk.pojo;

import com.google.gson.Gson;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "usrinfo")

public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name = "uuid")
	private String uuid;
	@Column(name = "usrname")
	private String usrName;
	@Column(name = "pwd")
	private String passwd;
	@Column(name = "auth")
	private String auth;
	@Column(name = "avatar")
	private String avatar;
	@Column(name = "dirno")
	private int dirNo;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsrName() {
		return usrName;
	}
	
	public String getPasswd() {
		return passwd;
	}
	
	public String getAuth() {
		return auth;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}
	
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	public void setAuth(String auth) {
		this.auth = auth;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getDirNo() {
		return dirNo;
	}

	public void setDirNo(int dirNo) {
		this.dirNo = dirNo;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}
}
