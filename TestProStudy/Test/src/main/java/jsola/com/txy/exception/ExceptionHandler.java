package jsola.com.txy.exception;

import jsola.com.txy.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ExceptionHandler  implements ThrowsAdvice {

    @org.springframework.web.bind.annotation.ExceptionHandler
    public Result exceptionHandler(Exception ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
}
