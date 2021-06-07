package org.CloudDisk.pojo;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "usrinfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

	public String toJson() {
		return new Gson().toJson(this);
	}
}
