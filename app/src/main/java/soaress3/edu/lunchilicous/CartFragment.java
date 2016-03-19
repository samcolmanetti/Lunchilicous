package soaress3.edu.lunchilicous;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;


public class CartFragment extends ListFragment {
    public interface OnReturnToFoodMenuListener {
        void returnToFoodMenu();
    }

    public static final String ARG_FOOD_QUANTITIES = "edu.soaress3.lunchilicious.ARG_FOOD_QUANTITIES";

    private String[] mFoodNames;
    private String[] mFoodDescriptions;
    private int[] mFoodPrices;
    private int[] mFoodCalories;
    private int[] mFoodQuantities;
    private int mOrderTotal;
    private TextView mOrderTotalTextView;
    private OnReturnToFoodMenuListener mHostListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();

        if (bundle != null) {
            mFoodQuantities = bundle.getIntArray(ARG_FOOD_QUANTITIES);
        }

        this.mFoodNames = getResources().getStringArray(R.array.menu_items);
        this.mFoodDescriptions = getResources().getStringArray(R.array.item_descriptions);
        this.mFoodCalories = getResources().getIntArray(R.array.item_calories);
        this.mFoodPrices = getResources().getIntArray(R.array.item_prices);

        this.mOrderTotal = this.orderTotal();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mHostListener = (OnReturnToFoodMenuListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        mOrderTotalTextView = (TextView) view.findViewById (R.id.tv_order_total);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private FoodMenuItem[] getFoodMenuItems() {
        ArrayList<FoodMenuItem> items = new ArrayList<FoodMenuItem>();;

        for (int i = 0; i < mFoodQuantities.length; i++){
            if (mFoodQuantities[i] > 0){
                items.add(new FoodMenuItem(mFoodNames[i], mFoodDescriptions[i], mFoodCalories[i],
                        mFoodPrices[i], mFoodQuantities[i]));
            }
        }
        FoodMenuItem[] menuItems = new FoodMenuItem[items.size()];
        for (int i = 0; i < items.size(); i++){
            menuItems[i] = items.get(i);
        }

        return menuItems;
    }

    private int orderTotal (){
        int total = 0;

        for (int i = 0; i < mFoodQuantities.length; i++){
            if (mFoodQuantities[i] > 0){
                total += mFoodQuantities[i] * mFoodPrices[i];

            }
        }

        return total;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CartItemArrayAdapter adapter = new CartItemArrayAdapter(getActivity(), getFoodMenuItems());
        setListAdapter(adapter);
        mOrderTotalTextView.setText(NumberFormat.getCurrencyInstance().format(mOrderTotal));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_shopping_cart).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
