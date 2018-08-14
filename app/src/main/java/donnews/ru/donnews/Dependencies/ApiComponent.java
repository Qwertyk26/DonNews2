package donnews.ru.donnews.Dependencies;


import dagger.Component;
import donnews.ru.donnews.AboutActivity;
import donnews.ru.donnews.AdvertActivity;
import donnews.ru.donnews.AuthActivity;
import donnews.ru.donnews.DetailNewsActivity;
import donnews.ru.donnews.Fragments.AuthorcolumnFragment;
import donnews.ru.donnews.Fragments.FullNewsFragment;
import donnews.ru.donnews.Fragments.InterviewFragment;
import donnews.ru.donnews.Fragments.MainNewsFragment;
import donnews.ru.donnews.Fragments.SpecproectsFragment;
import donnews.ru.donnews.Fragments.StoriesFragment;
import donnews.ru.donnews.MainActivity;
import donnews.ru.donnews.SearchActivity;
import donnews.ru.donnews.SignUpActivity;
import donnews.ru.donnews.SocAuthActivity;


/**
 * Created by antonnikitin on 25.08.16.
 */
@CustomScope
@Component(modules = ApiModule.class, dependencies = NetworkComponent.class)
public interface ApiComponent {
    void inject(MainActivity activity);
    void inject(FullNewsFragment fragment);
    void inject(MainNewsFragment fragment);
    void inject(StoriesFragment fragment);
    void inject(InterviewFragment fragment);
    void inject(SpecproectsFragment fragment);
    void inject(AuthorcolumnFragment fragment);
    void inject(AdvertActivity activity);
    void inject(AboutActivity activity);
    void inject(SearchActivity activity);
    void inject(SignUpActivity activity);
    void inject(AuthActivity activity);
    void inject(DetailNewsActivity activity);
    void inject(SocAuthActivity activity);
}
