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

import java.util.ArrayList;

public class ResponseHandler {
	
	
	public ArrayList<String> getObjects(RESPONSE_TYPE type,String responseString,String target)
	{
		//ArrayList<String> listOfObjects = new ArrayList();
		
		String JsonString = responseString.substring(responseString.indexOf("revisions"));
		int index= JsonString.indexOf("[");
		JsonString = JsonString.substring(index);
		String[] objects= JsonString.split(",");
		ArrayList<String> targets = new ArrayList<String>();
		for(int i=0;i<objects.length;i++)
		{
			String reply=getTargetString(objects[i],target);
			if(!reply.equals("NULL"))
			targets.add(reply);
		}
		return targets;
	}
	
	private String getTargetString(String obj,String target)
	{
		
		int index= obj.indexOf(target);
		if(index==-1)
			return "NULL";
		boolean isTime=false;
		if(target.equals("timestamp"))
			isTime=true;
		String[] objects= obj.split(":");
		int start=1;
		String temp="";
		while(start<objects.length)
		{
			temp+=objects[start];
			if(isTime)
				temp+=":";
			start++;
		}
		temp=temp.trim();
		temp=temp.replace("\"", "");
		return temp;
	}
	
}
