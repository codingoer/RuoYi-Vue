package com.ruoyi.framework.compiler.javax;

import com.ruoyi.framework.compiler.utils.ClassNameUtils;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

/**
 *
 *
 * @author Lionel
 * @date 2022/9/22 15:47
 */
public class JavaCodeInputObject extends SimpleJavaFileObject {

    private String source;

    public JavaCodeInputObject(String source) {
        this(ClassNameUtils.findClassName(source), source);
    }

    public JavaCodeInputObject(String fullClassName, String source) {
        super(URI.create("code:///" + ClassNameUtils.getShortClassName(fullClassName) + Kind.SOURCE.extension), Kind.SOURCE);
        this.source = source;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return source;
    }
}
