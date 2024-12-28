package net.renphis.libs.aspects;

import net.renphis.libs.Config;
import net.renphis.libs.annotations.IfBoolean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IfBooleanAspect {
    @Pointcut("execution(@net.renphis.libs.annotations.IfBoolean * *(..)) && @annotation(ifBoolean)")
    public void callAt(IfBoolean ifBoolean) {}

    @Around(value = "callAt(ifBoolean)", argNames = "joinPoint, ifBoolean")
    public Object aroundIfBoolean(ProceedingJoinPoint joinPoint, IfBoolean ifBoolean) throws Throwable {
        if (!Config.has(ifBoolean.key())) {
            return null;
        }

        final Boolean wrappedValue = Config.getBoolean(ifBoolean.key());
        if (wrappedValue == null || wrappedValue != ifBoolean.value()) {
            return null;
        }

        return joinPoint.proceed();
    }
}
