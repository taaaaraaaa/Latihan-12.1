package id.genta.ramadhan.latihan121;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TaskAlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "task_reminder_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String taskName = intent.getStringExtra("task_name");

        // Buat NotificationChannel untuk Android 8.0+ (API 26 ke atas)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Task Reminder Channel";
            String description = "Channel untuk pengingat tugas";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Buat intent untuk membuka DialogActivity
        Intent dialogIntent = new Intent(context, DialogActivity.class);
        dialogIntent.putExtra("task_name", taskName);

        PendingIntent dialogPendingIntent = PendingIntent.getActivity(
                context, 0, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Buat notifikasi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Pengingat Tugas")
                .setContentText("Waktunya mengerjakan: " + taskName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(dialogPendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(2, builder.build());
    }
}
