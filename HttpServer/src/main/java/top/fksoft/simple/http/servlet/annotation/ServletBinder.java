package top.fksoft.simple.http.servlet.annotation;

import java.lang.annotation.*;

/**
 * @author ExplodingDragon
 * @version 1.0
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServletBinder {
    String path();
    boolean bindDirectory() default false;
}
