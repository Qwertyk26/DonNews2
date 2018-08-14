package donnews.ru.donnews.Dependencies;

import dagger.Module;
import dagger.Provides;
import donnews.ru.donnews.Network.NetworkClient;
import retrofit2.Retrofit;

/**
 * Created by antonnikitin on 25.08.16.
 */
@Module
public class ApiModule {
    @Provides
    @CustomScope
    NetworkClient provideNetworkClient(Retrofit retrofit) {
        return retrofit.create(NetworkClient.class);
    }
}
