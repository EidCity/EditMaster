
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

public class QueryMaster {

	private WIKI wiki;
	private String url;

	public QueryMaster(WIKI wiki) {
		

		this.wiki=wiki;
	}

	public String generateQuery(QUERY query)
	{
		String query_mostrevisions = "?action=query&format=json&list=querypage&qppage=Mostrevisions&qplimit=10&format=json";
		String query_fewestrevisions = "?action=query&format=json&list=querypage&qppage=Fewestrevisions&qplimit=10&format=json";
		String queryString="NULL";
		if(this.wiki==WIKI.WIKIPEDIA)
		{
			this.url="https://en.wikipedia.org/w/api.php";
		}
		
		else if(this.wiki==WIKI.WIKIDATA)
		{
			this.url="https://wikidata.org/w/api.php";
		}
		
		switch (query)
		{
		case MOSTREVISIONS:
			queryString= this.url+query_mostrevisions;
			
			break;
		case LEASTREVISIONS:
			queryString= this.url+query_fewestrevisions;

			break;
		
		default:
			break;
		
		}

		return queryString;
	}

	public String generateQuery(QUERY query,String title)
	{
		//property: rvstart=2018-07-01T00:00:00Z&rvexcludeuser=SSethi (WMF) (WMF) [try in ApiSandbox]
		String query_revisions_latest= "?action=query&prop=revisions&titles="+title+"&rvlimit=500&rvprop=timestamp"
				+ "|user&format=json";
		String query_all =  "?action=query&prop=revisions&titles="+title+"&rvlimit=1&rvprop=timestamp"
				+ "|user&rvdir=newer&&rvlimit=500&rvstart=2012-07-01T00:00:00Z&format=json";
		String query_general = "?action=query&list=search&srsearch="+title+"&format=json";
		String query_contributors ="?action=query&titles="+title+"&prop=contributors&format=json";
		String query_categories = "?action=query&titles="+title+"&prop=categories&format=json"; 
		String query_Geo = "?action=query&prop=coordinates&titles="+title+"&format=json";
		//api.php?action=wbgetentities&ids=Q42&props=sitelinks [open in sandbox]
		String queryString="NULL";
		//action=query&prop=revisions&titles=Main%20Page&rvlimit=5&rvprop=timestamp|user|comment&rvdir=newer&rvstart=2006-05-01T00:00:00Z
		if(this.wiki==WIKI.WIKIPEDIA)
		{
			this.url="https://en.wikipedia.org/w/api.php";
		}
		
		else if(this.wiki==WIKI.WIKIDATA)
		{
			this.url = "https://www.wikidata.org/w/api.php";
		}
		
		//api.php?action=wbgetentities&ids=Q42&props=revisions&format=json
		switch (query)
		{

		case CATEGORY:
			queryString=this.url+query_categories;
			break;
		case CONTRIBUTORS:
			queryString=this.url+query_contributors;
			break;
		case FIRSTFIVE:
			queryString=this.url+query_revisions_latest;
			break;
		case GEOLOCATION:
			queryString=this.url+query_Geo;
			break;
		
		case ALLREVISIONS:
			queryString=this.url+query_all;
			break;

		default:
			break;
		
		}

		return queryString;
	}
}


