<template>
  <div>
    <div style="padding: 10px 0">
      <!-- 搜索栏 -->
      <el-input style="width: 200px" placeholder="Enter Patient Name" suffix-icon="el-icon-search" class="ml-5" v-model="name"></el-input>
      <el-input style="width: 200px" placeholder="Enter Patient Gender" suffix-icon="el-icon-notebook-2" class="ml-5" v-model="gender"></el-input>
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

    <!-- 表格显示病人数据 -->
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="name" label="Patient Name" width="150"></el-table-column>
      <el-table-column prop="gender" label="Gender"></el-table-column>
      <el-table-column prop="birthday" label="Birthday"></el-table-column>
      <el-table-column prop="hospitalizationDate" label="Hospitalization Date" width="200"></el-table-column>
      <el-table-column prop="dischargeDate" label="Discharge Date" width="150"></el-table-column>
      <el-table-column label="Operation" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)" v-if="canEditOrDelete">Edit<i class="el-icon-edit"></i></el-button>
          <!-- 单个删除病人数据 -->
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Sure'
              cancel-button-text='Think again'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure to delete these data?"
              @confirm="del(scope.row.patientId)"
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
    <el-dialog title="Patient Info" :visible.sync="dialogFormVisible">
      <el-form label-width="100px" size="small">
        <el-form-item label="Patient Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Patient Gender">
          <el-input v-model="form.gender" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Birthday">
          <el-date-picker v-model="form.birthday" type="date" placeholder="Select Date" format="yyyy-MM-dd"></el-date-picker>
        </el-form-item>
        <el-form-item label="Hospitalization Date">
          <el-date-picker v-model="form.hospitalizationDate" type="date" placeholder="Select Date" format="yyyy-MM-dd"></el-date-picker>
        </el-form-item>
        <el-form-item label="Discharge Date">
          <el-date-picker v-model="form.dischargeDate" type="date" placeholder="Select Date" format="yyyy-MM-dd"></el-date-picker>
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
  name: "Patient",
  data() {
    return {
      // 初始化数据
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      gender: "",
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
    methods: {
      // 新增：格式化日期方法
      formatDate(timestamp) {
        if (!timestamp) return '';
        const date = new Date(timestamp);
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
      },

      // 加载病人数据
      load() {
        this.request.get("/patients/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name: this.name,
            gender: this.gender,
          }
        }).then(res => {
          const formattedData = res.data.records.map(item => ({
            ...item,
            birthday: this.formatDate(item.birthday),
            hospitalizationDate: this.formatDate(item.hospitalizationDate),
            dischargeDate: this.formatDate(item.dischargeDate)
          }));
          this.tableData = formattedData;
          this.total = res.data.total;
        })
      },

      // 保存病人信息
      save() {
        this.request.post("/patients", this.form).then(res => {
          if (res.code === '200') {
            this.$message.success("Save Success")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error("Save Failed")
          }
        })
      },

      // 单个删除病人信息
      del(patientId) {
        this.request.delete("/patients/" + patientId).then(res => {
          if (res.code === '200') {
            this.$message.success("Delete Success")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error("Delete Failed")
          }
        })
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },

      // 批量删除病人信息
      delBatch() {
        let patientIds = this.multipleSelection.map(v => v.patientId)  // 从选中的病人记录中获取ID
        this.request.post("/patients/del/batch", patientIds).then(res => {
          if (res.code === '200') {
            this.$message.success("Batch Delete Success")
            this.load()
          } else {
            this.$message.error("Batch Delete Failed")
          }
        })
      },

      // 处理添加操作
      handleAdd() {
        this.dialogFormVisible = true
        this.form = {}
      },
      // 处理编辑操作
      handleEdit(row) {
        this.form = Object.assign({}, row); // 克隆对象以避免直接修改引用
        this.dialogFormVisible = true
      },
      // 处理分页大小变化
      handleSizeChange(pageSize) {
        this.pageSize = pageSize
        this.load()
      },
      // 处理当前页码变化
      handleCurrentChange(pageNum) {
        this.pageNum = pageNum
        this.load()
      },
      // 重置搜索栏
      reset() {
        this.name = ""
        this.gender = ""
        this.load()
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
