package ep.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class ItemService {
    interface RestApi {
        String URL = "http://10.0.2.2:80/netbeans/ep-server/api/";
        //String URL = "http://10.0.2.2:80/netbeans/mvc-rest/api/";

        @GET("items")
        Call<List<Item>> getAll();

        @GET("items/{id}")
        Call<Item> get(@Path("id") int id);

        @FormUrlEncoded
        @POST("items")
        Call<Void> insert(@Field("name") String name,
                          @Field("price") double price,
                          @Field("description") String description);

        @FormUrlEncoded



        @PUT("items/{id}")
        Call<Void> update(@Path("id") int id,
                          @Field("name") String name,
                          @Field("price") double price,
                          @Field("description") String description);


        @DELETE("items/{id}")
        Call<Void> delete(@Path("id") int id);
    }

    private static RestApi instance;

    public static synchronized RestApi getInstance() {
        if (instance == null) {
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RestApi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(RestApi.class);
        }

        return instance;
    }
}
