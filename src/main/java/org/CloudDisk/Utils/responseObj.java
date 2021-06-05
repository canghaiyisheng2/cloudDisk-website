package org.CloudDisk.Utils;

import com.google.gson.Gson;

public class responseObj {
	String status;
	Object msg;
	
	public responseObj() {
	}
	
	public responseObj(String stas, Object m) {
		this.status = stas;
		this.msg = m;
	}
	
	public String toJson() {
		return new Gson().toJson(this);
	}
}
