package com.example.demo.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

public interface GuavaListener<E extends GuavaEvent> {
    @AllowConcurrentEvents
    @Subscribe
    public void lister(E event);
}
