CREATE TABLE `sys_compiler_config` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `class_name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '类名称',
                            `class_full_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '类全限定名',
                            `source_code` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '源代码',
                            `del_flag` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
                            `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码动态编译配置表';