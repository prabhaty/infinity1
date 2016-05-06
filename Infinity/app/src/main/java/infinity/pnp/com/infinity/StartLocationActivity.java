package infinity.pnp.com.infinity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import infinity.pnp.com.infinity.common.Utils;

public class StartLocationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.user).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.user)).setText("Welcome "
                + Utils.getUserName(this)
                + ", Pls press the button to start tracker");
        findViewById(R.id.mobile).setVisibility(View.GONE);
        findViewById(R.id.loginbutton).setVisibility(View.GONE);
        findViewById(R.id.startbutton).setVisibility(View.VISIBLE);

        findViewById(R.id.startbutton).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (ActivityCompat.checkSelfPermission(StartLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(StartLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(StartLocationActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    2);


                        } else {
                            Utils.cancelAlarm(StartLocationActivity.this);
                            Utils.scheduleAlarm(StartLocationActivity.this);
                            Toast.makeText(StartLocationActivity.this,
                                    "Location tracker started", Toast.LENGTH_SHORT)
                                    .show();
                            startActivity(new Intent(StartLocationActivity.this, HomeActivity.class));
                        }


                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Utils.cancelAlarm(StartLocationActivity.this);
                    Utils.scheduleAlarm(StartLocationActivity.this);
                    Toast.makeText(StartLocationActivity.this,
                            "Location tracker started", Toast.LENGTH_SHORT)
                            .show();
                    startActivity(new Intent(StartLocationActivity.this, HomeActivity.class));

                } else {

                    Toast.makeText(StartLocationActivity.this,
                            "Location tracker could not start, permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
