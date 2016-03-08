package opensecurity.screencast;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Caster extends Service {
    public Caster() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    class ClientAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            File file = new File("/data/local/tmp/x.png");
            try {
                Socket client = new Socket(params[0], Integer.parseInt(params[1]));
                byte[] mybytearray = new byte[(int) file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

                bufferedInputStream.read(mybytearray, 0, mybytearray.length);
                OutputStream outputStream = client.getOutputStream();
                outputStream.write(mybytearray, 0, mybytearray.length);
                outputStream.flush();
                bufferedInputStream.close();
                outputStream.close();
                client.close();
                Log.d("MobSF", "File Send");

            } catch (UnknownHostException e) {
                Log.d("MobSF", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("MobSF", e.toString());
                e.printStackTrace();
            }
        return null;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful



        String ip="";

        try {
            ip = intent.getStringExtra("IP");

            Process process = Runtime.getRuntime().exec("su -c /system/bin/screencap -p /data/local/tmp/x.png");
            process.waitFor();
            Process process1 = Runtime.getRuntime().exec("su -c chmod 777 /data/local/tmp/x.png");
            process1.waitFor();

            /*Process sh = Runtime.getRuntime().exec("su", null,null);
            OutputStream os = sh.getOutputStream();
            os.write(("su -c /system/bin/screencap -p /data/local/tmp/x.png").getBytes("ASCII"));
            os.flush();
            os.write(("su -c chmod 777 /data/local/tmp/x.png").getBytes());
            os.flush();
            os.close();
            sh.waitFor();
            */

        } catch (Exception e) {
            Log.d("MobSF", "Screen Capture Failed: " + e.getMessage().toString());
        }
        try
        {
            ClientAsyncTask clientAST = new ClientAsyncTask();
            String [] ip_port = ip.split(":");
            clientAST.execute(ip_port);
        } catch (Exception e) {
            Log.d("MobSF", "File Upload Failed: " + e.getMessage().toString());
        }

        return Service.START_NOT_STICKY;
    }

}
