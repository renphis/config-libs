package net.renphis.libs.aspects;

import net.renphis.libs.Config;
import net.renphis.libs.annotations.IfLong;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IfLongAspect {
    @Pointcut("execution(@net.renphis.libs.annotations.IfLong * *(..)) && @annotation(ifLong)")
    public void callAt(IfLong ifLong) {}

    @Around(value = "callAt(ifLong)", argNames = "joinPoint, ifLong")
    public Object aroundIfLong(ProceedingJoinPoint joinPoint, IfLong ifLong) throws Throwable {
        if (!Config.has(ifLong.key())) {
            return null;
        }

        final Number wrappedValue = Config.getNumber(ifLong.key());
        if (wrappedValue == null || wrappedValue.longValue() != ifLong.value()) {
            return null;
        }

        return joinPoint.proceed();
    }
}