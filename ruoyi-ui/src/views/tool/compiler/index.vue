<template>
    <div class="app-container">

      <!-- 搜索条件 -->
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
        <el-form-item label="类名称" prop="className">
          <el-input
            v-model="queryParams.roleName"
            placeholder="请输入类名称"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="类完全限定名" prop="classFullName">
          <el-input
            v-model="queryParams.roleKey"
            placeholder="请输入类完全限定名"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="状态"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="dateRange"
            style="width: 240px"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 列表顶部按钮-->
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
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['system:role:edit']"
          >修改</el-button>
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
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <!-- 数据列表 -->
      <el-table v-loading="loading" :data="compilerList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="配置ID" prop="id" width="70" />
        <el-table-column label="类名称" prop="className" :show-overflow-tooltip="true" width="170" />
        <el-table-column label="完全限定名" prop="classFullName" :show-overflow-tooltip="true" width="200" />
        <el-table-column label="备注" prop="remark" width="100" />
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
      
        <!-- 添加或修改配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
          <el-form ref="form" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="类名称" prop="className">
              <el-input v-model="form.className" placeholder="类名称" />
            </el-form-item>
            <el-form-item label="完全限定名" prop="classFullName">
              <el-input v-model="form.classFullName" placeholder="完全限定名" />
            </el-form-item>
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="备注" />
            </el-form-item>
            <el-form-item label="源代码">
              <el-input v-model="form.sourceCode" type="textarea" placeholder="sourceCode"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>

   </div>
  </template>
  <script>
  import { listCompiler,addCompiler  } from "@/api/tool/compiler";
  import { getToken } from "@/utils/auth";
  export default {
    name: "Compiler",
    dicts: ['sys_normal_disable', 'sys_user_sex'],
    components: {},
    props: [],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 表格数据
        compilerList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          roleName: undefined,
          roleKey: undefined,
          status: undefined
        },
        // 日期范围
        dateRange: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {},

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
    mounted() {},
    created() {
       this.getList();
    },
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
      },

      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.roleId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 查询列表 */
      getList() {
        this.loading = true;
        listCompiler(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.compilerList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      /** 新增按钮操作 */
      handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加代码配置";
        },
      /** 修改按钮操作 */
      handleUpdate(row) {
          this.reset();
        },
      /** 提交按钮 */
      submitForm: function() {
            this.$refs["form"].validate(valid => {
              if (valid) {
                if (this.form.id != undefined) {


                } else {
                  addCompiler(this.form).then(response => {
                    this.$modal.msgSuccess("新增成功");
                    this.open = false;
                    this.getList();
                  });
                }
              }
            });
          },
      // 取消按钮
      cancel() {
          this.open = false;
          this.reset();
      },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        className: undefined,
        classFullName: undefined,
        status: "0",
        sourceCode: undefined
      };
      this.resetForm("form");
    },
  },
}  
</script>

<style>
  .el-upload__tip {
    line-height: 1.2;
  }  
</style>
  