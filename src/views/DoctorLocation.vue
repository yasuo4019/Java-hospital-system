<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 400px" placeholder="Enter Department Or Doctor Name" suffix-icon="el-icon-search" class="ml-5" v-model="searchQuery"></el-input>
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button type="warning" @click="reset">Reset</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd" v-if="canEditOrDelete">Add<i class="el-icon-circle-plus-outline"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="doctorName" label="Doctor Name" width="150"></el-table-column>
      <el-table-column prop="departmentName" label="Department Name" width="150"></el-table-column>
      <el-table-column prop="room" label="Room"></el-table-column>
      <el-table-column prop="timeTable" label="Time Table"></el-table-column>
      <el-table-column prop="doctorLevel" label="Doctor Level"></el-table-column>
      <el-table-column label="Operation" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)" v-if="canEditOrDelete">Edit<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Sure'
              cancel-button-text='Think again'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure to delete these data?"
              @confirm="del(scope.row)"
          >
            <el-button type="danger" slot="reference" v-if="canEditOrDelete">Delete<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

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


    <el-dialog title="DoctorLocation Info" :visible.sync="dialogFormVisible">
      <el-form label-width="100px" size="small">
        <el-form-item label="Doctor Name">
          <el-input v-model="form.doctorName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Department Name">
          <el-input v-model="form.departmentName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Room">
          <el-input v-model="form.room" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Time Table">
          <el-input type="textarea" v-model="form.timeTable" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Doctor Level">
          <el-input v-model="form.doctorLevel" autocomplete="off"></el-input>
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
  name: "DoctorLocation",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      searchQuery: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      userRole: null,
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
    load() {
      this.request.get("/doctorlocations/details").then(res => {
        if (res.code === '200') {
          this.allData = res.data;
          this.applyFilters(); // 如果您需要根据条件筛选数据
        } else {
          this.$message.error("Loading Failed");
        }
      })
    },

    applyFilters() {
      let filteredData = this.allData;
      if (this.searchQuery) {
        filteredData = filteredData.filter(item =>
            item.doctorName.includes(this.searchQuery) ||
            item.departmentName.includes(this.searchQuery)
        );
      }
      this.total = filteredData.length;
      this.tableData = filteredData.slice(
          (this.pageNum - 1) * this.pageSize,
          this.pageNum * this.pageSize
      );
    },

    // 处理新增医生位置的操作
    handleAddDoctorLocation() {
      // 调用后端接口添加医生位置
      this.request.post("/doctorlocations/add", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Add Success");
          this.dialogFormVisible = false;
          this.load(); // 重新加载数据
        } else {
          this.$message.error("Add Failed:" + res.message);
        }
      }).catch(error => {
        this.$message.error("Request Failed:" + error);
      });
    },

    // 编辑医生位置的操作
    handleUpdateDoctorLocation() {
      // 调用后端接口更新医生位置
      this.request.post("/doctorlocations/update", this.form)
          .then(res => {
            if (res.code === '200') {
              // 如果更新成功
              this.$message.success("Update Success");
              this.dialogFormVisible = false; // 关闭对话框
              this.load(); // 重新加载数据
            } else {
              // 如果更新失败
              this.$message.error("Update Failed:" + res.message);
            }
          })
          .catch(error => {
            // 处理请求错误
            this.$message.error("Request Failed:" + error);
          });
    },


    // 在保存按钮点击时根据模式调用不同的方法
    save() {
      if (this.isEditMode) {
        // 编辑模式下的保存操作
        this.handleUpdateDoctorLocation();
      } else {
        // 新增模式下的保存操作
        this.handleAddDoctorLocation();
      }
    },


    del(doctorLocation) {
      this.request.delete(`/doctorlocations/${doctorLocation.doctorId}/${doctorLocation.departmentId}`).then(res => {
        if (res.code === '200') {
          this.$message.success("Delete Success");
          this.load();
        } else {
          this.$message.error("Delete Failed");
        }
      })
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },


    delBatch() {
      let ids = this.multipleSelection.map(v => ({ doctorId: v.doctorId, departmentId: v.departmentId }));
      this.request.post("/doctorlocations/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("Batch Delete Success");
          this.load();
        } else {
          this.$message.error("Batch Delete Failed");
        }
      })
    },

    // 在打开新增对话框时重置表单并设置为新增模式
    handleAdd() {
      this.form = {}; // 重置表单
      this.isEditMode = false; // 设置为新增模式
      this.dialogFormVisible = true;
    },


    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },

    reset() {
      this.searchQuery = "";
      this.load();
    },
    initializeUserRole() {
      const user = JSON.parse(localStorage.getItem('user'));
      this.userRole = user ? user.role : null;
    },

    handleEdit(row) {
      this.form = {
        doctorName: row.doctorName,
        departmentName: row.departmentName,
        room: row.room,
        timeTable: row.timeTable,
        doctorLevel: row.doctorLevel,
      };
      this.isEditMode = true; // 设置为编辑模式
      this.dialogFormVisible = true;
    }
  }
}
</script>

<style>
.headerBg {
  background: #cccccc!important;
}
</style>
