<template>
  <div>
    <div style="padding: 10px 0">
      <!-- 搜索栏 -->
      <el-input style="width: 200px" placeholder="Enter The Keyword" suffix-icon="el-icon-search" class="ml-5" v-model="searchKeyword"></el-input>
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

    <!-- 表格显示治疗历史数据 -->
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="patientName" label="Patient Name" width="150"></el-table-column>
      <el-table-column prop="doctorName" label="Doctor Name" width="150"></el-table-column>
      <el-table-column prop="drugName" label="Drug Name" width="150"></el-table-column>
      <el-table-column prop="drugType" label="Drug Type" width="250"></el-table-column>
      <el-table-column prop="procedureName" label="Procedure Name" width="150"></el-table-column>
      <el-table-column prop="procedureType" label="Procedure Type" width="150"></el-table-column>
      <el-table-column prop="recordDate" label="Record Date" width="150"></el-table-column>
      <el-table-column prop="recordTime" label="Record Time" width="150"></el-table-column>
      <el-table-column prop="result" label="Result"></el-table-column>
      <el-table-column label="Operation" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)" v-if="canEditOrDelete">Edit<i class="el-icon-edit"></i></el-button>
          <!-- 单个删除治疗历史数据 -->
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Sure'
              cancel-button-text='Think again'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure to delete these data?"
              @confirm="del(scope.row.treatmentHistoryId)"
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
    <el-dialog title="Treatment History Info" :visible.sync="dialogFormVisible">
      <el-form label-width="100px" size="small">
        <el-form-item label="Patient Name">
          <el-input v-model="form.patientName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Doctor Name">
          <el-input v-model="form.doctorName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Drug Name">
          <el-input v-model="form.drugName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Drug Type">
          <el-input v-model="form.drugType" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Procedure Name">
          <el-input v-model="form.procedureName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Procedure Type">
          <el-input v-model="form.procedureType" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input type="textarea" v-model="form.procedureDescription" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Record Date">
          <el-date-picker v-model="form.recordDate" type="date" placeholder="Select Date" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="Record Time">
          <el-time-picker v-model="form.recordTime" placeholder="Select Time" style="width: 100%;"></el-time-picker>
        </el-form-item>
        <el-form-item label="Result">
          <el-input v-model="form.result" autocomplete="off"></el-input>
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
  name: "TreatmentHistory",
  data() {
    return {
      allData: [],
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      searchKeyword: "", // 搜索关键词
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
    formatDate(timestamp) {
      if (!timestamp) return '';
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    formatTime(milliseconds) {
      if (!milliseconds && milliseconds !== 0) return '';
      const seconds = Math.floor(milliseconds / 1000);
      const hours = Math.floor(seconds / 3600);
      const minutes = Math.floor((seconds % 3600) / 60);
      const secondsRemaining = seconds % 60;
      return [hours, minutes, secondsRemaining]
          .map(v => v.toString().padStart(2, '0'))
          .join(':');
    },
    load() {
      // 修改API路径以匹配治疗历史数据
      this.request.get("/treatmentHistory/details").then(res => {
        this.allData = res.data.map(item => ({
          ...item,
          recordDate: this.formatDate(item.recordDate),
          recordTime: this.formatTime(item.recordTime),
        }));
        this.applyFilters();
      })
    },
    applyFilters() {
      let filteredData = this.allData.filter(item =>
              item.patientName.includes(this.searchKeyword) ||
              item.doctorName.includes(this.searchKeyword) ||
              item.drugName.includes(this.searchKeyword) ||
              item.drugType.includes(this.searchKeyword) ||
              item.procedureName.includes(this.searchKeyword) ||
              item.procedureType.includes(this.searchKeyword) ||
              item.procedureDescription.includes(this.searchKeyword)
      );
      this.total = filteredData.length;
      this.tableData = filteredData.slice(
          (this.pageNum - 1) * this.pageSize,
          this.pageNum * this.pageSize
      );
    },

    // 保存治疗历史信息
    save() {
      // 修改API路径以匹配治疗历史数据
      this.request.post("/treatmentHistory", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Save Success");
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("Save Failed");
        }
      })
    },

    // 单个删除治疗历史信息
    del(historyId) {
      // 修改API路径以匹配治疗历史数据
      this.request.delete("/treatmentHistory/" + historyId).then(res => {
        if (res.code === '200') {
          this.$message.success("Delete Success");
          this.load();
        } else {
          this.$message.error("Delete Failed");
        }
      })
    },

    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    // 批量删除治疗历史信息
    delBatch() {
      let historyIds = this.multipleSelection.map(v => v.historyId)
      this.request.post("/treatmentHistory/del/batch", historyIds).then(res => {
        if (res.code === '200') {
          this.$message.success("Batch Delete Success");
          this.load();
        } else {
          this.$message.error("Batch Delete Failed");
        }
      })
    },

    reset() {
      this.searchKeyword = "";
      this.pageNum = 1;
      this.applyFilters();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.applyFilters();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.applyFilters();
    },

    // 处理添加操作
    handleAdd() {
      this.form = {}; // 重置表单
      this.dialogFormVisible = true;
    },

// 处理编辑操作
    handleEdit(row) {
      this.form = {
        treatmentHistoryId: row.treatmentHistoryId,
        patientName: row.patientName,
        doctorName: row.doctorName,
        drugName: row.drugName,
        drugType: row.drugType,
        procedureName: row.procedureName,
        procedureType: row.procedureType,
        procedureDescription: row.procedureDescription,
        recordDate: row.recordDate,
        recordTime: row.recordTime,
      };
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
