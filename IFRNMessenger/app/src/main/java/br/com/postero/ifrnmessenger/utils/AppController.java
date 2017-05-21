package br.com.postero.ifrnmessenger.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Francisco on 21/05/2017.
 */

public class AppController {
    private static AppController instance;
    private RequestQueue requestQueue;
    private static Context context;

    private AppController(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized AppController getInstance(Context context) {
        if (instance == null) {
            instance = new AppController(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}