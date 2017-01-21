package app.com.example.android.arxivreader;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Admin on 02-Jul-16.
 */
public interface ApiService {
    @GET("api/query?")
    Call<YourObject> getUser(@Query(value = "search_query",encoded = true) String query,
                             @Query("start") String startIndex,
                             @Query("max_results") String maxResults,
                             @Query("sortBy") String sortPreference);

    @GET
    Call<YourObject> getFromUrl(@Url String url);
}
