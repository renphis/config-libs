package net.renphis.libs.aspects;

import net.renphis.libs.Config;
import net.renphis.libs.annotations.IfByte;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IfByteAspect {
    @Pointcut("execution(@net.renphis.libs.annotations.IfByte * *(..)) && @annotation(ifByte)")
    public void callAt(IfByte ifByte) {}

    @Around(value = "callAt(ifByte)", argNames = "joinPoint, ifByte")
    public Object aroundIfByte(ProceedingJoinPoint joinPoint, IfByte ifByte) throws Throwable {
        if (!Config.has(ifByte.key())) {
            return null;
        }

        final Number wrappedValue = Config.getNumber(ifByte.key());
        if (wrappedValue == null || wrappedValue.byteValue() != ifByte.value()) {
            return null;
        }

        return joinPoint.proceed();
    }
}
