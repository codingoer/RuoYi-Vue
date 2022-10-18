package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysCompilerConfig;
import java.util.List;

/**
 * 代码动态编译配置Service
 *
 * @author Lionel
 * @date 2022/9/22 23:54
 */
public interface ISysCompilerConfigService {

    /**
     * 根据条件分页查询配置数据
     *
     * @param config 配置信息
     * @return 动态编译代码配置
     */
    List<SysCompilerConfig> selectCompilerConfigList(SysCompilerConfig config);

    /**
     * 根据ID查询配置数据
     *
     * @param id 配置ID
     * @return 配置信息
     */
    SysCompilerConfig selectById(Long id);

    /**
     * 新增配置信息
     *
     * @param config 配置信息
     * @return 结果
     */
    int insertCompilerConfig(SysCompilerConfig config);

    /**
     * 修改配置信息
     *
     * @param config 配置信息
     * @return 结果
     */
    int updateCompilerConfig(SysCompilerConfig config);

    /**
     * 通过配置ID删除配置
     *
     * @param configId 配置ID
     * @return 结果
     */
    int deleteCompilerConfigById(Long configId);
}
