package com.example.dp.filter;

import org.springframework.stereotype.Component;

@Component
public class RequestContext {

    private final ThreadLocal<String> threadLocalCorrelationId = ThreadLocal.withInitial(() ->"");

    public String getCorrelationId(){return threadLocalCorrelationId.get();}

    public void setCorrelationId(String correlationId){
        threadLocalCorrelationId.set(correlationId);
    }

    public void unloadCorrelationId(){ threadLocalCorrelationId.remove();}
}
