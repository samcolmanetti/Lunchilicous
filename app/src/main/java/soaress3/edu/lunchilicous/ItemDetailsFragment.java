package soaress3.edu.lunchilicous;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.NumberFormat;

public class ItemDetailsFragment extends Fragment implements View.OnClickListener{

    public interface OnReturnToFoodMenuListener {
        void returnToFoodMenu();
    }

    public interface OnItemAddedToCartListener {
        void itemAddedToCart (int itemId, int quantity);
    }
    public static final String ARG_POSITION ="edu.soaress3.lunchilicious.ARG_POSITION";
    private static final int QUANTITY_MIN = 1;
    private static final int QUANTITY_MAX = 100;

    private String[] foodNames;
    private String[] foodDescriptions;
    private int[] foodPrices;
    private int[] foodCalories;
    private TextView mFoodNameTextView;
    private TextView mFoodDescriptionTextView;
    private TextView mFoodUnitCostTextView;
    private TextView mFoodCaloriesTextView;
    private NumberPicker mFoodQuantityNumberPicker;
    private Button mAddToCartButton;
    private int mPosition = 0;
    private OnReturnToFoodMenuListener mReturnToFoodMenuListener;
    private OnItemAddedToCartListener mItemAddedToCartListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (bundle != null) mPosition = bundle.getInt(this.ARG_POSITION, 0);

        populateFoodData ();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mReturnToFoodMenuListener = (OnReturnToFoodMenuListener) activity;
        mItemAddedToCartListener = (OnItemAddedToCartListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_details,  container, false);

        mFoodNameTextView = (TextView) view.findViewById(R.id.tv_food_name);
        mFoodDescriptionTextView = (TextView) view.findViewById(R.id.tv_food_description);
        mFoodUnitCostTextView = (TextView) view.findViewById(R.id.tv_food_unit_cost);
        mFoodCaloriesTextView = (TextView) view.findViewById(R.id.tv_food_calories);
        mFoodQuantityNumberPicker = (NumberPicker) view.findViewById(R.id.np_food_quantity);

        mAddToCartButton = (Button) view.findViewById(R.id.btn_add_to_cart);
        mAddToCartButton.setOnClickListener(this);

        mFoodQuantityNumberPicker = (NumberPicker) view.findViewById(R.id.np_food_quantity);
        mFoodQuantityNumberPicker.setMinValue(QUANTITY_MIN);
        mFoodQuantityNumberPicker.setMaxValue(QUANTITY_MAX);

        updateDisplay(this.mPosition);

        return view;
    }

    private void updateDisplay(int position) {
        if (position >= foodNames.length)   return;

        this.mFoodNameTextView.setText(foodNames[position]);
        this.mFoodDescriptionTextView.setText(foodDescriptions[position]);
        this.mFoodUnitCostTextView.setText(NumberFormat.getCurrencyInstance().format(foodPrices[position]));
        this.mFoodCaloriesTextView.setText("Calories: " + foodCalories[position]);
    }

    private void populateFoodData () {
        this.foodNames = getResources().getStringArray(R.array.menu_items);
        this.foodDescriptions = getResources().getStringArray(R.array.item_descriptions);
        this.foodCalories = getResources().getIntArray(R.array.item_calories);
        this.foodPrices = getResources().getIntArray(R.array.item_prices);
    }

    private int getItemQuantity (){
        return this.mFoodQuantityNumberPicker.getValue();
    }

    @Override
    public void onClick(View v) {

        if (R.id.btn_add_to_cart == v.getId()) {
            mItemAddedToCartListener.itemAddedToCart(mPosition, getItemQuantity());
        }
        mReturnToFoodMenuListener.returnToFoodMenu();

    }
}
