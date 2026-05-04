package com.di.architecture;

import com.di.annotations.Service;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventBus {
    private final List<EventListenerInvoker> invokers = new ArrayList<>();

    public void register(Object bean, Method method) {
        invokers.add(new EventListenerInvoker(bean, method));
    }

    public void publish(Object event) {
        invokers.forEach(it -> it.invoke(event));
    }

    private record EventListenerInvoker(Object bean, Method method) {
        void invoke(Object event) {
            // Check if the event type matches the listener's first parameter type
            if (method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                try {
                    method.invoke(bean, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
