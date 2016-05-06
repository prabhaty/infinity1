package infinity.pnp.com.infinity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import infinity.pnp.com.infinity.common.Constants;
import infinity.pnp.com.infinity.common.CustomLogger;
import infinity.pnp.com.infinity.common.Utils;

/**
 * Created by ABC on 5/3/2016.
 */
public class OtpTask extends AsyncTask<Void, Void, Void> {

    private final Context context;
    private final String otp;
    private ProgressDialog dialog;
    private String responseString;

    public OtpTask(Context pContext, String otp) {
        context = pContext;
        this.otp = otp;
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
        nameValuePairs.add(new BasicNameValuePair("c_id", Utils.getUserId(context)));
        nameValuePairs.add(new BasicNameValuePair("otp", otp));
        responseString = Utils.sendRequestLegacy(Constants.VERIFY_URL, nameValuePairs, context);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        super.onPostExecute(result);

        try {
            dialog.dismiss();
            JSONObject json_data = new JSONObject(responseString);

            String resultValue = (String) json_data.get("re");
            if (resultValue.equals("0")) {
                Toast.makeText(context, "Wrong OTP Number ",
                        Toast.LENGTH_SHORT).show();
            } else {
                context.startActivity(new Intent(context, StartLocationActivity.class));
            }

        } catch (Exception e) {
            CustomLogger.getInsatance(context).putLog(
                    "::Exception::" + e.getMessage());
        }
    }
}
