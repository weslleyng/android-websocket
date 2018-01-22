package br.com.weslleybarbosa.appsocket;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.ServiceWorkerWebSettings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import br.com.weslleybarbosa.appsocket.model.Mensagem;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.schedulers.Schedulers;
import okhttp3.WebSocket;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;


/**
 * Criado por Weslley Barbosa em 20/01/2018.
 */

public class ClienSocketListener {

    private final String TAG = "AppSocket";


    private SocketListener listener;

    private StompClient mStompClient;

    private Gson gson;


    public ClienSocketListener(SocketListener listener) {
        this.listener = listener;
        gson = new GsonBuilder().create();


        mStompClient = Stomp.over(WebSocket.class, "ws://192.168.1.31:8080/socket/websocket");


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
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mStompClient.connect();
            }
        });
        mStompClient.topic("/topic/greetings").subscribe(
                new Consumer<StompMessage>() {
                    @Override
                    public void accept(StompMessage topicMessage) throws Exception {
                        Log.d(TAG, topicMessage.getPayload());
                        output(topicMessage.getPayload());
                    }
                });


//                Subscriber<StompMessage>() {
//
//            @Override
//            public void onSubscribe(Subscription s) {
//                Log.d(TAG, "onSubscribe: topic");
//            }
//
//            @Override
//            public void onNext(StompMessage stompMessage) {
//
//                Log.i(TAG, "onNext: "+stompMessage.compile());
//                Log.i(TAG, "onNext: "+stompMessage.getStompCommand());
//                output(stompMessage.compile());
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Log.e(TAG, "onError: ", t);
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete: ");
//            }
//        });
    }


    public void send(Mensagem msg) {
        mStompClient.send("/topic/hello", msg.getMensagem());
    }

    public void send(String msg) {
        Mensagem m = new Mensagem();
        m.setMensagem(msg);
        mStompClient.send("/app/hello", gson.toJson(m)).subscribe();
    }

    public void subscribe(String channel) {
        mStompClient.topic(channel);
    }

    public void close() {
        mStompClient.disconnect();
    }


    private void output(String msg) {
        Log.i(TAG, "output: ");
        listener.onMsg(msg);
    }


    public interface SocketListener {
        void onMsg(String msg);

        void toast(String toast);
    }

    public StompClient getmStompClient() {
        return mStompClient;
    }
}
