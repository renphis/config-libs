package net.renphis.libs.config.aspects;

import net.renphis.libs.config.Config;
import net.renphis.libs.config.annotations.IfInt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IfIntAspect {
    @Pointcut("execution(@net.renphis.libs.config.annotations.IfInt * *(..)) && @annotation(ifInt)")
    public void callAt(IfInt ifInt) {}

    @Around(value = "callAt(ifInt)", argNames = "joinPoint, ifInt")
    public Object aroundIfInt(ProceedingJoinPoint joinPoint, IfInt ifInt) throws Throwable {
        if (!Config.has(ifInt.key())) {
            return null;
        }

        final Number wrappedValue = Config.getNumber(ifInt.key());
        if (wrappedValue == null || wrappedValue.intValue() != ifInt.value()) {
            return null;
        }

        return joinPoint.proceed();
    }
}