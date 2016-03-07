package opensecurity.screencast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class Timer extends BroadcastReceiver {
    public Timer() {
    }
    final public static String ONE_TIME = "onetime";
    @Override
    public void onReceive(Context context, Intent intent) {
        String ip= intent.getStringExtra("IP");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "OPSEC");
        //Acquire the lock
        wl.acquire();
        Intent i = new Intent(context, Caster.class);
        i.putExtra("IP",ip);
        context.startService(i);
        //Release the lock
        wl.release();


    }
    public void SetAlarm(Context context,int seconds, String ip)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Timer.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        intent.putExtra("IP",ip);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * seconds , pi);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Timer.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
