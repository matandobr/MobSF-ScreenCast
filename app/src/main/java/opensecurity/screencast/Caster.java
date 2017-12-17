package opensecurity.screencast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.AsyncTask;
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
        protected Void doInBackground(String... params) {
            File file = new File("/data/local/tmp/x.png");
            try {
                Log.d("MobSF", "Sending image" + params);
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
                Log.d("MobSF", "File Sent");

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

    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful

        String ip="";
        try {
            ip = intent.getStringExtra("IP");
            Log.d("MobSF", "screencaping");
            ProcessBuilder pb = new ProcessBuilder("su", "-c", "/data/local/tmp/screencap_command.sh");
            Process p = pb.start();

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
