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


import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;



import eid.java.editmaster.QMEntity;
import eid.java.editmaster.QMFileSystem;
import eid.java.editmaster.QUERY;
import eid.java.editmaster.QueryMaster;
import eid.java.editmaster.RESPONSE_TYPE;
import eid.java.editmaster.ResponseHandler;
import eid.java.editmaster.Revision;
import eid.java.editmaster.User;
import eid.java.editmaster.WIKI;

/**
 * Servlet implementation class ServletMain
 */
@WebServlet("/ServletMain")
public class ServletMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletMain() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private String sendQuery(String queryString) throws IOException
	{
		URL url = new URL(queryString);

        //make connection
        HttpURLConnection urlc =(HttpURLConnection ) url.openConnection();
        urlc.setRequestMethod("GET");
        int responseCode = urlc.getResponseCode();
        boolean redirect = false;

        	if (responseCode == HttpURLConnection.HTTP_OK) { // success
    			BufferedReader in = new BufferedReader(new InputStreamReader(
    					urlc.getInputStream()));
    			String inputLine;
    			StringBuffer buff = new StringBuffer();

    			while ((inputLine = in.readLine()) != null) {
    				buff.append(inputLine);
    			}
    			in.close();
    			return buff.toString();
        	}
        	
        	return "NULL";
     }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String reqParam=request.getParameter("entity");
		String newPram=reqParam.replaceAll("\\s", "%20");
	    response.setContentType("text/html");
	    PrintWriter out=response.getWriter();
	     
	     
	     QueryMaster QM= new QueryMaster(WIKI.WIKIDATA);
	     String queryString=QM.generateQuery(QUERY.ALLREVISIONS,newPram);
	     String reply= sendQuery(queryString);
	    //out.println("<p>"+reply+"</P>");
	     ResponseHandler RH= new ResponseHandler();
	     
	     
	     ArrayList<String> li_Users=RH.getObjects(RESPONSE_TYPE.JSON, reply, "user");
	     ArrayList<String> li_times=RH.getObjects(RESPONSE_TYPE.JSON, reply, "timestamp");
	     
	     
	     // create list of revisions
	     // precondition: users and times must be equal in size
	     ArrayList<Revision> revisions = new ArrayList<Revision>();
	     Iterator<String> it=li_Users.iterator();
	     Iterator<String> it2= li_times.iterator();
	     
	     if(li_Users.size()==li_times.size())
	     {
	    	 for(int i=0;i<li_Users.size();i++)
	    	 {
	    		 Revision r = new Revision();
	    		 r.setUser(li_Users.get(i));
	    		 r.setTimestamp(li_times.get(i));	 
	    		 revisions.add(r);
	    	 }
	     }

	     //copy unique user names to a new list

	     ArrayList<String> li_User_temp=new ArrayList<String>();
	     Iterator<String> itUsers= li_Users.iterator();
	     Set<String> setofNames= new HashSet<String>();
	     
	     while (itUsers.hasNext())
	     {
	    	 setofNames.add(itUsers.next());
	     }
	     
	     li_Users.clear();
	     li_User_temp.addAll(setofNames);
	     
		 //create objects with unique usernames

	     Iterator<String> names= li_User_temp.iterator();
		 ArrayList<User> listOfUsers=new ArrayList<User>();

    	 
	     while(names.hasNext())
	     {
	    	 String n = names.next();	 
	    	 User user = new User(n);
	    	 listOfUsers.add(user);

	     }
	     
	     // Add entity object
	     QMEntity entity = new QMEntity();
	     entity.setqId(newPram);
	     entity.setRevs(revisions);
	     entity.setUsers(listOfUsers);
	     entity.assignUserEdits();
	     int period=entity.PERIOD();
	     int uC=entity.getCountUser();
	     out.println("TR: "+revisions.size()+"Total number of contributors: "
	    	 		+ entity.getCountUser());
	     out.flush();
	     out.println("Top contributor: "
	    	 		+ entity.getTopContributor().getName()
	    	 		+"("+entity.getTopContributor().getRevisionCount()+")");
	     out.flush();
	     
	     for(int i=0;i<listOfUsers.size();i++)
	     {
	    	 out.println(listOfUsers.get(i).getName()+"//");
	     }
	     
	     out.flush();
    	 out.println(String.valueOf(period)+" days");
	     out.flush();
	     double top=entity.getTopContributor().getRevisionCount();
	     
	     double e=(double)((uC-1)/(revisions.size()-top));
	     double w=(double)uC/period;
	     
	     out.flush();
    	 out.println(String.valueOf(e)+" --"+String.valueOf(w));
	     out.flush();
	     
	     String oldest = "Oldest: "+String.valueOf(entity.getOldestDate().getDayOfMonth())+
	    		 "/"+entity.getOldestDate().getMonthValue()
	    		 +"/"
	    		 +entity.getOldestDate().getYear();
	     
	     String latest = "Latest: "+String.valueOf(entity.getLatestDate().getDayOfMonth())+
	    		 "/"+entity.getLatestDate().getMonthValue()
	    		 +"/"
	    		 +entity.getLatestDate().getYear();
	     
	     out.println(oldest+latest+":::"+String.valueOf(entity.getTTA()));
	     out.flush();
	     
	     LocalDate dMin = entity.getMinDate();
	     LocalDate dMax=entity.getMaxDate();
	     
	     String str1="Min: "+ dMin.getDayOfMonth()+"/"+dMin.getMonthValue()+"/"+
	     dMin.getYear();
	     
	     String str2="Max: "+ dMax.getDayOfMonth()+"/"+dMax.getMonthValue()+"/"+
	    	     dMax.getYear();
	     out.println(str1+str2);
	     
	     long p2 = ChronoUnit.DAYS.between(dMin, dMax);
	     out.println("long:"+p2);
	     
	     String tta=String.valueOf(entity.getTTA());
	     out.println(tta);
	     
	     out.println("Totaldle: "+entity.getTotalIdle());
	     out.flush();
	     
	     out.println("PHET: "+String.valueOf(entity.PHET())+"/"+entity.getV());
	     out.println("WEIGHT: "+String.valueOf(entity.avgCongestion()));
	     for(int i =0;i<entity.getDF().size();i++)
	     {
	    	 out.print(String.valueOf(entity.getDF().get(i)));
	     }
	     out.flush();
	     
	   }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
