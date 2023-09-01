package com.szegheomarci.carAds.controller.datasource;

import com.szegheomarci.carAds.model.Car;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AutoscoutReader extends DatasourceReader {

    private final List<String> urls;
    private final int maxRetries = 3;

    public AutoscoutReader(String config) {
        super(config);
        this.urls = (List<String>) datasource.get("urls");
    }

    @Override
    public void readSource() {
        Document page;

        // iterate through the url list
        for (String url: urls) {
            System.out.println("Fetching: " + url);
            int numResults = 0;
            int pagenum = 1;

            // read the first page to get the number of search results
            int retryCount = 0;
            boolean success = false;
            while (retryCount < maxRetries && !success) {
                page = readPage(url + pagenum);
                try {
                    numResults = Integer.valueOf(page.select("div.ListHeader_top__jY34N").first().selectFirst("h1").selectFirst("span").text().replaceAll("[^\\d.]", ""));
                    success = true;
                } catch (NullPointerException e) {
                    retryCount++;
                    if (retryCount < maxRetries) {
                        System.out.println("Error reading number of results. Retry number " + retryCount);
                    } else {
                        System.out.println("Max retry attempts reached.");
                        throw new RuntimeException(e);
                    }
                }
            }
            System.out.println("Found " + numResults);
            System.out.println("Processing page " + pagenum);
            processPage(readPage(url + pagenum));

            int loops = numResults / 20;
            if(numResults % 20 > 0) {loops++;}
            while (pagenum < loops) {
                pagenum++;
                System.out.println("Processing page " + pagenum);
                processPage(readPage(url + pagenum));
            }
        }
    }

    private Document readPage(String url) {
        Document document = null;
        int retryCount = 0;
        boolean success = false;

        while (retryCount < maxRetries && !success) {
            try {
                document = Jsoup.connect(url).get();
                success = true;
            } catch (IOException e) {
                retryCount++;
                if (retryCount < maxRetries) {
                    System.out.println("Error reading page. Retry number " + retryCount);
                } else {
                    System.out.println("Max retry attempts reached.");
                    throw new RuntimeException(e);
                }
            }
        }
        return document;
    }

    private void processPage(Document document) {
        Elements resultItems = document.select("article.cldt-summary-full-item");
        for(Element item : resultItems) {
            try {
                Car car = new Car();
                //id
                car.setId(item.attr("id"));
                car.setUrl(item.selectFirst("a").attr("href"));

                //ad block
                Document ad = Jsoup.parseBodyFragment(item.html());
                //title
                car.setTitle(ad.select("h2").first().text());
                //subtitle
                car.setSubtitle(ad.select("span").first().text());
                //price
                car.setPrice(ad.select("p.Price_price__WZayw").first().text());

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
                car.setEngine(listIterator.next().text());

                Elements dealerdata = item.select("div.SellerInfo_wrapper__mLtya");
                if(!dealerdata.isEmpty()){
                    car.setDealer(dealerdata.first().select("span.SellerInfo_name__yjUE6").text() + "");
                    car.setZipCity(dealerdata.first().select("span").last().text() + "");
                } else {
                    car.setDealer("private");
                    car.setZipCity(ad.select("span.SellerInfo_private__JCxcm").first().text().substring(16));
                }
                results.add(car);
                //System.out.println(car.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Ad failure in page");
                System.out.println(item.html());
            }
        }
    }


}
