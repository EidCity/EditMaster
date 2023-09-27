/**
 * Copyright  2020-2023 Shereif Eid

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */


package eid.java.editmaster;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Revision {
	
	
	private String timestamp;
	private String user;
	private String day,month,year;
	private String hour,minutes,seconds;
	public String getHour() {
		return hour;
	}
	
	public String getMinutes() {
		return minutes;
	}
	
	public String getSeconds() {
		return seconds;
	}
	
	public String getDay() {
		return day;
	}
	public String getMonth() {
		return month;
	}
	
	public String getYear() {
		return year;
	}

	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
		setDateTime(timestamp);
		
	}
	
	private void setDateTime(String args)
	{
		String[] objs = args.split("T");
		setDate(objs[0]);
		String time= objs[1];
		objs=time.split("Z");
		time=objs[0];
		objs= time.split(":");
		this.hour=objs[0];
		this.minutes=objs[1];
		this.seconds=objs[2];
		
	}
	
	
	private void setDate(String args)
	{
		String[] objs = args.split("-");
		this.year=objs[0];
		this.month=objs[1];
		this.day=objs[2];
	}
	
	

}
