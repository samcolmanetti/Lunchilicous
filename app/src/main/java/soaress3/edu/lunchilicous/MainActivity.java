package soaress3.edu.lunchilicous;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
        FoodMenuFragment.OnFoodMenuItemSelectedListener,
        ItemDetailsFragment.OnReturnToFoodMenuListener,
        ItemDetailsFragment.OnItemAddedToCartListener,
        CartFragment.OnReturnToFoodMenuListener {

    private int[] mFoodItemQuantities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.frame_container) != null) {

            if (savedInstanceState != null) {
                return;
            }
            FoodMenuFragment foodMenu = new FoodMenuFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_container, foodMenu);
            ft.addToBackStack("HEADLINES");
            ft.commit();
        }
        else {

        }
        int numberOfItems = getResources().getStringArray(R.array.menu_items).length;
        mFoodItemQuantities = new int[numberOfItems];

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

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_shopping_cart){
            CartFragment cartFragment = new CartFragment();

            Bundle args = new Bundle();
            Resources r = getResources();
            args.putStringArray(CartFragment.ARG_FOOD_NAMES, r.getStringArray(R.array.menu_items));
            args.putStringArray(CartFragment.ARG_FOOD_DESCRIPTION, r.getStringArray(R.array.item_descriptions));
            args.putIntArray(CartFragment.ARG_FOOD_PRICES, r.getIntArray(R.array.item_prices));
            args.putIntArray(CartFragment.ARG_FOOD_CALORIES, r.getIntArray(R.array.item_calories));
            args.putIntArray(CartFragment.ARG_FOOD_QUANTITIES, mFoodItemQuantities);
            cartFragment.setArguments(args);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, cartFragment);
            ft.addToBackStack("CART");
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFoodMenuItemSelected(int position) {
        ItemDetailsFragment foodItem = new ItemDetailsFragment();

        Bundle args = new Bundle();
        args.putInt(ItemDetailsFragment.ARG_POSITION, position);
        foodItem.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, foodItem);
        transaction.addToBackStack("DETAILS");
        transaction.commit();
    }

    @Override
    public void returnToFoodMenu() {
        FoodMenuFragment foodMenu = new FoodMenuFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, foodMenu);
        //ft.addToBackStack("HEADLINES");
        ft.commit();
    }

    @Override
    public void itemAddedToCart(int itemId, int quantity) {
        mFoodItemQuantities[itemId] += quantity;
    }
}