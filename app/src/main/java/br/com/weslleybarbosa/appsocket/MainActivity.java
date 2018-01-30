package br.com.weslleybarbosa.appsocket;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import br.com.weslleybarbosa.appsocket.adapter.ListAdapter;
import br.com.weslleybarbosa.appsocket.model.Chat;


public class MainActivity extends AppCompatActivity implements ClienSocketListener.SocketListener<Chat[]> {

    ListView list;

    ClienSocketListener client;
    private ListAdapter adapter;
    private List<Chat> lista;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_iptions,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.menu_close)
            client.close();

        if (id==R.id.menu_restart) {
            client.close();
            initClient();
        }
        if (id ==R.id.menu_newchat){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            final EditText editor = new EditText(alert.getContext());
            alert.setView(editor);

            alert.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    client.send("/app/newchat",editor.getText().toString());
                    dialogInterface.dismiss();
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initClient() {
        client = new ClienSocketListener(this,getString(R.string.url_server));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        list = findViewById(R.id.list);

        lista = new ArrayList<>();
        adapter = new ListAdapter(lista, new ListAdapter.ChatListtener() {
            @Override
            public void onChatClick(Chat chat) {
                openChat(chat);
            }
        });
        list.setAdapter(adapter);
        initClient();
        client.subscribe("/topic/chats",Chat[].class);

    }

    private void openChat(Chat chat) {
        Intent in = new Intent(this,ChatActivity.class).putExtra(ChatActivity.EXTRA_CHAT_ID,chat.getId());
        startActivity(in);
    }

    @Override
    public void onMsg(Chat[] msg) {
        lista = convertToList(msg);
        Log.i("AppSocket", "onMsg: interface");
        adapter.setLista(lista);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("AppSocket", "onMsg: interface");
                adapter.notifyDataSetChanged();
                list.invalidate();
            }
        });

    }

    private List<Chat> convertToList(Chat[] msg) {
        List<Chat> l =new ArrayList<>();
        for (int i = 0; i < msg.length; i++) {
            l.add(msg[i]);
        }

        return l;
    }



    @Override
    public void toast(String toast) {
        Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
    }
}
