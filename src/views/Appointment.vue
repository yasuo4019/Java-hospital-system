<template>
  <div>
    <div style="padding: 10px 0">
      <!-- 搜索栏 -->
      <el-input style="width: 200px" placeholder="Please enter name" suffix-icon="el-icon-search" class="ml-5" v-model="searchQuery"></el-input>
      <el-button class="ml-5" type="primary" @click="applyFilters">Search</el-button>
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

    <!-- 表格显示预约数据 -->
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="doctorName" label="Doctor Name" width="150"></el-table-column>
      <el-table-column prop="patientName" label="Patient Name" width="150"></el-table-column>
      <el-table-column prop="appointmentDate" label="Appointment Date" width="150"></el-table-column>
      <el-table-column prop="startTime" label="Start Time"></el-table-column>
      <el-table-column prop="endTime" label="End Time"></el-table-column>
      <el-table-column prop="room" label="Room" width="150"></el-table-column>
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
              @confirm="del(scope.row.appointmentId)"
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
    <el-dialog title="Appointment Info" :visible.sync="dialogFormVisible">
      <el-form label-width="100px" size="small">
        <el-form-item label="Doctor Name">
          <el-input v-model="form.doctorName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Patient Name">
          <el-input v-model="form.patientName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Appoint Date">
          <el-date-picker v-model="form.appointmentDate" type="date" placeholder="Select Date" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="Start Time">
          <el-time-picker v-model="form.startTime" placeholder="Select Time" style="width: 100%;"></el-time-picker>
        </el-form-item>
        <el-form-item label="End Time">
          <el-time-picker v-model="form.endTime" placeholder="Select Time" style="width: 100%;"></el-time-picker>
        </el-form-item>
        <el-form-item label="Room">
          <el-input v-model="form.room" autocomplete="off"></el-input>
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
  name: "Appointment",
  data() {
    return {
      allData: [],
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      searchQuery: "",
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
    // 格式化日期时间
    formatDate(timestamp) {
      if (!timestamp) return '';
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    // 格式化一天中的时间
    formatTime(timeInMillis) {
      if (!timeInMillis) return '';
      const date = new Date();
      date.setHours(0, 0, 0, timeInMillis); // 将时间设置为当天的相应时间
      return date.toTimeString().substr(0, 5); // 获取时间的小时和分钟部分
    },

    load() {
      this.request.get("/appointments/details").then(res => {
        if (res.code === '200') {
          this.allData = res.data.map(item => ({
            ...item,
            appointmentDate: this.formatDate(item.appointmentDate),
            startTime: this.formatTime(item.startTime),
            endTime: this.formatTime(item.endTime)
          }));
          this.applyFilters();
        } else {
          this.$message.error("Loading Failed");
        }
      })
    },

    applyFilters() {
      let filteredData = this.allData.filter(item =>
          item.doctorName.includes(this.searchQuery) ||
          item.patientName.includes(this.searchQuery)
      );
      this.total = filteredData.length;
      this.tableData = filteredData.slice(
          (this.pageNum - 1) * this.pageSize,
          this.pageNum * this.pageSize
      );
    },
    save() {
      const url = this.form.appointmentId ? `/appointments/${this.form.appointmentId}` : '/appointments';
      const method = this.form.appointmentId ? 'put' : 'post';
      this.request[method](url, this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Save Success");
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("Save Failed:" + res.message);
        }
      });
    },
    del(appointmentId) {
      this.request.delete(`/appointments/${appointmentId}`).then(res => {
        if (res.code === '200') {
          this.$message.success("Delete Success");
          this.load();
        } else {
          this.$message.error("Delete Failed:" + res.message);
        }
      });
    },
    delBatch() {
      const appointmentIds = this.multipleSelection.map(item => item.appointmentId);
      this.request.post("/appointments/del/batch", appointmentIds).then(res => {
        if (res.code === '200') {
          this.$message.success("Batch Delete Success");
          this.load();
        } else {
          this.$message.error("Batch Delete Failed:" + res.message);
        }
      });
    },
    reset() {
      this.searchQuery = "";
      this.load();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    handleAdd() {
      this.form = {}; // 重置表单
      this.dialogFormVisible = true;
    },
    handleEdit(row) {
      this.form = Object.assign({}, row); // 使用 row 数据填充表单
      this.dialogFormVisible = true;
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
