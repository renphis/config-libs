package net.renphis.libs.aspects;

import net.renphis.libs.Config;
import net.renphis.libs.annotations.IfFloat;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IfFloatAspect {
    @Pointcut("execution(@net.renphis.libs.annotations.IfFloat * *(..)) && @annotation(ifFloat)")
    public void callAt(IfFloat ifFloat) {}

    @Around(value = "callAt(ifFloat)", argNames = "joinPoint, ifFloat")
    public Object aroundIfFloat(ProceedingJoinPoint joinPoint, IfFloat ifFloat) throws Throwable {
        if (!Config.has(ifFloat.key())) {
            return null;
        }

        final Number wrappedValue = Config.getNumber(ifFloat.key());
        if (wrappedValue == null || wrappedValue.floatValue() != ifFloat.value()) {
            return null;
        }

        return joinPoint.proceed();
    }
}