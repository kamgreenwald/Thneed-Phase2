package application;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * An Order represents a Customer's order in the Thneeds order system.
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int orderNumber;
	private HashMap<Thneed, Integer> thneedList;
	private Customer customer;
	private Date dateOrdered;
	private Date dateFilled;
	private Date projectedDateFilled;
	
	/**
	 * Construct a new Order
	 * @param orderNumber a unique order ID
	 * @param thneedList a HashMap containing the Order's Thneeds and their respective quantities
	 * @param customer the Customer that placed the Order
	 */
	public Order(int orderNumber, HashMap<Thneed, Integer> thneedList, Customer customer) {
		super();
		this.orderNumber = orderNumber;
		this.thneedList = thneedList;
		this.customer = customer;
		this.dateOrdered = new Date();
		this.projectedDateFilled = new Date(dateOrdered.getTime() + 15 * 60000);
	}
	
	/** Getters and Setters **/
	
	public int getOrderNumber() {
		return orderNumber;
	}
	
	public HashMap<Thneed, Integer> getThneedList() {
		return thneedList;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public Date getDateOrdered() {
		return dateOrdered;
	}
	
	public void setDateOrdered(Date date) {
		this.dateOrdered = date;
	}
	
	public Date getDateFilled() {
		return dateFilled;
	}
	
	public void setDateFilled(Date dateFilled) {
		this.dateFilled = dateFilled;
	}
	
	public Date getprojectedDateFilled() {
		return projectedDateFilled;
	}
	
	public void setprojectedDateFilled(Date projectedDateFilled) {
		this.projectedDateFilled = projectedDateFilled;
	}
	
	/**
	 * Converts the Order to a string.
	 * @return a string representation of the Order
	 */
	@Override
	public String toString() {
		int size = 0;
		for (Integer i : thneedList.values())
			size += i;
		return String.format("Order #%03d: " + customer.getName() + " (" + size + " thneeds)", orderNumber);
	}
}
