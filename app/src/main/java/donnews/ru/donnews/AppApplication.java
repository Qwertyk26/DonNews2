package donnews.ru.donnews;

import android.app.Application;

import donnews.ru.donnews.Dependencies.ApiComponent;
import donnews.ru.donnews.Dependencies.DaggerApiComponent;
import donnews.ru.donnews.Dependencies.DaggerNetworkComponent;
import donnews.ru.donnews.Dependencies.NetworkComponent;
import donnews.ru.donnews.Dependencies.NetworkModule;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;
/**
 * Created by antonnikitin on 25.03.17.
 */

public class AppApplication extends Application {

    private ApiComponent apiComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        resolveDependency();
        initMetric();
    }
    private void initMetric() {
        YandexMetricaConfig.Builder configBuilder = YandexMetricaConfig.newConfigBuilder("ff75bdde-a2dd-49d7-b966-3fa3c7921ca2");
        YandexMetrica.activate(getApplicationContext(), configBuilder.build());
        configBuilder.withLocationTracking(true);
        configBuilder.withCrashReporting(true);
    }
    public ApiComponent getApiComponent() {
        return apiComponent;
    }
    private void resolveDependency() {
        apiComponent = DaggerApiComponent.builder().networkComponent(getNetworkComponent()).build();
    }
    public NetworkComponent getNetworkComponent() {
        return DaggerNetworkComponent.builder().networkModule(new NetworkModule(Constant.BASE_URL)).build();
    }
}
