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

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class User {
	
	private final String name;
	private final String userID;
	private double medianTOD=-1;
	LocalDate[] sortedDates;

	private ArrayList<Revision> li_Revisions=new ArrayList<Revision>();
	public User(String name)
	{
		Date d=new Date();
		this.userID=d.getTime()+name;
		this.name=name;
	}

	public String getUserID() {
		return userID;
	}

	public String getName() {
		return name;
	}
	
	public void addRevision(Revision r)
	{
		Revision rv=new Revision();
		rv.setTimestamp(r.getTimestamp());
		rv.setUser(r.getUser());
		this.li_Revisions.add(rv);
	}
	
	public int getRevisionCount()
	{
		return this.li_Revisions.size();
		
	}
	
	public double getMedianTOD()
	{
		return this.medianTOD;
	}
	
	public void close()
	{
		
	}
	public LocalDate getFirstRevision()
	{
		LocalDate min=getMinDate();
		return min;
		
	}
	
	private LocalDate getMinDate()
	{
		Revision ref = this.li_Revisions.get(0);
		int month=Integer.parseInt(ref.getMonth());
		int year=Integer.parseInt(ref.getYear());
		int day=Integer.parseInt(ref.getDay());
		LocalDate min = LocalDate.of(year, month, day);
		for (int i=1;i<this.li_Revisions.size();i++)
		{
			Revision rv=this.li_Revisions.get(i);
			int localmonth=Integer.parseInt(rv.getMonth());
			int localyear=Integer.parseInt(rv.getYear());
			int localday=Integer.parseInt(rv.getDay());
			LocalDate max = LocalDate.of(localyear, localmonth, localday);
			int com = min.compareTo(max);
			if(com>0)
				min=max;

		}
		return min;
		
	}
	
	public void sort()
	{
		 sortedDates = new LocalDate[this.li_Revisions.size()];
		for(int i=0;i<this.li_Revisions.size();i++)
		{
			Revision r = this.li_Revisions.get(i);
			LocalDate d=LocalDate.of(Integer.parseInt(r.getYear())
					, Integer.parseInt(r.getMonth()),Integer.parseInt(r.getDay()));
			sortedDates[i]=d;
		}
		
		boolean complete=false;
		while(!complete)
		{
			complete=true;

			for(int i=0;i<sortedDates.length-1;i++)
			{
				int c=sortedDates[i].compareTo(sortedDates[i+1]);
				if(c>0)
				{
					LocalDate temp=sortedDates[i];
					sortedDates[i]=sortedDates[i+1];
					sortedDates[i+1]=temp;
					complete=false;
				}
			}
		}
	}
}


