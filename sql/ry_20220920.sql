CREATE TABLE `sys_compiler_config` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                       `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类名称',
                                       `class_full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类全限定名',
                                       `source_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '源代码',
                                       `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
                                       `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
                                       `status` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '状态 0 正常 1 失效',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码动态编译配置表'

INSERT INTO `sys_compiler_config` (`id`, `class_name`, `class_full_name`, `source_code`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `status`) VALUES
(1, 'SysConfigServiceImpl', 'com.ruoyi.system.service.impl.SysConfigServiceImpl', 'aaaaa', '0', 'admin', '2022-09-23 01:02:07', 'admin', '2022-09-23 01:02:07', '121212', '0');
