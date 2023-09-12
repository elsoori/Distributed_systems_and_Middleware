import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import java.util.*;
import Restaurant.*;


class Server extends _RestaurantInterfaceImplBase {

	// Static variables for maintaining the menu and orders list
	static Map<String, Integer> foodList;
	static HashMap<String, String[]> ordersList;
	static int total;


	public Server() {
		total = 0;
		ordersList = new HashMap<String, String[]>();
		foodList = new HashMap<String, Integer>();
		foodList.put("Cola", 1);
		foodList.put("Chicken", 5);

	}

	// Method to view the menu
	public String view_menu() {
		return "Cola : $1\nChicken : $5\n";
	}

	// Method to place an order
	public String place_order(String food_id, String name, int quantity) {
		// Get the price of the food item from the menu
		int price_per_item = foodList.get(food_id);
		String str = "";
		// Calculate the total price
		total = 0;
		total += price_per_item * quantity;
		// Add the order details to the orders list
		System.out.println("Food item: " + food_id + ",quantity ordered: " + quantity);
		String[] orderDetails = { food_id, Integer.toString(quantity), "placed", Integer.toString(total) };
		ordersList.put(name, orderDetails);
		str = "Food " + food_id + " ordered with quantities " + quantity;
		return str;
	}

	// Method to get the total price of all orders
	public int total_price() {
		return total;
	}

	// Method to view the current orders
	public String view_current_orders() {
		if (total == 0) {
			return "No orders placed yet.";
		}
		// Iterate over the orders list and create a string of all the orders
		String orders = "List of orders:\n";
		for (Map.Entry<String, String[]> entry : ordersList.entrySet()) {
			String name = entry.getKey();
			String[] orderDetails = entry.getValue();
			orders += "Customer Name : " + name +  "\n" + "  Name of Food: " + orderDetails[0] +  "\n"+ " Quantity: "
					+ orderDetails[1] + " ,Total Value: " + "$ " + orderDetails[3] +  "\n\n";
		}
		return orders;
	}

	// Method to check the status of an order
	public String check_order_status(String name) {
		String orders = "";
		// Get the order details for the given customer name
		String[] orderDetails = ordersList.get(name);
		if (orderDetails == null) {

			orders += "Customer name not found in order list!";
		} else {
			// Create a string with the order details
			String Food = orderDetails[0];
			int quantity = Integer.parseInt(orderDetails[1]);
			String status = orderDetails[2];
			orders += "Customer Name : " + name +  "\n" + "  Name of Food: " + orderDetails[0] +  "\n"+ " Quantity: "
					+ orderDetails[1] + " ,Total Value: " + "$ " + orderDetails[3] +  "\n\n";
		}
		return orders;
	}

	// Main method to start the server and register it with the Name
	public static void main(String[] args) {
		try {

			ORB orb = ORB.init(args, null);


			Server RestReference = new Server();


			orb.connect(RestReference);

			// resolve the initial references to NamingContext
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContext ncRef = NamingContextHelper.narrow(objRef);

			NameComponent nc = new NameComponent("Order Management", "");
			NameComponent path[] = { nc };
			ncRef.rebind(path, RestReference);

			// print a message indicating the server has started
			System.out.println("Server started!!");


			Thread.currentThread().join();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
