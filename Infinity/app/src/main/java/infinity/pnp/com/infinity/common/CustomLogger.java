package infinity.pnp.com.infinity.common;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomLogger {

    private static CustomLogger instance;
    private static File root, myFile;
    private static String fileName = "LocationTracker.txt";
    private String callerClassName, callerMethodName;
    private String timeStamp;

    private CustomLogger() {

    }

    /**
     * @param context
     * @return single instance of the class
     */
    public static CustomLogger getInsatance(Context context) {
        if (instance == null) {
            instance = new CustomLogger();
            setup();
        }
        return instance;
    }

    /**
     * Setup the file environment Create file
     */
    public static void setup() {
        root = Environment.getExternalStorageDirectory();
        myFile = new File(root + "/" + fileName);
    }

    /**
     * put logs into file
     *
     * @param message
     */
    public void putLog(String message) {

        // new file is created when file size exceeds 5MB
        if (myFile.length() >= 5 * 1024 * 1024) {
            myFile.delete();
            setup();
        }

        Throwable t = new Throwable();

        StackTraceElement[] elements = t.getStackTrace();
        callerClassName = elements[1].getClassName();
        callerMethodName = elements[1].getMethodName();
        timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar
                .getInstance().getTime());

        StringBuilder sb = new StringBuilder("\n\n");
        sb.append(timeStamp);
        sb.append(": " + callerClassName);
        sb.append(": " + callerMethodName);
        sb.append(message == null ? "NULL" : message);
        String finalString = sb.toString();

        try {
            FileWriter fw = new FileWriter(myFile.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(finalString);
            bw.close();
        } catch (FileNotFoundException e) {
            Log.e("TAG", "" + e.getMessage());
        } catch (Exception e) {
            Log.e("TAG", "" + e.getMessage());
        }

    }

}
