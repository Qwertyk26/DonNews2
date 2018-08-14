package donnews.ru.donnews.Dependencies;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by antonnikitin on 25.08.16.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomScope {

}
