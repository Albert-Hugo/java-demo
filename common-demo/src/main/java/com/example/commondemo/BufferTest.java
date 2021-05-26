package com.example.commondemo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ido
 * @date 2020/11/17 10:27
 */
public class BufferTest {


    public static class UsingTime {
        public long time;
        public String method;

    }

    public static void startContext(CallingContext callingContext) {
        CallingContext start = threadLocal.get();
        if (start.childContexts.isEmpty()) {
            callingContext.parent = start;
            start.childContexts.addLast(callingContext);
            return;
        }
        int siblingDepth = start.childContexts.getFirst().depth;

        if (siblingDepth == callingContext.depth) {
            callingContext.parent = start;
            start.childContexts.addLast(callingContext);
            return;
        }

        while (start.depth != callingContext.depth) {
            if (start.childContexts.isEmpty()) {
                callingContext.parent = start;
                start.childContexts.addLast(callingContext);
                return;
            }
            start = start.childContexts.getFirst();
        }
        callingContext.parent = start.parent;
        start.parent.childContexts.addLast(callingContext);
    }

    public static void endContext(CallingContext callingContext) {
        callingContext.end = System.currentTimeMillis();
        callingContext.consumeTime = callingContext.end - callingContext.start;
//        CallingContext start = threadLocal.get().get(1);
//        while (start.depth != callingContext.depth) {
//            start = start.childContexts.getFirst();
//        }
//        for (CallingContext c: start.siblingContexts){
//            if(c.uuid .equals( callingContext.uuid)){
//
//            }
//        }

    }


    public static void test2() {
        long start = System.currentTimeMillis();

        CallingContext callingContext = new CallingContext();
        startContext(callingContext);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        endContext(callingContext);

    }

    public static void test() {
        long start = System.currentTimeMillis();
        CallingContext callingContext = new CallingContext();
        startContext(callingContext);
        try {
            Thread.sleep(100);
            test3();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        endContext(callingContext);
    }

    public static void test3() {
        long start = System.currentTimeMillis();
        CallingContext callingContext = new CallingContext();
        startContext(callingContext);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        endContext(callingContext);
    }

    public static class CallingContext {
        @JsonIgnore
        public CallingContext parent;
        public LinkedList<CallingContext> childContexts = new LinkedList<>();
        public long start;
        public long end;
        public long consumeTime;
        public String method;
        public String callingMethod;
        public int depth;
        public String uuid = UUID.randomUUID().toString();

        public CallingContext() {
            this.start = System.currentTimeMillis();
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            this.depth = stackTraceElements.length;
            for (int i = 0; i < stackTraceElements.length; i++) {
                StackTraceElement s = stackTraceElements[i];

                if ("<init>".equals(s.getMethodName())) {
                    if (i + 2 == stackTraceElements.length) {
                        this.method = stackTraceElements[i + 1].getMethodName();
                        this.callingMethod = this.method;
                    } else {
                        this.method = stackTraceElements[i + 1].getMethodName();
                        this.callingMethod = stackTraceElements[i + 2].getMethodName();
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "CallingContext{" +
                    ", childContexts=" + childContexts +
                    ", consumeTime=" + consumeTime +
                    ", method='" + method + '\'' +
                    ", callingMethod='" + callingMethod + '\'' +
                    ", depth=" + depth +
                    ", uuid='" + uuid + '\'' +
                    '}';
        }
    }

    static ThreadLocal<CallingContext> threadLocal = new ThreadLocal<>();

    static {

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CallingContext start = new CallingContext();
        threadLocal.set(start);
        CallingContext callingContext = new CallingContext();
        startContext(callingContext);
        test();
        test2();
        endContext(callingContext);
        ObjectMapper o = new ObjectMapper();

        System.out.println(o.writeValueAsString(threadLocal.get()));


    }
}
