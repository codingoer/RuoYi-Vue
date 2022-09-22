<template>
    <div class="app-container">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['system:role:add']"
            >新增</el-button>
         </el-col>
        <el-col :span="1.5">
            <el-button
              type="info"
              plain
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
            >上传Java文件</el-button>
        </el-col>
      </el-row>


      <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="角色编号" prop="roleId" width="120" />
        <el-table-column label="角色名称" prop="roleName" :show-overflow-tooltip="true" width="150" />
        <el-table-column label="权限字符" prop="roleKey" :show-overflow-tooltip="true" width="150" />
        <el-table-column label="显示顺序" prop="roleSort" width="100" />
        <el-table-column label="状态" align="center" width="100">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope" v-if="scope.row.roleId !== 1">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:role:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:role:remove']"
            >删除</el-button>
            <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['system:role:edit']">
              <span class="el-dropdown-link">
                <i class="el-icon-d-arrow-right el-icon--right"></i>更多
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="handleDataScope" icon="el-icon-circle-check"
                  v-hasPermi="['system:role:edit']">数据权限</el-dropdown-item>
                <el-dropdown-item command="handleAuthUser" icon="el-icon-user"
                  v-hasPermi="['system:role:edit']">分配用户</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".java"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 测试一下下
          </div>
          <span>仅允许导入Java文件。</span>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
    </div>
    

  </template>
  <script>
  import { getToken } from "@/utils/auth";
  export default {
    name: "Compiler",
    dicts: ['sys_normal_disable', 'sys_user_sex'],
    components: {},
    props: [],
    data() {
      return {
        formData: {
          field102: null,
        },
        rules: {},
        field102Action: 'https://jsonplaceholder.typicode.com/posts/',
        field102fileList: [],

        upload: {
            open: false,
            title: "",
            // 是否禁用上传
            isUploading: false,
            // 是否更新已经存在的用户数据
            updateSupport: 0,
            // 设置上传的请求头部
            headers: { Authorization: "Bearer " + getToken() },
            // 上传的地址
            url: process.env.VUE_APP_BASE_API + "/tool/compiler/dynamic"
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      /** 上传按钮操作 */
      handleImport() {
        this.upload.title = "Java文件上传";
        this.upload.open = true;
      },
      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.upload.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
        },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      }
  }
}  
</script>

<style>
  .el-upload__tip {
    line-height: 1.2;
  }  
</style>
  