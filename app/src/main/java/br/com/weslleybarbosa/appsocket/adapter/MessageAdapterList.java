package br.com.weslleybarbosa.appsocket.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.weslleybarbosa.appsocket.R;
import br.com.weslleybarbosa.appsocket.model.Message;

/**
 * Criado por Weslley Barbosa em 26/01/2018.
 */

public class MessageAdapterList extends RecyclerView.Adapter<MessageAdapterList.MessageHolder>{


    private List<Message> messages;


    private MessageListener listener;


    public MessageAdapterList(List<Message> messages, MessageListener listener) {
        this.messages = messages;
        this.listener = listener;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public MessageListener getListener() {
        return listener;
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false));
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        holder.onBind(messages.get(position));
    }

    @Override
    public int getItemViewType(int position) {

        if (listener.isSender(messages.get(position))){
            return R.layout.layout_item_message_sender;
        }else{
            return R.layout.layout_item_message_received;
        }

    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{

        TextView txmessage,txuser;

        public MessageHolder(View itemView) {
            super(itemView);
            txmessage = itemView.findViewById(R.id.tx_content);
            txuser = itemView.findViewById(R.id.tx_title);

        }
        void onBind(Message msg){
            txmessage.setText(msg.getContent());
            txuser.setText(msg.getUser().getName());
        }
    }

    public interface MessageListener{
        boolean isSender(Message msg);
    }



}
