package infinity.pnp.com.infinity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import infinity.pnp.com.infinity.common.Constants;
import infinity.pnp.com.infinity.common.CustomLogger;
import infinity.pnp.com.infinity.common.Utils;

public class GetUserDetailTask extends AsyncTask<Void, Void, Void> {

    private final Context context;
    private final String mobileNumber;
    private ProgressDialog dialog;
    private String responseString;

    public GetUserDetailTask(Context context, String mobileNumber) {
        this.context = context;
        this.mobileNumber = mobileNumber;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("mobile", mobileNumber));
        responseString = Utils.sendRequestLegacy(Constants.FETCH_USER_DETAILS,
                nameValuePairs, context);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        try {
            dialog.dismiss();
            // JSONObject json_data = new JSONObject(responseString);
            // JSONArray jsonArray = json_data.optJSONArray("post");

            JSONArray jsonarray = null;
            JSONObject jsonobject = new JSONObject(responseString);

            Log.e("jsonobject", "" + jsonobject);
            int length = jsonobject.length();

            jsonarray = jsonobject.getJSONArray("post");

            Log.e("Name Length", "" + length);
            String name = "";
            String id = "";
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONArray test = null;

                test = jsonarray.getJSONArray(i);
                JSONObject dd = test.getJSONObject(0);
                name = dd.getString("name").toString();
                id = dd.getString("id").toString();
                String time = dd.getString("tracking_time").toString();
                // Log.e("TIME", time);
                // Log.e("ID", id);

            }
            Utils.setUserId(id, context);
            Utils.setUserName(name, context);

        } catch (Exception e) {
            CustomLogger.getInsatance(context).putLog(
                    "::Exception::" + e.getMessage());
        }

        //context.startActivity(new Intent(context, StartLocationActivity.class));

        if (context instanceof MainActivity) {
            ((MainActivity) context).finish();
        }

    }
}
