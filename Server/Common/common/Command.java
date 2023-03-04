package common;

public enum Command {
	Connect { 
		@Override
		public String toString() {
			return "Connect";
		}
	},
	
	EKTConnect{
		@Override
		public String toString() {
			return " EKT Connect";
		}
	},
	
	Disconnect {
		@Override
		public String toString() {
			return "Disconnect";
		}
	},
	
	DatabaseUpdate {
		@Override
		public String toString() {
			return "Database Update";
		}
	},
	
	UpdateDiscount{
		@Override
		public String toString() {
			return "Update Discount";
		}
	},
	
	UpdateRequest{
		@Override
		public String toString() {
			return "Update request";
		}
	},
	
	DatabaseRead{
		@Override
		public String toString() {
			return "Database Read";
		}
	},
	
	InsertUser {
		@Override
		public String toString() {
			return "Insert User";
		}
	},

	InsertRequest{
		@Override
		public String toString() {
			return "Insert request";
		}
	},
	
	InsertOrder {
		@Override
		public String toString() {
			return "Insert Order";
		}
	},

	InsertOrderReport {
		@Override
		public String toString() {
			return "Insert Order Report";
		}
	},
	
	InsertInventoryReport{
		@Override
		public String toString() {
			return "Insert Inventory Report";
		}
	},
	
	
	//----------------------------------------------------------------
	//SQL query enums
	//----------------------------------------------------------------
	
	ReadMachines {
		@Override
		public String toString() {
			return "Read Machines";
		}
	},
	
	ReadUsers {
		@Override
		public String toString() {
			return "Read Users";
		}
	},
	
	ReadUserReports{
		@Override
		public String toString() {
			return "Read users reports";
		}
	},
	
	ReadDeliveries {
		@Override
		public String toString() {
			return "Read Deliveries";
		}
	},
	
	ReadOrders {
		@Override
		public String toString() {
			return "Read Orders";
		}
	},
	
	UpdateOrders {
		@Override
		public String toString() {
			return "Update Orders";
		}
	},
	
	UpdateOrderSupplyMethod {
		@Override
		public String toString()
		{
			return "Update Order Supply Method";
		}
	},
	
	
	ReadRequests {
		@Override
		public String toString() {
			return "Read Requests";
		}
	},
	
	ReadItems {
		@Override
		public String toString() {
			return "Read Items";
		}
	},
	
	ReadLocations{
		@Override
		public String toString() {
			return "Read Location";
		}
	},
	
	ReadOrdersReports{
		@Override
		public String toString() {
			return "Read Order reports";
		}
	},
	
	UpdateDeliveries {
		@Override
		public String toString() {
			return "Update Deliveries Table";
		}
	},
	
	UpdateUsers{
		
		@Override
		public String toString() {
			return "update users";
		}
	},
	
	ReadUserVisa{
		@Override
		public String toString() {
			return "Read User Visa";
		}
	},
	
	UpdateMachineStock{
		@Override
		public String toString() {
			return "Update Machine Stock";
		}
	},
	
	UpdateMachineThreshold{
		@Override
		public String toString() {
			return "Update Machine Threshold";
		}
	},
	
	InsertStockRequest{
		@Override
		public String toString() {
			return "Insert Stock Request";
		}
	},
	
	UpdateStockRequest{
		@Override
		public String toString() {
			return "Update Stock Request";
		}
	},
	
	ReadExternalTable {
		@Override
		public String toString() {
			return "Read External Table";
		}
	},
	
	UpdateUserFirstCart{
		@Override
		public String toString()
		{
			return "Update User First Cart";
		}
	},
	
	InsertDelivery{
		@Override
		public String toString() {
			return "Insert Delivery";
		}
	},
	
	InsertUsersReport{
		@Override
		public String toString() {
			return "Insert User Report";
		}
	},
	
	ReadStockRequests{
		@Override
		public String toString() {
			return "Read Stock Request";
		}
	},
	
	ReadInventoryReports{
		@Override
		public String toString() {
			return "Read Inventory reports";
		}
	};
	
	/**
	 * @returns a query based on the command type
	 */
	public String GetQuery() {
		switch(this) {
			case ReadMachines:
				return "SELECT * FROM machines";
			
			case ReadUsers:
				return "SELECT * FROM users";
				
			case ReadUserVisa:
				return "SELECT credit_card_number FROM users";
			
			case ReadOrders:
				return "SELECT * FROM orders";
			
			case ReadRequests:
				return "SELECT * FROM requests";
			
			case ReadDeliveries:
				return "SELECT * FROM delivery";
				
			case ReadItems:
				return "SELECT * FROM items";
				
			case ReadLocations:
				return "SELECT * FROM location";
				
			case ReadOrdersReports:
				return "SELECT * FROM ordersreport";
				
			case ReadExternalTable:
				return "SELECT * FROM externaluserstable";
			
			case UpdateMachineStock:
				return "UPDATE machines SET amount_per_item = ?, items = ?, total_inventory = ? WHERE machine_id = ?";
				
			case UpdateMachineThreshold:
				return "UPDATE machines SET threshold = ?? WHERE machine_id = ??"; 
				
			case ReadStockRequests:
				return "SELECT * FROM stockrequests";
				
			case ReadInventoryReports:
				return "SELECT * FROM inventoryreport";
			
			case ReadUserReports:
				return "SELECT * FROM usersreports";
				
			default:
				return "Illegal Command";
		}
	}
	
	/**
	 * @returns a primary key based on the command type
	 */
	public String GetID() {
		switch(this) {
			case ReadMachines:
				return "machine_id";
			
			case ReadUsers:
				return "ID";
				
			case ReadUserVisa:
				return "ID";
				
			case ReadOrders:
				return "order_number";
			
			case ReadRequests:
				return "request_id";
			
			case ReadDeliveries:
				return "delivery_id";
				
			case ReadItems:
				return "name";
				
			case ReadLocations:
				return "name";
				
			case ReadOrdersReports:
				return "report_id";
				
			case InsertOrderReport:
				return "report_id";
				
			case InsertInventoryReport:
				return "machine_id";
				
			case InsertUsersReport:
				return "report_id";
			
			case InsertStockRequest:
				return "stock_request_id";
				
			case ReadStockRequests:
				return "stock_request_id";
				
			case ReadInventoryReports:
				return "report_id";
				
			case ReadUserReports:
				return "report_id";
				
			case InsertDelivery:
				return "delivery_id";
				
			case InsertRequest:
				return "request_id";
				
			case InsertUser:
				return "subscriber_number";
				
			default:
				return "Illegal Command";
		}
	}
}
