package com.arathimani.tapandcount;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Count {
	private long id;
	private String date;
	private String desc;
	private int value;
	
	public Count() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String dateValue = dateFormat.format(date);
		
		this.id = -1;
		this.date = dateValue;
		this.desc = "new_count";
		this.value = 0;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return desc + " ==> " + value;
	}
}
