package soaress3.edu.lunchilicous;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

public class ConfirmationItemArrayAdapter extends ArrayAdapter<ConfirmationItem> {
    private final Activity context;
    private List<ConfirmationItem> items;

    static class ViewHolder {
        TextView confirmLineNumberTextView;
        TextView confirmItemNameTextView;
        TextView confirmQuantityTextView;
    }

    public ConfirmationItemArrayAdapter(Activity context, List<ConfirmationItem> items) {
        super(context, R.layout.view_cart_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        // makes sure each view is initialized only once
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.view_confirmation_item, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.confirmItemNameTextView = (TextView) rowView
                    .findViewById(R.id.tv_confirm_name);
            viewHolder.confirmLineNumberTextView = (TextView) rowView.findViewById(R.id.tv_confirm_line_number);
            viewHolder.confirmQuantityTextView = (TextView) rowView.findViewById(R.id.tv_confirm_quantity);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        ConfirmationItem item = items.get(position);
        holder.confirmLineNumberTextView.setText(Integer.toString(item.getmItemLineNumber()));
        holder.confirmItemNameTextView.setText(item.getmItemName());
        holder.confirmQuantityTextView.setText(Integer.toString(item.getmItemQuantity()));

        return rowView;
    }


}
