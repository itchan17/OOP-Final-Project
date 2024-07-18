import java.util.Date;
import java.text.SimpleDateFormat;

public class Transaction {
	private String firstName;
    private String lastName;
    private int orderId;
    private double orderSubTotal;
    private double vat;
    private double cash;
    private double change;
    private double orderTotal;
    private int user_id;
    private String date;
    private String time;
    private double discount_amount;
    private String discount_id;
    
    protected String itemName; 
    protected String itemSize; 
    protected double itemEachPrice; 
    protected int itemQty; 
    protected double eachTotalPrice;  
    protected int item_details_id;
    
	private String customerName;
	private String customerAddress;
	private int discountRate;	
 
    
    public Transaction(int item_details_id, int itemQty, String itemName, String itemSize, double itemEachPrice, double eachTotalPrice)  {
        this.itemName = itemName;
        this.itemSize = itemSize;
        this.itemEachPrice = itemEachPrice;
        this.itemQty = itemQty;
        this.eachTotalPrice = eachTotalPrice;
        this.item_details_id = item_details_id;       
    }
    
          
    public Transaction(double cash, double change, double orderSubTotal, double vat, double orderTotal, int user_id, String discount_id, String customerName, String customerAddress, double discount_amount) {
        this.cash = cash;
        this.change = change;
        this.orderTotal = orderTotal;
        this.orderSubTotal = orderSubTotal;  
        this.vat = vat;  
        this.user_id = user_id;   
        this.discount_id = discount_id;   
        this.customerName = customerName;  
        this.customerAddress = customerAddress;  
        this.discount_amount = discount_amount;         
    }
    
    public Transaction(int orderId, String firstName, String lastName, double cash, double change, double orderSubTotal, double orderTotal, int user_id, String discount_id, double discount_amount) {
    	this.orderId = orderId;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.cash = cash;
        this.change = change;
        this.orderTotal = orderTotal;
        this.orderSubTotal = orderSubTotal;  
        this.user_id = user_id;   
        this.discount_id = discount_id;   
        this.discount_amount = discount_amount;         
    }
    
    
    
    // Getters
    public double getOrderSubTotal() {
        return orderSubTotal;
    }
    
    public double getVat() {
        return vat;
    }
    
    public double getCash() {
        return cash;
    }
    
    public double getChange() {
        return change;
    }
    
    public double getOrderTotal() {
        return orderTotal;
    }
    
    public int getUserId() {
        return user_id;
    }

    public String getDate() {   	     
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getDiscountAmount() {
        return discount_amount;
    }

    public String getDiscountId() {
        return discount_id;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public String getCustomerAddress() {
        return customerAddress;
    }
    
    //Setters
    public void setDateAndTime() {
        // Get current date and time
        Date currentDate = new Date();

        // Format the date and time using SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(currentDate);
        String[] split = date.split(" ");
        this.date = split[0];
        this.time = split[1];
    }
}
