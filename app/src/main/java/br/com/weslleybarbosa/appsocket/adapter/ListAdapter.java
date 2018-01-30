package br.com.weslleybarbosa.appsocket.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import br.com.weslleybarbosa.appsocket.model.Chat;

/**
 * Criado por Weslley Barbosa em 21/01/2018.
 */

public class ListAdapter extends BaseAdapter {

    private List<Chat> lista;

    public ListAdapter(List<Chat> lista, ChatListtener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    private ChatListtener listener;
    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Chat getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1,viewGroup,false).findViewById(android.R.id.text1);
        ((TextView) view).setText(getItem(i).getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChatClick(getItem(i));
            }
        });
        return view;
    }

    public List<Chat> getLista() {
        return lista;
    }

    public void setLista(List<Chat> lista) {
        this.lista = lista;
    }

    public ListAdapter(List<Chat> lista) {
        this.lista = lista;
    }

    public interface ChatListtener{
        void onChatClick(Chat chat);
    }
}
