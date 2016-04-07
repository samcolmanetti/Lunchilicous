package soaress3.edu.lunchilicous;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfirmationDialog extends DialogFragment {
    public static final int OPTION_YES = 1;
    public static final int OPTION_NO = 0;
    public static final String ITEM = "foodMenuItem";
    public static final String QUANTITY = "quantity";

    private FoodMenuItem mItem;
    private int mQuantity;

    public static interface MessageDialogHostListener {
        public void onDialogReturn(int message, FoodMenuItem item, int quantity);
    }

    public static ConfirmationDialog newInstance(FoodMenuItem item, int quantity) {
        ConfirmationDialog f = new ConfirmationDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable(ITEM, item);
        args.putInt(QUANTITY, quantity);
        f.setArguments(args);

        return f;
    }

    private MessageDialogHostListener mMessageDialogHostListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.mItem = (FoodMenuItem) getArguments().getSerializable(ITEM);
        this.mQuantity = getArguments().getInt(QUANTITY, -1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you wish to add this item to your cart?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMessageDialogHostListener.onDialogReturn(OPTION_YES, mItem, mQuantity);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMessageDialogHostListener.onDialogReturn(OPTION_NO, mItem, mQuantity);
                    }
                });
        return builder.create();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mMessageDialogHostListener = (MessageDialogHostListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "Must implement MessageDialogListener");
        }
    }
}
