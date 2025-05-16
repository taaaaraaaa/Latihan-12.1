package id.genta.ramadhan.latihan121;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private EditText taskInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskInput = findViewById(R.id.taskInput);
        timePicker = findViewById(R.id.timePicker);
        Button scheduleButton = findViewById(R.id.scheduleButton);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        scheduleButton.setOnClickListener(v -> {
            String taskName = taskInput.getText().toString();

            if (taskName.isEmpty()) {
                Toast.makeText(this, "Masukkan nama tugas!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ambil waktu dari TimePicker
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
            calendar.set(Calendar.SECOND, 0);

            // Pastikan waktu di masa depan
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                // Tambah 1 hari jika waktu sudah lewat
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            // Buat intent untuk BroadcastReceiver
            Intent intent = new Intent(this, TaskAlarmReceiver.class);
            intent.setAction("id.septa.latihan121.TASK_ALARM_ACTION");
            intent.putExtra("task_name", taskName);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            );

            // Jadwalkan alarm
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Toast.makeText(this, "Pengingat tugas dijadwalkan untuk " + taskName, Toast.LENGTH_SHORT).show();
        });
    }
}
