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
            File file = new File("/sdcard/x.png");
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



        try {
            String ip = intent.getStringExtra("IP");
        Process sh = Runtime.getRuntime().exec("su", null,null);
        OutputStream os = sh.getOutputStream();
        os.write(("/system/bin/screencap -p /sdcard/x.png").getBytes("ASCII"));
        os.flush();
        os.close();
        sh.waitFor();

        ClientAsyncTask clientAST = new ClientAsyncTask();
        String [] ip_port = ip.split(":");
        clientAST.execute(ip_port);


            //
      /*  File myFile = new File("/sdcard/x.png");
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("AUTH","MobSF-Screen-Service");
        RequestParams params = new RequestParams();
        params.put("file", myFile);
       client.post("http://"+ip+"/ScreenUpload/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                Log.d("MobSF", "Post Failure");
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                //Log.d("MobSF", "Post Success");
            }

            });*/
        } catch (Exception e) {
            Log.d("MobSF", "File Upload Failed: " + e.getMessage().toString());
        }

        return Service.START_NOT_STICKY;
    }

}
