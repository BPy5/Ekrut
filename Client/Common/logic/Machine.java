package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Machine implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * the identification of the current machine
	 */
	private int machine_id;
	
	/**
	 * machine stock threshold
	 */
	private int threshold;
	
	/**
	 * the total inventory in the current machine
	 */
	private int total_inventory;
	
	/**
	 * the location of the current machine
	 */
	private String location;
	
	/**
	 * all items in the current machine
	 */
	private String allItems;
	
	/**
	 * the amount per item in the current machine  
	 */
	private String amount_per_item;
	
	/**
	 * ArrayList of the items in the current machine
	 */
	private ArrayList<String> items;
	
	/**
	 * ArrayList of the amount of the items in the current machine
	 */
	private ArrayList<Integer> amount;
	

	/**
	 *  constructor that sets all attributes to default value
	 */
	public Machine() {
	}
	/**
	 * constructor that gets the information of machine and sets all attribute to new values
	 * @param machine_id - machine ID
	 * @param threshold - machine stock threshold
	 * @param total_inventory - total in machine
	 * @param location - machine location
	 * @param allItems - all items in machine
	 * @param amount_per_item - amount per item in machine
	 */
	public Machine(int machine_id, int threshold, int total_inventory, String location, String allItems, String amount_per_item) {
		this.machine_id = machine_id;
		this.threshold = threshold;
		this.total_inventory = total_inventory;
		this.location = location;
		this.allItems = allItems;
		this.amount_per_item = amount_per_item;
	}

	/**
	 * @return machine id
	 */
	public int getMachine_id() {
		return machine_id;
	}

	/**
	 * Sets machine id
	 * @param machine_id
	 */
	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}

	/**
	 * @return threshold 
	 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * Sets threshold
	 * @param threshold - machine stock threshold
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	/**
	 * @return total inventory
	 */
	public int getTotal_inventory() {
		return total_inventory;
	}

	/**
	 * Sets total inventory
	 * @param total_inventory - total inventory in machine
	 */
	public void setTotal_inventory(int total_inventory) {
		this.total_inventory = total_inventory;
	}

	/**
	 * @return  machine location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets machine location
	 * @param location - machine location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return all items
	 */
	public String getAllItems() {
		return allItems;
	}

	/**
	 * Sets the field "all items" using split
	 * @param items - items in machine
	 */
	public void setAllItems(String items) {
		this.allItems = items;
		this.items = new ArrayList<>(this.total_inventory);
		
		String[] splitItems = allItems.split(",");
		for (String item : splitItems)
			this.items.add(item);
	}
	
	/**
	 * @param index
	 * @returns the item according to  the index
	 */
	public String getItem(int index) {
		return this.items.get(index);

	}
	
	/**
	 * @param name
	 * @returns true if the item is existed
	 */
	public boolean existItem(String name) {
		for(String item : items)
		{
			if(name.equals(item))
				return true;
		}
		return false;

	}
	
	/**
	 * @returns ArrayList of items
	 */
	public ArrayList<String> getItems()
	{
		return this.items;
	}

	/**
	 * @return amount per item
	 */
	public String getAmount_per_item() {
		return amount_per_item;
	}

	/**
	 * Sets amount per item using split
	 * @param amount_per_item - amount per item in machine
	 */
	public void setAmount_per_item(String amount_per_item) {
		this.amount_per_item = amount_per_item;
		String[] splitItems = amount_per_item.split(",");
		this.amount = new ArrayList<>(splitItems.length);
		for (String item : splitItems)
			this.amount.add(Integer.parseInt(item));
	}
	
	
	/**
	 * @returns ArrayList of items amount
	 */
	public ArrayList<Integer> getAmountItems()
	{
		return amount;
	}
	
	/**
	 * @param index
	 * @returns the amount according to the index
	 */
	public int getAmount(int index)
	{
		return amount.get(index);
		
	}

	
	
}
