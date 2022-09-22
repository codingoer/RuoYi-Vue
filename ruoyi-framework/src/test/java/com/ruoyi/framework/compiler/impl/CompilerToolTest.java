package com.ruoyi.framework.compiler.impl;

import com.ruoyi.framework.compiler.CompilerTool;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 动态编译单元测试
 *
 * @author Lionel
 * @date 2022/9/22 18:48
 */
public class CompilerToolTest {

    private String code = "package com.ruoyi.framework.compiler.impl;\n" +
            "\n" +
            "public class CompilerWork implements Runnable {\n" +
            "\n" +
            "    @Override\n" +
            "    public void run() {\n" +
            "        System.out.printf(\"Compiled %s by %s and run!\\n\", this.getClass(), this.getClass().getClassLoader());\n" +
            "    }\n" +
            "}";

    @Test
    public void compilerSource() throws Exception {
        AtomicInteger count = new AtomicInteger();

        List<Runnable> runList = new ArrayList<>();
        long cur = System.currentTimeMillis();
        for(int i = 0; i < 100; ++i) {
            Class<Runnable> clazz = CompilerTool.compiler(code);
            Runnable runnable = clazz.newInstance();
            runnable.run();
            runList.add(runnable);
            count.incrementAndGet();
        }
        System.out.printf("Compiler and run spent %sms\n", System.currentTimeMillis() - cur);

//        cur = System.currentTimeMillis();
//        for(Runnable run : runList) {
//            run.run();
//            System.out.println(run.getClass().getClassLoader());
//        }
//        System.out.printf("Compiler and run spent %sms\n", System.currentTimeMillis() - cur);
    }
}
