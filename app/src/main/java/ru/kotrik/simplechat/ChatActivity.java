package ru.kotrik.simplechat;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import ru.kotrik.simplechat.Adapters.ChatAdapter;
import ru.kotrik.simplechat.Models.ChatItem;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    String nickname;

    RecyclerView rv;
    Button send;
    EditText message;
    RelativeLayout rl;

    ArrayList<ChatItem> items;
    ChatAdapter ca;

    Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if(getIntent().getStringExtra("nickname")==null) {
            Toast.makeText(this, "Nickname is empty", Toast.LENGTH_SHORT).show();
            finish();
        }
        if(savedInstanceState != null) {
            nickname = savedInstanceState.getString("nickname");
        }
        nickname = getIntent().getStringExtra("nickname");

        rv = findViewById(R.id.rv_items);
        rv.setLayoutManager(new LinearLayoutManager(this));
        send = findViewById(R.id.button_chatbox_send);
        message = findViewById(R.id.edittext_chatbox);

        rl = findViewById(R.id.rl);
        items = new ArrayList<ChatItem>();
        ca = new ChatAdapter(items);

        try {
            socket = IO.socket(getResources().getString(R.string.socket_ip)+"?nickname="+nickname);
            socket.on(Socket.EVENT_CONNECT, new ConnectedListener())
                    .on(Socket.EVENT_MESSAGE, new MainListener());
            socket.connect();
            rv.setAdapter(ca);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Socket error");
        }

        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_chatbox_send:
                if(message.getText().toString().isEmpty()) {
                    Snackbar.make(rl,"No message",Snackbar.LENGTH_SHORT).show();
                    break;
                }
                socket.send(message.getText().toString());
                message.getText().clear();
                break;
        }
    }

    class ConnectedListener implements Emitter.Listener {
        @Override
        public void call(Object... args) {
            Snackbar.make(rl,"Connected",Snackbar.LENGTH_SHORT).show();
        }
    }

    class MainListener implements Emitter.Listener {

        @Override
        public void call(Object... args) {
            JSONObject obj = (JSONObject)args[0];
            try{
                System.out.println(obj.getString("event"));
                switch (obj.getString("event")) {
                    case "messageNewReceived":
                        String name = obj.getString("name");
                        String time = obj.getString("time");
                        String message = obj.getString("text");
                        items.add(new ChatItem(2, name, time, message));
                        break;
                    case "messageSent":
                        String name_m = obj.getString("name");
                        String time_m = obj.getString("time");
                        String message_m = obj.getString("text");
                        items.add(new ChatItem(1, name_m, time_m, message_m));
                        break;
                    case "userJoin":
                        String name_j = obj.getString("nickname");
                        String time_j = obj.getString("time");
                        String message_j = obj.getString("text");
                        items.add(new ChatItem(0, name_j, time_j, message_j));
                        break;
                    case "userLeave":
                        String name_l = obj.getString("nickname");
                        String time_l = obj.getString("time");
                        String message_l = obj.getString("text");
                        items.add(new ChatItem(0, name_l, time_l, message_l));
                        break;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ca.notifyDataSetChanged();
                    }
                });
                System.out.println("Received " + items.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}
