package soaress3.edu.lunchilicous;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class CartFragment extends ListFragment implements View.OnClickListener{

    public static final String ARG_PURCHASE_ORDER_ID = "edu.soaress3.lunchilicious.ARG_PURCHASE_ORDER_ID";
    public static final String TAG = "CartFragment";

    private OnCheckoutButtonPressedListener mCheckoutListener;

    public interface OnCheckoutButtonPressedListener {
        void onCheckoutButtonPressed(Double orderTotal);
    }

    private Double mOrderTotal;
    private Integer mPurchaseOrderId = null;

    private TextView mOrderTotalTextView;
    private Button mCheckoutButton;
    private DbProvider mDbProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPurchaseOrderId = bundle.getInt(ARG_PURCHASE_ORDER_ID);
        } else {
            Log.e(TAG, "Purchase order id is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        mOrderTotalTextView = (TextView) view.findViewById (R.id.tv_confirm_order_total);
        mCheckoutButton = (Button) view.findViewById(R.id.btn_checkout);
        mCheckoutButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDbProvider = (DbProvider) context;
        mCheckoutListener = (OnCheckoutButtonPressedListener) context;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<FoodMenuItem> cartItems = getCartItems();

        CartItemArrayAdapter adapter = new CartItemArrayAdapter(getActivity(), cartItems);
        setListAdapter(adapter);

        this.mOrderTotal = getOrderTotal();
        mOrderTotalTextView.setText(NumberFormat.getCurrencyInstance().format(mOrderTotal));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_shopping_cart).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_checkout){
            mCheckoutListener.onCheckoutButtonPressed(mOrderTotal);
        }
    }

    private List<FoodMenuItem> getCartItems (){
        Cursor c = null;
        List<FoodMenuItem> items = new LinkedList<FoodMenuItem>();
        try {
            String selection = FoodOrderContract.OrderDetails.COLUMN_NAME_PURCHASE_ORDER_ID + " = ?";
            String[] selectionArgs = {Integer.toString(mPurchaseOrderId)};

            c = mDbProvider.getReadableDb().query(
                FoodOrderContract.OrderDetails.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null
            );

            int nameIndex = c.getColumnIndex(FoodOrderContract.OrderDetails.COLUMN_NAME_PRODUCT_NAME);
            int descriptionIndex = c.getColumnIndex(FoodOrderContract.OrderDetails.COLUMN_NAME_DESCRIPTION);
            int priceIndex = c.getColumnIndex(FoodOrderContract.OrderDetails.COLUMN_NAME_PRICE);
            int quantityIndex = c.getColumnIndex(FoodOrderContract.OrderDetails.COLUMN_NAME_QUANTITY);

            c.moveToFirst();
            while (!c.isAfterLast()) {
                items.add(new FoodMenuItem(c.getString(nameIndex), c.getString(descriptionIndex),
                        c.getDouble(priceIndex), c.getInt(quantityIndex)));
                c.moveToNext();
            }
        }
        catch (Exception ex){
            Log.e(TAG, "populateCartItemsList query failed ", ex);
        }
        finally {
            if (c != null){
                c.close();
            }
        }
        return items;
    }

    private Double getOrderTotal (){
        Cursor c = null;
        Double total = null;
        try {
            String selection = FoodOrderContract.PurchaseOrder._ID + " = ?";
            String[] selectionArgs = {Integer.toString(mPurchaseOrderId)};

            c = mDbProvider.getReadableDb().query(
                    FoodOrderContract.PurchaseOrder.TABLE_NAME,
                    new String[] {FoodOrderContract.PurchaseOrder.COLUMN_NAME_TOTAL_COST},
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null,
                    null
            );

            int totalIndex = c.getColumnIndex(FoodOrderContract.PurchaseOrder.COLUMN_NAME_TOTAL_COST);

            c.moveToFirst();
            total = c.getDouble(totalIndex);
        }
        catch (Exception ex){
            Log.e(TAG, "getOrderTotal query failed ", ex);
        }
        finally {
            if (c != null){
                c.close();
            }
        }
        return total;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbProvider = null;
    }
}
