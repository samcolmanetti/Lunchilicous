package soaress3.edu.lunchilicous;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodOrderDbOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "FoodOrder.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA = ", ";
    private static final String SINGLE_QUOTE = "'";

    // Product(_ID, name, description, calories, price)
    private static final String SQL_CREATE_PRODUCT =
            "CREATE TABLE " + FoodOrderContract.Product.TABLE_NAME + "(" +
            FoodOrderContract.Product._ID + " INTEGER PRIMARY KEY, " +
            FoodOrderContract.Product.COLUMN_NAME_NAME + TEXT_TYPE + COMMA +
            FoodOrderContract.Product.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA +
            FoodOrderContract.Product.COLUMN_NAME_CALORIES + INTEGER_TYPE + COMMA +
            FoodOrderContract.Product.COLUMN_NAME_PRICE + REAL_TYPE + ")";

    // OrderDetails(_ID, purchaseOrderId, lineNo, productName, quantity)
    private static final String SQL_CREATE_ORDER_DETAILS =
            "CREATE TABLE " + FoodOrderContract.OrderDetails.TABLE_NAME + "(" +
            FoodOrderContract.OrderDetails._ID + " INTEGER PRIMARY KEY, " +
            FoodOrderContract.OrderDetails.COLUMN_NAME_PURCHASE_ORDER_ID + INTEGER_TYPE + COMMA +
            FoodOrderContract.OrderDetails.COLUMN_NAME_LINE_NUMBER + INTEGER_TYPE + COMMA +
            FoodOrderContract.OrderDetails.COLUMN_NAME_PRODUCT_NAME + TEXT_TYPE + COMMA +
            FoodOrderContract.OrderDetails.COLUMN_NAME_QUANTITY + INTEGER_TYPE + ")";

    // PurchaseOrder(_ID, orderDate, totalCost);
    private static final String SQL_CREATE_PURCHASE_ORDER =
            "CREATE TABLE " + FoodOrderContract.PurchaseOrder.TABLE_NAME + "(" +
            FoodOrderContract.PurchaseOrder._ID + " INTEGER PRIMARY KEY, " +
            FoodOrderContract.PurchaseOrder.COLUMN_NAME_ORDER_DATE + TEXT_TYPE + COMMA +
            FoodOrderContract.PurchaseOrder.COLUMN_NAME_TOTAL_COST + REAL_TYPE + ")";

    private static final String SQL_DROP_PRODUCT =
            "DROP TABLE IF EXISTS " + FoodOrderContract.Product.TABLE_NAME;

    private static final String SQL_DROP_PURCHASE_ORDER =
            "DROP TABLE IF EXISTS " + FoodOrderContract.PurchaseOrder.TABLE_NAME;

    private static final String SQL_DROP_ORDER_DETAILS =
            "DROP TABLE IF EXISTS " + FoodOrderContract.OrderDetails.TABLE_NAME;

    public FoodOrderDbOpenHelper(Context context) {
        // call the super constructor which will call the callbacks
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        // create all tables
        db.execSQL(SQL_CREATE_PRODUCT);
        db.execSQL(SQL_CREATE_ORDER_DETAILS);
        db.execSQL(SQL_CREATE_PURCHASE_ORDER);

        // initialize the database
        initialize(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVerion, int newVersion) {
        db.execSQL(SQL_DROP_PRODUCT);
        db.execSQL(SQL_DROP_ORDER_DETAILS);
        db.execSQL(SQL_DROP_PURCHASE_ORDER);

        onCreate(db);
    }

    // Product(_ID, name, description, calories, price)
    private void initialize(SQLiteDatabase db) {
        String initInsert = "INSERT INTO " + FoodOrderContract.Product.TABLE_NAME + " (" +
                SINGLE_QUOTE + FoodOrderContract.Product.COLUMN_NAME_NAME + SINGLE_QUOTE + COMMA +
                SINGLE_QUOTE + FoodOrderContract.Product.COLUMN_NAME_DESCRIPTION + SINGLE_QUOTE + COMMA +
                SINGLE_QUOTE + FoodOrderContract.Product.COLUMN_NAME_CALORIES + SINGLE_QUOTE + COMMA +
                SINGLE_QUOTE + FoodOrderContract.Product.COLUMN_NAME_PRICE + SINGLE_QUOTE  + ")"
                + "VALUES " +
                " ('Pizza', 'A 14in pepperoni with extra pizza.', 285 , 3)," +
                " ('Sandwich', 'Ham and cheese 12in toasted sub.', 340, 5)," +
                " ('Pasta', 'Hot spaghetti pasta served with unlimited bread sticks.', 75, 8), " +
                " ('Cheeseburger', 'Bacon cheeseburger with your choice of toppings.', 303, 5), " +
                " ('Bottled Coke', 'Chilled 16oz. bottle', 184, 2), " +
                " ('Bottled Water', 'Chilled 24oz. bottle', 0, 1), " +
                " ('Garlic Knots', '3 knots.', 200, 2);" ;

        db.execSQL(initInsert);
    }

}
