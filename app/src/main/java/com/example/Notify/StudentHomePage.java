package com.example.Notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentHomePage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<NoticeItem> noticeItems;
    DatabaseReference databaseReference;
    NoticeStuAdapter adapter;
    TextView noticeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_page);

        getSupportActionBar();

        recyclerView = findViewById(R.id.noticeRecyclerView);
        noticeCount = findViewById(R.id.noticeCount);
        databaseReference = FirebaseDatabase.getInstance().getReference("Notice");
        noticeItems = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoticeStuAdapter adapter = new NoticeStuAdapter(this, noticeItems, new NoticeStuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NoticeItem noticeItem) {
                // Handle item click here
                // Example: Launch NoticeDetailActivity and pass data

                Intent intent = new Intent(StudentHomePage.this, NoticeDetail.class);
                intent.putExtra("noticeHead", noticeItem.getNoticeHead());
                intent.putExtra("noticeBody", noticeItem.getNoticebody());
                intent.putExtra("pdfUrl",noticeItem.getUrl());
                // Add any additional data you need to pass
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    NoticeItem noticeItem = dataSnapshot.getValue(NoticeItem.class);
                    noticeItems.add(noticeItem);
                    noticeCount.setText("Total Notice :- "+noticeItems.size());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}