package infinity.pnp.com.infinity.common;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import infinity.pnp.com.infinity.JobSchedulerService;
import infinity.pnp.com.infinity.OtpTask;
import infinity.pnp.com.infinity.R;
import infinity.pnp.com.infinity.UpdateLocationService;

public class Utils {

    public static long INTERVAL = 15 * 60 * 1000;

    public static void scheduleAlarm(Context pContext) {

        CustomLogger.getInsatance(pContext).putLog(" In ScheduleAlarm");
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            JobScheduler jobScheduler = (JobScheduler) pContext
                    .getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(
                    pContext.getPackageName(),
                    JobSchedulerService.class.getName()));
            builder.setPeriodic(INTERVAL);
            jobScheduler.schedule(builder.build());
        } else {
            AlarmManager alarmManager;
            PendingIntent alarmIntent;
            alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(pContext, UpdateLocationService.class);
            alarmIntent = PendingIntent.getService(pContext, 0, intent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), INTERVAL, alarmIntent);

        }

    }

    public static void cancelAlarm(Context pContext) {

        CustomLogger.getInsatance(pContext).putLog(" In CancelAlarm");
        if (android.os.Build.VERSION.SDK_INT < 21) {
            Intent intent = new Intent(pContext, UpdateLocationService.class);
            PendingIntent alarmIntent = PendingIntent.getService(pContext, 0,
                    intent, 0);
            AlarmManager alarmManager = (AlarmManager) pContext
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
        } else {
            JobScheduler mJobScheduler = (JobScheduler) pContext
                    .getSystemService(Context.JOB_SCHEDULER_SERVICE);
            mJobScheduler.cancel(1);
        }

    }

    public static String makeHttpURLConnection(String url,
                                               String requestString, Context context) throws IOException {

        String responseString = null;
        URL requestURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) requestURL
                .openConnection();

        setHttpCongiguration(conn);
        if (requestString != null) {
            OutputStreamWriter writer = new OutputStreamWriter(
                    conn.getOutputStream());
            writer.write(requestString);
            writer.flush();
            writer.close();
        }
        int HttpResult = conn.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            responseString = Utils.readStream(in);
            in.close();

        } else {
            Log.e("TAG", conn.getResponseMessage());
        }
        conn.disconnect();

        return responseString;
    }

    public static String sendRequestLegacy(String URL,
                                           List<NameValuePair> nameValuePairs, Context context) {
        String responseString = null;
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream isr = entity.getContent();
            responseString = Utils.readStream(isr);

            Log.e("log_tag", "http connection ");
            CustomLogger.getInsatance(context).putLog(
                    "::Connected Successfully::" + URL);

        } catch (Exception e) {
            CustomLogger.getInsatance(context).putLog(
                    "::Could not connect :: " + e.getMessage());
            Log.e("log_tag", "Error in http connection " + e.toString());

        }
        return responseString;
    }

    private static String readStream(InputStream in) {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    in, "UTF-8"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException ioe) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static void setHttpCongiguration(HttpURLConnection conn) {
        conn.setReadTimeout(10 * 1000 /* milliseconds */);
        conn.setConnectTimeout(10 * 1000 /* milliseconds */);
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
        } catch (ProtocolException e) {

        } catch (IOException e) {

        }
    }

    public static boolean isDeviceLocationOn(Context context) {

        LocationManager lm = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                return true;
            }
        } catch (Exception e) {
            CustomLogger.getInsatance(context).putLog(
                    "::Exception::" + e.getMessage());
        }
        return false;

    }

    public static void setUserId(String id, Context context) {

        SharedPreferences pref = context.getSharedPreferences(
                Constants.LOCATION_TRACKER_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ID", id);
        editor.commit();

    }

    public static String getUserId(Context context) {

        SharedPreferences pref = context.getSharedPreferences(
                Constants.LOCATION_TRACKER_PREF, 0);
        return pref.getString("ID", "");

    }

    public static void setUserName(String name, Context context) {

        SharedPreferences pref = context.getSharedPreferences(
                Constants.LOCATION_TRACKER_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("NAME", name);
        editor.commit();

    }

    public static String getUserName(Context context) {

        SharedPreferences pref = context.getSharedPreferences(
                Constants.LOCATION_TRACKER_PREF, 0);
        return pref.getString("NAME", "");

    }

    public static void sendLocation(Context context, Location location) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("lat", ""
                + location.getLatitude()));
        nameValuePairs.add(new BasicNameValuePair("long", ""
                + location.getLongitude()));
        nameValuePairs.add(new BasicNameValuePair("id", Utils
                .getUserId(context)));
        nameValuePairs.add(new BasicNameValuePair("attend", "login"));
        String response = Utils.sendRequestLegacy(
                Constants.UPDATE_LOCATION_URL, nameValuePairs, context);
        CustomLogger.getInsatance(context).putLog(
                ":: Location reponse ::" + response);

    }

    public boolean isOnline(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.DISCONNECTED) {
                        return true;
                    }

        }
        return false;

    }

    public static void verifyDialog(final Context context) {
        final Dialog dialog = new Dialog(context);

         dialog.setContentView(R.layout.otp_verify);
         dialog.setTitle("Infinity Verify");
         dialog.findViewById(R.id.btn_send_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_verify = (EditText) dialog.findViewById(R.id.editVerify);
                String otp = et_verify.getText().toString();
                new OtpTask(context, otp).execute();
             }
        });

        dialog.show();
    }

}
