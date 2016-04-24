package soaress3.edu.lunchilicous;

import android.provider.BaseColumns;

public final class FoodOrderContract {

    public FoodOrderContract () {}

    // Product(_ID, name, description, calories, price)
    public static abstract class Product implements BaseColumns {
        public static final String TABLE_NAME = "Product";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CALORIES = "calories";
        public static final String COLUMN_NAME_PRICE = "price";
    }

    // OrderDetails(_ID, purchaseOrderId, lineNo, productName, quantity)
    public static abstract class OrderDetails implements BaseColumns {
        public static final String TABLE_NAME = "OrderDetails";
        public static final String COLUMN_NAME_PURCHASE_ORDER_ID = "purchaseOrderId";
        public static final String COLUMN_NAME_LINE_NUMBER = "lineNumber";
        public static final String COLUMN_NAME_PRODUCT_NAME = "productName";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    // PurchaseOrder(_ID, orderDate, totalCost);
    public static abstract class PurchaseOrder implements BaseColumns {
        public static final String TABLE_NAME = "PurchaseOrder";
        public static final String COLUMN_NAME_ORDER_DATE = "orderDate";
        public static final String COLUMN_NAME_TOTAL_COST = "totalCost";
    }
}
