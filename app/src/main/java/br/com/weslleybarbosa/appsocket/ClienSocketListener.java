package br.com.weslleybarbosa.appsocket;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.ServiceWorkerWebSettings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.WebSocket;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;


/**
 * Criado por Weslley Barbosa em 20/01/2018.
 */

public class ClienSocketListener<T>{

    private final String TAG = "AppSocket";


    private SocketListener<T> listener;

    private StompClient mStompClient;

    private Gson gson;


    public ClienSocketListener(SocketListener listener, String server) {

        this.listener = listener;
        init(server);

    }
    public ClienSocketListener(String server) {
        super();

        init(server);
    }

    private void init(String server) {
        gson = new GsonBuilder().create();
        mStompClient = Stomp.over(WebSocket.class, server);


        mStompClient.lifecycle().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LifecycleEvent>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i(TAG, "onSubscribe: lifecycle");
            }

            @Override
            public void onNext(LifecycleEvent lifecycleEvent) {
                Log.i(TAG, "onNext: type" + lifecycleEvent.getType());
                Log.i(TAG, "onNext: type" + lifecycleEvent.getMessage());
                Log.e(TAG, "onNext: type", lifecycleEvent.getException());
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });

        mStompClient.connect();
    }

    public void send(String destiny, String msg) {
        mStompClient.send(destiny, msg).subscribe();
    }


    public void send(String destiny) {
        mStompClient.send(destiny).subscribe();
    }
    public void send(String destiny, Object msg) {
        mStompClient.send(destiny, gson.toJson(msg)).subscribe();
    }


    public void subscribe(String channel,final Class<T> classe) {
        mStompClient.topic(channel).subscribe(new Consumer<StompMessage>() {
            @Override
            public void accept(StompMessage stompMessage) throws Exception {
                Log.i(TAG, "accept: ");
                if (listener!=null)
                listener.onMsg(gson.fromJson(stompMessage.getPayload(),classe));
            }
        });
    }
    public void subscribe(String channel,Class<?> classe,final SubscribeListener subscribeListener) {
        mStompClient.topic(channel).subscribe(new Consumer<StompMessage>() {
            @Override
            public void accept(StompMessage stompMessage) throws Exception {
                Log.i(TAG, "accept: ");

                subscribeListener.onAccpt(gson.fromJson(stompMessage.getPayload(),classe));
            }
        });
    }

    public void close() {
        mStompClient.disconnect();
    }





    public interface SubscribeListener<K>{
        void onAccpt(K k);
    }
    public interface SocketListener<T> {
        void onMsg(T response);

        void toast(String toast);
    }

    public StompClient getmStompClient() {
        return mStompClient;
    }
}
