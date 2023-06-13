package com.szegheomarci.carAds.model;


public class Car {
private String id;
private String title;
private String subtitle;
private String url;
private String price;
private String odometer;
private String productionDate;
private String engine;
private String dealer;
private String country = "";
private String zipCity = "";

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getSubtitle() {
	return subtitle;
}

public void setSubtitle(String subtitle) {
	this.subtitle = subtitle;
}

public String getPrice() {
	return price;
}

public void setPrice(String price) {
	//String newPrice = price.replaceAll("[^\\d.]+", "");
	//this.price = Double.valueOf(newPrice);
	this.price = price;
}

public String getOdometer() {
	return odometer;
}

public void setOdometer(String odometer) {
	//String newOdo = odometer.replaceAll("[^\\d.]+", "");
	//this.odometer = Integer.valueOf(newOdo);
	this.odometer = odometer;
}

public String getProductionDate() {
	return productionDate;
}

public void setProductionDate(String productionDate) {
	this.productionDate = productionDate;
}

public String getEngine() {
	return engine;
}

public void setEngine(String engine) {
	this.engine = engine;
}

public String getCountry() {
	return country;
}

public void setCountry(String country) {
	this.country = country;
}

public String toString() {
	String details = this.getTitle().concat(": ");
	details = details.concat(this.getSubtitle()).concat(". Price: ");
	details = details.concat(this.getPrice().toString()).concat(". Odo: ");
	details = details.concat(this.getOdometer().toString()).concat(". DOP: ");
	details = details.concat(this.getProductionDate()).concat(". Engine: ");
	details = details.concat(this.getEngine()).concat(". Location: ");
	details = details.concat(this.getCountry());
	details = details + ". ZIP, City: " + this.getZipCity();	
	return details;
}

public String toText() {
	String sep = "####";
	String details = this.getId() + sep + this.getTitle() + sep;
	details = details + this.getSubtitle() + sep + this.getPrice() + sep;
	details = details + this.getOdometer() + sep + this.getProductionDate() + sep;
	details = details + this.getEngine() + sep + this.getDealer() + sep;
	details = details + this.getCountry() + sep + this.getZipCity() + sep;
	details = details + this.getUrl();
	
	return details;
}
public String getZipCity() {
	return zipCity;
}

public void setZipCity(String zipCity) {
	this.zipCity = zipCity;
}

public String getDealer() {
	return dealer;
}

public void setDealer(String dealer) {
	this.dealer = dealer;
}

public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
}
}

