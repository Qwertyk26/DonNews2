package donnews.ru.donnews.Network;


import donnews.ru.donnews.Models.AboutResonse;
import donnews.ru.donnews.Models.RequestResponse;
import donnews.ru.donnews.Models.SignUpRespones;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by antonnikitin on 26.03.17.
 */

public interface NetworkClient {


    @GET("api.getFullArticleList")
    Observable<RequestResponse> getMixNews(@Query("offset") int offset, @Query("count") int count);

    @GET("api.getArticleList")
    Observable<RequestResponse> getArticleList(@Query("category") String category, @Query("offset") int offset, @Query("count") int count);

    @GET("api.getFullArticleList")
    Observable<RequestResponse> getLoadMoreMixNews(@Query("offset") int offset, @Query("count") int count);

    @GET("api.getArticleList")
    Observable<RequestResponse> getLoadMoreArticleList(@Query("category") String category, @Query("offset") int offset, @Query("count") int count);

    @GET("api.Advert")
    Observable<AboutResonse> getAdvert();

    @GET("api.About")
    Observable<AboutResonse> getAbout();

    @GET("api.getSearch")
    Observable<RequestResponse> search(@Query("pattern") String pattern, @Query("offset") int offset, @Query("count") int count);

    @GET("api.registration")
    Observable<SignUpRespones> signUpRequest(@Query("email") String email, @Query("username") String username, @Query("password1") String password1, @Query("password2") String password2);

    @GET("api.Login")
    Observable<SignUpRespones> loginRequest(@Query("email") String email, @Query("password") String password);

    @GET("api.addComment")
    Observable<SignUpRespones> addComment(@Query("alias") String alias, @Query("text") String text, @Query("dn_token") String dn_token);

    @GET("api.getToken")
    Observable<SignUpRespones> getToken();

    @GET("api.soc_auth")
    Observable<SignUpRespones> socAuth(@Query("dn_token") String dn_token);
}
