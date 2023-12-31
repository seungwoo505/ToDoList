package org.techtown.todolist;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private static final String TAG = "NoteAdapter";

    //todolist아이템이 들어갈 배열
    ArrayList<Note> items = new ArrayList<>();

    //todo_item.xml을 인플레이션
    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.todo_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        Note item = items.get(position);
        try {
            holder.setProgressBar(item);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.setItem(item);
        holder.setLayout();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    //ViewHolder의 역할을 하는 클래스
    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout layoutTodo;
        CheckBox checkBox;
        Button deleteButton;
        TextView progressText;

        private ProgressBar progressBar;
        private int value = 0;
        private Boolean threadStatus;
        private BackgroundTask task;
        public ViewHolder(View itemView){
            super(itemView);

            layoutTodo = itemView.findViewById(R.id.layoutTodo);
            checkBox = itemView.findViewById(R.id.checkBox);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            progressBar = itemView.findViewById(R.id.Progress);
            progressText = itemView.findViewById(R.id.ProgressText);

            //버튼 클릭 시 SQLite에서 데이터 삭제
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //CheckBox의 String 가져오기
                    String TODO = (String) checkBox.getText();
                    deleteToDo(TODO);
                    Toast.makeText(v.getContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                }
                Context context;

                private void deleteToDo(String TODO){
                    //테이블을 삭제하는 sql문 delete...
                    String deleteSql = "delete from " + NoteDatabase.TABLE_NOTE + " where " + "  TODO = '" + TODO + "'";
                    NoteDatabase database = NoteDatabase.getInstance(context);
                    //삭제하는 sql문 실행
                    database.execSQL(deleteSql);
                }
            });
        }
        //EditText에서 입력받은 checkBox의 텍스트를 checkBox의 Text에 넣을 수 있게 하는 메서드
        public void setItem(Note item){
            checkBox.setText(item.getTodo());
        }

        //아이템들을 담은 LinearLayout을 보여주게하는 메서드
        public void setLayout(){
            layoutTodo.setVisibility(View.VISIBLE);
        }

        public void setProgressBar(Note item) throws ParseException {
            LocalDateTime Today, days, to_day;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                to_day = LocalDateTime.parse(item.getToday());
                Today = LocalDateTime.now();
                days = LocalDateTime.parse(item.getDate());
                int d_day = (int)(Duration.between(Today, days).toDays() + 1);;
                int v = item.getDays();
                value = (int)(((v - d_day)  / (float)v) * 100);
                progressText.setText(to_day.getYear() + "-" + to_day.getMonthValue() + "-" + to_day.getDayOfMonth()
                                    + "\n         ~\n"
                                    + days.getYear() + "-" + days.getMonthValue() + "-" + days.getDayOfMonth() + "\n"
                                    + "        "  + value + "%");
                task = new BackgroundTask();
                task.execute();
            }
        }

        class BackgroundTask extends AsyncTask<Integer, Integer, Integer>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                progressBar.setProgress(value);
                threadStatus=true;
                Log.d(TAG, "onPreExecute: ");
            }
            @Override
            protected Integer doInBackground(Integer... integers) {
                Log.d(TAG, "doInBackground: ");
                publishProgress(value);
                return 1;
            }

            @Override
            protected void onProgressUpdate(Integer... values){
                Log.d(TAG, "onProgressUpdate: ");
                super.onProgressUpdate(values);
                progressBar.setProgress(values[0]);
            }
        }
    }
    
    //배열에 있는 item들을 가리킴
    public void setItems(ArrayList<Note> items){
        this.items = items;
    }
}
