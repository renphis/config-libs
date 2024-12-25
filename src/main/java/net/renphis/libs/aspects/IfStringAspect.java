package net.renphis.libs.aspects;

import net.renphis.libs.Config;
import net.renphis.libs.annotations.IfString;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IfStringAspect {

    @Pointcut("execution(@net.renphis.libs.annotations.IfString * *(..)) && @annotation(ifString)")
    public void callAt(IfString ifString) {}

    @Around(value = "callAt(ifString)", argNames = "joinPoint, ifString")
    public Object aroundIfString(ProceedingJoinPoint joinPoint, IfString ifString) throws Throwable {
        if (!Config.isInitialized() || !Config.has(ifString.key())) {
            System.out.println("Config not initialized or key not found");
            return null;
        }

        final String value = Config.getString(ifString.key());
        if (value == null || !value.equals(ifString.value())) {
            System.out.println("Value not found or does not match");
            return null;
        }

        return joinPoint.proceed();
    }
}
