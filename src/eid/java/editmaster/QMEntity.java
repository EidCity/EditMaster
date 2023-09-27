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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class QMEntity {

	private String qId;
	private LocalDate m_min,m_max;
	private int totalIdle=0;
	LocalDate[] sortedDates;
	int congestion=0;
	Revision[] SortedRevisions;
	int wait=0;
	int value=0;
	ArrayList<Integer> df=new ArrayList<Integer>();
	public int getTotalIdle()
	{

		for(int i=0;i<this.users.size()-1;i++)
		{
			totalIdle+=ChronoUnit.DAYS.between(users.get(i).getFirstRevision()
					, users.get(i+1).getFirstRevision());
		}

		return totalIdle;
	}
	public LocalDate getOldestDate() {
		return m_min;
	}
	public LocalDate getLatestDate() {
		return m_max;
	}
	public String getqId() {
		return qId;
	}
	public void setqId(String qId) {
		this.qId = qId;
	}
	public ArrayList<Revision> getRevs() {
		return revs;
	}
	public ArrayList<User> getUsers() {
		return users;
	}
	public void setRevs(ArrayList<Revision> revisions)
	{
		this.revs=revisions;
	}
	public void setUsers(ArrayList<User> listUsers)
	{
		this.users=listUsers;
	}
	private  ArrayList<Revision> revs= new ArrayList<Revision>();
	private  ArrayList<User> users= new ArrayList<User>();

	public void assignUserEdits()
	{
		for(int i=0;i<this.users.size();i++)
		{
			User u = users.get(i);
			for(int j=0;j<this.revs.size();j++)
			{
				Revision v= revs.get(j);
				if(u.getName().equals(v.getUser()))
					u.addRevision(v);
			}
			//u.close();
		}
	}

	public int getCountUser()
	{
		return this.users.size();
	}
	public User getTopContributor()
	{
		int i=users.get(0).getRevisionCount();
		User u=users.get(0);
		for(int j=1;j<users.size();j++)
		{
			int k=users.get(j).getRevisionCount();
			if(k>i)
			{
				u=users.get(j);
				i=k;
			}
		}

		return u;
	}

	public int PERIOD()
	{
		LocalDate max=getMaxDate();
		LocalDate min=getMinDate();
		this.m_min=min;
		this.m_max=max;

		Period period = Period.between(min, max);
		return period.getDays();
	}

	public LocalDate getMinDate()
	{
		int month=Integer.parseInt(this.revs.get(0).getMonth());
		int year=Integer.parseInt(this.revs.get(0).getYear());
		int day=Integer.parseInt(this.revs.get(0).getDay());
		LocalDate min = LocalDate.of(year, month, day);
		for (int i=1;i<this.revs.size();i++)
		{
			int localmonth=Integer.parseInt(this.revs.get(i).getMonth());
			int localyear=Integer.parseInt(this.revs.get(i).getYear());
			int localday=Integer.parseInt(this.revs.get(i).getDay());
			LocalDate max = LocalDate.of(localyear, localmonth, localday);
			int com = min.compareTo(max);
			if(com>0)
				min=max;

		}
		return min;
	}

	public LocalDate getMaxDate()
	{
		Revision ref = this.revs.get(0);
		int month=Integer.parseInt(ref.getMonth());
		int year=Integer.parseInt(ref.getYear());
		int day=Integer.parseInt(ref.getDay());
		LocalDate max = LocalDate.of(year, month, day);
		for (int i=1;i<this.revs.size();i++)
		{
			Revision rv=this.revs.get(i);
			int localmonth=Integer.parseInt(rv.getMonth());
			int localyear=Integer.parseInt(rv.getYear());
			int localday=Integer.parseInt(rv.getDay());
			LocalDate min = LocalDate.of(localyear, localmonth, localday);
			int com = max.compareTo(min);
			if(com<0)
				max=min;

		}

		return max;

	}

	public long getTTA()
	{
		// we need the max of the min

		User ref = this.users.get(0);
		int month=ref.getFirstRevision().getMonthValue();
		int year=ref.getFirstRevision().getYear();
		int day=ref.getFirstRevision().getDayOfMonth();
		LocalDate max = LocalDate.of(year, month, day);
		for (int i=1;i<this.users.size();i++)
		{
			User rv=this.users.get(i);
			int localmonth=rv.getFirstRevision().getMonthValue();;
			int localyear=rv.getFirstRevision().getYear();
			int localday=rv.getFirstRevision().getDayOfMonth();
			LocalDate min = LocalDate.of(localyear, localmonth, localday);
			int com = max.compareTo(min);
			if(com<0)
				max=min;
		}

		return ChronoUnit.DAYS.between(this.m_min, max);
	}

	public double PHET()
	{

		sortedDates = new LocalDate[this.revs.size()];
		for(int i=0;i<revs.size();i++)
		{
			Revision r = this.revs.get(i);
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

		SortedRevisions = new Revision[revs.size()];
		for(int i=0;i<sortedDates.length;i++)
		{
			for(int j=0;j<revs.size();j++)
			{
				Revision r = revs.get(j);
				LocalDate d=LocalDate.of(Integer.parseInt(r.getYear())
						, Integer.parseInt(r.getMonth()),Integer.parseInt(r.getDay()));
				int c = sortedDates[i].compareTo(d);
				if(c==0)
					SortedRevisions[i]=r;
			}
		}

		double total = 0;
		for(int i=0;i<SortedRevisions.length-1;i++)
		{
			if(i==100)
				break;
			if(!SortedRevisions[i].getUser().equals(SortedRevisions[i+1].getUser()))
			{
				total++;
				df.add(1);
			}
			else
			{
				df.add(0);
			}

		}

		//		Thread thread = new Thread(){
		//		    public void run(){
		//		      avgCongestion();
		//		    }
		//		  };

		// thread.start();
		this.value=(int)total;

		return total/(double)revs.size();

	}
	
	public int getV()
	{
		return this.value;
	}

	public int avgCongestion() 
	{
		//QPort q= new QPort();
		Revision temp=SortedRevisions[0];
		//q.land(temp);
		LocalDate now=this.sortedDates[0];
		int buffer=0;
		for(int i =1;i<this.SortedRevisions.length;i++)
		{
			//Thread.sleep(1000);
			//q.depart();
			//Period p = Period.between(now, sortedDates[i]);
			if(Integer.parseInt(temp.getYear())==Integer.parseInt(SortedRevisions[i].getYear()))
				if(Integer.parseInt(temp.getMonth())==Integer.parseInt(SortedRevisions[i].getMonth()))
					if(Integer.parseInt(temp.getDay())==Integer.parseInt(SortedRevisions[i].getDay()))
						if(Integer.parseInt(temp.getHour())==Integer.parseInt(SortedRevisions[i].getHour()))
						{
							int k = Integer.parseInt(SortedRevisions[i].getMinutes())-Integer.parseInt(temp.getMinutes());
							if(k-15>0)
							{
								wait+=k+buffer;
								buffer=k;

							}
						}

			//q.land(SortedRevisions[i]);
			temp=SortedRevisions[i];
			//now=sortedDates[i];

		}

		return wait;
		//write in file
	}
	
	public ArrayList<Integer> getDF()
	{
		return this.df;
	}

}
