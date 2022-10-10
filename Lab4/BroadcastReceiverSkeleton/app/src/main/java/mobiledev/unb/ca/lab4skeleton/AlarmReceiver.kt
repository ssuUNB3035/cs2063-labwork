package mobiledev.unb.ca.lab4skeleton

import android.app.Notification
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.from

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(p0: Context, p1: Intent?) {
        val intent = Intent(p0, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(p0, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val name = p0?.getString(R.string.channel_name)
        Log.i( "onReceive", "received!")
        var builder = Notification.Builder(p0, "0")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(p0?.getString(R.string.notification_title))
            .setContentText(p0?.getString(R.string.notification_text))
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(p0)) {
            notify(1, builder.build())
        }
    }

}