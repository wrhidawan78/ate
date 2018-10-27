package org.waw.project.ate.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {


    public static final String BASE_URL = "http://192.168.43.67";
    //public static final String BASE_URL = "http://192.168.0.13";
    //public static final String BASE_URL = "http://192.168.9.41";
    //public static final String BASE_URL = "http://103.105.66.214:4444";

    private static Retrofit retrofit = null;
    
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
