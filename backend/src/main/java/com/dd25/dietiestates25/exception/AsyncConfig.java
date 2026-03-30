package com.dd25.dietiestates25.exception;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;

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
        public void handleUncaughtException(@NonNull Throwable throwable, @NonNull Method method, @NonNull Object... obj) 
        {
            logger.info("--------------------------------------------------");
            logger.info("ASYNC EXCEPTION");
            
            logger.log(Level.INFO, "In method: {0}", method.getName());
            logger.log(Level.INFO, "Error message: {0}", throwable.getMessage());
            
            for (Object param : obj)
            {
                logger.log(Level.INFO, "param: {0}", param);
            }
            
            logger.info("Stacktrace:");
            logger.log(Level.SEVERE, "Exception details:", throwable);
            logger.info("--------------------------------------------------");
        }
    }
}