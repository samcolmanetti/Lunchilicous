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
import java.util.LinkedList;
import java.util.List;


public class ConfirmationFragment extends ListFragment implements View.OnClickListener{
    static final String TAG = "ConfirmationFragment";
    static final String ARG_PURCHASE_ORDER_ID = "edu.soaress3.lunchilicious.ARG_PURCHASE_ORDER_ID";
    static final String ARG_ORDER_TOTAL = "edu.soaress3.lunchilicious.ARG_ORDER_TOTAL";

    private List<ConfirmationItem> mConfirmationItems;

    private TextView mOrderTotalTextView;
    private Button mConfirmButton;
    private DbProvider mDbProvider;
    private Integer mPurchaseOrderId;
    private Double mOrderTotal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPurchaseOrderId = bundle.getInt(ARG_PURCHASE_ORDER_ID);
            mOrderTotal = bundle.getDouble(ARG_ORDER_TOTAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        mOrderTotalTextView = (TextView) view.findViewById (R.id.tv_confirm_order_total);
        mConfirmButton = (Button) view.findViewById(R.id.btn_confirm);
        mConfirmButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDbProvider = (DbProvider) context;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mConfirmationItems = getConfirmationItems();
        ConfirmationItemArrayAdapter adapter = new ConfirmationItemArrayAdapter(getActivity(), mConfirmationItems);
        setListAdapter(adapter);
        mOrderTotalTextView.setText(NumberFormat.getCurrencyInstance().format(mOrderTotal));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_shopping_cart).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm){
            // start confirmation dialog box
            getActivity().finish();
        }
    }

    private List<ConfirmationItem> getConfirmationItems (){
        Cursor c = null;
        List<ConfirmationItem> items = new LinkedList<ConfirmationItem>();

        try {
            String columns[] = {
                    FoodOrderContract.OrderDetails.COLUMN_NAME_PRODUCT_NAME,
                    FoodOrderContract.OrderDetails.COLUMN_NAME_LINE_NUMBER,
                    FoodOrderContract.OrderDetails.COLUMN_NAME_QUANTITY
            };

            String selection = FoodOrderContract.OrderDetails.COLUMN_NAME_PURCHASE_ORDER_ID + " = ?";
            String[] selectionArgs = {Integer.toString(mPurchaseOrderId)};

            c = mDbProvider.getReadableDb().query(
                    FoodOrderContract.OrderDetails.TABLE_NAME,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null,
                    null
            );

            int nameIndex = c.getColumnIndex(FoodOrderContract.OrderDetails.COLUMN_NAME_PRODUCT_NAME);
            int lineNumberIndex = c.getColumnIndex(FoodOrderContract.OrderDetails.COLUMN_NAME_LINE_NUMBER);
            int quantityIndex = c.getColumnIndex(FoodOrderContract.OrderDetails.COLUMN_NAME_QUANTITY);

            c.moveToFirst();
            while (!c.isAfterLast()) {
                items.add(new ConfirmationItem(c.getString(nameIndex),c.getInt(lineNumberIndex),
                        c.getInt(quantityIndex)));
                c.moveToNext();
            }
        }
        catch (Exception ex){
            Log.e(TAG, "getConfirmationItems query failed ", ex);
        }
        finally {
            if (c != null){
                c.close();
            }
        }
        return items;
    }

}
