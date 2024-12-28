package net.renphis.libs.aspects;

import net.renphis.libs.Config;
import net.renphis.libs.annotations.IfChar;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IfCharAspect {
    @Pointcut("execution(@net.renphis.libs.annotations.IfChar * *(..)) && @annotation(ifChar)")
    public void callAt(IfChar ifChar) {}

    @Around(value = "callAt(ifChar)", argNames = "joinPoint, ifChar")
    public Object aroundIfChar(ProceedingJoinPoint joinPoint, IfChar ifChar) throws Throwable {
        if (!Config.has(ifChar.key())) {
            return null;
        }

        final String wrappedValue = Config.getString(ifChar.key());
        if (wrappedValue == null || wrappedValue.charAt(0) != ifChar.value()) {
            return null;
        }

        return joinPoint.proceed();
    }
}