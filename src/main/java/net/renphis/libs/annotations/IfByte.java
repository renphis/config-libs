package net.renphis.libs.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IfByte {
    /**
     * The key to check in the configuration
     */
    String key();

    /**
     * The value to compare against the configuration
     */
    byte value();
}
