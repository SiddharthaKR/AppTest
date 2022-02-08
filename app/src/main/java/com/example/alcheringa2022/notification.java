package com.example.alcheringa2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class notification extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView=findViewById(R.id.notificationlist);
        database= FirebaseDatabase.getInstance().getReference("NotificationData");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list=new ArrayList<>();
        notificationAdapter=new NotificationAdapter(this,list);
        recyclerView.setAdapter(notificationAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                     NotificationData notificationData=dataSnapshot.getValue(NotificationData.class);
                     list.add(notificationData);
                 }
                 notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}