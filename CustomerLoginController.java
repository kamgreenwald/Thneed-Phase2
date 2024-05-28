package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Login page controller
 */
public class CustomerLoginController implements Initializable {
	
	protected static final String CUSTOMERS_FILEPATH = "Customers.txt";
	protected static final String ORDERS_FILEPATH = "Orders.txt";
	protected static final String INVENTORY_FILEPATH = "Inventory.txt";
	
	@FXML
	private AnchorPane pane;
	
	@FXML
	private TextField newNameField;
	
	@FXML
	private TextField newAddressField;
	
	@FXML
	private TextField newPhoneField;
	
	@FXML
	private TextField existingNameField;
	
	@FXML
	private TextField existingIdField;
	
	@FXML
	private Button registerButton;
	
	@FXML
	private Button loginButton;
	
	@FXML
	private Label errorInvalidRegister;
	
	@FXML
	private Label errorExistingUser;
	
	@FXML
	private Label errorInvalidLogin;
	
	private Stage stage;
	private Customer user;
	private ArrayList<Customer> customers = new ArrayList<>();
	private CustomerHomeController customerHomeController;
	
	/**
	 * Capitalizes the first letter of each word in the given string
	 * @param name the name to be formatted
	 * @return a formatted string
	 */
	public static String formatName(String name) {
		String formattedName = "";
		for (String s : name.split(" "))
			formattedName +=  s.substring(0, 1).toUpperCase() + s.substring(1) + " ";
		return formattedName;
	}
	
	/**
	 * Sets a controller for the home page
	 * @param c the home page controller
	 */
	public void setCallingController(CustomerHomeController c) {
		customerHomeController = c;
	}
	
	/**
	 * Toggles the visibility of error messages on the page
	 * @param b the state of error message visibility
	 */
	public void setErrorVisibility(boolean b) {
		errorInvalidRegister.setVisible(b);
		errorExistingUser.setVisible(b);
		errorInvalidLogin.setVisible(b);
	}
	
	/**
	 * Opens the customer home page
	 */
	private void openHomePage() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/CustomerHome.fxml"));
		AnchorPane root;
		
		try {
			root = (AnchorPane) loader.load();
			Scene scene = new Scene(root);
			stage = new Stage();
			stage.setScene(scene);
			customerHomeController = (CustomerHomeController) loader.getController();
			customerHomeController.setCallingController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		customerHomeController.setNameField(user.getName());
		customerHomeController.setIdField(String.format("%03d", user.getCustomerId()));
		customerHomeController.setCustomer(user);
		stage.show();
		pane.getScene().getWindow().hide();
	}
	
	/**
	 * Finds the customer with the given name and ID if one exists
	 * @param name the customers name
	 * @param id the customers ID
	 * @return the customer with the given name and ID. If none exist, returns null.
	 */
	private Customer getCustomer(String name, int id) {
		for (Customer c : customers) {
			if (c.getName().replaceAll("\\s+", "").equalsIgnoreCase(name.replaceAll("\\s+", "")) && c.getCustomerId() == id)
				return c;
		}
		return null;
	}
	
	/**
	 * Determines if the given customer already exists
	 * @param c the customer to evaluate
	 * @return true if the customer already exists. Otherwise, false.
	 */
	private boolean customerExists(Customer c) {
		for (Customer customer : customers) {
			if (customer.equals(c))
				return true;
		}
		return false;
	}
	
	/**
	 * Creates a new customer ID to ensure each customer's ID is unique
	 * @return a new customer ID
	 */
	private int getNewCustomerId() {
		if (customers.isEmpty())
			return 1;
		return customers.size() + 1;
	}
	
	/**
	 * Loads customers from a customer data file
	 */
	private void loadCustomers() {
		File file = new File(CUSTOMERS_FILEPATH);
		try {
			file.createNewFile();
			BufferedReader br = new BufferedReader(new FileReader(CUSTOMERS_FILEPATH));
			if (br.readLine() != null) {
				FileInputStream fis = new FileInputStream(CUSTOMERS_FILEPATH);
				ObjectInputStream input = new ObjectInputStream(fis);
				customers = (ArrayList<Customer>) input.readObject();
				fis.close();
				input.close();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves all customers to a customer data file
	 */
	private void saveCustomers() {
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(CUSTOMERS_FILEPATH));
			output.writeObject(customers);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Registers a new customer and proceeds to the home page
	 * @param event on-click event for "Register" button
	 */
	@FXML
	private void onRegisterButtonClick(ActionEvent event) {
		if (newNameField.getText().isEmpty() || newAddressField.getText().isEmpty() || newPhoneField.getText().isEmpty())
			errorInvalidRegister.setVisible(true);
		else {
			user = new Customer(getNewCustomerId(), formatName(newNameField.getText()), newAddressField.getText(), newPhoneField.getText(), new ArrayList<>());
			if (!customerExists(user)) {
				customers.add(user);
				saveCustomers();
				openHomePage();
			} else {
				errorExistingUser.setVisible(true);
			}
		}
	}
	
	/**
	 * Loads the returning user with the given credentials and proceeds to the home page
	 * @param event on-click event for "Login" button
	 */
	@FXML
	private void onLoginButtonClick(ActionEvent event) {
		try {
			user = getCustomer(existingNameField.getText(), Integer.parseInt(existingIdField.getText()));
		} catch (Exception e) {
			user = null;
			errorInvalidLogin.setVisible(true);
		}
		if (user != null)
			openHomePage();
		else
			errorInvalidLogin.setVisible(true);
	}

	/**
	 * Initializes the login page
	 */
	@Override
	public void initialize(URL loc, ResourceBundle res) {
		setErrorVisibility(false);
		loadCustomers();
	}

}
