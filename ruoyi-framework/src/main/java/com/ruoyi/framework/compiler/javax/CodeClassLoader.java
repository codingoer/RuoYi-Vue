package com.ruoyi.framework.compiler.javax;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *
 * @author Lionel
 * @date 2022/9/22 15:55
 */
public class CodeClassLoader extends ClassLoader {

    private static final ClassLoader PARENT = CodeClassLoader.class.getClassLoader();

    private static final AtomicInteger VERSION_COUNT = new AtomicInteger();

    private final int version;
    private final String toStringText;

    private Map<String, JavaCodeOutputObject> outputCodeObjectMap = new WeakHashMap<>();

    public CodeClassLoader() {
        super(PARENT);
        this.version = VERSION_COUNT.incrementAndGet();
        this.toStringText = getClass().getSimpleName() + "-" + version;
    }

    public void outputCodeObject(JavaCodeOutputObject codeObject) {
        outputCodeObjectMap.put(codeObject.getClassName(), codeObject);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        boolean contains = outputCodeObjectMap.containsKey(name);
        if(contains) {
            Class<?> c = findLoadedClass(name);
            if(c == null || c.getClassLoader() != this) {
                synchronized (getClassLoadingLock(name)) {
                    return findClass(name);
                }
            }
        }
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        JavaCodeOutputObject codeObject = outputCodeObjectMap.get(name);
        if(codeObject == null) {
            return super.findClass(name);
        }

        byte[] classBytes = codeObject.getClassBytes();
        return defineClass(name, classBytes, 0, classBytes.length);
    }

    @Override
    public String toString() {
        return toStringText;
    }
}
