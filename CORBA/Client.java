import Restaurant.*; // This line imports the package that contains the stubs.
import org.omg.CosNaming.*; // The client will utilize the Naming Service.
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*; // These classes are essential for all CORBA applications
import java.util.*;

class Client {
	public static void main(String[] args) {
		try {
			// Define variables for user input and object references
			int custInput, Input, mangInput;
			String foodName,customerName;
			Scanner scanner = new Scanner(System.in);
			String s, name;

			ORB orb = ORB.init(args, null);
			
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			NamingContext ncRef = NamingContextHelper.narrow(objRef);
			
			NameComponent nc = new NameComponent("Order Management", "");
			NameComponent path[] = { nc };
			
			RestaurantInterface RestReference = RestaurantInterfaceHelper.narrow(ncRef.resolve(path));
			
			do {
				// Display options for the user to choose from
				System.out.println("Choose the User");
				System.out.println("Customer - 1");
				System.out.println("Manager - 2");
				System.out.println("Exit - 3");
				System.out.print("Enter chosen option no. : ");

				// Read the user's input
				Input = scanner.nextInt();
				
				switch (Input) {
					case 1: // Customer role
						do {
							int cse = 0;
							System.out.println( "View Menu - " + (++cse));
							
							// Only show the "View Orders" option if there are any orders placed
							if (RestReference.total_price() > 0) {
								System.out.println("View Orders - " + (++cse));
							}

							System.out.println("Exit From Customer Role - " + "3");
							System.out.print("Enter chosen option no. : ");
							custInput = scanner.nextInt();

							switch (custInput) {
								case 1: // View menu inorder to place order
									System.out.print("Enter username: ");
									name = scanner.next();
									System.out.print(RestReference.view_menu());
									System.out.print("Enter food name: ");
									
									do {
										foodName = scanner.next();
									
										if (!foodName.equals("Chicken") && !foodName.equals("Cola") ) {
											System.out.print("Please enter a valid option: ");
										}
									} while (!foodName.equals("Chicken") && !foodName.equals("Cola"));
									
									int quantity;
									System.out.print("Enter number of quantities: ");
									do {
										quantity = scanner.nextInt();
										if (quantity <= 0) {
											System.out.print("Please enter a valid quantity: ");
										}
									} while (quantity <= 0);
									
									s = RestReference.place_order(foodName, name, quantity);
									if (s.equals("")) {
										s = "Sorry the order is not placed due to technical error";
									}
									System.out.println(s);

									break;

								case 2:
									System.out.print("Enter Customer Name ");
									customerName = scanner.next();
									s = RestReference.check_order_status(customerName);
									System.out.println(s);
									break;

								default:
									System.out.println("Invalid Option");

									break;
							}
						} while (custInput != 3);
						break;

					case 2:// Manager role
						mangInput = 0;
						do {
							System.out.println("Manager View");
							System.out.println("View Current Orders - 1");
							System.out.println("Return to Main Menu - 2");
							System.out.print("Enter choice: ");
							mangInput = scanner.nextInt();
							switch (mangInput) {
								case 1:
									s = RestReference.view_current_orders();
									System.out.println(s);
									break;
								case 2:
									break;
								default:
									System.out.println("Invalid Option");
									break;
							}
						} while (mangInput != 2);
						break;

					default:
						System.out.println("Invalid Choice");
						break;
				}
			} while (Input != 3);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
