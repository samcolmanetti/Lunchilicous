package soaress3.edu.lunchilicous;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;

public class CartItemArrayAdapter extends ArrayAdapter<FoodMenuItem> {
    private final Activity context;
    private FoodMenuItem[] items;

    static class ViewHolder {
        TextView foodNameTextView;
        TextView foodDescriptionTextView;
        TextView foodQuantityTextView;
        TextView foodPriceTextView;
    }

    public CartItemArrayAdapter(Activity context, FoodMenuItem[] items) {
        super(context, R.layout.view_cart_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        // make's sure each view is initialized only once
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.view_cart_item, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.foodDescriptionTextView = (TextView) rowView
                    .findViewById(R.id.tv_item_food_description);
            viewHolder.foodNameTextView = (TextView) rowView.findViewById(R.id.tv_item_food_name);
            viewHolder.foodPriceTextView = (TextView) rowView.findViewById(R.id.tv_item_food_price);
            viewHolder.foodQuantityTextView = (TextView) rowView.findViewById(R.id.tv_item_food_quantity);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        FoodMenuItem item = items[position];
        holder.foodNameTextView.setText(item.getmFoodName());
        holder.foodDescriptionTextView.setText(item.getmFoodDescription());
        holder.foodQuantityTextView.setText(Integer.toString(item.getmFoodQuantity()));
        holder.foodPriceTextView.setText(NumberFormat.getCurrencyInstance()
                .format(item.getmFoodPrice()));

        return rowView;
    }


}
