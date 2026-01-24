package ru.practicum.shareit.aspect.LoggingAspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(30)
public class AllMethodsExceptRestControllersAndExceptionsLoggingAspect {

    // Pointcut для ВСЕХ методов в пакете ru.practicum и подпакетах
    @Pointcut("execution(* ru.practicum.shareit..*.*(..))")
    public void allMethodsInPackage() {
    }

    // Исключаем классы с аннотацией @RestController
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerClasses() {
    }

    // Исключаем классы exceptions
    @Pointcut("within(ru.practicum.shareit.exceptions..*)")
    public void exceptionsClasses() {
    }

    // Итоговый pointcut: все методы кроме тех, что в @RestController классах
    @Pointcut("allMethodsInPackage() && !restControllerClasses() && !exceptionsClasses()")
    public void allMethodsExceptRestControllersAndExceptions() {
    }

    @Before("allMethodsExceptRestControllersAndExceptions()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // Класс, в котором объявлен метод
        Class<?> declaringClass = signature.getDeclaringType();

        String fullClassName = declaringClass.getName();
        String simpleClassName = declaringClass.getSimpleName();

        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        log.info("Вызов метода {} -> {} ", simpleClassName, methodName);
        log.debug("args: {}, класс: {}", java.util.Arrays.toString(args), fullClassName);
    }

    @AfterReturning(pointcut = "allMethodsExceptRestControllersAndExceptions()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Class<?> declaringClass = signature.getDeclaringType();
        String simpleClassName = declaringClass.getSimpleName();

        String methodName = signature.getName();

        log.debug("Успешное завершение  {} -> {}. Возвращаемое значение: {}", simpleClassName, methodName, result);
    }

    @AfterThrowing(pointcut = "allMethodsExceptRestControllersAndExceptions()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Class<?> declaringClass = signature.getDeclaringType();
        String simpleClassName = declaringClass.getSimpleName();

        String methodName = signature.getName();

        log.warn("Исключение в методе {} -> {}: {}", simpleClassName, methodName, ex.getMessage());
    }
}