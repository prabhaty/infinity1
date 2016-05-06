package infinity.pnp.com.infinity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import infinity.pnp.com.infinity.common.CustomLogger;
import infinity.pnp.com.infinity.common.Utils;

public class MainActivity extends Activity {

    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.loginbutton).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        EditText mobileText = (EditText) findViewById(R.id.mobile);
                        mobileNumber = mobileText.getText().toString();
                        if (mobileNumber.isEmpty()) {
                            Toast.makeText(MainActivity.this,
                                    "Enter Mobile Number", Toast.LENGTH_SHORT)
                                    .show();
                        } else {

                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        3);
                            }
                            new  LoginTask(MainActivity.this,mobileNumber).execute();

                        }

                    }
                });
        findViewById(R.id.startbutton).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Utils.cancelAlarm(MainActivity.this);
                        Utils.scheduleAlarm(MainActivity.this);

                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new LoginTask(MainActivity.this, mobileNumber)
                            .execute();

                    CustomLogger.getInsatance(this).putLog("Permission Granted");

                } else {

                    CustomLogger.getInsatance(this).putLog("Permission Granted");
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
