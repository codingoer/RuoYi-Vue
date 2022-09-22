package com.ruoyi.framework.compiler.impl;

public class CompilerWork implements Runnable {

    @Override
    public void run() {
        System.out.printf("Compiled %s by %s and run!\n", this.getClass(), this.getClass().getClassLoader());
    }
}
