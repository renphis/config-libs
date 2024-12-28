package net.renphis.libs.aspects;

import net.renphis.libs.Config;
import net.renphis.libs.annotations.IfShort;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IfShortAspect {
    @Pointcut("execution(@net.renphis.libs.annotations.IfShort * *(..)) && @annotation(ifShort)")
    public void callAt(IfShort ifShort) {}

    @Around(value = "callAt(ifShort)", argNames = "joinPoint, ifShort")
    public Object aroundIfShort(ProceedingJoinPoint joinPoint, IfShort ifShort) throws Throwable {
        if (!Config.has(ifShort.key())) {
            return null;
        }

        final Number wrappedValue = Config.getNumber(ifShort.key());
        if (wrappedValue == null || wrappedValue.shortValue() != ifShort.value()) {
            return null;
        }

        return joinPoint.proceed();
    }
}