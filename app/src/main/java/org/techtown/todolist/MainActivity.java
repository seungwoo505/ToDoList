package org.techtown.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Fragment mainFragment;
    EditText inputToDo;
    Context context;
    Button saveButton, dateButton;
    private LocalDateTime date, Today;
    private Duration days;
    private int value;

    public static NoteDatabase noteDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

        saveButton = findViewById(R.id.saveButton);
        dateButton = findViewById(R.id.dateButton);
        inputToDo = findViewById(R.id.inputToDo);

        openDatabase();
    }

    public void onClick(View v) throws ParseException {
        if(v == saveButton){
            saveToDo();
            Toast.makeText(getApplicationContext(),"추가되었습니다.",Toast.LENGTH_SHORT).show();
        }
        if(v == dateButton){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Today = LocalDateTime.now();
                        date = LocalDateTime.of(year, month + 1, day, 0, 0);
                        days = Duration.between(Today, date);
                        dateButton.setText(date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth());
                        value = (int)(days.toDays() + 1);
                    }
                }
            }, year, month, day);
            datePickerDialog.show();
        }
    }
    private void saveToDo() throws ParseException {

        //EditText에 적힌 글을 가져오기
        String todo = inputToDo.getText().toString();

        //테이블에 값을 추가하는 sql구문
        String sqlSave = "insert into " + NoteDatabase.TABLE_NOTE + " (TODO, days, today, date) values ( '" + todo + "', '" + value + "', '" + Today + "', '" + date + "')";

        //sql문 실행
        NoteDatabase database = NoteDatabase.getInstance(context);
        database.execSQL(sqlSave);

        //저장과 동시에 EditText 안의 글 초기화
        inputToDo.setText("");
        dateButton.setText("날짜 선택");
    }


    public void openDatabase() {
        // open database
        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }

        noteDatabase = NoteDatabase.getInstance(this);
        boolean isOpen = noteDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }
    }
}