package ru.kotrik.simplechat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText nickname;
    Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nickname = findViewById(R.id.txt_nickname);
        go = findViewById(R.id.btn_join);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nickname.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Nickname is empty!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                    i.putExtra("nickname", nickname.getText().toString());
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}
