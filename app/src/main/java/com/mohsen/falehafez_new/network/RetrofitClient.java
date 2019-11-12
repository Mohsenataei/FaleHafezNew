package com.mohsen.falehafez_new.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mohsen.falehafez_new.network.converter.JsonConvertFactory;
import com.mohsen.falehafez_new.network.converter.WebService;
import com.mohsen.falehafez_new.util.Constants;

import java.util.concurrent.TimeUnit;



import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by alirezaahmadi on 9/11/16.
 * This class provides a singleton of retrofit client for all the places in the application.
 */
public abstract class RetrofitClient {
    private  static  Retrofit retrofit;
    private static Gson gson;
    private static WebService webService;

    /**
     * returns a retrofit instance. if none exists it first create one.
     * @return retrofit instance
     */
    public static Retrofit getInstance(){
        if(retrofit == null){
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(200, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();
            gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    //ApplicationContext.isDebug() ? JsonConvertFactory.create(gson) : has bugs
                    .addConverterFactory(JsonConvertFactory.create(gson))
                    .build();

        }

        return retrofit;
    }

    public static void removeInstance(){ //force to recreate retrofit instance
        retrofit = null;
    }


    public static WebService getWebService(){
        if(webService == null)
            webService = RetrofitClient.getInstance().create(WebService.class);

        return webService;
    }

    public static Gson getGson(){
        getInstance();
        return gson;
    }

}