package com.example.monitoringairsumur;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView tvSuhu, tvStatusSuhu, tvKekeruhan, tvStatusKekeruhan;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSuhu = findViewById(R.id.tvSuhu);
        tvStatusSuhu = findViewById(R.id.tvStatusSuhu);
        tvKekeruhan = findViewById(R.id.tvKekeruhan);
        tvStatusKekeruhan = findViewById(R.id.tvStatusKekeruhan);

        databaseRef = FirebaseDatabase.getInstance().getReference("monitoring");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Object suhuObj = snapshot.child("suhu").getValue();
                    if (suhuObj != null) {
                        double suhu = Double.parseDouble(suhuObj.toString());
                        tvSuhu.setText(suhu + " °C");

                        if (suhu > 32.0) {
                            tvStatusSuhu.setText("Status: Suhu Air Hangat / Tinggi");
                            tvStatusSuhu.setTextColor(Color.parseColor("#EF4444"));
                        } else {
                            tvStatusSuhu.setText("Status: Suhu Air Normal");
                            tvStatusSuhu.setTextColor(Color.parseColor("#10B981"));
                        }
                    }

                    Object keruhObj = snapshot.child("kekeruhan").getValue();
                    if (keruhObj != null) {
                        double kekeruhan = Double.parseDouble(keruhObj.toString());
                        tvKekeruhan.setText(kekeruhan + " NTU");

                        if (kekeruhan > 25.0) {
                            tvStatusKekeruhan.setText("Status: Air Keruh (Perlu Penyaringan)");
                            tvStatusKekeruhan.setTextColor(Color.parseColor("#EF4444"));
                        } else {
                            tvStatusKekeruhan.setText("Status: Air Jernih");
                            tvStatusKekeruhan.setTextColor(Color.parseColor("#10B981"));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Gagal membaca data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
