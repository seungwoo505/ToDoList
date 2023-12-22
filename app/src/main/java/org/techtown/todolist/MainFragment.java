
package org.techtown.todolist;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    RecyclerView recyclerView;
    NoteAdapter adapter;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //fragment_main에 인플레이션을 함
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        initUI(rootView);

        try {
            loadNoteListData();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //당겨서 새로고침
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    try {
                        loadNoteListData();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
        });
        return rootView;
    }

    private void initUI(ViewGroup rootView){

        //recyclerView연결
        recyclerView = rootView.findViewById(R.id.recyclerView);

        //LinearLayoutManager을 이용하여 recyclerView설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //어댑터 연결결
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
    }

    public int loadNoteListData() throws ParseException {

        //데이터를 가져오는 sql문 (id의 역순으로 정렬)
        String loadSql = "select _id, TODO, days, today, date from " + NoteDatabase.TABLE_NOTE + " order by _id desc";

        int recordCount = -1;
        NoteDatabase database = NoteDatabase.getInstance(context);

        if(database != null){
            //cursor를 객체화하여 rawQuery문 저장
            Cursor outCursor = database.rawQuery(loadSql);
            recordCount = outCursor.getCount();

            //DB에 있는 모든 데이터 저장할 배열 생성
            ArrayList<Note> items = new ArrayList<>();

            //for문을 통해 하나하나 추가
            for(int i = 0; i < recordCount; i++){
                outCursor.moveToNext();
                int _id = outCursor.getInt(0);
                String todo = outCursor.getString(1);
                int days = outCursor.getInt(2);
                String today = outCursor.getString(3);
                String date = outCursor.getString(4);
                items.add(new Note(_id, todo, days, today, date));
            }
            outCursor.close();

            //어댑터에 연결 및 데이터셋 변경
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
        return recordCount;
    }
}