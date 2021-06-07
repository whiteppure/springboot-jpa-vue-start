package com.github.springboot.template.exception.handler;

import com.github.springboot.template.model.ApiResponse;
import com.github.springboot.template.service.ISysWebLogService;
import com.github.springboot.template.support.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 全局统一异常处理
 * </p>
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final ISysWebLogService sysLogService;

    public GlobalExceptionHandler(ISysWebLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResponse handlerException(Exception e) {
        // 异常信息最大长度
        final int maxLength = 1500;

        //可以根据不同的异常来做不同的操作 ex instanceof LogException ...
        e = (Exception) deepestException(e);
        log.error("【全局异常拦截】： {} ", e.getMessage());

        // 异常信息记录截取前50字符写入数据库中.
        String exceptionMessage = String.format("异常信息: %s,异常描述：%s", e.getClass().getSimpleName(), e.getMessage());
        if (exceptionMessage.length() > maxLength) {
            exceptionMessage = exceptionMessage.substring(0, maxLength);
        }

        // 保存全局未处理的异常信息
        sysLogService.create(exceptionMessage);

        ApiResponse apiResponse = ApiResponse.ofStatus(Status.ERROR);
        apiResponse.setData(e.getMessage());
        return apiResponse;
    }



    /**
     * 获取最原始的异常出处，即最初抛出异常的地方
     */
    private Throwable deepestException(Throwable e) {
        Throwable tmp = e;
        int breakPoint = 0;
        while (tmp.getCause() != null) {
            if (tmp.equals(tmp.getCause())) {
                break;
            }
            tmp = tmp.getCause();
            breakPoint++;
            if (breakPoint > 1000) {
                break;
            }
        }
        return tmp;
    }

}
