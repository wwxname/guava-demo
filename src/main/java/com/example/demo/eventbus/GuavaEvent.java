package com.example.demo.eventbus;

public abstract class GuavaEvent<INFO extends Object> {
    private INFO info;

    public GuavaEvent(INFO info) {
        this.info = info;
    }

    public INFO getInfo() {
        return this.info;
    }

}
