package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Home page controller
 */
public class CustomerHomeController implements Initializable {
	
	@FXML
	private AnchorPane pane;
	
	@FXML
	private Label userNameLabel;
	
	@FXML
	private Label userIdLabel;
	
	@FXML
	private Label displayedCustomerLabel;
	
	@FXML
	private Label errorLabel;
	
	@FXML
	private Button placeNewOrderButton;
	
	@FXML
	private Button updateOrderButton;
	
	@FXML
	private Button fillOrderButton;
	
	@FXML
	private Button logoutButton;
	
	@FXML
	private TextArea orderDisplayArea;
	
	@FXML
	private ListView<String> orderHistoryList;
	
	@FXML
	private Button createReport;
	
	static HashMap<Thneed, Integer> inventoryItems = Inventory.thneedList;
	private Stage stage;
	private Customer user;
	private CustomerLoginController customerLoginController;
	private EditOrdersController editOrdersController;
	private InventoryController inventoryController;
	private ArrayList<Order> orders = new ArrayList<>();
	private Order displayedOrder = null;
	
	/**
	 * Sets the current user
	 * @param c the customer currently signed in to the system
	 */
	public void setCustomer(Customer c) {
		user = c;
		for (Order o : orders) {
			if (o.getCustomer().equals(user)) 
				user.getOrderList().add(o);
		}
	}
	
	/**
	 * Displays the current user's name
	 * @param name the user's name
	 */
	public void setNameField(String name) {
		userNameLabel.setText(name);
	}
	
	/**
	 * Displays the current user's ID
	 * @param id the user's ID
	 */
	public void setIdField(String id) {
		userIdLabel.setText(userIdLabel.getText() + id);
	}
	
	/**
	 * Sets a controller for the login page
	 * @param c the login page controller
	 */
	public void setCallingController(CustomerLoginController c) {
		customerLoginController = c;
	}
	
	/**
	 * Set a controller for the edit orders page
	 * @param c the edit orders page controller
	 */
	public void setCallingController(EditOrdersController c) {
		editOrdersController = c;
	}
	
	/**
	 * Set a controller for the current inventory page
	 * @param c the edit orders page controller
	 */
	public void setCallingController(InventoryController c) {
		inventoryController = c;
	}
	
	/**
	 * Saves and adds the given order to the system
	 * @param o the order to be added
	 */
	public void addOrder(Order o) {
		user.getOrderList().add(o);
		orders.add(o);
		orderHistoryList.getItems().add(o.toString());
		saveOrders();
		displayOrder(o);
	}
	
	/**
	 * Saves and updates the given order in the system
	 * @param oldOrder the order to be updated
	 * @param newOrder the newly updated order
	 */
	private void updateOrder(Order oldOrder, Order newOrder) {
		newOrder.setDateFilled(null);
		user.getOrderList().set(user.getOrderList().indexOf(oldOrder), newOrder);
		orders.set(orders.indexOf(oldOrder), newOrder);
		orderHistoryList.getItems().set(orderHistoryList.getItems().indexOf(oldOrder.toString()), newOrder.toString());
		saveOrders();
		displayOrder(newOrder);
	}
	
	/**
	 * Displays the given order on the page's top text panel
	 * @param o the order to be displayed
	 */
	private void displayOrder(Order o) {
		String s = String.format("\n\nOrder #%03d\n\n", o.getOrderNumber());
		displayedCustomerLabel.setText("Customer: " + o.getCustomer().getName());
		displayedCustomerLabel.setVisible(true);
		for(Map.Entry<Thneed, Integer> entry : o.getThneedList().entrySet()) {
			String thneed = entry.getKey().getSize() + " " + entry.getKey().getColor() + " thneed";
			s += String.format("%02d %-25.25s\n", entry.getValue(), thneed);
		}
		s += "\nOrdered: " + o.getDateOrdered() + "\n";
		if (o.getDateFilled() == null)
			s += "Status: Not filled\nProjected Fill Date: " + o.getprojectedDateFilled();
		else
			s += "Status: Filled on " + o.getDateFilled();
		orderDisplayArea.setText(s);
		displayedOrder = o;
	}
	
	/**
	 * Loads orders from an order data file
	 */
	private void loadOrders() {
		File file = new File(CustomerLoginController.ORDERS_FILEPATH);
		try {
			file.createNewFile();
			BufferedReader br = new BufferedReader(new FileReader(CustomerLoginController.ORDERS_FILEPATH));
			if (br.readLine() != null) {
				FileInputStream fis = new FileInputStream(CustomerLoginController.ORDERS_FILEPATH);
				ObjectInputStream input = new ObjectInputStream(fis);
				orders = (ArrayList<Order>) input.readObject();
				fis.close();
				input.close();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Order o : orders) {
			orderHistoryList.getItems().add(o.toString()); 
		}
	}
	
	/**
	 * Saves all orders to an order data file
	 */
	private void saveOrders() {
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(CustomerLoginController.ORDERS_FILEPATH));
			output.writeObject(orders);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Logs the current user out and returns to the login page
	 * @param event on-click event for "Logout" button
	 */
	@FXML
	private void onLogoutButtonClick(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/CustomerLogin.fxml"));
		AnchorPane root;
		
		try {
			root = (AnchorPane) loader.load();
			Scene scene = new Scene(root);
			stage = new Stage();
			stage.setScene(scene);
			customerLoginController = (CustomerLoginController) loader.getController();
			customerLoginController.setCallingController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		customerLoginController.setErrorVisibility(false);
		stage.show();
		pane.getScene().getWindow().hide();
	}
	
	/**
	 * Initializes and displays a new window where the user can place a new order
	 * @param event on-click event for "Place New Order" button
	 */
	@FXML
	private void onPlaceNewOrderButtonClick(ActionEvent event) {
		errorLabel.setVisible(false);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditOrders.fxml"));
		AnchorPane root;
		
		try {
			root = (AnchorPane) loader.load();
			Scene scene = new Scene(root);
			stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Place New Order");
			editOrdersController = (EditOrdersController) loader.getController();
			editOrdersController.setCallingController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		editOrdersController.setTitle("Place New Order");
		editOrdersController.setConfirmButtonText("Place Order");
		editOrdersController.setErrorVisibility(false);
		editOrdersController.setOrderNumber(orders.size() + 1);
		editOrdersController.setCustomer(user);
		stage.showAndWait();
		if (editOrdersController.getOrder() != null)
			addOrder(editOrdersController.getOrder());
	}
	
	/**
	 * Initializes and displays a new window where the user can update the selected order
	 * @param event on-click event for "Update Order" button
	 */
	@FXML
	private void onUpdateOrderButtonClick(ActionEvent event) {
		if (displayedOrder != null) {
			errorLabel.setVisible(false);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditOrders.fxml"));
			AnchorPane root;
			
			try {
				root = (AnchorPane) loader.load();
				Scene scene = new Scene(root);
				stage = new Stage();
				stage.setScene(scene);
				stage.setTitle("Update Order");
				editOrdersController = (EditOrdersController) loader.getController();
				editOrdersController.setCallingController(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			editOrdersController.setErrorVisibility(false);
			editOrdersController.setTitle(String.format("Update Order #%03d", displayedOrder.getOrderNumber()));
			editOrdersController.setConfirmButtonText("Update Order");
			editOrdersController.setErrorVisibility(false);
			editOrdersController.setOrderNumber(displayedOrder.getOrderNumber());
			editOrdersController.setCustomer(user);
			stage.showAndWait();
			if (editOrdersController.getOrder() != null)
				updateOrder(displayedOrder, editOrdersController.getOrder());
		} else
			errorLabel.setVisible(true);
	}
	
	/**
	 * Fills the currently displayed order
	 * @param event on-click event for "Fill Order" button
	 */
	@FXML
	private void onFillOrderButtonClick(ActionEvent event) {
		if (displayedOrder != null) {
			errorLabel.setVisible(false);
			Order o = orders.get(orders.indexOf(displayedOrder));
			o.setDateFilled(new Date());
			Inventory inventory= InventoryController.getInventory();
			HashMap<Thneed, Integer> thneedList = o.getThneedList();
			for (Map.Entry<Thneed, Integer> entry : thneedList.entrySet()) {
	            Thneed thneed = entry.getKey();
	            Integer quantityFilled = entry.getValue();
	            inventory.decreaseQuantity(quantityFilled, thneed);
	        }
//			inventoryItems = Inventory.thneedList;
			inventoryController.saveInventory();
			inventoryController.loadInventory();
			orders.set(orders.indexOf(displayedOrder), o);
			saveOrders();
			displayOrder(o);
		} else
			errorLabel.setVisible(true);
	}
	
	/**
	 * Displays an alert window containing information about the given customer
	 * @param event on-click event for the underlined customer label of the currently displayed order
	 */
	
	@FXML
	private void onAddtoInventory(ActionEvent event) {
		errorLabel.setVisible(false);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inventory.fxml"));
		AnchorPane root;
		
		try {
			root = (AnchorPane) loader.load();
			Scene scene = new Scene(root);
			stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Current Inventory");
			inventoryController = (InventoryController) loader.getController();
			inventoryController.setCallingController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.showAndWait();
	}
	/**
	 * Displays a pop up window with the customer's information when you click on their name.
	 * @param event
	 */
	@FXML
	private void onCustomerNameLabelClick(MouseEvent event) {
		if (displayedOrder != null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Customer c = displayedOrder.getCustomer();
			alert.setTitle("Customer Info");
			alert.setHeaderText(CustomerLoginController.formatName(displayedOrder.getCustomer().getName()));
			String context = String.format("Customer ID: #%03d", c.getCustomerId());
			context += "\nAddress: " + c.getAddress();
			context += "\nPhone Number: " + c.getPhoneNumber();
			context += "\nNumber of Orders: " + c.getOrderList().size();
			alert.setContentText(context);
			alert.showAndWait();
		}
	}
	/**
	 * When the create report button is clicked on the CustomerHome page the code goes through the inventory, orders, and customers 
	 * to find the most and least popular thneeds, the items on back order, the average time to fill an order, and our best customer.
	 * @param event
	 */
	@FXML
	public void createReportButtonClick(ActionEvent event) {
		final String ORDERS_FILEPATH = "Orders.txt";
		double avg = 0;
		HashMap<String, Integer> count = new HashMap<>();
		int max = Integer.MIN_VALUE;
		int thneed_max = Integer.MIN_VALUE;
		int thneed_min = Integer.MAX_VALUE;
		String top_customer = "";
		String pop_thneed = "";
		String lpop_thneed = ";";
	try {
		FileWriter write = new FileWriter("Report " + LocalDate.now()+".txt");
		FileInputStream fis = new FileInputStream(ORDERS_FILEPATH);
		ObjectInputStream input = new ObjectInputStream(fis);
		
		try {
			@SuppressWarnings("unchecked")
			ArrayList<Order> orders = (ArrayList<Order>) input.readObject();
			int filled = 0;
			for (Order o : orders) {
				if (o.getDateFilled() != null) {
					filled++;
					Date d1 = (o.getDateOrdered());
		            Date d2 = (o.getDateFilled());
		            avg += (Math.ceil((d2.getTime() - d1.getTime())/86400000.0)); 
				}
				for (Thneed key : o.getThneedList().keySet()) {
					if (o.getThneedList().get(key) > thneed_max) {
						thneed_max = o.getThneedList().get(key);
						pop_thneed = key.toString();
					}
					if (o.getThneedList().get(key)< thneed_min) {
						thneed_min = o.getThneedList().get(key);
						lpop_thneed = key.toString();
					}
				}
				
				for (Thneed key : o.getThneedList().keySet()) {
					if (!count.containsKey(key)) {
						count.put(o.getCustomer().toString(), o.getThneedList().get(key));
					}
					else if (count.containsKey(key)) {
						count.put(o.getCustomer().toString(), o.getThneedList().get(key)+count.get(key));
					}
				}
				
				
			}
			for (String s : count.keySet()) {
				if (count.get(s) > max) {
					max = count.get(s);
					top_customer = s;
				}
			}
			ArrayList<Thneed> backordered = new ArrayList<>();
			inventoryItems = Inventory.thneedList;
			for (Thneed t : inventoryItems.keySet()) {
				if (inventoryItems.get(t) < 0) {
					backordered.add(t);
				}
			}
			avg/=filled;
			write.write("The average time to fill an order is " + avg + " days.\n");
			write.write("The most popular thneed is " + pop_thneed + ".\n");
			write.write("The least popular thneed is " + lpop_thneed + ".\n");
			write.write("The top customer is " + top_customer + ".\n");
			write.write("The backordered item(s) is/are " + backordered.toString().substring(1, backordered.toString().length()-1) + "thneed(s).");
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		write.close();
		System.out.println("report created.");
		
	}
	catch(IOException e) {
		e.printStackTrace();
	}
	
	}
	
	/**
	 * Initializes the home page
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// Hide error messages
		errorLabel.setVisible(false);
		
		// Create ListView item handler
		orderHistoryList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String ref, String value) {
		    	for (Order o : orders) {
		    		String[] orderData = value.split(" ");
		    		int orderNumber = Integer.parseInt(orderData[1].substring(1, orderData[1].length() - 1));		
		    		if (o.getOrderNumber() == orderNumber)
		    			displayOrder(o);
		    	}
		    }
		});
		
		// Load order history
		loadOrders(); 
	}
}
