package soaress3.edu.lunchilicous;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        FoodMenuFragment.OnFoodMenuItemSelectedListener,
        ItemDetailsFragment.OnItemAddedToCartListener,
        ConfirmationDialog.MessageDialogHostListener,
        DbProvider {

    private SQLiteDatabase mReadOnlyDb;
    private SQLiteDatabase mWritableDb;

    private CartFragment mCartFragment;

    private int mItemId = 0;
    private Long mPurchaseOrderId;
    private Integer mLineNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FoodOrderDbOpenHelper dbHelper = new FoodOrderDbOpenHelper (this);

        mReadOnlyDb = dbHelper.getReadableDatabase();
        mWritableDb = dbHelper.getWritableDatabase();

        if (findViewById(R.id.food_menu_fragment) == null) {

            if (savedInstanceState != null) {
                return;
            }
            FoodMenuFragment foodMenu = new FoodMenuFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_container, foodMenu);
            ft.commit();
        } else {
            // large device is being used
            ItemDetailsFragment foodItem = new ItemDetailsFragment();

            Bundle args = new Bundle();
            args.putInt(ItemDetailsFragment.ARG_ITEM_ID, mItemId);
            foodItem.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, foodItem);
            transaction.addToBackStack("DETAILS");
            transaction.commit();
        }

        //int numberOfItems = getResources().getStringArray(R.array.menu_items).length;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // shopping cart selected since it's the only item in the menu

        if (this.mCartFragment == null) {
            this.mCartFragment = new CartFragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, mCartFragment);
        ft.addToBackStack("CART");
        ft.commit();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFoodMenuItemSelected(int itemId) {
        ItemDetailsFragment foodItem = new ItemDetailsFragment();
        this.mItemId = itemId;

        Bundle args = new Bundle();
        args.putInt(ItemDetailsFragment.ARG_ITEM_ID, itemId);
        foodItem.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, foodItem);
        transaction.addToBackStack("DETAILS");
        transaction.commit();
    }

    @Override
    public void itemAddedToCart(FoodMenuItem item, int quantity) {
        ConfirmationDialog confirmationAlertDialog = ConfirmationDialog.newInstance(item, quantity);
        confirmationAlertDialog.show(getFragmentManager(), "confirmation_dialog");
    }

    @Override
    protected void onRestoreInstanceState(Bundle restoredInstanceState) {
       // mFoodItemQuantities = restoredInstanceState.getIntArray(CartFragment.ARG_PURCHASE_ORDER_ID);
        super.onRestoreInstanceState(restoredInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        //savedInstanceState.putIntArray(CartFragment.ARG_PURCHASE_ORDER_ID, mFoodItemQuantities);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (mReadOnlyDb != null) mReadOnlyDb.close();
        if (mWritableDb != null) mWritableDb.close();

        // save updated data in retainedFragment

        super.onDestroy();
    }

    @Override
    public SQLiteDatabase getReadableDb() {
        return this.mReadOnlyDb;
    }

    @Override
    public SQLiteDatabase getWritableDb() {
        return this.mWritableDb;
    }

    @Override
    public void onDialogReturn(int message, FoodMenuItem item, int quantity) {
        if (message == ConfirmationDialog.OPTION_YES) { // Add item to cart
            addProductToDatabase(item, quantity);

            FragmentManager fm = getSupportFragmentManager();

            if (findViewById(R.id.food_menu_fragment) == null){
                fm.popBackStackImmediate();
            } else {
                fm.popBackStackImmediate("DETAILS",fm.POP_BACK_STACK_INCLUSIVE);

                // start new instance of ItemDetailsFragment
                this.onFoodMenuItemSelected(0);
            }
        }
    }

    private void addProductToDatabase (FoodMenuItem item, int quantity){
        double cost = quantity * item.getmFoodPrice();
        if (mPurchaseOrderId == null){
            addPurchaseOrderRecord(cost);
        } else {
            updatePurchaseOrderTotalCost(cost);
        }

        try {
            ContentValues map = new ContentValues(4);
            map.put(FoodOrderContract.OrderDetails.COLUMN_NAME_PURCHASE_ORDER_ID, mPurchaseOrderId);
            map.put(FoodOrderContract.OrderDetails.COLUMN_NAME_LINE_NUMBER, mLineNumber);
            map.put(FoodOrderContract.OrderDetails.COLUMN_NAME_PRODUCT_NAME, item.getmFoodName());
            map.put(FoodOrderContract.OrderDetails.COLUMN_NAME_QUANTITY, quantity);

            mPurchaseOrderId = mWritableDb.insert(FoodOrderContract.OrderDetails.TABLE_NAME, null, map);
            mLineNumber++;
        } catch (Exception ex){
            Log.e ("MainActivity", "addProductToDatabase insert statement failed: ", ex);
        }
    }

    private void addPurchaseOrderRecord (double cost){
        try {
            ContentValues map = new ContentValues(2);
            map.put(FoodOrderContract.PurchaseOrder.COLUMN_NAME_ORDER_DATE, today());
            map.put(FoodOrderContract.PurchaseOrder.COLUMN_NAME_TOTAL_COST, cost);

            mPurchaseOrderId = mWritableDb.insert(FoodOrderContract.PurchaseOrder.TABLE_NAME, null, map);
            mLineNumber = 1;
        } catch (Exception ex){
            Log.e ("MainActivity", "addPurchaseOrderRecord insert statement failed: ", ex);
        }
    }

    private void updatePurchaseOrderTotalCost (double cost){
        try {
            String sql = "UPDATE " + FoodOrderContract.PurchaseOrder.TABLE_NAME +
                    " SET " + FoodOrderContract.PurchaseOrder.COLUMN_NAME_TOTAL_COST + " = " +
                    FoodOrderContract.PurchaseOrder.COLUMN_NAME_TOTAL_COST + " + " + cost + ";";

            mWritableDb.execSQL(sql);
        } catch (Exception ex){
            Log.e ("MainActivity", "updatePurchaseOrderTotalCost update failed: ", ex);
        }
    }

    private String today (){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        return sdf.format(c.getTime());
    }

}