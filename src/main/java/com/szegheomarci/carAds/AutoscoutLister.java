package com.szegheomarci.carAds;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.szegheomarci.carAds.model.Car;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AutoscoutLister {
	private ArrayList<Car> results;
	
	public ArrayList<Car> Lister(){
		//Maximum 400 results returned for a search
		results = new ArrayList<Car>();

		try {
			AppConfig config = new AppConfig();
			List<String> asUrls = config.getAutoscouturls();
			int i = 0;
			while (i < asUrls.size()) {
				String url = asUrls.get(i);
				System.out.println("fetch: " + url);
				Parser(url,1,"URL[]");
				i++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("config file with urls not found");
		}

		return results;
	}
	
	private void Parser(String url, Integer page, String alias){
		String newURL = url + page;
		Document doc = loadPage(newURL);

		Integer numResults=0;
		try {//<div class="ListHeader_top__jY34N" style="border-bottom:0"><h1><span><span>79</span> <span>Offers</span></span> <b
			//numResults = Integer.valueOf(doc.select("span[class=css-1efi8gv]").first().text().replaceAll("[^\\d.]", ""));
			numResults = Integer.valueOf(doc.select("div.ListHeader_top__jY34N").first().selectFirst("h1").selectFirst("span").text().replaceAll("[^\\d.]", ""));
			/*nextdata = doc.html();
			nextdata = nextdata.substring(nextdata.indexOf("search_numberOfArticles")+32);
			nextdata = nextdata.substring(nextdata.indexOf(";")+1);
			nextdata = nextdata.substring(0,nextdata.indexOf("&"));
			numResults = Integer.valueOf(nextdata);*/
		} catch (Exception e) {
			//System.out.println(doc.html());
			System.out.println("Error trying to read number of results in page " + newURL);
			System.out.println("Trying again..." );
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				doc = loadPage(newURL);
				numResults = Integer.valueOf(doc.select("span[class=css-1efi8gv]").first().text().replaceAll("[^\\d.]", ""));
				//numResults = Integer.valueOf(doc.select("span[class=cl-filters-summary-counter]").first().text().replaceAll("[^\\d.]", ""));	
				/*nextdata = doc.selectFirst("script#__NEXT_DATA__").html();
				nextdata = nextdata.substring(nextdata.indexOf("numberOfResults")+17);
				nextdata = nextdata.substring(0,nextdata.indexOf(","));
				numResults = Integer.valueOf(nextdata);		*/		
			} catch (Exception e2) {
				System.out.println("Error trying to read number of results in page " + newURL);
				System.out.println("Trying for the third time..." );
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					doc = loadPage(newURL);
					numResults = Integer.valueOf(doc.select("span[class=css-1efi8gv]").first().text().replaceAll("[^\\d.]", ""));
					//numResults = Integer.valueOf(doc.select("span[class=cl-filters-summary-counter]").first().text().replaceAll("[^\\d.]", ""));
					/*nextdata = doc.selectFirst("script#__NEXT_DATA__").html();
					nextdata = nextdata.substring(nextdata.indexOf("numberOfResults")+17);
					nextdata = nextdata.substring(0,nextdata.indexOf(","));
					numResults = Integer.valueOf(nextdata);				*/		
				} catch (Exception e3) {
					System.out.println("Error trying to read number of results in page " + newURL);
					System.out.println("Giving up." );	
				}
			}
		}
		if (numResults==0) {System.out.println(alias + ": 0"); return;}
		System.out.println(numResults + " found, processing page" + page);
				
		//Elements resultItems = doc.select("div.cl-list-element");
		//Elements resultItems = doc.select("div.emtvtq414");
		Elements resultItems = doc.select("article.cldt-summary-full-item");
	for(Element item : resultItems) {
		try {
	    //if (! item.hasAttr("data-guid")) {continue;}
	    Car car = new Car();
		//id
		//car.setId(item.attr("data-guid"));
		car.setId(item.attr("id"));
		//System.out.println(car.getId());
		car.setUrl(item.selectFirst("a").attr("href"));
		//ad block
		Document ad = Jsoup.parseBodyFragment(item.html());
		//System.out.println("ad: " + ad.text());
			//title
			//car.setTitle(ad.select("h2.cldt-summary-makemodel").first().text());
			car.setTitle(ad.select("h2").first().text());
			//subtitle
			//ad.select("h2").next();
			//car.setSubtitle(ad.select("h2").text());
			//subtitle
			car.setSubtitle(ad.select("span").first().text());
			//car.setTitle(ad.select("h2.cldt-summary-version").first().text() + "");
			//price
			//car.setPrice(ad.select("span.cldt-price").first().text());
			//car.setPrice(ad.select("span.css-113e8xo").first().text());
			//car.setPrice(ad.select("span.css-1b5kt8d").first().text());
			//car.setPrice(ad.select("p[data-testid=\"regular-price\"]").first().text());
			car.setPrice(ad.select("p.Price_price__WZayw").first().text());
			//Document details = Jsoup.parseBodyFragment(item.select("div.VehicleDetailTableNewDesign_container__wH_QF").first().html());
			Document details = Jsoup.parseBodyFragment(item.select("div.VehicleDetailTable_container__mUUbY").first().html());
			Elements listItems = details.getElementsByTag("span");
			Iterator<Element> listIterator = listItems.iterator();
				//odo
				car.setOdometer(listIterator.next().text());
				//transmission
			    listIterator.next();
				//DoP
				car.setProductionDate(listIterator.next().text());
				//fuel
			    listIterator.next();
				//engine
				//listItems.next();
				car.setEngine(listIterator.next().text());	
            //odo
		    //car.setOdometer(ad.select("li[data-type=mileage]").first().text());
		    //car.setOdometer(ad.select("span[type=mileage]").first().text());
			//DoP
			//car.setProductionDate(ad.select("li[data-type=first-registration]").first().text());
			//car.setProductionDate(ad.select("span[type=registrationDate]").first().text());
			//engine
			//car.setEngine(ad.select("span[type=\"power\"]").first().text());	
			//Location
			/*Elements dealerdata = item.select("div.styles_wrapper__eew08");
			if(!dealerdata.isEmpty()){
				car.setDealer(dealerdata.first().select("span.css-1nd898p").text() + "");
				car.setZipCity(dealerdata.first().select("span").last().text() + "");
			} else {
				car.setDealer("private");
				car.setZipCity(ad.select("span.styles_private__mUdme").first().text().substring(16));
			}*/


			Elements dealerdata = item.select("div.SellerInfo_wrapper__mLtya");
			if(!dealerdata.isEmpty()){
				car.setDealer(dealerdata.first().select("span.SellerInfo_name__yjUE6").text() + "");
				car.setZipCity(dealerdata.first().select("span").last().text() + "");
			} else {
				car.setDealer("private");
				car.setZipCity(ad.select("span.SellerInfo_private__JCxcm").first().text().substring(16));
			}
			
			/*
			Element sellerInfo = item.select("div.css-r5sw3y").first();
			car.setDealer(sellerInfo.select("span.css-2kn1no").text() + "");
			if (car.getDealer().isEmpty()) {
				car.setDealer("private");
			}
			car.setZipCity(sellerInfo.select("span").last().text() + "");
			*/
			/*
			Document details = Jsoup.parseBodyFragment(ad.select("ul[data-item-name=vehicle-details]").html());
			Elements listItems = details.getElementsByTag("li");
			Iterator<Element> listIterator = listItems.iterator();
				//odo
				car.setOdometer(listIterator.next().text());
				//DoP
				car.setProductionDate(listIterator.next().text());
				//engine
				listItems.next();
				car.setEngine(listIterator.next().text());
			//Location
			car.setCountry(item.select("span[class=cldf-summary-seller-contact-country]").text() + "");
			car.setDealer(item.select("div[class=cldf-summary-seller-company-name]").text() + "");
			if (car.getCountry().isEmpty()) {
				car.setCountry(item.select("span[class=cldt-summary-seller-contact-country]").text()); 
				car.setDealer("private");
			}
			car.setZipCity(item.select("span[class=cldf-summary-seller-contact-zip-city]").text() + "");
			if (car.getZipCity().isEmpty()) {car.setZipCity(item.select("span[class=cldt-summary-seller-contact-zip-city]").text()); }
			*/
		//System.out.println(car.getId());
		results.add(car);
		
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Ad failure in " + alias + " page " + page);
			System.out.println(item.html());
		} 
	}	
		
		/*
		for(Element item : resultItems) {
			try {
			Car car = new Car();
			//id
			car.setId(item.attr("data-guid"));
			car.setUrl(item.selectFirst("a[data-item-name=detail-page-link]").attr("href"));
			//ad block
			Document ad = Jsoup.parseBodyFragment(item.html());
			//System.out.println("ad: " + ad.text());
				//title
				car.setTitle(ad.select("h2").first().text());
				//subtitle
				ad.select("h2").next();
				car.setSubtitle(ad.select("h2").text());
				//price
				String price = ad.select("span[data-item-name=price]").first().text();
				car.setPrice(price);
				//details block
				Document details = Jsoup.parseBodyFragment(ad.select("ul[data-item-name=vehicle-details]").html());
				Elements listItems = details.getElementsByTag("li");
				Iterator<Element> listIterator = listItems.iterator();
					//odo
					car.setOdometer(listIterator.next().text());
					//DoP
					car.setProductionDate(listIterator.next().text());
					//engine
					listItems.next();
					car.setEngine(listIterator.next().text());
				//Location
				car.setCountry(item.select("span[class=cldf-summary-seller-contact-country]").text() + "");
				car.setDealer(item.select("div[class=cldf-summary-seller-company-name]").text() + "");
				if (car.getCountry().isEmpty()) {
					car.setCountry(item.select("span[class=cldt-summary-seller-contact-country]").text()); 
					car.setDealer("private");
				}
				car.setZipCity(item.select("span[class=cldf-summary-seller-contact-zip-city]").text() + "");
				if (car.getZipCity().isEmpty()) {car.setZipCity(item.select("span[class=cldt-summary-seller-contact-zip-city]").text()); }
					
			//System.out.println(car.getId());
			results.add(car);
			
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Ad failure in " + alias + " page " + page);
			} 
		}	*/
		Integer loops = numResults / 20;
		if(numResults % 20 > 0) {loops++;}
		if (page < loops) {
			page++;
			Parser(url,page,alias);
		}
	}
	
	private Document loadPage(String url) {

		Document doc = null;
		
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed getting page " + url);
			System.out.println("Trying again...");
			try {
				doc = Jsoup.connect(url).get();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				System.out.println("Failed getting page " + url);
				System.out.println("Trying for the second time...");
				try {
					doc = Jsoup.connect(url).get();
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
					System.out.println("Failed getting page " + url);
					System.out.println("Giving up.");
					//return;
				}
			}
		}
		return doc;
	}

}
