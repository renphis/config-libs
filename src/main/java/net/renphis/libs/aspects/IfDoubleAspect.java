package net.renphis.libs.aspects;

import net.renphis.libs.Config;
import net.renphis.libs.annotations.IfDouble;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IfDoubleAspect {
    @Pointcut("execution(@net.renphis.libs.annotations.IfDouble * *(..)) && @annotation(ifDouble)")
    public void callAt(IfDouble ifDouble) {}

    @Around(value = "callAt(ifDouble)", argNames = "joinPoint, ifDouble")
    public Object aroundIfDouble(ProceedingJoinPoint joinPoint, IfDouble ifDouble) throws Throwable {
        if (!Config.has(ifDouble.key())) {
            return null;
        }

        final Number wrappedValue = Config.getNumber(ifDouble.key());
        if (wrappedValue == null || wrappedValue.doubleValue() != ifDouble.value()) {
            return null;
        }

        return joinPoint.proceed();
    }
}