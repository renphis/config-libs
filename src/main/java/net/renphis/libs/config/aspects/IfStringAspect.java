package net.renphis.libs.config.aspects;

import net.renphis.libs.config.Config;
import net.renphis.libs.config.annotations.IfString;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IfStringAspect {
    @Pointcut("execution(@net.renphis.libs.config.annotations.IfString * *(..)) && @annotation(ifString)")
    public void callAt(IfString ifString) {}

    @Around(value = "callAt(ifString)", argNames = "joinPoint, ifString")
    public Object aroundIfString(ProceedingJoinPoint joinPoint, IfString ifString) throws Throwable {
        if (!Config.has(ifString.key())) {
            return null;
        }

        final String value = Config.getString(ifString.key());
        if (value == null || !value.equals(ifString.value())) {
            return null;
        }

        return joinPoint.proceed();
    }
}
