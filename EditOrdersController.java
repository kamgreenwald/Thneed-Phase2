package application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for "Place New Order" and "Update Order" pages
 */
public class EditOrdersController {
	
	@FXML
	private RadioButton smallRadioButton;
	
	@FXML
	private RadioButton mediumRadioButton;
	
	@FXML
	private RadioButton largeRadioButton;
	
	@FXML
	private RadioButton redRadioButton;
	
	@FXML
	private RadioButton orangeRadioButton;
	
	@FXML
	private RadioButton yellowRadioButton;
	
	@FXML
	private RadioButton greenRadioButton;
	
	@FXML
	private RadioButton blueRadioButton;
	
	@FXML
	private RadioButton purpleRadioButton;
	
	@FXML
	private Label titleLabel;
	
	@FXML
	private Label errorLabel1;
	
	@FXML
	private Label errorLabel2;
	
	@FXML
	private TextField quantityField;
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private Button addToOrderButton;
	
	@FXML
	private Button confirmButton;
	
	@FXML
	private ListView<String> orderList;
	
	@FXML
	private AnchorPane pane;
	
	private int orderNumber;
	private HashMap<Thneed, Integer> orderItems = new HashMap<>();
	private Customer customer;
	private CustomerHomeController customerHomeController;
	private Order order;
	
	/**
	 * Gets the user's order
	 * @return the user's order
	 */
	public Order getOrder() {
		return order;
	}
	
	/**
	 * Sets the page's title
	 * @param title the title to be displayed
	 */
	public void setTitle(String title) {
		titleLabel.setText(title);
		titleLabel.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(titleLabel, 0.0);
		AnchorPane.setRightAnchor(titleLabel, 0.0);
		titleLabel.setAlignment(Pos.CENTER);	
	}
	
	/**
	 * Sets the "Confirm" button's text (i.e. "Place Order" or "Update Order")
	 * @param text the text to be displayed
	 */
	public void setConfirmButtonText(String text) {
		confirmButton.setText(text);
	}
	
	/**
	 * Toggles the visibility of error messages on the page
	 * @param b the state of error message visibility
	 */
	public void setErrorVisibility(boolean b) {
		errorLabel1.setVisible(b);
		errorLabel2.setVisible(b);
	}
	
	/**
	 * Sets the current order number
	 * @param n a unique order number
	 */
	public void setOrderNumber(int n) {
		orderNumber = n;
	}
	
	/**
	 * Set the current user
	 * @param c the customer currently signed into the system
	 */
	public void setCustomer(Customer c) {
		customer = c;
	}
	
	/**
	 * Sets a controller for the home page
	 * @param c the home page controller
	 */
	public void setCallingController(CustomerHomeController c) {
		customerHomeController = c;
	}
	
	/**
	 * Gets the order item based on the user's Thneed size, color, and quantity selections
	 * @return a map containing the user-selected Thneed and its respective quantity
	 */
	private Map<Thneed, Integer> getOrderItem() {
		String size = "";
		String color = "";
		int quantity = 0;
		boolean sizeSelected = true;
		boolean colorSelected = true;
		boolean quantitySelected = true;
		
		// Get Size
		if (smallRadioButton.isSelected())
			size = smallRadioButton.getText();
		else if (mediumRadioButton.isSelected())
			size = mediumRadioButton.getText();
		else if (largeRadioButton.isSelected())
			size = largeRadioButton.getText();
		else
			sizeSelected = false;
		
		// Get Color
		if (redRadioButton.isSelected())
			color = "Red";
		else if (orangeRadioButton.isSelected())
			color = "Orange";
		else if (yellowRadioButton.isSelected())
			color = "Yellow";
		else if (greenRadioButton.isSelected())
			color = "Green";
		else if (blueRadioButton.isSelected())
			color = "Blue";
		else if (purpleRadioButton.isSelected())
			color = "Purple";
		else
			colorSelected = false;
		
		// Get Quantity
		try {
			quantity = Integer.parseInt(quantityField.getText());
		} catch (Exception e) {
			quantitySelected = false;
		}
		
		// Show errors if necessary
		if (!sizeSelected || !colorSelected) {
			errorLabel2.setVisible(true);
			return null;
		}
		
		if (!quantitySelected) {
			errorLabel1.setVisible(true);
			return null;
		} 
		
		Map<Thneed, Integer> result = new HashMap<>();
		result.put(new Thneed(size, color), quantity);
		return result;
	}
	
	/**
	 * Clears all errors and user input
	 */
	private void clearInput() {
		smallRadioButton.setSelected(false);
		mediumRadioButton.setSelected(false);
		largeRadioButton.setSelected(false);
		redRadioButton.setSelected(false);
		orangeRadioButton.setSelected(false);
		yellowRadioButton.setSelected(false);
		greenRadioButton.setSelected(false);
		blueRadioButton.setSelected(false);
		purpleRadioButton.setSelected(false);
		quantityField.setText("");
		setErrorVisibility(false);
	}
	
	/**
	 * Cancels the current operation and returns to the home page
	 * @param event on-click event for "Cancel" button
	 */
	@FXML
	private void onCancelButtonClick(ActionEvent event) {
		order = null;
		pane.getScene().getWindow().hide();
	}
	
	/**
	 * Adds the user-selected item to the order
	 * @param event on-click event for "Add to Order" button
	 */
	@FXML
	private void onAddToOrderButtonClick(ActionEvent event) {
		if (getOrderItem() != null) {
			Map<Thneed, Integer> orderItem = getOrderItem();
			Thneed thneed = (Thneed) orderItem.keySet().toArray()[0];
			int quantity = (int) orderItem.values().toArray()[0];
			orderItems.put(thneed, quantity);
			orderList.getItems().add(thneed.getSize() + " " + thneed.getColor() + " Thneed  (" + quantity + ")");
			clearInput();
		}
	}
	
	/**
	 * Creates a new order using the selected order items.
	 * @param event on-click event for confirmation button ("Place Order" or "Update Order" button)
	 */
	@FXML
	private void onConfirmButtonClick(ActionEvent event) {
		if (orderItems.size() > 0) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/CustomerHome.fxml"));
			
			try {
				loader.load();
				customerHomeController = (CustomerHomeController) loader.getController();
				customerHomeController.setCallingController(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			pane.getScene().getWindow().hide();
		} else {
			errorLabel2.setVisible(true);
		}
		order = new Order(orderNumber, orderItems, customer);
	}
}
