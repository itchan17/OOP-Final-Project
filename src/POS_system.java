import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.Statement;

import javax.swing.JCheckBox;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class POS_system extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPane;
	private JTextField num_textField;
    private String currentUser;
    private int user_id;
    private String userType;
    private String displayNumber = "";
    private String idType = "";
    private int userId;
    
    private Connection con;
    
    private JList<ItemList> list;
    private static ArrayList<ItemList> itemList = new ArrayList<>();
    
    private String itemName;
    private String itemSize;
    private int sizeId;
    private double itemEachPrice;
    private int itemQty;
    private double sizePrice;
    
    private double eachTotalPrice;
    private double orderSubTotal;
    private double vat;
    private double cash;
    private double change;
    private double orderTotal;
    private String discountId = "";
    private double discountAmount;
    
    private int item_details_id;
    
    private JLabel subtotal_label = new JLabel("0.00");
    private JLabel total_label = new JLabel("0.00");
    private JLabel change_label = new JLabel("0.00");
    private JLabel cash_label = new JLabel("0.00");
    private JLabel discount_label = new JLabel("0.00");
    private JLabel vat_label = new JLabel("0.00");
	private int dailySaleId = 0;
	private JTable voidTable;
	private JTable dailySales_table;
	
	private String customerName;
	private String customerAddress;
	private String idNumber;
	private int discountRate;
	/**
	 * Launch the application.
	 */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    String currentUser = "username"; 
                    int user_id = 0; 
                    String userType = "user type";
                    String idType = " ";
                    POS_system frame = new POS_system(currentUser, user_id, userType, idType);
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//                  frame.setUndecorated(true);
                    frame.setVisible(true);               
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void openDiscountFrame() {
        Discount discountFrame = new Discount(new DiscountFrameListener() {
            @Override
            public void onDiscountEntered(String cName, String cAddress, String idNum, int dRate) {
                // Update instance variables with entered data
                customerName = cName;
                customerAddress = cAddress;
                idNumber = idNum;
                discountRate = dRate;
           
                discountAmount = orderSubTotal * (discountRate / 100.0); // Calculate discount amount
                orderTotal = orderSubTotal - discountAmount; // Calculate order total

                // Update UI components with calculated values (assuming these are JLabels)
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        discount_label.setText(String.format("%.2f", discountAmount));
                        total_label.setText(String.format("%.2f", orderTotal));
                        num_textField.setText(""); // Clear any input field if necessary

                        if (cash != 0) {
                            double change = cash - orderTotal; // Calculate change if cash is provided
                            change_label.setText(String.format("%.2f", change));
                        }
                    }
                });

                // Print debug information
                System.out.println("Customer Name: " + cName);
                System.out.println("Customer Address: " + cAddress);
                System.out.println("ID Number: " + idNum);
                System.out.println("Discount Rate: " + dRate);
            }
        });
        discountFrame.setLocationRelativeTo(null);
        discountFrame.setVisible(true); // Show the discountFrame
    }


    public void calcVAT() {
    	vat = orderSubTotal - (orderSubTotal / 1.12); 
    	vat_label.setText(String.format("%.2f", vat));
    }
      
    public void setTime(JLabel timeLabel) {  	
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime time = LocalTime.now();       
            timeLabel.setText(time.format(formatter));                        
    }
    
    public void setDate(JLabel dateLabel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.now();
        dateLabel.setText(date.format(formatter));
    }
    
    // Product button, add the items in array and insert in JList
    public void productButton(JButton btn, DefaultTableModel model, int itemId, String category) {
    	btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(sizeId == 0) {
					if (category == "meal") {
						sizeId = 100;
					}
					else if(category == "drink") {
						sizeId = 103;
					}
				}
				String qty = num_textField.getText();
				
				String getItem = "SELECT idt.item_details_id, it.item_name, sz.size_name, idt.item_price "
	                       		+ "FROM item_details_tb AS idt "
	                       		+ "INNER JOIN items_tb AS it ON idt.item_id = it.item_id "
	                       		+ "INNER JOIN size_tb AS sz ON idt.size_id = sz.size_id "
	                       		+ "WHERE idt.item_id = ? AND idt.size_id = ?";
                try {      
                	PreparedStatement ps = con.prepareStatement(getItem);
                    
                	ps.setInt(1, itemId);
                    ps.setInt(2, sizeId);
                	
                    ResultSet itemRS = ps.executeQuery();
                    
                    
                    while (itemRS.next()) {                       
                        
                    	item_details_id = itemRS.getInt("item_details_id");
                        itemName = itemRS.getString("item_name");      
                        itemSize = itemRS.getString("size_name");     
                        itemEachPrice = itemRS.getInt("item_price") + sizePrice;
                        itemQty = (qty != null && !qty.isEmpty()) ? Integer.parseInt(qty) : 1;
                        eachTotalPrice = itemEachPrice * itemQty;
                                               
                        orderSubTotal += eachTotalPrice;
                        orderTotal = orderSubTotal;
                                               
                        if(discountAmount != 0) {
                       	 discountAmount = orderSubTotal * (discountRate / 100.0);
                       	 orderTotal = orderSubTotal - discountAmount;
                        } 
                        if (cash != 0) {
                        	change = cash - orderTotal;
                        }
                        if (cash < orderTotal) {
                        	System.out.println("Invalid cash amount");
                        }
                        
                        // Create object ItemList
                        ItemList items = new ItemList(item_details_id, itemQty, itemName, itemSize, itemEachPrice, eachTotalPrice);
                        
                        // Add the item to the array list
                        itemList.add(items);
//                        System.out.println(itemList);
                        
                        // Calculate VAT
                        calcVAT();
                        
                        // Add sun total and total
                        subtotal_label.setText(String.format("%.2f", orderSubTotal));
                        total_label.setText(String.format("%.2f", orderTotal));
                        discount_label.setText(String.format("%.2f", discountAmount));
                        change_label.setText(String.format("%.2f", change));
                        
                        
                        // Display the item in the list
                        model.addRow(new Object[]{
                                Integer.toString(items.getItemQty()),
                                items.getItemName(),
                                items.getItemSize(),
                                String.format("%.2f", items.getItemPrice()),
                                String.format("%.2f", items.getEachTotalPrice())
                        }); 
                        
                    }
                } catch (Exception ex) {
                    System.out.println("Failed to retrieve data from database");
                    ex.printStackTrace();
                }
                // Empty the text field
                num_textField.setText("");
                sizeId = 0;

            }
			
		});
    }
    
    // Size button
    public void sizeButton(JButton btn, int id) {
    	btn.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				sizeId = id;				
 			}
 		});
    }
    
    //Scroll bar
    public void scrollBar(JPanel panel, JScrollPane scrollPane) {
    	this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Print sizes after resizing
            	int panelWidth = panel.getSize().width;

                // Set height of scroll pane for different screen width
                if(panelWidth > 550) {
                	System.out.println(panelWidth);
                	panel.setPreferredSize(new Dimension(180, 10));
                }
                if(panelWidth < 550) {
                	System.out.println(panelWidth);
                	panel.setPreferredSize(new Dimension(180, 850));
                }
                if(panelWidth < 373) {
                	
                	panel.setPreferredSize(new Dimension(180, 1700));
                }
                              
                panel.revalidate();
                panel.repaint();
            }
        });
    }
    
    // Scale image to fit in the container
    public void scaleImage(String imgPath, JButton label) {
  		SwingUtilities.invokeLater(() -> {
  			Image img = new ImageIcon(this.getClass().getResource(imgPath)).getImage();
  	    	Image imgScale = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
  	        ImageIcon scaledIcon = new ImageIcon(imgScale);
  	        label.setIcon(scaledIcon);
 		});	
     }
    
    // Function for number buttons
    public void numButton(JButton numButton, String btnNumber) {
    	numButton.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				displayNumber = num_textField.getText();
 				displayNumber += btnNumber;
 				num_textField.setText(displayNumber);
 			}
 		});
    }
    
    
    // Display transactions in void panel
    public void displayTransactions(DefaultTableModel voidModel ) {
    	 ResultSet rs = null;
		 PreparedStatement pstmt = null;
		 ResultSet rs2 = null;
		 PreparedStatement pstmt2 = null;
		 ResultSet rs3 = null;
		 PreparedStatement pstmt3 = null;
		 
		 
		 String sqlQuery = "SELECT * " +
                 "FROM orders_tb AS otb " +	                        
                 "WHERE otb.order_date = CURDATE();";
		 
		 String sqlOrderCashier = "SELECT * FROM orders_tb AS otb " +
                 "INNER JOIN cashier_tb AS ctb ON otb.cashier_id = ctb.cashier_id " +
                 "INNER JOIN users_tb AS utb ON ctb.user_id = utb.user_id " +
                 "WHERE otb.order_id = ?";
		 
		 String sqlOrderAdmin = "SELECT * FROM orders_tb AS otb " +
                 "INNER JOIN admin_tb AS atb ON otb.admin_id = atb.admin_id " +
                 "INNER JOIN users_tb AS utb ON atb.user_id = utb.user_id " +
                 "WHERE otb.order_id = ?";

		 
		 voidModel.setRowCount(0);
		 
		 try {
			pstmt = con.prepareStatement(sqlQuery);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int transactionID = rs.getInt("order_id");
				String date = rs.getString("order_date");
				String time = rs.getString("order_time");
				if(rs.getInt("admin_id") == 0) {
										
					pstmt2 = con.prepareStatement(sqlOrderCashier);
					pstmt2.setInt(1, transactionID);
					
					rs2 = pstmt2.executeQuery();
					
					if(rs2.next()) {
						userId = rs2.getInt("user_id");
					}
					
				}
				else {
					pstmt3 = con.prepareStatement(sqlOrderAdmin);
					pstmt3.setInt(1, transactionID);
					
					rs3 = pstmt3.executeQuery();
					
					if(rs3.next()) {
						userId = rs3.getInt("user_id");
					}								
				}
				double totalAmount = rs.getDouble("order_total");
				double totalPayment = rs.getDouble("order_cash");
				double discountAmount = rs.getDouble("order_discount_amount");
				
				voidModel.addRow(new Object[]{
						transactionID,
						date,
						time,
						userId,
						totalAmount,
						totalPayment,
						discountAmount
                }); 
			}
			
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    //Display Daily Sales
    public void displayDialySales(DefaultTableModel dailySalesModel) {
   	 ResultSet rs = null;
		 PreparedStatement pstmt = null;
		 		 
		 String sqlQuery = "SELECT * " +
                "FROM daily_sales_tb;";
		 
		 dailySalesModel.setRowCount(0);
		 try {
			pstmt = con.prepareStatement(sqlQuery);			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int dailySalesID = rs.getInt("daily_sale_id");
				String date = rs.getString("daily_sale_date");
				double totalDailyDiscount = rs.getDouble("total_daily_discountAmount");								
				double totalDailySales = rs.getDouble("total_daily_sale");
				
				dailySalesModel.addRow(new Object[]{
						dailySalesID,
						date,
						totalDailyDiscount,
						totalDailySales,						
               }); 
			}
			
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
   }
    
    // Insert Daily Sales
    public void insertDailySales(Connection con) {
    	PreparedStatement pstmt = null;
        ResultSet rs = null;        

        // Query to check if a record exists for today
        String checkDailySalesQuery = "SELECT COUNT(*) FROM daily_sales_tb WHERE DATE(daily_sale_date) = CURDATE()";
        
        // Insert query to be executed if no record exists for today
        String insertDailySalesQuery = "INSERT INTO daily_sales_tb (daily_sale_date, total_daily_discountAmount, total_daily_sale) " +
                "SELECT CURDATE(), SUM(order_discount_amount), SUM(order_total) " +
                "FROM orders_tb " +
                "WHERE DATE(order_date) = CURDATE() AND " +
                "NOT EXISTS (" +
                "    SELECT 1 FROM daily_sales_tb WHERE DATE(daily_sale_date) = CURDATE()" +
                ")";
        
        // Update the data in daily_sale_tb
        String updateDailyTotalSale = "UPDATE daily_sales_tb " +
                "SET total_daily_sale = (SELECT SUM(order_total) FROM orders_tb WHERE DATE(order_date) = CURDATE()), " +
                "    total_daily_discountAmount = (SELECT SUM(order_discount_amount) FROM orders_tb WHERE DATE(order_date) = CURDATE()) " +
                "WHERE DATE(daily_sale_date) = CURDATE()";
      
        try {
            // Check if a record exists for today
            pstmt = con.prepareStatement(checkDailySalesQuery);
            rs = pstmt.executeQuery();
            rs.next();
            int rowCount = rs.getInt(1);

            if (rowCount == 0) {
                // If no record exists, insert the daily sales		            	
                pstmt = con.prepareStatement(insertDailySalesQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.executeUpdate();
                System.out.println("Daily Sale Inserted");
               
            } else {
                System.out.println("Daily sales already recorded for today. Skipping insertion.");
                // If record exists, update the existing daily sales
                pstmt = con.prepareStatement(updateDailyTotalSale);		             
                pstmt.executeUpdate();
                System.out.println("Daily Sale Updated");				                
            }
            
            // Sum the data from order_tb
            String dailySalesDetailsData = "SELECT " +
                    "dsd.daily_sale_id, " +
                    "od.item_details_id, " +
                    "SUM(od.item_quantity) AS item_total_quantity, " +
                    "SUM(od.each_total_price) AS item_total_sales " +
                    "FROM " +
                    "orders_tb AS o " +
                    "INNER JOIN order_details_tb AS od ON o.order_id = od.order_id " +
                    "INNER JOIN item_details_tb AS isz ON od.item_details_id = isz.item_details_id " +
                    "INNER JOIN items_tb AS it ON isz.item_id = it.item_id " +
                    "INNER JOIN size_tb AS sz ON isz.size_id = sz.size_id " +
                    "INNER JOIN daily_sales_tb AS dsd ON dsd.daily_sale_date = CURDATE() " +
                    "WHERE DATE(o.order_date) = CURDATE() " +
                    "GROUP BY " +
                    "dsd.daily_sale_id, od.item_details_id";
            
            // For checking if the item is already exists
            String checkItemDetailsId = "SELECT COUNT(*) FROM daily_sale_details_tb dsd " +
                    "INNER JOIN daily_sales_tb ds ON dsd.daily_sale_id = ds.daily_sale_id " +
                    "WHERE dsd.item_details_id = ? " +
                    "AND ds.daily_sale_date = CURDATE()";

            
            // Update the data of item
            String updateItemsDailyTotalSale = "UPDATE daily_sale_details_tb " +
                    "SET item_total_quantity = ?, item_total_sales = ? WHERE item_details_id = ? AND daily_sale_id = ?";
            
            // Insert the data
            String insertItemsDailyTotalSale = "INSERT INTO daily_sale_details_tb (daily_sale_id, item_details_id, item_total_quantity, item_total_sales) " +
                    "VALUES (?, ?, ?, ?)";
            
            // Remove the items with that do not exist in orders table
            String deleteVoidedItem = "DELETE dsdtb " +
                    "FROM daily_sale_details_tb dsdtb " +
                    "INNER JOIN daily_sales_tb dstb ON dsdtb.daily_sale_id = dstb.daily_sale_id " +
                    "WHERE dsdtb.item_details_id NOT IN (" +
                    "    SELECT odtb.item_details_id " +
                    "    FROM order_details_tb odtb " +
                    "    INNER JOIN orders_tb otb ON odtb.order_id = otb.order_id " +
                    "    WHERE DATE(otb.order_date) = CURDATE()" +
                    ") AND DATE(dstb.daily_sale_date) = CURDATE()";
            
            PreparedStatement dailyStmt = null;	            
            PreparedStatement checkItemStmt = null;            
            PreparedStatement updateStmt = null;
            PreparedStatement insertStmt = null;
            PreparedStatement deleteStmt = null;
            ResultSet dailyRs = null;
            ResultSet existenceRs = null;
            
            try{
            	dailyStmt = con.prepareStatement(dailySalesDetailsData);
            	
                // Execute the query
                dailyRs = dailyStmt.executeQuery();
             
                // Process the result set
                if (!dailyRs.next()) {
                    // No rows found, execute delete statement
                    deleteStmt = con.prepareStatement(deleteVoidedItem);
                    deleteStmt.executeUpdate();
                    System.out.println("No daily sales data found for today. Deleted voided items.");
                } else {
                	
                    // Rows found, process each row
                    do {
                        int dailySaleId = dailyRs.getInt("daily_sale_id");
                        int itemDetailsId = dailyRs.getInt("item_details_id");
                        int itemTotalQty = dailyRs.getInt("item_total_quantity");
                        double itemTotalSales = dailyRs.getDouble("item_total_sales");

                        // Check if item exists
                        checkItemStmt = con.prepareStatement(checkItemDetailsId);
                        checkItemStmt.setInt(1, itemDetailsId);
                        existenceRs = checkItemStmt.executeQuery();
                        existenceRs.next();
                        int count = existenceRs.getInt(1);

                        if (count > 0) {
                            // Update existing item details
                            deleteStmt = con.prepareStatement(deleteVoidedItem);
                            updateStmt = con.prepareStatement(updateItemsDailyTotalSale);
                            updateStmt.setInt(1, itemTotalQty); 
                            updateStmt.setDouble(2, itemTotalSales); 
                            updateStmt.setInt(3, itemDetailsId);    
                            updateStmt.setInt(4, dailySaleId);
                                       
                            deleteStmt.executeUpdate(); // Delete items with no values
                            updateStmt.executeUpdate(); // Update items
                            System.out.println("Daily Sale Details Updated");
                        } else {
                            // Insert new item details
                            insertStmt = con.prepareStatement(insertItemsDailyTotalSale);
                            insertStmt.setInt(1, dailySaleId);   
                            insertStmt.setInt(2, itemDetailsId);  
                            insertStmt.setInt(3, itemTotalQty);  
                            insertStmt.setDouble(4, itemTotalSales);
                                       
                            insertStmt.executeUpdate(); // Insert new items
                            System.out.println("Daily Sale Details Inserted");
                        }

                        // Close ResultSet and Statements inside the loop
                        existenceRs.close();
                        checkItemStmt.close();
                    } while (dailyRs.next());
                }
               
            }
            catch (Exception ex) {
	            System.out.println("Failed to insert daily sales into daily_sale_details_tb");
	            ex.printStackTrace();
	        } 

        } catch (Exception ex) {
            System.out.println("Failed to insert daily sales into daily_sales_tb");
            ex.printStackTrace();
        } 
    }
    
	/**
	 * Create the frame.
	 */
	public POS_system(String username, int user_id, String userType, String idType) {
		
		this.currentUser = username; // Set the name of currently logged in user
		this.user_id = user_id; //  Set the ID of currently logged in user
		this.userType = userType; //  Set the ID of currently logged in user
		this.idType = idType;
		
		System.out.println("User: " + currentUser);
		System.out.println("ID: " + user_id);
		
		// Establish connection
		try{
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_db", "root", "");
			System.out.println("Connected");
		}
		catch(Exception ee) {
			System.out.println("Failed to connect");
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1500, 920);
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPane);
		mainPane.setLayout(new BorderLayout(7, 7));
		
		JPanel northPanel = new JPanel();
		northPanel.setPreferredSize(new Dimension(70, 60));
		northPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		mainPane.add(northPanel, BorderLayout.NORTH);
		northPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(260, 10));
		northPanel.add(panel, BorderLayout.WEST);
		panel.setLayout(null);
		
		JLabel logo_photo = new JLabel("Logo");
		logo_photo.setBackground(Color.GRAY);
		logo_photo.setBounds(10, 5, 252, 47);
		panel.add(logo_photo);
		
		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(450, 10));
		northPanel.add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(null);
		
		JLabel dateLabel = new JLabel("June 21, 2024");
		dateLabel.setHorizontalAlignment(SwingConstants.LEFT);
		dateLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		dateLabel.setBounds(235, 11, 130, 17);
		panel_1.add(dateLabel);
		
		Timer date = new Timer(1000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        setDate(dateLabel); // Assuming timeLabel is accessible here
		    }
		});
		date.start();
		
		JLabel timeLabel = new JLabel("9:30 AM");
		timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		timeLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		timeLabel.setBounds(235, 30, 92, 17);
		panel_1.add(timeLabel);
		
		Timer timer = new Timer(1000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        setTime(timeLabel); // Assuming timeLabel is accessible here
		    }
		});
		timer.start();
		
		JLabel logoutLabel = new JLabel("Log out");
		logoutLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		logoutLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		logoutLabel.setBounds(391, 21, 49, 15);
		panel_1.add(logoutLabel);
		
		JLabel name_lbl = new JLabel("<dynamic>");
		name_lbl.setText(currentUser.toUpperCase() + " - " + Character.toUpperCase(userType.charAt(0)) + userType.substring(1).toLowerCase());
		name_lbl.setHorizontalAlignment(SwingConstants.RIGHT);
		name_lbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		name_lbl.setBounds(21, 20, 156, 17);
		panel_1.add(name_lbl);
		
		JPanel westPanel = new JPanel();
		westPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		westPanel.setPreferredSize(new Dimension(98, 10));
		mainPane.add(westPanel, BorderLayout.WEST);
		
		JButton meal_btn = new JButton("Meals");
		meal_btn.setForeground(new Color(223, 223, 223));
		meal_btn.setFont(new Font("Arial Black", Font.BOLD, 16));
		meal_btn.setFocusPainted(false);
		meal_btn.setBackground(new Color(183, 108, 60));
		
		JButton drinks_btn = new JButton("Drinks");
		drinks_btn.setForeground(new Color(223, 223, 223));
		drinks_btn.setFont(new Font("Arial Black", Font.BOLD, 16));
		drinks_btn.setFocusPainted(false);
		drinks_btn.setBackground(new Color(60, 103, 142));
		
		JButton settings_btn = new JButton("Settings");
		settings_btn.setMargin(new Insets(2, 2, 2, 2));
		settings_btn.setFont(new Font("Arial Black", Font.BOLD, 15));
		settings_btn.setFocusPainted(false);
		settings_btn.setBackground(new Color(255, 255, 255));
		GroupLayout gl_westPanel = new GroupLayout(westPanel);
		gl_westPanel.setHorizontalGroup(
			gl_westPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_westPanel.createSequentialGroup()
					.addGroup(gl_westPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(meal_btn, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
						.addComponent(settings_btn, GroupLayout.PREFERRED_SIZE, 96, Short.MAX_VALUE)
						.addComponent(drinks_btn, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_westPanel.setVerticalGroup(
			gl_westPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_westPanel.createSequentialGroup()
					.addComponent(meal_btn, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(drinks_btn, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 751, Short.MAX_VALUE)
					.addComponent(settings_btn, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE))
		);
		westPanel.setLayout(gl_westPanel);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(700, 1000));
		centerPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		mainPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new CardLayout(0, 0));
		
		JPanel meals_panel = new JPanel();
		centerPanel.add(meals_panel, "name_1341038903500900");
		meals_panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setPreferredSize(new Dimension(10, 90));
		meals_panel.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton regular_btn = new JButton("<html><div style='text-align: center;'>Regular<br></div></html>");
		regular_btn.setMargin(new Insets(2, 2, 2, 2));
		regular_btn.setPreferredSize(new Dimension(92, 80));
		regular_btn.setFont(new Font("Arial Black", Font.BOLD, 14));
		regular_btn.setFocusPainted(false);
		regular_btn.setBackground(new Color(192, 192, 192));
		panel_3.add(regular_btn);
		
		JButton special_btn = new JButton("<html><div style='text-align: center;'>Special<br></div></html>");		
		special_btn.setMargin(new Insets(2, 2, 2, 2));
		special_btn.setPreferredSize(new Dimension(92, 80));
		special_btn.setFont(new Font("Arial Black", Font.BOLD, 14));
		special_btn.setFocusPainted(false);
		special_btn.setBackground(new Color(192, 192, 192));
		panel_3.add(special_btn);
		
		JButton jumbo_btn = new JButton("<html><div style='text-align: center;'>Jumbo<br></div></html>");
		jumbo_btn.setMargin(new Insets(2, 2, 2, 2));
		jumbo_btn.setPreferredSize(new Dimension(92, 80));
		jumbo_btn.setFont(new Font("Arial Black", Font.BOLD, 14));
		jumbo_btn.setFocusPainted(false);
		jumbo_btn.setBackground(new Color(192, 192, 192));
		panel_3.add(jumbo_btn);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new MatteBorder(0, 0, 1, 0, new Color(130, 135, 144)));
		scrollPane_1.setPreferredSize(new Dimension(180, 1000));
		JPanel panel_2 = new JPanel();
		panel_2.setForeground(new Color(255, 255, 255));
		
		panel_2.setPreferredSize(new Dimension(180, 10));
		scrollPane_1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		meals_panel.add(scrollPane_1, BorderLayout.CENTER);	
		scrollPane_1.setViewportView(panel_2);
		
		// Meal panel scroll bar
		scrollBar(panel_2, scrollPane_1);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_5_1_4_4_2_2_7_2 = new JPanel();
		panel_5_1_4_4_2_2_7_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.add(panel_5_1_4_4_2_2_7_2);
		
		JLabel lblNewLabel_5_1_4_4_2_2_7_2 = new JLabel("Lomi");
		lblNewLabel_5_1_4_4_2_2_7_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_7_2.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton lomi_btn = new JButton("");
		lomi_btn.setFocusPainted(false);
		lomi_btn.setMargin(new Insets(0, 0, 0, 0));
		lomi_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_7_2 = new GroupLayout(panel_5_1_4_4_2_2_7_2);
		gl_panel_5_1_4_4_2_2_7_2.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_7_2.createParallelGroup(Alignment.LEADING)
				.addGap(0, 174, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_7_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_7_2, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(lomi_btn, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_7_2.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_7_2.createParallelGroup(Alignment.LEADING)
				.addGap(0, 204, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_7_2.createSequentialGroup()
					.addComponent(lomi_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_7_2, GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_7_2.setLayout(gl_panel_5_1_4_4_2_2_7_2);
		
		JPanel panel_5_1_4_4_2_2_1 = new JPanel();
		panel_5_1_4_4_2_2_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.add(panel_5_1_4_4_2_2_1);
		
		JLabel dfsg = new JLabel("Pancit Bihon");
		dfsg.setHorizontalAlignment(SwingConstants.CENTER);
		dfsg.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton bihon_btn = new JButton("");
		bihon_btn.setFocusPainted(false);
		bihon_btn.setMargin(new Insets(0, 0, 0, 0));
		bihon_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_1 = new GroupLayout(panel_5_1_4_4_2_2_1);
		gl_panel_5_1_4_4_2_2_1.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 172, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(dfsg, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(bihon_btn, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_1.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 202, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_1.createSequentialGroup()
					.addComponent(bihon_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(dfsg, GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_1.setLayout(gl_panel_5_1_4_4_2_2_1);
		
		JPanel panel_5_1_4_4_2_2_2 = new JPanel();
		panel_5_1_4_4_2_2_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.add(panel_5_1_4_4_2_2_2);
		
		JLabel lblNewLabel_5_1_4_4_2_2_2 = new JLabel("Sotanghon");
		lblNewLabel_5_1_4_4_2_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_2.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton sotanghon_btn = new JButton("");
		sotanghon_btn.setFocusPainted(false);
		sotanghon_btn.setMargin(new Insets(0, 0, 0, 0));
		sotanghon_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_2 = new GroupLayout(panel_5_1_4_4_2_2_2);
		gl_panel_5_1_4_4_2_2_2.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_2.createParallelGroup(Alignment.LEADING)
				.addGap(0, 172, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_2, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(sotanghon_btn, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_2.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_2.createParallelGroup(Alignment.LEADING)
				.addGap(0, 202, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_2.createSequentialGroup()
					.addComponent(sotanghon_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_2, GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_2.setLayout(gl_panel_5_1_4_4_2_2_2);
		
		JPanel panel_5_1_4_4_2_2_3 = new JPanel();
		panel_5_1_4_4_2_2_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.add(panel_5_1_4_4_2_2_3);
		
		JLabel lblNewLabel_5_1_4_4_2_2_3 = new JLabel("Miki Guisado");
		lblNewLabel_5_1_4_4_2_2_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_3.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton miki_btn = new JButton("");
		miki_btn.setFocusPainted(false);
		miki_btn.setMargin(new Insets(0, 0, 0, 0));
		miki_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_3 = new GroupLayout(panel_5_1_4_4_2_2_3);
		gl_panel_5_1_4_4_2_2_3.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_3.createParallelGroup(Alignment.LEADING)
				.addGap(0, 172, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_3, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(miki_btn, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_3.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_3.createParallelGroup(Alignment.LEADING)
				.addGap(0, 202, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_3.createSequentialGroup()
					.addComponent(miki_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_3, GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_3.setLayout(gl_panel_5_1_4_4_2_2_3);
		
		JPanel panel_5_1_4_4_2_2_4 = new JPanel();
		panel_5_1_4_4_2_2_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.add(panel_5_1_4_4_2_2_4);
		
		JLabel lblNewLabel_5_1_4_4_2_2_4 = new JLabel("Chami");
		lblNewLabel_5_1_4_4_2_2_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_4.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton chami_btn = new JButton("");
		chami_btn.setFocusPainted(false);
		chami_btn.setMargin(new Insets(0, 0, 0, 0));
		chami_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_4 = new GroupLayout(panel_5_1_4_4_2_2_4);
		gl_panel_5_1_4_4_2_2_4.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_4.createParallelGroup(Alignment.LEADING)
				.addGap(0, 172, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_4.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_4, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(chami_btn, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_4.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_4.createParallelGroup(Alignment.LEADING)
				.addGap(0, 202, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_4.createSequentialGroup()
					.addComponent(chami_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_4, GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_4.setLayout(gl_panel_5_1_4_4_2_2_4);
		
		JPanel panel_5_1_4_4_2_2_5 = new JPanel();
		panel_5_1_4_4_2_2_5.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.add(panel_5_1_4_4_2_2_5);
		
		JLabel lblNewLabel_5_1_4_4_2_2_5 = new JLabel("Pancit Canton");
		lblNewLabel_5_1_4_4_2_2_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_5.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton canton_btn = new JButton("");
		canton_btn.setFocusPainted(false);
		canton_btn.setMargin(new Insets(0, 0, 0, 0));
		canton_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_5 = new GroupLayout(panel_5_1_4_4_2_2_5);
		gl_panel_5_1_4_4_2_2_5.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_5.createParallelGroup(Alignment.LEADING)
				.addGap(0, 172, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_5.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_5, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(canton_btn, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_5.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_5.createParallelGroup(Alignment.LEADING)
				.addGap(0, 202, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_5.createSequentialGroup()
					.addComponent(canton_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_5, GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_5.setLayout(gl_panel_5_1_4_4_2_2_5);
		
		JPanel panel_5_1_4_4_2_2_6 = new JPanel();
		panel_5_1_4_4_2_2_6.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.add(panel_5_1_4_4_2_2_6);
		
		JLabel lblNewLabel_5_1_4_4_2_2_6 = new JLabel("Palabok");
		lblNewLabel_5_1_4_4_2_2_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_6.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton palabok_btn = new JButton("");
		palabok_btn.setFocusPainted(false);
		palabok_btn.setMargin(new Insets(0, 0, 0, 0));
		palabok_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_6 = new GroupLayout(panel_5_1_4_4_2_2_6);
		gl_panel_5_1_4_4_2_2_6.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_6.createParallelGroup(Alignment.LEADING)
				.addGap(0, 172, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_6.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_6, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(palabok_btn, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_6.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_6.createParallelGroup(Alignment.LEADING)
				.addGap(0, 202, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_6.createSequentialGroup()
					.addComponent(palabok_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_6, GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_6.setLayout(gl_panel_5_1_4_4_2_2_6);
		
		JPanel panel_5_1_4_4_2_2_7 = new JPanel();
		panel_5_1_4_4_2_2_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.add(panel_5_1_4_4_2_2_7);
		
		JLabel lblNewLabel_5_1_4_4_2_2_7 = new JLabel("Lugaw");
		lblNewLabel_5_1_4_4_2_2_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_7.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton lugaw_btn = new JButton("");
		lugaw_btn.setFocusPainted(false);
		lugaw_btn.setMargin(new Insets(0, 0, 0, 0));
		lugaw_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_7 = new GroupLayout(panel_5_1_4_4_2_2_7);
		gl_panel_5_1_4_4_2_2_7.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_7.createParallelGroup(Alignment.LEADING)
				.addGap(0, 172, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_7.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_7, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(lugaw_btn, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_7.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_7.createParallelGroup(Alignment.LEADING)
				.addGap(0, 202, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_7.createSequentialGroup()
					.addComponent(lugaw_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_7, GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_7.setLayout(gl_panel_5_1_4_4_2_2_7);
		
		
		
		
    ;
		
		JPanel drinks_panel = new JPanel();
		centerPanel.add(drinks_panel, "name_1343865071917800");
		drinks_panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1_1 = new JScrollPane();
		scrollPane_1_1.setBorder(new MatteBorder(0, 0, 1, 0, new Color(130, 135, 144)));
		scrollPane_1_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_1_1.setPreferredSize(new Dimension(180, 1000));
		drinks_panel.add(scrollPane_1_1, BorderLayout.CENTER);
		
		JPanel panel_2_1 = new JPanel();
		panel_2_1.setPreferredSize(new Dimension(180, 10));
		scrollPane_1_1.setViewportView(panel_2_1);
		panel_2_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		//Drinks panel scroll bar
			scrollBar(panel_2_1, scrollPane_1_1);
		
		JPanel panel_5_1_4_4_2_2_8 = new JPanel();
		panel_5_1_4_4_2_2_8.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2_1.add(panel_5_1_4_4_2_2_8);
		
		JLabel lblNewLabel_5_1_4_4_2_2_8 = new JLabel("Coke");
		lblNewLabel_5_1_4_4_2_2_8.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_8.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton coke_btn = new JButton("");
		coke_btn.setFocusPainted(false);
		coke_btn.setMargin(new Insets(0, 0, 0, 0));
		coke_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_8 = new GroupLayout(panel_5_1_4_4_2_2_8);
		gl_panel_5_1_4_4_2_2_8.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_8.createParallelGroup(Alignment.LEADING)
				.addGap(0, 172, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_8.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_8, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(coke_btn, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_8.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_8.createParallelGroup(Alignment.LEADING)
				.addGap(0, 202, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_8.createSequentialGroup()
					.addComponent(coke_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_8, GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_8.setLayout(gl_panel_5_1_4_4_2_2_8);
		
		JPanel panel_5_1_4_4_2_2_1_1 = new JPanel();
		panel_5_1_4_4_2_2_1_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2_1.add(panel_5_1_4_4_2_2_1_1);
		
		JLabel lblNewLabel_5_1_4_4_2_2_1_1 = new JLabel("Sprite");
		lblNewLabel_5_1_4_4_2_2_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_1_1.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton sprite_btn = new JButton("");		
		sprite_btn.setFocusPainted(false);
		sprite_btn.setMargin(new Insets(0, 0, 0, 0));
		sprite_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_1_1 = new GroupLayout(panel_5_1_4_4_2_2_1_1);
		gl_panel_5_1_4_4_2_2_1_1.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_1_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 174, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_1_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_1_1, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(sprite_btn, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_1_1.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_1_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 204, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_1_1.createSequentialGroup()
					.addComponent(sprite_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_1_1, GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_1_1.setLayout(gl_panel_5_1_4_4_2_2_1_1);
		
		JPanel panel_5_1_4_4_2_2_2_1 = new JPanel();
		panel_5_1_4_4_2_2_2_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2_1.add(panel_5_1_4_4_2_2_2_1);
		
		JLabel lblNewLabel_5_1_4_4_2_2_2_1 = new JLabel("Iced Tea");
		lblNewLabel_5_1_4_4_2_2_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_2_1.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton icedTea_btn = new JButton("");
		icedTea_btn.setFocusPainted(false);
		icedTea_btn.setMargin(new Insets(0, 0, 0, 0));
		icedTea_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_2_1 = new GroupLayout(panel_5_1_4_4_2_2_2_1);
		gl_panel_5_1_4_4_2_2_2_1.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_2_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 174, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_2_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_2_1, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(icedTea_btn, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_2_1.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_2_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 204, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_2_1.createSequentialGroup()
					.addComponent(icedTea_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_2_1, GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_2_1.setLayout(gl_panel_5_1_4_4_2_2_2_1);
		
		JPanel panel_5_1_4_4_2_2_3_1 = new JPanel();
		panel_5_1_4_4_2_2_3_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2_1.add(panel_5_1_4_4_2_2_3_1);
		
		JLabel lblNewLabel_5_1_4_4_2_2_3_1 = new JLabel("Iced Coffee");
		lblNewLabel_5_1_4_4_2_2_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_3_1.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton icedCoffee_btn = new JButton("");
		icedCoffee_btn.setFocusPainted(false);
		icedCoffee_btn.setMargin(new Insets(0, 0, 0, 0));
		icedCoffee_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_3_1 = new GroupLayout(panel_5_1_4_4_2_2_3_1);
		gl_panel_5_1_4_4_2_2_3_1.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_3_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 174, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_3_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_3_1, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(icedCoffee_btn, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_3_1.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_3_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 204, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_3_1.createSequentialGroup()
					.addComponent(icedCoffee_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_3_1, GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_3_1.setLayout(gl_panel_5_1_4_4_2_2_3_1);
		
		JPanel panel_5_1_4_4_2_2_4_1 = new JPanel();
		panel_5_1_4_4_2_2_4_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2_1.add(panel_5_1_4_4_2_2_4_1);
		
		JLabel lblNewLabel_5_1_4_4_2_2_4_1 = new JLabel("Lemonade");
		lblNewLabel_5_1_4_4_2_2_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_4_1.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton lemonade_btn = new JButton("");
		lemonade_btn.setFocusPainted(false);
		lemonade_btn.setMargin(new Insets(0, 0, 0, 0));
		lemonade_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_4_1 = new GroupLayout(panel_5_1_4_4_2_2_4_1);
		gl_panel_5_1_4_4_2_2_4_1.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_4_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 174, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_4_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_4_1, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(lemonade_btn, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_4_1.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_4_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 204, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_4_1.createSequentialGroup()
					.addComponent(lemonade_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_4_1, GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_4_1.setLayout(gl_panel_5_1_4_4_2_2_4_1);
		
		JPanel panel_5_1_4_4_2_2_5_1 = new JPanel();
		panel_5_1_4_4_2_2_5_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2_1.add(panel_5_1_4_4_2_2_5_1);
		
		JLabel lblNewLabel_5_1_4_4_2_2_5_1 = new JLabel("Mountain Dew");
		lblNewLabel_5_1_4_4_2_2_5_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_4_4_2_2_5_1.setFont(new Font("Arial Black", Font.BOLD, 16));
		
		JButton mountainDew_btn = new JButton("");
		mountainDew_btn.setFocusPainted(false);
		mountainDew_btn.setMargin(new Insets(0, 0, 0, 0));
		mountainDew_btn.setBackground(Color.WHITE);
		GroupLayout gl_panel_5_1_4_4_2_2_5_1 = new GroupLayout(panel_5_1_4_4_2_2_5_1);
		gl_panel_5_1_4_4_2_2_5_1.setHorizontalGroup(
			gl_panel_5_1_4_4_2_2_5_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 174, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_5_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_5_1_4_4_2_2_5_1, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(mountainDew_btn, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
		);
		gl_panel_5_1_4_4_2_2_5_1.setVerticalGroup(
			gl_panel_5_1_4_4_2_2_5_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 204, Short.MAX_VALUE)
				.addGroup(gl_panel_5_1_4_4_2_2_5_1.createSequentialGroup()
					.addComponent(mountainDew_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5_1_4_4_2_2_5_1, GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_5_1_4_4_2_2_5_1.setLayout(gl_panel_5_1_4_4_2_2_5_1);
		
		JPanel panel_5_1_4_4_2_2_8_1 = new JPanel();
        panel_5_1_4_4_2_2_8_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_2_1.add(panel_5_1_4_4_2_2_8_1);
        
        JLabel lblNewLabel_5_1_4_4_2_2_8_1 = new JLabel("Orange Juice");
        lblNewLabel_5_1_4_4_2_2_8_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_5_1_4_4_2_2_8_1.setFont(new Font("Arial Black", Font.BOLD, 16));
        
        JButton orangeJuice_btn = new JButton("");
        orangeJuice_btn.setMargin(new Insets(0, 0, 0, 0));
        orangeJuice_btn.setFocusPainted(false);
        orangeJuice_btn.setBackground(Color.WHITE);
        GroupLayout gl_panel_5_1_4_4_2_2_8_1 = new GroupLayout(panel_5_1_4_4_2_2_8_1);
        gl_panel_5_1_4_4_2_2_8_1.setHorizontalGroup(
        	gl_panel_5_1_4_4_2_2_8_1.createParallelGroup(Alignment.LEADING)
        		.addGap(0, 174, Short.MAX_VALUE)
        		.addGroup(gl_panel_5_1_4_4_2_2_8_1.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblNewLabel_5_1_4_4_2_2_8_1, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
        			.addContainerGap())
        		.addComponent(orangeJuice_btn, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
        );
        gl_panel_5_1_4_4_2_2_8_1.setVerticalGroup(
        	gl_panel_5_1_4_4_2_2_8_1.createParallelGroup(Alignment.LEADING)
        		.addGap(0, 204, Short.MAX_VALUE)
        		.addGroup(gl_panel_5_1_4_4_2_2_8_1.createSequentialGroup()
        			.addComponent(orangeJuice_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblNewLabel_5_1_4_4_2_2_8_1, GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)
        			.addContainerGap())
        );
        panel_5_1_4_4_2_2_8_1.setLayout(gl_panel_5_1_4_4_2_2_8_1);
        
        JPanel panel_5_1_4_4_2_2_8_2 = new JPanel();
        panel_5_1_4_4_2_2_8_2.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_2_1.add(panel_5_1_4_4_2_2_8_2);
        
        JLabel lblNewLabel_5_1_4_4_2_2_8_2 = new JLabel("Milk Tea");
        lblNewLabel_5_1_4_4_2_2_8_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_5_1_4_4_2_2_8_2.setFont(new Font("Arial Black", Font.BOLD, 16));
        
        JButton milkTea_btn = new JButton("");
        milkTea_btn.setMargin(new Insets(0, 0, 0, 0));
        milkTea_btn.setFocusPainted(false);
        milkTea_btn.setBackground(Color.WHITE);
        GroupLayout gl_panel_5_1_4_4_2_2_8_2 = new GroupLayout(panel_5_1_4_4_2_2_8_2);
        gl_panel_5_1_4_4_2_2_8_2.setHorizontalGroup(
        	gl_panel_5_1_4_4_2_2_8_2.createParallelGroup(Alignment.LEADING)
        		.addGap(0, 174, Short.MAX_VALUE)
        		.addGroup(gl_panel_5_1_4_4_2_2_8_2.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblNewLabel_5_1_4_4_2_2_8_2, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
        			.addContainerGap())
        		.addComponent(milkTea_btn, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
        );
        gl_panel_5_1_4_4_2_2_8_2.setVerticalGroup(
        	gl_panel_5_1_4_4_2_2_8_2.createParallelGroup(Alignment.LEADING)
        		.addGap(0, 204, Short.MAX_VALUE)
        		.addGroup(gl_panel_5_1_4_4_2_2_8_2.createSequentialGroup()
        			.addComponent(milkTea_btn, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblNewLabel_5_1_4_4_2_2_8_2, GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)
        			.addContainerGap())
        );
        panel_5_1_4_4_2_2_8_2.setLayout(gl_panel_5_1_4_4_2_2_8_2);
		
		JPanel panel_3_1 = new JPanel();
		panel_3_1.setPreferredSize(new Dimension(10, 90));
		drinks_panel.add(panel_3_1, BorderLayout.SOUTH);
		panel_3_1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton small_btn = new JButton("<html><div style='text-align: center;'>Small<br></div></html>");
		small_btn.setMargin(new Insets(2, 2, 2, 2));
		small_btn.setPreferredSize(new Dimension(92, 80));
		small_btn.setFont(new Font("Arial Black", Font.BOLD, 14));
		small_btn.setFocusPainted(false);
		small_btn.setBackground(new Color(192, 192, 192));
		panel_3_1.add(small_btn);
		
		JButton medium_btn = new JButton("<html><div style='text-align: center;'>Medium<br></div></html>");
		medium_btn.setMargin(new Insets(2, 2, 2, 2));
		medium_btn.setPreferredSize(new Dimension(92, 80));
		medium_btn.setFont(new Font("Arial Black", Font.BOLD, 14));
		medium_btn.setFocusPainted(false);
		medium_btn.setBackground(new Color(192, 192, 192));
		panel_3_1.add(medium_btn);
		
		JButton large_btn = new JButton("<html><div style='text-align: center;'>Large<br></div></html>");
		large_btn.setMargin(new Insets(2, 2, 2, 2));
		large_btn.setPreferredSize(new Dimension(92, 80));
		large_btn.setFont(new Font("Arial Black", Font.BOLD, 14));
		large_btn.setFocusPainted(false);
		large_btn.setBackground(new Color(192, 192, 192));
		panel_3_1.add(large_btn);
		
		JPanel settings_panel = new JPanel();
		settings_panel.setBorder(null);
		centerPanel.add(settings_panel, "name_1344575977168100");
		settings_panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2_1_1 = new JPanel();
		panel_2_1_1.setPreferredSize(new Dimension(100, 10));
		panel_2_1_1.setBorder(null);
		settings_panel.add(panel_2_1_1, BorderLayout.CENTER);
		panel_2_1_1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JButton void_transaction_btn = new JButton("<html><div style='text-align: center;'>Void<br>Transaction</div></html>");		
		void_transaction_btn.setForeground(new Color(233, 233, 233));
		void_transaction_btn.setPreferredSize(new Dimension(160, 85));
		void_transaction_btn.setFont(new Font("Arial Black", Font.BOLD, 14));
		void_transaction_btn.setFocusPainted(false);
		void_transaction_btn.setBackground(new Color(177, 33, 33));
		panel_2_1_1.add(void_transaction_btn);
		
		JButton dailySales_btn = new JButton("Daily Sales");
		dailySales_btn.setForeground(new Color(233, 233, 233));
		dailySales_btn.setPreferredSize(new Dimension(160, 85));
		dailySales_btn.setFont(new Font("Arial Black", Font.BOLD, 14));
		dailySales_btn.setFocusPainted(false);
		dailySales_btn.setBackground(new Color(55, 143, 48));
		panel_2_1_1.add(dailySales_btn);
		
		JButton createAcc_btn = new JButton("<html><div style='text-align: center;'>Create<br>Account</div></html>");
		createAcc_btn.setPreferredSize(new Dimension(160, 85));
		createAcc_btn.setForeground(new Color(233, 233, 233));
		createAcc_btn.setFont(new Font("Arial Black", Font.BOLD, 14));
		createAcc_btn.setFocusPainted(false);
		createAcc_btn.setBackground(new Color(60, 103, 142));
		panel_2_1_1.add(createAcc_btn);
		
		JButton changePass_btn = new JButton("<html><div style='text-align: center;'>Change<br>Password</div></html>");		
		changePass_btn.setForeground(new Color(233, 233, 233));
		changePass_btn.setPreferredSize(new Dimension(160, 85));
		changePass_btn.setFont(new Font("Arial Black", Font.BOLD, 14));
		changePass_btn.setFocusPainted(false);
		changePass_btn.setBackground(new Color(215, 172, 0));
		panel_2_1_1.add(changePass_btn);
		
		JPanel eastPanel = new JPanel();
		eastPanel.setBackground(new Color(255, 255, 255));
		eastPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		mainPane.add(eastPanel, BorderLayout.EAST);
		
		num_textField = new JTextField();
		num_textField.setForeground(new Color(0, 0, 0));
		num_textField.setFont(new Font("Arial", Font.BOLD, 13));
		num_textField.setMargin(new Insets(6, 6, 6, 6));
		num_textField.setColumns(10);
		
		JButton discount_btn = new JButton("Discount");
		discount_btn.setFocusPainted(false);
		discount_btn.setBackground(new Color(255, 128, 64));
		discount_btn.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JButton printReceipt_btn = new JButton("Print Receipt");
		printReceipt_btn.setFocusPainted(false);
		printReceipt_btn.setBackground(new Color(0, 128, 255));
		printReceipt_btn.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JButton cash_btn = new JButton("Cash");
		cash_btn.setFocusPainted(false);
		cash_btn.setBackground(new Color(0, 189, 0));
		cash_btn.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JButton btn_9 = new JButton("9");
		btn_9.setFocusPainted(false);
		btn_9.setBackground(new Color(178, 178, 178));
		btn_9.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JButton btn_4 = new JButton("4");
		btn_4.setFocusPainted(false);
		btn_4.setBackground(new Color(178, 178, 178));
		btn_4.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JButton btn_5 = new JButton("5");
		btn_5.setFocusPainted(false);
		btn_5.setBackground(new Color(178, 178, 178));
		btn_5.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JButton btn_6 = new JButton("6");
		btn_6.setFocusPainted(false);
		btn_6.setBackground(new Color(178, 178, 178));
		btn_6.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JButton btn_1 = new JButton("1");
		btn_1.setFocusPainted(false);
		btn_1.setBackground(new Color(178, 178, 178));
		btn_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JButton btn_2 = new JButton("2");
		btn_2.setFocusPainted(false);
		btn_2.setBackground(new Color(178, 178, 178));
		btn_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JButton btn_3 = new JButton("3");
		btn_3.setFocusPainted(false);
		btn_3.setBackground(new Color(178, 178, 178));
		btn_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JButton clearItem_btn = new JButton("<html><div style='text-align: center;'>Clear<br>Item</div></html>");
		clearItem_btn.setFocusPainted(false);
		clearItem_btn.setBackground(new Color(255, 79, 79));
		clearItem_btn.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JButton btn_0 = new JButton("0");
		btn_0.setFocusPainted(false);
		btn_0.setBackground(new Color(178, 178, 178));
		btn_0.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JButton clear_btn = new JButton("Clear");
		clear_btn.setFocusPainted(false);
		clear_btn.setBackground(new Color(255, 255, 85));
		
		clear_btn.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel dfgd = new JLabel("Sub Total:");
		dfgd.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel asdas = new JLabel("Discount:");
		asdas.setHorizontalAlignment(SwingConstants.RIGHT);
		asdas.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel ghjgh = new JLabel("Total:");
		ghjgh.setHorizontalAlignment(SwingConstants.RIGHT);
		ghjgh.setFont(new Font("Arial", Font.BOLD, 12));
			
		subtotal_label.setHorizontalAlignment(SwingConstants.RIGHT);
		subtotal_label.setFont(new Font("Arial", Font.BOLD, 12));
				
		discount_label.setHorizontalAlignment(SwingConstants.RIGHT);
		discount_label.setFont(new Font("Arial", Font.BOLD, 12));
	
		total_label.setHorizontalAlignment(SwingConstants.RIGHT);
		total_label.setFont(new Font("Arial", Font.BOLD, 12));	

		cash_label.setHorizontalAlignment(SwingConstants.RIGHT);
		cash_label.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel subtotal_label_3 = new JLabel("Cash:");
		subtotal_label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		subtotal_label_3.setFont(new Font("Arial", Font.BOLD, 12));

		change_label.setHorizontalAlignment(SwingConstants.RIGHT);
		change_label.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel subtotal_label_3_1 = new JLabel("Change:");
		subtotal_label_3_1.setHorizontalAlignment(SwingConstants.RIGHT);
		subtotal_label_3_1.setFont(new Font("Arial", Font.BOLD, 12));
		
		JButton btn_8 = new JButton("8");
		btn_8.setFocusPainted(false);
		btn_8.setBackground(new Color(178, 178, 178));
		btn_8.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JButton btn_7 = new JButton("7");
		btn_7.setFocusPainted(false);
		btn_7.setBackground(new Color(178, 178, 178));
		btn_7.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JLabel asdas_1 = new JLabel("VAT:");
		asdas_1.setHorizontalAlignment(SwingConstants.RIGHT);
		asdas_1.setFont(new Font("Arial", Font.BOLD, 12));
		
		vat_label.setHorizontalAlignment(SwingConstants.RIGHT);
		vat_label.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel lblNewLabel = new JLabel("Enter Amount:");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 12));
		GroupLayout gl_eastPanel = new GroupLayout(eastPanel);
		gl_eastPanel.setHorizontalGroup(
			gl_eastPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_eastPanel.createSequentialGroup()
					.addGroup(gl_eastPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_eastPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btn_7, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btn_8, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btn_9, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_eastPanel.createSequentialGroup()
							.addGap(14)
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(discount_btn, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
								.addComponent(printReceipt_btn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
								.addComponent(cash_btn, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_eastPanel.createSequentialGroup()
									.addComponent(btn_4, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
									.addGap(6)
									.addComponent(btn_5, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
									.addGap(6)
									.addComponent(btn_6, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_eastPanel.createSequentialGroup()
									.addComponent(btn_1, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
									.addGap(6)
									.addComponent(btn_2, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
									.addGap(6)
									.addComponent(btn_3, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_eastPanel.createSequentialGroup()
									.addComponent(clearItem_btn, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btn_0, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
									.addGap(6)
									.addComponent(clear_btn, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))))
					.addGap(14))
				.addGroup(gl_eastPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_eastPanel.createSequentialGroup()
					.addGap(14)
					.addGroup(gl_eastPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_eastPanel.createSequentialGroup()
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_eastPanel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED, 153, Short.MAX_VALUE)
									.addGroup(gl_eastPanel.createParallelGroup(Alignment.TRAILING, false)
										.addGroup(gl_eastPanel.createSequentialGroup()
											.addComponent(subtotal_label_3_1, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(change_label, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_eastPanel.createSequentialGroup()
											.addComponent(subtotal_label_3, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(cash_label, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))))
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(ghjgh, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
								.addComponent(asdas_1, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addComponent(asdas, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addComponent(dfgd))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(total_label, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
								.addComponent(discount_label, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(vat_label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(subtotal_label, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
							.addGap(20))
						.addGroup(gl_eastPanel.createSequentialGroup()
							.addComponent(num_textField, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_eastPanel.setVerticalGroup(
			gl_eastPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_eastPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
					.addGap(11)
					.addGroup(gl_eastPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_eastPanel.createSequentialGroup()
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(subtotal_label_3)
								.addComponent(cash_label))
							.addGap(11)
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(subtotal_label_3_1)
								.addComponent(change_label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(gl_eastPanel.createSequentialGroup()
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(dfgd)
								.addComponent(subtotal_label))
							.addGap(11)
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(asdas_1)
								.addComponent(vat_label))
							.addGap(12)
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(asdas)
								.addComponent(discount_label, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(ghjgh)
								.addComponent(total_label)
								.addComponent(lblNewLabel))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addComponent(num_textField, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_eastPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btn_9, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_8, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_7, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_eastPanel.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_eastPanel.createSequentialGroup()
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(btn_4, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(btn_5, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(btn_6, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
							.addGap(6)
							.addGroup(gl_eastPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(btn_1, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(btn_2, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(btn_3, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_eastPanel.createSequentialGroup()
							.addComponent(discount_btn, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(cash_btn, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_eastPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_eastPanel.createParallelGroup(Alignment.TRAILING)
							.addComponent(btn_0, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addComponent(clear_btn, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_eastPanel.createSequentialGroup()
								.addComponent(printReceipt_btn, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)))
						.addComponent(clearItem_btn, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
					.addGap(12))
		);
		
		// Insert the items in JList
		String[] columnNames = {"Quantity", "Name", "Size", "Price", "Total"};
		DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        // Make all cells non-editable
		        return false;
		    }
		};
		
		// Table for item list		
		JTable table = new JTable(model);
		
		TableColumnModel columnModel = table.getColumnModel();
		
		//Set column width
	    columnModel.getColumn(0).setPreferredWidth(50);   
        columnModel.getColumn(1).setPreferredWidth(150);  
        columnModel.getColumn(2).setPreferredWidth(75); 
        columnModel.getColumn(3).setPreferredWidth(100);  
        columnModel.getColumn(4).setPreferredWidth(100);  
        
        //Remove re sizable
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setResizable(false);
        }
        
        //Remove reordering
        table.getTableHeader().setReorderingAllowed(false);
       
        // Single row selection
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        
         
		table.setShowGrid(false);
		table.setFocusable(false);
		table.setShowVerticalLines(false);
				
		scrollPane.setViewportView(table);
		eastPanel.setLayout(gl_eastPanel);		
		
		// Void Transaction Table
		JPanel void_transaction_panel = new JPanel();
        centerPanel.add(void_transaction_panel, "name_340298329054200");
        void_transaction_panel.setLayout(new BorderLayout(0, 0));
        
        JPanel panel_3_2 = new JPanel();
        panel_3_2.setPreferredSize(new Dimension(10, 90));
        void_transaction_panel.add(panel_3_2, BorderLayout.SOUTH);
        panel_3_2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        
        JButton remove_btn = new JButton("<html><div style='text-align: center;'>Void<br></div></html>");
        remove_btn.setForeground(new Color(223, 223, 223));
        remove_btn.setPreferredSize(new Dimension(92, 80));
        remove_btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        remove_btn.setFocusPainted(false);
        remove_btn.setBackground(new Color(177, 33, 33));
        panel_3_2.add(remove_btn);
        
        JScrollPane scrollPane_2 = new JScrollPane();
        void_transaction_panel.add(scrollPane_2, BorderLayout.CENTER);
        scrollPane_2.setBorder(new MatteBorder(0, 0, 1, 0, new Color(130, 135, 144)));
        
        String[] voidTableColumnNames = {
        	    "Transaction ID", 
        	    "Date", 
        	    "Time",  
        	    "User ID", 
        	    "Total Amount", 
        	    "Total Payment",       	   
        	    "Discount Amount"
        	};
        
        DefaultTableModel voidModel = new DefaultTableModel(voidTableColumnNames, 0){
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        // Make all cells non-editable
		        return false;
		    }
		};
        voidTable = new JTable(voidModel);
        scrollPane_2.setViewportView(voidTable);                      
        TableColumnModel VoidColumnModel = voidTable.getColumnModel();
        
        //Remove re sizable
        for (int i = 0; i < VoidColumnModel.getColumnCount(); i++) {
            TableColumn column = VoidColumnModel.getColumn(i);
            column.setResizable(false);
        }
		
        //Remove reordering
        voidTable.getTableHeader().setReorderingAllowed(false);
        voidTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        
        voidTable.setShowGrid(false);
        voidTable.setFocusable(false);
        voidTable.setShowVerticalLines(false);
        
        // Daily Sales Table
        JPanel daily_sales_panel = new JPanel();
        centerPanel.add(daily_sales_panel, "name_340334437515800");	
        daily_sales_panel.setLayout(new BorderLayout(0, 0));
        
        JPanel panel_3_2_1 = new JPanel();
        panel_3_2_1.setPreferredSize(new Dimension(10, 90));
        daily_sales_panel.add(panel_3_2_1, BorderLayout.SOUTH);
        panel_3_2_1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        
        JButton generateReport_btn = new JButton("<html><div style='text-align: center;'>Generate<br>Sales Report</div></html>");
        generateReport_btn.setMargin(new Insets(2, 2, 2, 2));
        generateReport_btn.setForeground(new Color(233, 233, 233));
        generateReport_btn.setPreferredSize(new Dimension(92, 80));
        generateReport_btn.setFont(new Font("Arial Black", Font.BOLD, 12));
        generateReport_btn.setFocusPainted(false);
        generateReport_btn.setBackground(new Color(55, 143, 48));
        panel_3_2_1.add(generateReport_btn);
        
        JScrollPane scrollPane_3 = new JScrollPane();
        scrollPane_3.setBorder(new MatteBorder(0, 0, 1, 0, new Color(130, 135, 144)));
        daily_sales_panel.add(scrollPane_3, BorderLayout.CENTER);
        
        String[] dailySalesColumn = {
        	    "Daily Sales ID", 
        	    "Daily Sales Date",         	   
        	    "Total Daily Discount", 
        	    "Total Daily Sales"       	          	  
        	};
        
        DefaultTableModel dailySalesModel = new DefaultTableModel(dailySalesColumn, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };
        
        dailySales_table = new JTable(dailySalesModel);
        scrollPane_3.setViewportView(dailySales_table);
        TableColumnModel dailySalesColumnModel = dailySales_table.getColumnModel();
        
        //Remove re sizable
        for (int i = 0; i < dailySalesColumnModel.getColumnCount(); i++) {
            TableColumn column = dailySalesColumnModel.getColumn(i);
            column.setResizable(false);
        }
		
        //Remove reordering
        dailySales_table.getTableHeader().setReorderingAllowed(false);
        dailySales_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        
        dailySales_table.setShowGrid(false);
        dailySales_table.setFocusable(false);
        dailySales_table.setShowVerticalLines(false);
        
        //
        createAcc_btn.setVisible(false);
        if(userType == "admin") {
        	createAcc_btn.setVisible(true);
        }
        
		//Event handlers
        
        //Side buttons
        meal_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 meals_panel.setVisible(true);
                 drinks_panel.setVisible(false);
                 settings_panel.setVisible(false);
                 void_transaction_panel.setVisible(false);
                 daily_sales_panel.setVisible(false);
                
        	}
        });
        drinks_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 meals_panel.setVisible(false);
                 drinks_panel.setVisible(true);
                 settings_panel.setVisible(false);
                 void_transaction_panel.setVisible(false);
                 daily_sales_panel.setVisible(false);
        	}
        });
        
        settings_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		settings_panel.setVisible(true);
        		meals_panel.setVisible(false);
                drinks_panel.setVisible(false);
                void_transaction_panel.setVisible(false);
                daily_sales_panel.setVisible(false);
                
             
        	}
        });
        
        // Add logo image
        SwingUtilities.invokeLater(() -> {
  			Image img = new ImageIcon(this.getClass().getResource("/logo.png")).getImage();
  	    	Image imgScale = img.getScaledInstance(logo_photo.getWidth(), logo_photo.getHeight(), Image.SCALE_SMOOTH);
  	        ImageIcon scaledIcon = new ImageIcon(imgScale);
  	      logo_photo.setIcon(scaledIcon);
 		});	
        
        // Add product images
        
        // Set the images for meals
	    scaleImage("/lomi.png", lomi_btn);
	    scaleImage("/bihon.png", bihon_btn);
	    scaleImage("/sotanghon.png", sotanghon_btn);
	    scaleImage("/guisado.png", miki_btn);
	    scaleImage("/chami.png", chami_btn);
	    scaleImage("/canton.png", canton_btn);
	    scaleImage("/palabok.png", palabok_btn);
	    scaleImage("/lugaw.png", lugaw_btn);
        
	    // Set the images for drinks
	    scaleImage("/coke.png", coke_btn);
	    scaleImage("/sprite.png", sprite_btn);
	    scaleImage("/icedtea.png", icedTea_btn);
	    scaleImage("/iced coffee.png", icedCoffee_btn);
	    scaleImage("/lemonade.png", lemonade_btn);
	  	scaleImage("/mt dew.png", mountainDew_btn);
	  	scaleImage("/juice.png", orangeJuice_btn);
	  	scaleImage("/milktea.png", milkTea_btn);
	   	   	    
        //Number buttons
        numButton(btn_0, "0");
        numButton(btn_1, "1");
        numButton(btn_2, "2");
        numButton(btn_3, "3");
        numButton(btn_4, "4");
        numButton(btn_5, "5");
        numButton(btn_6, "6");
        numButton(btn_7, "7");
        numButton(btn_8, "8");
        numButton(btn_9, "9");
        
        
        // Clear number button
        clear_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String modifiedString = num_textField.getText().substring(0, num_textField.getText().length() - 1);
				displayNumber = modifiedString;
				num_textField.setText(modifiedString);
			}
		});
        
       
        // Clear item button
        clearItem_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = table.getSelectedRow(); // Get the selected row index from the JTable
                if (selectedIndex != -1) {
                    // Update the sub total every time an item is removed
                    ItemList itemData = itemList.get(selectedIndex);
                    System.out.println(itemData.getItemName());

                    orderSubTotal -= itemData.getEachTotalPrice();
                    orderTotal = orderSubTotal;

                    if (discountAmount != 0) {
                        discountAmount = orderSubTotal * (discountRate / 100.0);
                        orderTotal = orderSubTotal - discountAmount;
                    }
                    
                    if (cash != 0) {
                    	change = cash - orderTotal;
                        change_label.setText(String.format("%.2f", change));
                    }  
                    
                    
                    // Calculate VAT
                    calcVAT();

                    // Update labels
                    subtotal_label.setText(String.format("%.2f", orderSubTotal));
                    total_label.setText(String.format("%.2f", orderTotal));
                    discount_label.setText(String.format("%.2f", discountAmount));
                    
                    
                                     

                    itemList.remove(selectedIndex); // Remove the item from the itemList
                    model.removeRow(selectedIndex); // Remove the row from the table model
                    System.out.println(itemList);
                }
            }
        });


        // Product buttons
        
        //Meals
        productButton(lomi_btn, model, 100, "meal");
        productButton(bihon_btn, model, 102, "meal");
        productButton(sotanghon_btn, model, 103, "meal");
        productButton(miki_btn, model, 104, "meal");
        productButton(chami_btn, model, 105, "meal");
        productButton(canton_btn, model, 106, "meal");
        productButton(palabok_btn, model, 107, "meal");
        productButton(lugaw_btn, model, 108, "meal");
        
        //Drinks
        productButton(coke_btn, model, 109,"drink");
        productButton(sprite_btn, model, 110,"drink");
        productButton(icedTea_btn, model, 111,"drink");
        productButton(icedCoffee_btn, model, 112,"drink");
        productButton(lemonade_btn, model, 113,"drink");
        productButton(mountainDew_btn, model, 114,"drink");
        productButton(orangeJuice_btn, model, 115,"drink");
        productButton(milkTea_btn, model, 116,"drink");
        
       			
        // Size buttons
        sizeButton(regular_btn, 100);
        sizeButton(special_btn, 101);
        sizeButton(jumbo_btn, 102);
        sizeButton(small_btn, 103);
        sizeButton(medium_btn, 104);
        sizeButton(large_btn, 105);
               
        // Discount button
        discount_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
					
				openDiscountFrame();
				
			}
		});
        
        // Cash button
        cash_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cashString = num_textField.getText();				

				if(cashString.isEmpty()) {
					JOptionPane.showMessageDialog(POS_system.this, "Please input a valid cash amount!", "Invalid Amount", JOptionPane.WARNING_MESSAGE);      
				}
				else {
					cash = Double.parseDouble(cashString);
					if(cash < orderTotal) {
						JOptionPane.showMessageDialog(POS_system.this, "Please input a valid cash amount!", "Invalid Amount", JOptionPane.WARNING_MESSAGE);  
					}
					else {
						cash_label.setText(String.format("%.2f", cash));
						change = cash - orderTotal;
						change_label.setText(String.format("%.2f", change));
						num_textField.setText("");
					}				
				}				
			}
		});
        
        // Print receipt button
		printReceipt_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {						
				if(itemList.isEmpty()) {
					JOptionPane.showMessageDialog(POS_system.this, "Please make an order first!", "Order Required", JOptionPane.WARNING_MESSAGE);  
				}						
				
				else {					
					if(cash == 0 || orderTotal > cash) {
						JOptionPane.showMessageDialog(POS_system.this, "Please input a valid cash amount!", "Invalid Amount", JOptionPane.WARNING_MESSAGE);      
					}
					else {
						int answer = JOptionPane.showConfirmDialog(POS_system.this, "Are you sure you want to finalize transaction?", "Order Required", JOptionPane.OK_CANCEL_OPTION);
						
						if(answer == 0) {
							Transaction transaction = new Transaction(cash, change, orderSubTotal, vat, orderTotal, user_id, idNumber, customerName, customerAddress, discountAmount);
							
							System.out.println("User id: " + transaction.getUserId());
							
							PreparedStatement pstmt = null;
							
							ResultSet rs = null;
							
							try {	
								
								String insertTransactionsTB = "INSERT INTO orders_tb (" + idType + ", order_total, sub_total, vat, order_cash, order_change, customer_name, customer_address, order_discountId, order_discount_amount, order_date, order_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), CURTIME())";
								pstmt = con.prepareStatement(insertTransactionsTB, PreparedStatement.RETURN_GENERATED_KEYS);
								
								pstmt.setDouble(1, user_id);
								pstmt.setDouble(2, transaction.getOrderTotal());
					            pstmt.setDouble(3, transaction.getOrderSubTotal());
					            pstmt.setDouble(4, transaction.getVat());
					            pstmt.setDouble(5, transaction.getCash());
					            pstmt.setDouble(6, transaction.getChange());
					            pstmt.setString(7 , transaction.getCustomerName());
					            pstmt.setString(8, transaction.getCustomerAddress());
					            pstmt.setString(9, transaction.getDiscountId());
					            pstmt.setDouble(10, transaction.getDiscountAmount());
					            
								int rowsInserted = pstmt.executeUpdate();
								
								if (rowsInserted > 0) {
					                System.out.println("A new item was inserted successfully!");
					                
					                rs = pstmt.getGeneratedKeys();
					                if (rs.next()) {
					                    int id = rs.getInt(1);
					                    System.out.println("Inserted ID: " + id);
					                    String insertTItemTB = "INSERT INTO order_details_tb (order_id, item_details_id, item_quantity, each_total_price) VALUES (?, ?, ?, ?)";
					                    
					                    try (PreparedStatement pstmt2 = con.prepareStatement(insertTItemTB)) {
					                        System.out.println("Items:");
					                        for (ItemList item : itemList) {
					                        	pstmt2.setInt(1, id);                // transaction_id
					                        	pstmt2.setInt(2, item.getItemDetailsId());     // item_name
					                        	pstmt2.setInt(3, item.getItemQty());         // item_quantity
					                        	pstmt2.setDouble(4, item.getEachTotalPrice());     // item_size	                        	
					                            
					                            // Execute the update for each item
					                        	pstmt2.executeUpdate();	  
					                        	
					                        	Receipt printReceipt = new Receipt(con, id, userType);
					            		    	printReceipt.generateReceipt();
					                        }
					                    } catch (Exception ex) {
					                        System.out.println("Failed to insert transaction items into database");
					                        ex.printStackTrace();
					                    }
					                }
					            }
				                                 					
							}
							catch (Exception ex) {
			                 System.out.println("Failed to insert data");
			                 ex.printStackTrace();
							}
							 
							// EMpty Jlist, Arraylist and other variables
							itemList.clear();
							model.setRowCount(0);
			            	cash = change = orderSubTotal = orderTotal = vat = discountAmount = 0;
			            	discountId = "";
			            	
			            	// Set text
			            	cash_label.setText(String.format("%.2f", cash));				
							change_label.setText(String.format("%.2f", change));
							subtotal_label.setText(String.format("%.2f", orderSubTotal));
							total_label.setText(String.format("%.2f", orderTotal));
							discount_label.setText(String.format("%.2f", discountAmount));
							vat_label.setText(String.format("%.2f", vat));
					}
					
					}							
				}														
			}
		});
				
		// Daily Sales
		dailySales_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 if(userType == "cashier") {
        			 LoginForm loginForm = new LoginForm();
 					 loginForm.setLocationRelativeTo(null);
 					 loginForm.getFirstPanel().setVisible(false);
 					 loginForm.setLabel();
 					 loginForm.setIdType("admin_id");
 					 loginForm.setTable("admin_tb");
 					 loginForm.setUserType("admin");
 					 loginForm.setVisible(true);
 					
        		 }
        		 else if(userType == "admin") {
        			 daily_sales_panel.setVisible(true);       		
             		 void_transaction_panel.setVisible(false);
             		 meals_panel.setVisible(false);
                     drinks_panel.setVisible(false);
                     settings_panel.setVisible(false);
             		 
                     insertDailySales(con);	
                     displayDialySales(dailySalesModel);     
        		 }
				
				                          
			}
		});
		
		// Print sales report
		generateReport_btn.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		 int selectedRow = dailySales_table.getSelectedRow();	
	        		 if(selectedRow == -1) {
	         			JOptionPane.showMessageDialog(POS_system.this, "Please select daily sales!", "No Selected Sales", JOptionPane.WARNING_MESSAGE); 
	         		}
	                 if(selectedRow != -1) {
	                	int answer = JOptionPane.showConfirmDialog(POS_system.this, "Do you want to print a sales report?", "Print Sales Report", JOptionPane.OK_CANCEL_OPTION);	 					
	 					if(answer == 0) {
	 						Object dailySalesId = dailySalesModel.getValueAt(selectedRow, 0);
		                 	System.out.println("Daily Sales ID: " + dailySalesId);		                 	
	                 	    DailySalesReport dailySalesReport = new DailySalesReport(con, (int) dailySalesId);
	                 	    dailySalesReport.generateReport();
	                 	    JOptionPane.showMessageDialog(POS_system.this, "Sales report generated!", "Sales Report", JOptionPane.INFORMATION_MESSAGE);		                                  	
	 					}	               	
	                 }
	        	}
	        });
       										
		// Void transaction 
		void_transaction_btn.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		
	        		 if(userType == "cashier") {
	        			 LoginForm loginForm = new LoginForm();
	 					 loginForm.setLocationRelativeTo(null);
	 					 loginForm.getFirstPanel().setVisible(false);
	 					 loginForm.setLabel();
	 					 loginForm.setIdType("admin_id");
	 					 loginForm.setTable("admin_tb");
	 					 loginForm.setUserType("admin");
	 					 loginForm.setVisible(true);
	 					
	        		 }
	        		 else if(userType == "admin") {
	        			 void_transaction_panel.setVisible(true);
		        		 settings_panel.setVisible(false);
		        		 meals_panel.setVisible(false);
		                 drinks_panel.setVisible(false);
		                 settings_panel.setVisible(false);
		                 
		        		 displayTransactions(voidModel);
	        		 }
	        		 	        		 	        			                		
	        	}
	        });

		// Button to void selected transaction
		remove_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int selectedRow = voidTable.getSelectedRow();
        		if(selectedRow == -1) {
        			JOptionPane.showMessageDialog(POS_system.this, "Please select a transaction!", "No Selected Transaction", JOptionPane.WARNING_MESSAGE); 
        		}
        		if (selectedRow != -1) {
        			int answer = JOptionPane.showConfirmDialog(POS_system.this, "Are you sure you want to void this transaction?", "Void Transaction", JOptionPane.OK_CANCEL_OPTION);
					
					if(answer == 0) {
						Object transactionId = voidModel.getValueAt(selectedRow, 0);
	        			 
	        			 System.out.println(transactionId);
	        			 
	        			 String voidTransaction = "DELETE FROM orders_tb WHERE order_id = ?";
	        			 
	        			 // Query to insert data in order table archive
	        			 String archiveOrdersQuery = "INSERT INTO orders_tb_archive (order_id, cashier_id, admin_id, order_total, sub_total, vat, order_cash, order_change, customer_name, customer_address, order_discountId, order_discount_amount, order_date, order_time) " +
	                             "SELECT order_id, cashier_id, admin_id, order_total, sub_total, vat, order_cash, order_change, customer_name, customer_address, order_discountId, order_discount_amount, order_date, order_time " +
	                             "FROM orders_tb " +
	                             "WHERE order_id = ?";
	        			 
	        			 // Query to insert data in order details archive
	        			 String archiveOrderDetailsQuery = "INSERT INTO order_details_tb_archive (order_detailS_id, order_id, item_details_id, item_quantity, each_total_price) " +
	                                    "SELECT order_detail_id, order_id, item_details_id, item_quantity, each_total_price " +
	                                    "FROM order_details_tb " +
	                                    "WHERE order_id = ?";	        			        	
		        		
		        		 try  {
		        	            // Archive order into orders_tb_archive
		        	            try (PreparedStatement pstmtArchiveOrder = con.prepareStatement(archiveOrdersQuery)) {
		        	            	pstmtArchiveOrder.setInt(1, (int) transactionId);
		        	                int rowsAffectedArchive = pstmtArchiveOrder.executeUpdate();
		        	                System.out.println("Rows inserted into orders_tb_archive: " + rowsAffectedArchive);
		        	            }
		        	            
		        	            // Archive order details into orders_details_tb_archive
		        	            try (PreparedStatement pstmtArchiveOrderDetails = con.prepareStatement(archiveOrderDetailsQuery)) {
		        	            	pstmtArchiveOrderDetails.setInt(1, (int) transactionId);
		        	                int rowsAffectedArchive = pstmtArchiveOrderDetails.executeUpdate();
		        	                System.out.println("Rows inserted into orders_details_tb_archive: " + rowsAffectedArchive);
		        	            }

		        	            // Void transaction in orders_tb
		        	            try (PreparedStatement pstmtVoid = con.prepareStatement(voidTransaction)) {
		        	                pstmtVoid.setInt(1,  (int) transactionId);
		        	                int rowsAffectedVoid = pstmtVoid.executeUpdate();
		        	                System.out.println("Transaction voided in orders_tb: " + rowsAffectedVoid);

		        	                // Example: Display updated transactions after voiding
		        	                displayTransactions(voidModel);
		        	            }

		        	        } catch (SQLException ex) {
		        	            ex.printStackTrace();
		        	        }
					}
        			 
	        	    }
        			     		
        	}
        });
		
		// Create account
		createAcc_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.showConfirmDialog(POS_system.this, "Are you sure you want to leave this page?", "Create Account", JOptionPane.OK_CANCEL_OPTION);
				
				if(answer == 0) {
					POS_system.this.dispose();
					CreateAcc createAcc = new CreateAcc();
					createAcc.setLocationRelativeTo(null);
					createAcc.setVisible(true);				
				}				
			}
		});
		
		// Change password button
		changePass_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.showConfirmDialog(POS_system.this, "Are you sure you want to leave this page?", "Change Password", JOptionPane.OK_CANCEL_OPTION);
				
				if(answer == 0) {
					POS_system.this.dispose();
					ChangePassword changePass = new ChangePassword(userType);
					changePass.setLocationRelativeTo(null);
					changePass.setVisible(true);				
				}				
			}
		});
		
		// Log out button
		logoutLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int answer = JOptionPane.showConfirmDialog(POS_system.this, "Are you sure you want to logout?", "Logout", JOptionPane.OK_CANCEL_OPTION);
				
				if(answer == 0) {
					POS_system.this.dispose();
					LoginForm loginForm = new LoginForm();
					loginForm.setLocationRelativeTo(null);
					loginForm.setVisible(true);
					
				}
			}
		});
		
		
	}		
}
