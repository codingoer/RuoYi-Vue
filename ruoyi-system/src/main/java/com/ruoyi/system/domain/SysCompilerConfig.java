package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 代码动态编译配置表 sys_compiler_config
 *
 * @author Lionel
 * @date 2022/9/22 23:40
 */
public class SysCompilerConfig extends BaseEntity {

    private static final long serialVersionUID = 8653789691641282342L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 类名称
     */
    private String className;

    /**
     * 类完全限定名
     */
    private String classFullName;

    /**
     * 源代码
     */
    private String sourceCode;

    /**
     * 0 正常 1 停用
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除)
     */
    private String delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
