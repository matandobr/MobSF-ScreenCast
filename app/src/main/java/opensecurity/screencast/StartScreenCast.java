package opensecurity.screencast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StartScreenCast extends Service {
    public StartScreenCast() {
    }
    private  Timer alarm;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("MobSF", "ScreenCast Launched!");
        String post_server = "";
        post_server = intent.getAction().toString();
        Log.d("MobSF", "ScreenCast Server: "+ post_server);

        alarm = new Timer();
        alarm.SetAlarm(getApplicationContext(), 1,post_server);
        Log.d("MobSF", "Timer Started");
        return START_STICKY;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();

    }
}
