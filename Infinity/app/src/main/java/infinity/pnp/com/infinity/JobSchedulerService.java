package infinity.pnp.com.infinity;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import infinity.pnp.com.infinity.common.CustomLogger;
import infinity.pnp.com.infinity.common.LocationTracker;
import infinity.pnp.com.infinity.common.Utils;

public class JobSchedulerService extends JobService {

    protected JobParameters mParams;

    @Override
    public boolean onStartJob(JobParameters params) {
        mParams = params;
        new TrackLocationTask(this).execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }

    class TrackLocationTask extends AsyncTask<Void, Void, Void> {

        Context context;
        Location location = null;

        public TrackLocationTask(Context pContext) {
            context = pContext;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            location = LocationTracker.getLocationTrackerInstance(context)
                    .getLocation();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (!Utils.isDeviceLocationOn(context)) {
                CustomLogger.getInsatance(context).putLog("Location not Found");

                return null;
            }

            if (location == null) {
                CustomLogger.getInsatance(context).putLog("Location not Found");
                return null;
            }
            Utils.sendLocation(context, location);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            jobFinished(mParams, true);
        }

    }

}
