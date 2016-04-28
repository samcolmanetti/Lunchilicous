package soaress3.edu.lunchilicous;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public class LoadConfirmationItemsTask extends AsyncTask<Integer,Void, List<ConfirmationItem>> {
    static final String TAG = "LoadConfirmItemsTask";

    private WeakReference<ConfirmationFragment> mConfirmationFragment;
    private WeakReference<SQLiteDatabase> mReadableDb;

    public LoadConfirmationItemsTask (ConfirmationFragment confirmFragment, SQLiteDatabase readableDb){
        mConfirmationFragment = new WeakReference<>(confirmFragment);
        mReadableDb = new WeakReference<>(readableDb);
    }

    @Override
    protected List<ConfirmationItem> doInBackground(Integer... params) {
        Cursor c = null;
        List<ConfirmationItem> items = new LinkedList<>();
        Integer purchaseOrderId = params[0];
        SQLiteDatabase readableDb = mReadableDb.get();
        if (readableDb == null || purchaseOrderId == null) return null;

        try {
            String columns[] = {
                    FoodOrderContract.OrderDetails.COLUMN_NAME_PRODUCT_NAME,
                    FoodOrderContract.OrderDetails.COLUMN_NAME_LINE_NUMBER,
                    FoodOrderContract.OrderDetails.COLUMN_NAME_QUANTITY
            };

            String selection = FoodOrderContract.OrderDetails.COLUMN_NAME_PURCHASE_ORDER_ID + " = ?";
            String[] selectionArgs = {Integer.toString(purchaseOrderId)};

            c = readableDb.query(
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
            Log.e(TAG, "LoadConfirmationItemsTask query failed ", ex);
        }
        finally {
            if (c != null){
                c.close();
            }
        }
        return items;
    }

    @Override
    protected void onPostExecute(List<ConfirmationItem> confirmationItems) {
        super.onPostExecute(confirmationItems);

        ConfirmationFragment fragment = mConfirmationFragment.get();
        if (fragment == null) return;

        ConfirmationItemArrayAdapter adapter = new ConfirmationItemArrayAdapter(
                fragment.getActivity(), confirmationItems);
        fragment.setListAdapter(adapter);

    }
}
