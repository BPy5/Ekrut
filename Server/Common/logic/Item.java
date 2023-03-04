package logic;

import java.io.Serializable;

public class Item implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * the identification of the product 
	 */
	private String productID ;
	
	/**
	 * the amount of a product
	 */
	private String amount;
	
	/**
	 * the price of a product 
	 */
	private int price;
	
	/**
	 * constructor that gets the information of item and sets all attribute to new values
	 * @param productID - item ID
	 * @param amount - amount of product
	 * @param price - price of product
	 */
	public Item(String productID, String amount, int price) {
		this.productID = productID;
		this.amount = amount;
		this.price = price;
	}

	/**
	 *  constructor that sets all attributes to default value
	 */
	public Item() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return product id
	 */
	public String getProductID() {
		return productID;
	}

	/**
	 * Sets product id
	 * @param productID
	 */
	public void setProductID(String productID) {
		this.productID = productID;
	}

	/**
	 * @return amount 
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * Sets amount
	 * @param amount - amount of product
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return price 
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Sets price
	 * @param price - price of product
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * returns the objects in the form of a string
	 */
	@Override
	public String toString() {
		return "Items [productID=" + productID + ", Amount=" + amount + ", Price=" + price + "]";
	}
	
	
}
