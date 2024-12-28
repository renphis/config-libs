package net.renphis.libs.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IfChar {
    /**
     * The key to check in the configuration
     */
    String key();

    /**
     * The value to compare against the configuration
     */
    char value();
}
