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
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;


public class ConfirmationFragment extends ListFragment implements View.OnClickListener{
    static final String ARG_PURCHASE_ORDER_ID = "edu.soaress3.lunchilicious.ARG_PURCHASE_ORDER_ID";
    static final String ARG_ORDER_TOTAL = "edu.soaress3.lunchilicious.ARG_ORDER_TOTAL";

    private TextView mOrderTotalTextView;
    private Button mConfirmButton;
    private DbProvider mDbProvider;
    private Integer mPurchaseOrderId;
    private Double mOrderTotal;
    private LoadConfirmationItemsTask mConfirmTask;

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
        mConfirmTask = new LoadConfirmationItemsTask(this, mDbProvider.getReadableDb());
        mConfirmTask.execute(mPurchaseOrderId);

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
            Toast.makeText(getActivity(), "Order placed successfully", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbProvider = null;
        if (mConfirmTask != null) mConfirmTask.cancel(true);
    }
}
