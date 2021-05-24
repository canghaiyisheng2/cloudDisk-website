package org.CloudDisk.Utils;

import com.google.gson.Gson;

public class responseObj {
	String status;
	String msg;
	
	public responseObj() {
	}
	
	public responseObj(String stas, String m) {
		this.status = stas;
		this.msg = m;
	}
	
	public String toJson() {
		return new Gson().toJson(this);
	}
}
