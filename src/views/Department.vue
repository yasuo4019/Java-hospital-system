<template>
  <div>
    <div style="padding: 10px 0">
      <!-- 搜索栏 -->
      <el-input style="width: 200px" placeholder="Please Enter Name" suffix-icon="el-icon-search" class="ml-5" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button type="warning" @click="reset">Reset</el-button>
    </div>

    <div style="margin: 10px 0">
      <!-- 操作按钮 -->
      <el-button type="primary" @click="handleAdd" v-if="canEditOrDelete">Add<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='Sure'
          cancel-button-text='Think again'
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure to delete these data?"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" v-if="canEditOrDelete">Batch Delete<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <!-- 表格显示部门数据 -->
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="name" label="Department Name" width="200"></el-table-column>
      <el-table-column prop="description" label="Department Description"></el-table-column>
      <el-table-column label="Operation" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)" v-if="canEditOrDelete">Edit<i class="el-icon-edit"></i></el-button>
          <!-- 单个删除部门数据 -->
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Sure'
              cancel-button-text='Think again'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure to delete these data?"
              @confirm="del(scope.row.departmentId)"
          >
            <el-button type="danger" slot="reference" v-if="canEditOrDelete">Delete<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页控件 -->
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <!-- 编辑对话框 -->
    <el-dialog title="Department Info" :visible.sync="dialogFormVisible">
      <el-form label-width="100px" size="small">
        <el-form-item label="Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input type="textarea" v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Sure</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Department",
  data() {
    return {
      // 初始化数据
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      userRole: null, // 初始化为 null
    }
  },
  created() {
    this.load()
    this.initializeUserRole();
  },
  computed: {
    // 根据角色判断是否显示编辑和删除按钮
    canEditOrDelete() {
      return this.userRole === 'ROLE_ADMIN' || this.userRole === 'ROLE_DOCTOR';
    }
  },
  methods : {
    // 加载部门数据
    load() {
      this.request.get("/departments/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        this.tableData = res.data.records;
        this.total = res.data.total;
      })
    },

    // 保存部门信息
    save() {
      this.request.post("/departments", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Save Success");
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("Save Failed");
        }
      })
    },

    // 单个删除部门信息
    del(departmentId) {
      this.request.delete("/departments/" + departmentId).then(res => {
        if (res.code === '200') {
          this.$message.success("Delete Success");
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("Delete Failed");
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    // 批量删除部门信息
    delBatch() {
      let departmentIds = this.multipleSelection.map(v => v.departmentId);  // 从选中的部门记录中获取ID
      this.request.post("/departments/del/batch", departmentIds).then(res => {
        if (res.code === '200') {
          this.$message.success("Batch Delete Success");
          this.load();
        } else {
          this.$message.error("Batch Delete Failed");
        }
      })
    },
    // 处理添加操作
    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {};
    },
    // 处理编辑操作
    handleEdit(row) {
      this.form = Object.assign({}, row);
      this.dialogFormVisible = true;
    },
    // 处理分页大小变化
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    // 处理当前页码变化
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    // 重置搜索栏
    reset() {
      this.name = "";
      this.load();
    },
    initializeUserRole() {
      const user = JSON.parse(localStorage.getItem('user'));
      this.userRole = user ? user.role : null;
    },
  }
}
</script>

<style>
.headerBg {
  background: #cccccc!important;
}
</style>
