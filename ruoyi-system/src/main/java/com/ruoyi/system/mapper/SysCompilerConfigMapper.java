package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysCompilerConfig;

import java.util.List;

/**
 * Description：动态编译代码配置mapper
 *
 * @author Lionel
 * @date 2022/9/22 23:58
 */
public interface SysCompilerConfigMapper {

    /**
     * 根据条件分页查询配置
     *
     * @param config 配置信息
     * @return 配置集合信息
     */
    List<SysCompilerConfig> selectCompilerConfigList(SysCompilerConfig config);

    /**
     * 根据ID查询配置
     *
     * @param id 主键
     * @return 配置信息
     */
    SysCompilerConfig selectById(Long id);

    /**
     * 新增配置信息
     *
     * @param config 配置信息
     * @return 结果
     */
    int updateCompilerConfig(SysCompilerConfig config);

    /**
     * 修改配置信息
     *
     * @param config 配置信息
     * @return 结果
     */
    int insertCompilerConfig(SysCompilerConfig config);

    /**
     * 删除配置信息
     *
     * @param configId 配置ID
     * @return 结果
     */
    int deleteCompilerConfigById(Long configId);
}
