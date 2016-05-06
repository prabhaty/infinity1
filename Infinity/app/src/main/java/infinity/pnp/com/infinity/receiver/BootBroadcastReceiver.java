package infinity.pnp.com.infinity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import infinity.pnp.com.infinity.common.CustomLogger;
import infinity.pnp.com.infinity.common.Utils;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        CustomLogger.getInsatance(context).putLog("Deive Restarted");
        Utils.cancelAlarm(context);
        Utils.scheduleAlarm(context);

        Log.e("NETWORK", "" + Utils.isDeviceLocationOn(context));

        if (Utils.isDeviceLocationOn(context)) {
            Toast.makeText(context, "on line", Toast.LENGTH_LONG).show();
            Log.e("NETWORK", "online");
        } else {
            Toast.makeText(context, "Off line", Toast.LENGTH_LONG).show();
            Log.e("NETWORK", "offline");
        }


    }

}
