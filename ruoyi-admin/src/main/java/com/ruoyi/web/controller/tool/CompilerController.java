package com.ruoyi.web.controller.tool;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.framework.compiler.CompilerTool;
import com.ruoyi.system.domain.SysCompilerConfig;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysCompilerConfigService;
import com.ruoyi.system.service.impl.SysNoticeServiceImpl;
import org.springframework.objenesis.instantiator.util.ClassUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 动态编译接口
 *
 * @author Lionel
 */
@Controller
@ResponseBody
@RequestMapping("/tool/compiler")
public class CompilerController extends BaseController {

    @Resource
    private ISysCompilerConfigService sysCompilerConfigService;

    @PreAuthorize("@ss.hasPermi('tool:compiler:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCompilerConfig config) {
        startPage();
        List<SysCompilerConfig> list = sysCompilerConfigService.selectCompilerConfigList(config);
        return getDataTable(list);
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody SysCompilerConfig config) {
        config.setCreateBy(getUsername());
        sysCompilerConfigService.insertCompilerConfig(config);
        return AjaxResult.success();
    }

    @GetMapping(value = "/get/{id}")
    public AjaxResult getById(@PathVariable Long id) {
        return AjaxResult.success(sysCompilerConfigService.selectById(id));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody SysCompilerConfig config) {
        config.setUpdateBy(getUsername());
        sysCompilerConfigService.updateCompilerConfig(config);
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('tool:compiler:dynamic')")
    @PostMapping("/dynamic")
    public AjaxResult register(MultipartFile file) {
        String name = file.getOriginalFilename();
        return AjaxResult.success(name);
    }

    @GetMapping(value = "/dynamic/{id}")
    public AjaxResult compilerByConfig(@PathVariable Long id) {
        SysCompilerConfig sysCompilerConfig = sysCompilerConfigService.selectById(id);
        Class<SysNoticeServiceImpl> clazz = CompilerTool.compiler(sysCompilerConfig.getSourceCode());
        SysNoticeServiceImpl sysNoticeService = ClassUtils.newInstance(clazz);
        List<SysNotice> sysNotices = sysNoticeService.selectNoticeList(null);
        return AjaxResult.success();
    }
}
