public class ItemList extends Transaction {
	
    public ItemList(int item_details_id, int itemQty, String itemName, String itemSize, double itemEachPrice, double eachTotalPrice)  {
        super(item_details_id, itemQty, itemName, itemSize, itemEachPrice, eachTotalPrice);        
    }

    public int getItemDetailsId() {
        return item_details_id;
    }
    public String getItemName() {
        return itemName;
    }
    
    public String getItemSize() {
        return itemSize;
    }

    public double getItemPrice() {
        return itemEachPrice;
    }
    
    public int getItemQty() {
        return itemQty;
    }
    
    public double getEachTotalPrice() {
        return eachTotalPrice;
    }
    
    public String getItem() {
        return itemName + "," + itemSize + "," + eachTotalPrice + "," + itemQty + "," + eachTotalPrice;
    }
    
    @Override
    public String toString() {
        String items = String.format("Quantity: %s\nName: %s\nSize: %s\nPrice: %s", itemQty, itemName, itemSize, itemEachPrice);
        return items;
    }
}

