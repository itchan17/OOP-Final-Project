import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DailySalesReport {
    StringBuilder reportContent = new StringBuilder();
         
    int dailySalesId;
    String dailySalesDate;
    double totalDiscount;
    double totalSales;
    String itemName;
    String sizeName;
    double itemPrice;
    int totalQuantity;
    double itemTotalSales;
    
    
    Connection con;
    int getDailySalesId;
    
    DailySalesReport(Connection con, int getDailySalesId) {
        this.con = con;
        this.getDailySalesId = getDailySalesId;
    }

    public void generateReport() {
    	
    	String sqlQuery = "SELECT dstb.daily_sale_id, " +
                "dstb.daily_sale_date, " +
                "dstb.total_daily_discountAmount, " +
                "dstb.total_daily_sale, " +
                "itb.item_name, " +
                "stb.size_name, " +
                "idtb.item_price, " +
                "dsdtb.item_total_quantity, " +
                "dsdtb.item_total_sales " +
                "FROM daily_sales_tb AS dstb " +
                "INNER JOIN daily_sale_details_tb AS dsdtb " +
                "ON dstb.daily_sale_id = dsdtb.daily_sale_id " +
                "INNER JOIN item_details_tb AS idtb " +
                "ON dsdtb.item_details_id = idtb.item_details_id " +
                "INNER JOIN items_tb AS itb " +
                "ON idtb.item_id = itb.item_id " +
                "INNER JOIN size_tb AS stb " +
                "ON idtb.size_id = stb.size_id " +
                "WHERE dstb.daily_sale_id = ?;";


        PreparedStatement statement;
        try {
            statement = con.prepareStatement(sqlQuery);
            statement.setInt(1, getDailySalesId);
            
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Order basic info
            	dailySalesId = resultSet.getInt("daily_sale_id");               
            	dailySalesDate = resultSet.getString("daily_sale_date");               
            	totalDiscount = resultSet.getDouble("total_daily_discountAmount");
            	totalSales = resultSet.getDouble("total_daily_sale");            
            	itemName = resultSet.getString("item_name");
            	sizeName = resultSet.getString("size_name");
            	itemPrice = resultSet.getDouble("item_price");
            	totalQuantity = resultSet.getInt("item_total_quantity");
            	itemTotalSales = resultSet.getDouble("item_total_sales");

              

                if (resultSet.isFirst()) {
                    // Header
                	reportContent.append(String.format("%-16s%s%n",
                            "", "DAILY SALES REPORT" + "\n"));
                	reportContent.append(String.format("%-21s%s%n",
                            "", "Lomi Haus"));
                	reportContent.append(String.format("%-13s%s%n",
                            "", "Santa Rosa City, Laguna"));
                	reportContent.append(String.format("%-14s%s%n",
                            "", "Mobile No. 09123456789"));

                	reportContent.append(String.format("%-12s%-15s%n",
                            "Sales Date:", dailySalesDate));                	
                	reportContent.append(String.format("%-12s%-15s%n",
                            "Sales ID:", dailySalesId));
                

                	reportContent.append("-----------------------------------------------\n");
                	reportContent.append(String.format("%-15s%-10s%-7s%-9s%-10s%n", 
                            "Item", "Size", "Qty", "Each", "Total") + "\n");
                }                                 

                // Order Details                  
                reportContent.append(String.format("%-15s%-10s%-6d%6.2f%10.2f%n",
                		itemName, sizeName, totalQuantity, itemPrice, itemTotalSales));

                if (resultSet.isLast()) {
                	reportContent.append("-----------------------------------------------\n");               	
                	reportContent.append(String.format("%-37s%10.2f%n",
                            "Total Discount:", totalDiscount));
                	reportContent.append(String.format("%-37s%10.2f%n",
                            "Total Sales:", totalSales) + "\n");               	
                }
            }
            // Write receipt to file
            String fileName = "receipts/DailySalesReport.txt";
            try {
                FileWriter fw = new FileWriter(fileName);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(reportContent.toString());
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