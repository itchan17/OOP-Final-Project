import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Receipt {
    StringBuilder receiptContent = new StringBuilder();
    int orderId;
    int userId;
    String firstName;
    String lastName;
    double orderTotal;
    double subTotal;
    double orderCash;
    double orderChange;
    String orderDiscountId;
    double orderDiscountAmount;
    String orderDate;
    String orderTime;
    int itemDetailsId;
    int itemQuantity;
    double eachTotalPrice;
    int itemId;
    int sizeId;
    double itemPrice;
    String itemName;
    String sizeName;
    int ctr = 0;
    double vat;
    String customerName;
    String customerAddress;
    ResultSet resultSet;
    
    Connection con;
    int getOrderId;
    String userType;
    
    Receipt(Connection con, int getOrderId, String userType) {
        this.con = con;
        this.getOrderId = getOrderId;
        this.userType = userType;
    }

    public void generateReceipt() {
    	String sqlOrderCashier = "SELECT otb.order_id, utb.user_id, utb.firstName, otb.admin_id, otb.cashier_id, " +
                "otb.order_total, otb.sub_total, otb.vat, " +
                "otb.order_cash, otb.order_change, otb.customer_name, otb.customer_address, otb.order_discountId, otb.order_discount_amount, " +
                "otb.order_date, otb.order_time, " +
                "odtb.item_details_id, odtb.item_quantity, odtb.each_total_price, " +
                "idtb.item_id, idtb.size_id, idtb.item_price, " +
                "itb.item_name, stb.size_name " +
                "FROM orders_tb AS otb " +
                "INNER JOIN order_details_tb AS odtb ON otb.order_id = odtb.order_id " +
                "INNER JOIN item_details_tb AS idtb ON odtb.item_details_id = idtb.item_details_id " +
                "INNER JOIN items_tb AS itb ON idtb.item_id = itb.item_id " +
                "INNER JOIN size_tb AS stb ON idtb.size_id = stb.size_id " +
                "INNER JOIN cashier_tb AS ctb ON otb.cashier_id = ctb.cashier_id " +
                "INNER JOIN users_tb AS utb ON ctb.user_id = utb.user_id " +
                "WHERE otb.order_id = ?";
    	
    	String sqlOrderAdmin = "SELECT otb.order_id, utb.user_id, utb.firstName, otb.admin_id, otb.cashier_id, " +
                "otb.order_total, otb.sub_total, otb.vat, " +
                "otb.order_cash, otb.order_change, otb.customer_name, otb.customer_address, otb.order_discountId, otb.order_discount_amount, " +
                "otb.order_date, otb.order_time, " +
                "odtb.item_details_id, odtb.item_quantity, odtb.each_total_price, " +
                "idtb.item_id, idtb.size_id, idtb.item_price, " +
                "itb.item_name, stb.size_name " +
                "FROM orders_tb AS otb " +
                "INNER JOIN order_details_tb AS odtb ON otb.order_id = odtb.order_id " +
                "INNER JOIN item_details_tb AS idtb ON odtb.item_details_id = idtb.item_details_id " +
                "INNER JOIN items_tb AS itb ON idtb.item_id = itb.item_id " +
                "INNER JOIN size_tb AS stb ON idtb.size_id = stb.size_id " +
                "INNER JOIN admin_tb AS atb ON otb.admin_id = atb.admin_id " +
                "INNER JOIN users_tb AS utb ON atb.user_id = utb.user_id " +
                "WHERE otb.order_id = ?";


        PreparedStatement statement;
        try {
        	if(userType == "cashier") {
        		statement = con.prepareStatement(sqlOrderCashier);
                statement.setInt(1, getOrderId);
                
                resultSet = statement.executeQuery();
        	}
        	else if(userType == "admin") {
        		statement = con.prepareStatement(sqlOrderAdmin);
                statement.setInt(1, getOrderId);
                
                resultSet = statement.executeQuery();
        	}
            
                     
            while (resultSet.next()) {
                // Order basic info
                orderId = resultSet.getInt("order_id");
                firstName = resultSet.getString("firstName");
                userId = resultSet.getInt("user_id");                 
                orderTotal = resultSet.getDouble("order_total");
                subTotal = resultSet.getDouble("sub_total");
                vat = resultSet.getDouble("vat");
                orderCash = resultSet.getDouble("order_cash");
                orderChange = resultSet.getDouble("order_change");
                orderDiscountId = resultSet.getString("order_discountId");
                orderDiscountAmount = resultSet.getDouble("order_discount_amount");
                orderDate = resultSet.getString("order_date");
                orderTime = resultSet.getString("order_time");
                customerName = resultSet.getString("customer_name");
                customerAddress = resultSet.getString("customer_address");

                // Order Details
                itemDetailsId = resultSet.getInt("item_details_id");
                itemQuantity = resultSet.getInt("item_quantity");
                eachTotalPrice = resultSet.getDouble("each_total_price");
                itemId = resultSet.getInt("item_id");
                sizeId = resultSet.getInt("size_id");
                itemPrice = resultSet.getDouble("item_price");
                itemName = resultSet.getString("item_name");
                sizeName = resultSet.getString("size_name");
                

                if (resultSet.isFirst()) {
                    // Header
                    receiptContent.append(String.format("%-20s%s%n", "", "Lomi Haus"));
                    receiptContent.append(String.format("%-12s%s%n",  "", "Santa Rosa City, Laguna"));
                    receiptContent.append(String.format("%-13s%s%n", "", "Mobile No. 09123456789"));
                    receiptContent.append(String.format("%-11s%-15s%n", "Order Date:", orderDate));
                    receiptContent.append(String.format("%-11s%-15s%n", "Order Time:", orderTime));
                    receiptContent.append(String.format("%-11s%-15s%n","Order No.", orderId));                  
                  	receiptContent.append(String.format("%-11s%-15s%n", "Cashier:", userId + " - " + firstName));
                   
                  

                    receiptContent.append("----------------------------------------------\n");
                    receiptContent.append(String.format("%-5s%-15s%-10s%-11s%-13s%n" + "\n",
                            "Qty", "Item", "Size", "Each", "Total"));
                }

                // Order Details
                receiptContent.append(String.format("%-5s%-15s%-9s%6.2f%11.2f%n",
                        itemQuantity, itemName, sizeName, itemPrice, eachTotalPrice));

                if (resultSet.isLast()) {
                    receiptContent.append("----------------------------------------------\n");
                    receiptContent.append(String.format("%-36s%10.2f%n",
                            "Sub Total:", subTotal));
                    receiptContent.append(String.format("%-36s%10.2f%n",
                            "VAT - 12%:", vat));
                    receiptContent.append(String.format("%-36s%10.2f%n",
                            "Discount:", orderDiscountAmount));
                    receiptContent.append(String.format("%-36s%10.2f%n",
                            "Total:", orderTotal) + "\n");
                    receiptContent.append(String.format("%-36s%10.2f%n",
                            "Total Payment:", orderCash));
                    receiptContent.append(String.format("%-36s%10.2f%n",
                            "Change:", orderChange));
                    receiptContent.append("----------------------------------------------\n");
                    receiptContent.append(String.format("%-13s%s%n",
                            "", "Thank You for Purchasing"));
                }
            }

            // Write receipt to file
            String fileName = "receipts/receipt.txt";
            try {
                FileWriter fw = new FileWriter(fileName);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(receiptContent.toString());
                bw.close(); // Close the BufferedWriter
                System.out.println("Receipt saved to " + fileName);          

            } catch (IOException ex) {
                System.err.println("Error writing receipt: " + ex.getMessage());
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}