package com.ruoyi.framework.compiler;

import com.ruoyi.common.utils.ExceptionUtil;
import com.ruoyi.framework.compiler.javax.CodeClassLoader;
import com.ruoyi.framework.compiler.javax.CompilerJavaFileManager;
import com.ruoyi.framework.compiler.javax.JavaCodeInputObject;
import com.ruoyi.framework.compiler.utils.ClassNameUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 动态编译工具类
 *
 * @author Lionel
 * @date 2022/9/22 15:31
 */
public class CompilerTool {

    private static final Logger log = getLogger(CompilerTool.class);

    public static final List<String> SYS_OPTIONS;
    static {
        SYS_OPTIONS = options();
    }

    private static String getClassPath() {
        String catalinaHome = System.getProperty("catalina.home");
        if(StringUtils.isBlank(catalinaHome)) {
            return null;
        }

        StringBuilder classPath = new StringBuilder();

        String selfLibHome = System.getProperty("self.lib.home");
        if(StringUtils.isNotBlank(selfLibHome)) {
            File[] files = new File(selfLibHome).listFiles();
            if (ArrayUtils.isNotEmpty(files)) {
                for (File file : files) {
                    classPath.append(':').append(file.getAbsolutePath());
                }
            }
        }

        File[] files = new File(catalinaHome + "/webapps/ROOT/WEB-INF/lib").listFiles();
        if(ArrayUtils.isNotEmpty(files)) {
            for (File file : files) {
                classPath.append(':').append(file.getAbsolutePath());
            }
        }

        files = new File(catalinaHome + "/lib").listFiles();
        if(ArrayUtils.isNotEmpty(files)) {
            for (File file : files) {
                classPath.append(':').append(file.getAbsolutePath());
            }
        }
        classPath.append(':').append(catalinaHome + "/webapps/ROOT/WEB-INF/classes");
        String selfClassesHome = System.getProperty("self.classes.home");
        if(StringUtils.isNotBlank(selfClassesHome)) {
            classPath.append(':').append(selfClassesHome);
        }
        return classPath.toString();
    }

    private static String getClassPathNew() {
        return ManagementFactory.getRuntimeMXBean().getClassPath();
    }

    private static final List<String> options() {
        String classPath = getClassPathNew();
        log.info("Compiler option is {}", classPath);
        if(StringUtils.isBlank(classPath)) {
            return null;
        }
        return Arrays.asList("-cp", classPath);
    }

    public static <T> Class<T> compilerWithOptions(String source, List<String> options) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        JavaFileManager manager = compiler.getStandardFileManager(collector, null, null);

        CodeClassLoader classLoader = new CodeClassLoader();
        manager = new CompilerJavaFileManager(manager, classLoader);

        String fullClassName = ClassNameUtils.findClassName(source);
        JavaCodeInputObject codeObject = new JavaCodeInputObject(fullClassName, source);

        List<JavaFileObject> fileList = Arrays.asList(codeObject);

        //        List<String> options = Arrays.asList("-target", "1.8");

        Callable<Boolean> callable = compiler.getTask(null, manager, collector, options, null, fileList);
        try {
            Boolean ok = callable.call();
            if(!Boolean.TRUE.equals(ok)) {
                throw new RuntimeException("Compiler " + ok + " --> by " + collector.getDiagnostics());
            }
        } catch (Exception e) {
            throw ExceptionUtil.uncheck(e);
        }

        classLoader.clearAssertionStatus();
        try {
            return (Class<T>) classLoader.loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            throw ExceptionUtil.uncheck(e);
        }
    }

    public static <T> Class<T> compiler(String source) {
        //return compilerWithOptions(source, SYS_OPTIONS);
        return compilerWithOptions(source, SYS_OPTIONS);
    }

    private static void doCompiler(JavaCompiler compiler, JavaFileManager manager, DiagnosticCollector<JavaFileObject> collector, List<String> options, List<String> classeNames, List<String> sourceList, List<Class<?>> classList) {
        CodeClassLoader classLoader = new CodeClassLoader();
        CompilerJavaFileManager compilerManager = new CompilerJavaFileManager(manager, classLoader);

        List<JavaFileObject> fileList = new ArrayList<>(sourceList.size());
        int len = classeNames.size();
        for (int i = 0; i < len; ++i) {
            JavaCodeInputObject codeObject = new JavaCodeInputObject(classeNames.get(i), sourceList.get(i));
            fileList.add(codeObject);
        }

        Callable<Boolean> callable = compiler.getTask(null, compilerManager, collector, options, null, fileList);
        try {
            Boolean ok = callable.call();
            if (!Boolean.TRUE.equals(ok)) {
                log.error("Compiler {} {} --> by {}", classeNames, ok, collector.getDiagnostics());
                return;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            while (--len > -1) {
                classList.add(null);
            }
            return;
        }

        for (String className : classeNames) {
            try {
                classList.add(classLoader.loadClass(className));
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
                classList.add(null);
            }
        }
    }

    private static List<Class<?>> compilerWithOptions(List<String> sources, List<String> options) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        JavaFileManager manager = compiler.getStandardFileManager(collector, null, null);

        List<Class<?>> classList = new ArrayList<>(sources.size());

        List<String> classNames = new ArrayList<>();
        List<String> sourceList = new ArrayList<>();

        for(String source : sources) {
            String fullClassName = ClassNameUtils.findClassName(source);
            if(!classNames.contains(fullClassName)) {
                classNames.add(fullClassName);
                sourceList.add(source);
                continue;
            }

            try {
                doCompiler(compiler, manager, collector, options, classNames, sourceList, classList);
            } finally {
                classNames.clear();
                sourceList.clear();
                classNames.add(fullClassName);
                sourceList.add(source);
            }
        }

        if(CollectionUtils.isNotEmpty(classNames)) {
            doCompiler(compiler, manager, collector, options, classNames, sourceList, classList);
        }
        return classList;
    }

    public static List<Class<?>> compiler(List<String> sources) {
//        return compilerWithOptions(sources, null);
        return compilerWithOptions(sources, SYS_OPTIONS);
    }
}
