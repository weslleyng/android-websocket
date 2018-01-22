package br.com.weslleybarbosa.appsocket.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Criado por Weslley Barbosa em 21/01/2018.
 */

public class ListAdapter extends BaseAdapter {

    private List<String> lista;

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public String getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1,viewGroup,false).findViewById(android.R.id.text1);
        ((TextView) view).setText(getItem(i));
        return view;
    }

    public List<String> getLista() {
        return lista;
    }

    public void setLista(List<String> lista) {
        this.lista = lista;
    }

    public ListAdapter(List<String> lista) {
        this.lista = lista;
    }
}
