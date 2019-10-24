import android.util.Log;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DD_LOG {

    private static String DD_API_KEY = "xxxxxxxxxxxxxxxxxxxxxxxx";                          //Enter your Datadog API key as a string.
    private static String DD_ENDPOINT = "https://http-intake.logs.datadoghq.com/v1/input/"; // change to eu endpoint for european accounts
    private static String DD_SERVICE = "DataLog";                                           // Default service
    private static String DD_SOURCE = "Android";                                            // Source is Android
    private static String DD_HOSTNAME = android.os.Build.MODEL;                             // Device model is grabbed as hostname

    private int DD_MAXQSIZE;
    private ArrayList logQueue;
    private int currentQSize;


    public DD_LOG() {
        this.DD_MAXQSIZE = 1;
        this.currentQSize = 0;
        this.logQueue = new ArrayList<String>();
    }

    public static void sendLog(final String[] payload) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Long time = System.currentTimeMillis();
                    URL url = new URL(DD_ENDPOINT + "" + DD_API_KEY);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    //Default Attributes
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("date", time);
                    jsonParam.put("service", DD_SERVICE);
                    jsonParam.put("ddsource", DD_SOURCE);
                    jsonParam.put("host", DD_HOSTNAME);

                    //Custom Attributes
                    for (String pairs : payload) {
                        String[] pair = pairs.split(":");
                        jsonParam.put(pair[0], pair[1]);
                    }
                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    os.close();
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


    public void Log(final String[] payload) {
        Long time = System.currentTimeMillis();
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("date", time);
            jsonParam.put("service", DD_SERVICE);
            jsonParam.put("ddsource", DD_SOURCE);
            jsonParam.put("host", DD_HOSTNAME);

            //Custom Attributes
            for (String pairs : payload) {
                String[] pair = pairs.split(":");
                jsonParam.put(pair[0], pair[1]);
            }

            Log.i("JSON", jsonParam.toString());
            Q(jsonParam.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void Q(String message) {

        if (logQueue.size() < DD_MAXQSIZE) {
            logQueue.add(message);
            currentQSize++;
        } else {
            flush(this);
            currentQSize = 0;
        }
    }


    public void flush(final DD_LOG logger) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL(DD_ENDPOINT + "" + DD_API_KEY);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    for (int i = 0; i < logger.logQueue.size(); i++) {
                        String logInfo = "" + logger.logQueue.get(i);
                        os.writeBytes(logInfo);
                        os.flush();
                    }
                    logger.logQueue.clear();
                    os.close();
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}




