package net.renphis.libs.aspects;

import net.renphis.libs.Config;
import net.renphis.libs.annotations.IfString;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class IfStringAspect {

    @Pointcut("@annotation(ifString)")
    public void ifStringCut(IfString ifString) {
    }

    @Around(value = "ifStringCut(ifString)", argNames = "joinPoint,ifString")
    public Object aroundIfString(ProceedingJoinPoint joinPoint, IfString ifString) throws Throwable {
        if (!Config.isInitialized() || !Config.has(ifString.key())) {
            return null;
        }

        final String value = Config.getString(ifString.key());
        if (value == null || !value.equals(ifString.value())) {
            return null;
        }

        return joinPoint.proceed();
    }
}
