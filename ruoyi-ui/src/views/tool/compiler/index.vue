<template>
    <div class="app-container">
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
        <el-form-item label="上传" prop="field102" required>
          <el-upload ref="field102" :file-list="field102fileList" :action="field102Action"
            :before-upload="field102BeforeUpload">
            <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item size="large">
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
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
      submitForm() {
        this.$refs['elForm'].validate(valid => {
          if (!valid) return
          // TODO 提交表单
        })
      },
      resetForm() {
        this.$refs['elForm'].resetFields()
      },
      field102BeforeUpload(file) {
        let isRightSize = file.size / 1024 / 1024 < 4
        if (!isRightSize) {
          this.$message.error('文件大小超过 4MB')
        }
        return isRightSize
      },

      /** 导入按钮操作 */
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
  