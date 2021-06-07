package com.github.springboot.template.annotation;

import com.github.springboot.template.model.ApiResponse;
import com.github.springboot.template.service.ISysWebLogService;
import com.github.springboot.template.support.Status;
import com.github.springboot.template.utils.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Facade的切面处理类，统一统计进行参数校验及异常捕获
 */
@Slf4j
@Aspect
@Component
public class FacadeAspect {

    private final ISysWebLogService sysLogService;

    public FacadeAspect(ISysWebLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    @Around("@annotation(com.github.springboot.template.annotation.Facade)")
    public Object facade(ProceedingJoinPoint pjp) throws Exception {

        // 获取，执行目标方法
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();

        Object[] args = pjp.getArgs();

        log.info("获取@Facede注解参数列表,参数: {}", args);

        // 参数类型
        Class<?> returnType = ((MethodSignature) pjp.getSignature()).getMethod().getReturnType();

        //循环遍历所有参数，进行参数校验
        for (Object parameter : args) {
            try {
                BeanValidator.validateObject(parameter);
            } catch (ValidationException e) {
                return getFailedResponse(returnType, e);
            }
        }

        try {
            // 目标方法执行
            return pjp.proceed();
        } catch (Throwable throwable) {
            // 保存异常信息
            sysLogService.create(throwable.getMessage());

            // 返回通用失败响应
            return getFailedResponse(returnType, throwable);
        }
    }

    /**
     * 定义并返回一个通用的失败响应
     */
    private Object getFailedResponse(Class<?> returnType, Throwable throwable)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        //如果返回值的类型为BaseResponse 的子类，则创建一个通用的失败响应
        if (returnType.getDeclaredConstructor().newInstance() instanceof ApiResponse) {
            ApiResponse response = (ApiResponse) returnType.getDeclaredConstructor().newInstance();

            String message = throwable.getMessage();
            log.error("校验bean异常：", throwable);

            response.setMessage(message);
            response.setCode(Status.ERROR.getCode());
            return response;
        }

        log.error("failed to getFailedResponse , returnType ({}) is not instanceof BaseResponse", returnType);
        return null;
    }

}
