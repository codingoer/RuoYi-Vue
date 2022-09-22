package com.ruoyi.framework.compiler.javax;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

/**
 *
 *
 * @author Lionel
 * @date 2022/9/22 15:51
 */
public class CompilerJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private CodeClassLoader classLoader;

    public CompilerJavaFileManager(JavaFileManager fileManager, CodeClassLoader classLoader) {
        super(fileManager);
        this.classLoader = classLoader;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        JavaCodeOutputObject javaFileObject = new JavaCodeOutputObject(className, kind);
        classLoader.outputCodeObject(javaFileObject);
        return javaFileObject;
    }
}
