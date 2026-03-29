package com.dd25.dietiestates25.exception;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;
import java.util.logging.Logger;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer
{
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler()
    {
        return new AsyncExceptionHandler();
    }

    private static class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler
    {
        private Logger logger = Logger.getLogger(getClass().getName());
        
        @Override
        public void handleUncaughtException(@NonNull Throwable throwable, @NonNull Method method, @NonNull Object... obj) {
            logger.info("--------------------------------------------------");
            logger.info("ASYNC EXCEPTION");
            logger.info("In method: " + method.getName());
            logger.info("Error message: " + throwable.getMessage());
            
            for (Object param : obj)
            {
                logger.info("param: " + param);
            }
            
            logger.info("Stacktrace:");
            throwable.printStackTrace();
            logger.info("--------------------------------------------------");
        }
    }
}