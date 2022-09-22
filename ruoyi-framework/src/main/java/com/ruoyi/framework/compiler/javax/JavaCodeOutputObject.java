package com.ruoyi.framework.compiler.javax;

import com.ruoyi.framework.compiler.utils.ClassNameUtils;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 *
 *
 * @author Lionel
 * @date 2022/9/22 15:45
 */
public class JavaCodeOutputObject extends SimpleJavaFileObject {

    private String className;

    private ByteArrayOutputStream outputStream;

    public JavaCodeOutputObject(String className, Kind kind) {
        super(URI.create("code:///" + ClassNameUtils.getShortClassName(className) + kind.extension), kind);
        this.className = className;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return this.outputStream = new ByteArrayOutputStream();
    }

    public byte[] getClassBytes() {
        return outputStream.toByteArray();
    }

    public String getClassName() {
        return className;
    }
}
