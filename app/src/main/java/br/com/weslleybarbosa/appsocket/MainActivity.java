package br.com.weslleybarbosa.appsocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.weslleybarbosa.appsocket.adapter.ListAdapter;


public class MainActivity extends AppCompatActivity implements ClienSocketListener.SocketListener {

    ListView list;
    EditText editmsg;
    ClienSocketListener client;
    private ListAdapter adapter;
    private List<String> lista;

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
            client = new ClienSocketListener(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);
        editmsg = findViewById(R.id.editmsg);
        lista = new ArrayList<>();
        adapter = new ListAdapter(lista);
        list.setAdapter(adapter);
        client = new ClienSocketListener(this);
    }


    public void send(View view){

        client.send(editmsg.getText().toString());
        editmsg.setText("");
    }



    @Override
    public void onMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lista.add(msg);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void toast(String toast) {
        Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
    }
}
