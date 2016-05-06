package infinity.pnp.com.infinity.common;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * @author azmat.ali.khan
 */

public class LocationTracker implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 10
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 10; // 1 minute
    private static LocationTracker instance;
    private final Context mContext;
    // flag for GPS status
    private boolean isGPSEnabled = false;
    // flag for network status
    private boolean isNetworkEnabled = false;
    // meters
    // flag for GPS status
    private boolean canGetLocation = false;
    private Location location; // location
    // Declaring a Location Manager
    private LocationManager locationManager;

    private LocationTracker(Context context) {

        mContext = context;
    }

    public static LocationTracker getLocationTrackerInstance(Context context) {

        if (instance == null) {
            instance = new LocationTracker(context);
        }
        return instance;
    }

    /**
     * Return the latest location of Device.
     *
     * @return Location
     */
    public Location getLocation() {

        Location newLocation = null;
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    newLocation = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (newLocation == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        newLocation = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    }
                }
            }

            location = newLocation;

        } catch (Exception e) {
            CustomLogger.getInsatance(mContext).putLog(
                    " Exception::" + e.getMessage());
        }

        return location;
    }

    @Override
    public void onLocationChanged(Location loc) {

        // CustomLogger.getInsatance(mContext).putLog("::onLocationChanged");
    }

    @Override
    public void onProviderDisabled(String arg0) {

        // CustomLogger.getInsatance(mContext).putLog(
        // "::onProviderDisabled::" + arg0);

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // CustomLogger.getInsatance(mContext).putLog(
        // "::onProviderEnabled::" + arg0);

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

        // CustomLogger.getInsatance(mContext).putLog("::onStatusChanged");

    }

}
