package com.ruoyi.web.controller.tool;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 动态编译接口
 *
 * @author Lionel
 */
@Controller
@ResponseBody
@RequestMapping("/tool/compiler")
public class CompilerController extends BaseController {

    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/dynamic")
    public AjaxResult register(MultipartFile file) {
        String name = file.getOriginalFilename();
        return AjaxResult.success(name);
    }
}
