package application;

import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import application.Thneed;
import application.Customer;
import application.Order;
import org.junit.Test;

public class Tests {
	
	// Sample Thneeds
	private Thneed thneed1 = new Thneed("small", "blue");
	private Thneed thneed2 = new Thneed("medium", "red");
	private Thneed thneed3 = new Thneed("large", "yellow");
	
	// Sample Order item lists
	private HashMap<Thneed, Integer> itemList1 = new HashMap<Thneed, Integer>();
	private HashMap<Thneed, Integer> itemList2 = new HashMap<Thneed, Integer>() {{
		put(thneed1, 1);
	}};
	private HashMap<Thneed, Integer> itemList3 = new HashMap<Thneed, Integer>() {{
		put(thneed1, 1);
		put(thneed2, 2);
		put(thneed3, 3);
	}};
	
	// Sample Customers
	private Customer customer1 = new Customer(1, "Carter", "123 N. Wall St.", "(555) 555-5555", new ArrayList<>());
	private Customer customer2 = new Customer(2, "Sam", "2468 S. Main Ave.", "(123) 456-7890", new ArrayList<>());
	private Customer customer3 = new Customer(3, "Ade", "555 E. Sample St.", "(098) 765-4321", new ArrayList<>());
	
	// Sample Orders
	private Order order1 = new Order(1, itemList1, customer1);
	private Order order2 = new Order(2, itemList2, customer2);
	private Order order3 = new Order(3, itemList3, customer3);

	@Test
	public void testThneeds() {
		assertEquals("small", thneed1.getSize());
		assertEquals("blue", thneed1.getColor());
	}
	
	@Test
	public void testOrder() {
		assertEquals(1, order1.getOrderNumber());
		assertEquals(2, order2.getOrderNumber());
		assertEquals(3, order3.getOrderNumber());
		
		assertEquals(itemList1, order1.getThneedList());
		assertEquals(itemList2, order2.getThneedList());
		assertEquals(itemList3, order3.getThneedList());
		
		assertEquals(customer1, order1.getCustomer());
		assertEquals(customer2, order2.getCustomer());
		assertEquals(customer3, order3.getCustomer());
		
		assertEquals(new Date().toString(), order1.getDateOrdered().toString());
		
		assertNull(order1.getDateFilled());
		
		order1.setDateFilled(new Date());
		assertEquals(new Date().toString(), order1.getDateFilled().toString());
		
		assertEquals("Order #001: Carter (0 thneeds)", order1.toString());
		assertEquals("Order #002: Sam (1 thneeds)", order2.toString());
		assertEquals("Order #003: Ade (6 thneeds)", order3.toString());
	}
	// Can't test inventory without running the program because the controllers need to be initialized for the inventory to exist\
	// and I can't loadInventory without the controllers.
}
