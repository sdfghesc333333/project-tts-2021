/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.tts.server;

import com.app.tts.server.vertical.TTSVertical;
import com.app.tts.util.LoggerInterface;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 *
 * @author hungdt
 */
public class TTSServer implements Runnable, LoggerInterface {

    private int workerPoolSize;
    private long workerMaxExecuteTime;
    private int eventLoopPoolSize;
    private long eventLoopMaxExecuteTime;
    private long threadCheckInterval;

    private TTSVertical vertxVertical;

    public void setWorkerPoolSize(int workerPoolSize) {
        this.workerPoolSize = workerPoolSize;
    }

    public void setWorkerMaxExecuteTime(long workerMaxExecuteTime) {
        this.workerMaxExecuteTime = workerMaxExecuteTime;
    }

    public void setEventLoopPoolSize(int eventLoopPoolSize) {
        this.eventLoopPoolSize = eventLoopPoolSize;
    }

    public void setEventLoopMaxExecuteTime(long eventLoopMaxExecuteTime) {
        this.eventLoopMaxExecuteTime = eventLoopMaxExecuteTime;
    }

    public void setThreadCheckInterval(long threadCheckInterval) {
        this.threadCheckInterval = threadCheckInterval;
    }

    public void setVertxVertical(TTSVertical vertxVertical) {
        this.vertxVertical = vertxVertical;
    }

    public void init() throws InterruptedException {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {

            VertxOptions vertxOptions = new VertxOptions();

            vertxOptions.setWorkerPoolSize(workerPoolSize);            
            vertxOptions.setMaxWorkerExecuteTime(workerMaxExecuteTime);
            vertxOptions.setEventLoopPoolSize(eventLoopPoolSize);
            vertxOptions.setMaxEventLoopExecuteTime(eventLoopMaxExecuteTime);
            vertxOptions.setBlockedThreadCheckInterval(threadCheckInterval);            
            
            Vertx.vertx(vertxOptions).deployVerticle(vertxVertical);

        } catch (Exception e) {
            logger.error("", e);
        }
    }

}
