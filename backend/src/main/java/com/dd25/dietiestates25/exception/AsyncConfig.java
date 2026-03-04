package com.dd25.dietiestates25.exception;

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
        @Override
        public void handleUncaughtException(@NonNull Throwable throwable, @NonNull Method method, @NonNull Object... obj) {
            System.err.println("--------------------------------------------------");
            System.err.println("ASYNC EXCEPTION");
            System.err.println("In method: " + method.getName());
            System.err.println("Error message: " + throwable.getMessage());
            
            for (Object param : obj)
            {
                System.err.println("param: " + param);
            }
            
            System.err.println("Stacktrace:");
            throwable.printStackTrace();
            System.err.println("--------------------------------------------------");
        }
    }
}