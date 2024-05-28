package application;

import javafx.scene.control.TextField;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import java.util.Comparator;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {
	@FXML
	private RadioButton smallRadioButton;
	@FXML
	private ToggleGroup sizeGroup;
	@FXML
	private RadioButton mediumRadioButton;
	@FXML
	private RadioButton largeRadioButton;
	@FXML
	private RadioButton redRadioButton;
	@FXML
	private ToggleGroup colorGroup;
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
	private TextField quantityField;
	@FXML
	private ListView<String> inventoryDisplayArea;
	
	
	private CustomerHomeController customerHomeController;
	static HashMap<Thneed, Integer> inventoryItems = Inventory.thneedList;
	private static Inventory inventory;
	/**
	 * method that pull the CustomerHomeController into invetory controller.
	 * @param c
	 */
	public void setCallingController(CustomerHomeController c) {
		customerHomeController = c;
	}
	/**
	 * return the inventory as a hashmap if its not null else return null
	 * @return inventory as hashmap or null if null
	 */
	public static Inventory getInventory() {
	    return inventory != null ? new Inventory(new HashMap<>(Inventory.getThneedList())) : null;
	}

	/**
	 * Method that checks what color and size are selected if any and returns the given thneed as a hashmap
	 * @return HashMap<Thneed, Integer> 
	 */
	private Map<Thneed, Integer> getInventoryItem() {
		String size = "";
		String color = "";
		int quantity = 0;
		boolean sizeSelected = true;
		boolean colorSelected = true;
		boolean quantitySelected = true;
		
		if (smallRadioButton.isSelected())
			size = smallRadioButton.getText();
		else if (mediumRadioButton.isSelected())
			size = mediumRadioButton.getText();
		else if (largeRadioButton.isSelected())
			size = largeRadioButton.getText();
		else
			sizeSelected = false;
		
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
		
		try {
			quantity = Integer.parseInt(quantityField.getText());
		} catch (Exception e) {
			quantitySelected = false;
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
	}
	
	/**
	 * When the addToInvetory button is clicked this method calls getInvetoryItem() and adds it to the inventory HashMap 
	 * and also adds it to the display area. 
	 * @param event
	 */
	@FXML
	private void onAddToInventoryButtonClick(ActionEvent event) {
		if (getInventoryItem() != null) {
			Map<Thneed, Integer> InventoryItem = getInventoryItem();
			Thneed Inventory = (Thneed) InventoryItem.keySet().toArray()[0];
			int quantity = (int) InventoryItem.values().toArray()[0];
			inventoryItems.put(Inventory, quantity);
			inventoryDisplayArea.getItems().add(Inventory.getSize() + " " + Inventory.getColor() + " Thneed (" + quantity + ") Arrival Date: " + new Date());
			clearInput();
			inventory = new Inventory(inventoryItems);
			saveInventory();
		}
	}
	/**
	 * This method reads the inventory.txt file and inputs the data from it into a 
	 */
	@SuppressWarnings("unchecked")
	public void loadInventory() {
	    File file = new File(CustomerLoginController.INVENTORY_FILEPATH);
	    try {
	        file.createNewFile();
	        BufferedReader br = new BufferedReader(new FileReader(CustomerLoginController.INVENTORY_FILEPATH));
	        if (br.readLine() != null) {
	            FileInputStream fis = new FileInputStream(CustomerLoginController.INVENTORY_FILEPATH);
	            ObjectInputStream input = new ObjectInputStream(fis);
	            inventoryItems = (HashMap<Thneed, Integer>) input.readObject();
	            inventory = new Inventory(inventoryItems);
	            inventoryItems = Inventory.thneedList;
	            fis.close();
	            input.close();
	            inventoryDisplayArea.getItems().clear();
	            for (Map.Entry<Thneed, Integer> entry : inventoryItems.entrySet()) {
	                Thneed Inventory = entry.getKey();
	                Integer quantity = entry.getValue();
	                inventoryDisplayArea.getItems().add(Inventory.getSize() + " " + Inventory.getColor()  + " Thneed (" + quantity + ") Arrival Date: " + new Date()); 
	            }
	        }
	        br.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	/**
	 * This method takes the current instance of the inventory hashmap and writes it to the inventory.txt file.
	 */
	public void saveInventory() {
		try {
			inventoryItems = Inventory.thneedList;
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(CustomerLoginController.INVENTORY_FILEPATH));
			output.writeObject(inventoryItems);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This is the intialize method that runs on start up to check if anything changes and if so runs the load inventory method.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    inventoryDisplayArea.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	        }
	    });

	    loadInventory(); 
	}


}
