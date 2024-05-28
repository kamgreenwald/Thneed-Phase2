package application;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A Customer represents a single user that can interact with the Thneeds order system.
 */
public class Customer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int customerId;
	private String name;
	private String address;
	private String phoneNumber;
	private ArrayList<Order> orderList;
	
	/**
	 * Constructs a new Customer
	 * @param customerId a unique customer ID 
	 * @param name customer's name
	 * @param address customer's home address
	 * @param phoneNumber customer's phone number
	 * @param orderList an ArrayList of orders placed under the customer's name
	 */
	public Customer(int customerId, String name, String address, String phoneNumber, ArrayList<Order> orderList) {
		this.customerId = customerId;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.orderList = orderList;
	}

	/** Getters and Setters **/
	
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public ArrayList<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(ArrayList<Order> orderList) {
		this.orderList = orderList;
	}

	/**
	 * Determines whether this Customer is equal to another given Customer
	 * @param other another Customer
	 * @return true if the two Customers are equal. Otherwise, false.
	 */
	public boolean equals(Customer other) {
		boolean name = false;
		boolean address = false;
		boolean phoneNo = false;
		if (other.getName().replaceAll("\\s+", "").equalsIgnoreCase(getName().replaceAll("\\s+", "")))
			name = true;
		if (other.getAddress().replaceAll("\\s+", "").equalsIgnoreCase(getAddress().replaceAll("\\s+", "")))
			address = true;
		if (other.getPhoneNumber().replaceAll("\\s+", "").equalsIgnoreCase(getPhoneNumber().replaceAll("\\s+", "")))
			phoneNo = true;
		return name && address && phoneNo;
	}
	public String toString() {
		return this.getName();
	}
}
