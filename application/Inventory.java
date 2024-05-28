package application;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class Inventory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	static HashMap<Thneed, Integer> thneedList;
	private Date arrivalDate;
	private Date estimatedDate;
	private String size;
	private String color;
	/**
	 * Constructor for the inventory class
	 * @param thneedList
	 */
	public Inventory(HashMap<Thneed, Integer> thneedList) {
		super();
		this.thneedList = thneedList;
		this.arrivalDate = new Date();
		
	}
	/**
	 * return the current instance of thneedList
	 * @return thneedList
	 */
	public static HashMap<Thneed, Integer> getThneedList() {
		return thneedList;
	}
	/**
	 * 
	 * @return size of thneed.
	 */
	public String getSize() {
		return size;
	}
	/**
	 * 
	 * @return color of thneed
	 */
	public String getColor() {
		return color;
	}
	/**
	 * 
	 * @return arrivalDate of thneeds
	 */
	public Date getArrivalDate() {
		return arrivalDate;
	}
	/**
	 * set the variable arrivalDate to the given Date arrivalDate
	 * @param arrivalDate
	 */
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getEstimatedDate() {
		return estimatedDate;
	}
	/**
	 * 
	 * @param estimatedDate
	 */
	public void setestimatedDate(Date estimatedDate) {
		this.estimatedDate = estimatedDate;
	}

	/**
	 * takes in a thneed that needs subtracted from and a value to subtract 
	 * and subtracts it from the given key value pairing in the hashmap.
	 * @param newVal
	 * @param thneed
	 */
	public void decreaseQuantity(Integer newVal, Thneed thneed) {
		System.out.println(newVal);
		System.out.println(thneed);
		for (Thneed invthneed : thneedList.keySet()) {
			if (invthneed.toString().equals(thneed.toString())) {
				Integer currentQuantity = thneedList.get(invthneed);
		        Integer newQuantity = currentQuantity - newVal;
		        thneedList.replace(invthneed, newQuantity);
		        System.out.println("The quantity was subtracted.");
		        System.out.println(thneedList);
		        break;
		    } else {
		        System.out.println("Thneed not found in inventory.");
		    }
		}   
	}
}
