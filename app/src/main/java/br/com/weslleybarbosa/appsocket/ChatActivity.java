package br.com.weslleybarbosa.appsocket;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import br.com.weslleybarbosa.appsocket.adapter.MessageAdapterList;
import br.com.weslleybarbosa.appsocket.model.Chat;
import br.com.weslleybarbosa.appsocket.model.Message;

public class ChatActivity extends AppCompatActivity implements MessageAdapterList.MessageListener {
    public static final String EXTRA_CHAT_ID = "br.com.weslleydev.EXTRA_CHAT_KEY";
    EditText editmsg;
    RecyclerView list;
    ClienSocketListener client;
    private Chat currentChat;
    private MessageAdapterList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        list = findViewById(R.id.list);
        client = new ClienSocketListener(getString(R.string.url_server));
        client.subscribe("/messages/" + getChatId(), Chat.class, new ClienSocketListener.SubscribeListener<Chat>() {
            @Override
            public void onAccpt(Chat chat) {
                currentChat= chat;

                atualizar();
            }

        });

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                client.send("/app/chat",getChatId());
            }
        },1000);


        editmsg = findViewById(R.id.editmsg);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void atualizar() {
        adapter = new MessageAdapterList(currentChat.messageList(),this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        client.subscribe("/topic/" + currentChat.getId(), Message.class, new ClienSocketListener.SubscribeListener<Message>() {

            @Override
            public void onAccpt(Message message) {
                currentChat.getMessages().add(message);
                adapter.setMessages(currentChat.messageList());
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStop() {
        client.getmStompClient().disconnect();
        super.onStop();
    }

    public void send(View view){

        Message m = new Message();
        m.setContent(editmsg.getText().toString());
        if (currentChat!=null) {
            client.send("/message/" + currentChat.getId(), m);
            editmsg.setText("");
        }else{
            editmsg.setError("Problema ao enviar");
            client.send("/app/chat",getChatId());

        }
    }

    public String getChatId() {
        return getIntent().getStringExtra(EXTRA_CHAT_ID);
    }

    @Override
    public boolean isSender(Message msg) {


        return false;
    }
}
