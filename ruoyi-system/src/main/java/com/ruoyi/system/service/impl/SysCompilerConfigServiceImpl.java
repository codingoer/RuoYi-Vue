package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysCompilerConfig;
import com.ruoyi.system.mapper.SysCompilerConfigMapper;
import com.ruoyi.system.service.ISysCompilerConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 代码动态编译配置Service
 *
 * @author Lionel
 * @date 2022/9/22 23:56
 */
@Service
public class SysCompilerConfigServiceImpl implements ISysCompilerConfigService {

    @Resource
    private SysCompilerConfigMapper sysCompilerConfigMapper;

    @Override
    public List<SysCompilerConfig> selectCompilerConfigList(SysCompilerConfig config) {
        return sysCompilerConfigMapper.selectCompilerConfigList(config);
    }

    @Override
    public SysCompilerConfig selectById(Long id) {
        return sysCompilerConfigMapper.selectById(id);
    }

    @Override
    public int deleteCompilerConfigById(Long configId) {
        return sysCompilerConfigMapper.deleteCompilerConfigById(configId);
    }

    @Override
    public int insertCompilerConfig(SysCompilerConfig config) {
        return sysCompilerConfigMapper.insertCompilerConfig(config);
    }

    @Override
    public int updateCompilerConfig(SysCompilerConfig config) {
        return sysCompilerConfigMapper.updateCompilerConfig(config);
    }


}
