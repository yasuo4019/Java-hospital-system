<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="Enter Patient Name" suffix-icon="el-icon-search" class="ml-5" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="applyFilters">Search</el-button>
      <el-button type="warning" @click="reset">Reset</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">Add<i class="el-icon-circle-plus-outline"></i></el-button>
    </div>

    <!-- 表格显示支付数据 -->
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="patientName" label="Patient Name" width="120"></el-table-column>
      <el-table-column prop="patientGender" label="Patient Gender" width="120"></el-table-column>
      <el-table-column prop="recordDate" label="Record Date" width="120"></el-table-column>
      <el-table-column prop="recordText" label="Record Text"></el-table-column>
      <el-table-column label="Operation" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">Edit<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Sure'
              cancel-button-text='Think again'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure to delete these data?"
              @confirm="del(scope.row.medicalRecordId)"
          >
            <el-button type="danger" slot="reference">Delete<i class="el-icon-remove-outline"></i></el-button>
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
    <el-dialog title="MedicalRecord Info" :visible.sync="dialogFormVisible">
      <el-form label-width="100px" size="small">
        <el-form-item label="Patient Name">
          <el-input v-model="form.patientName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Patient Gender">
          <el-input v-model="form.patientGender" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Record Date">
          <el-input v-model="form.recordDate" type="date" placeholder="Select Date" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="Record Text">
          <el-input v-model="form.recordText" autocomplete="off"></el-input>
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
  name: "MedicalRecord",
  data() {
    return {
      allData: [],
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: []
    }
  },
  created() {
    this.load()
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
    load() {
      this.request.get("/medicalRecords/details").then(res => {
        this.allData = res.data.map(item => ({
          ...item,
          recordDate: this.formatDate(item.recordDate) // Format the record date
        }));
        this.applyFilters();
      })
    },
    applyFilters() {
      let filteredData = this.allData.filter(item =>
          item.patientName.includes(this.name) ||
          (item.patientGender && item.patientGender.includes(this.name))
      );
      this.total = filteredData.length;
      this.tableData = filteredData.slice(
          (this.pageNum - 1) * this.pageSize,
          this.pageNum * this.pageSize
      );
    },

    // 保存信息
    save() {
      this.request.post("/medicalRecords", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Save Success");
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("Save Failed");
        }
      })
    },
    // 单个删除信息
    del(paymentId) {
      this.request.delete("/medicalRecords/" + paymentId).then(res => {
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
    // 批量删除信息
    delBatch() {
      let paymentIds = this.multipleSelection.map(v => v.paymentId)
      this.request.post("/medicalRecords/del/batch", paymentIds).then(res => {
        if (res.code === '200') {
          this.$message.success("Batch Delete Success");
          this.load();
        } else {
          this.$message.error("Batch Delete Failed");
        }
      })
    },

    reset() {
      this.name = "";
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
      this.form = {
        medicalRecordId: null,
        patientName: '',
        patientGender: '',
        recordDate: '',
        recordText: ''
      }; // 重置表单
      this.dialogFormVisible = true;
    },

    // 处理编辑操作
    handleEdit(row) {
      this.form = {
        medicalRecordId: row.medicalRecordId,
        patientName: row.patientName,
        patientGender: row.patientGender,
        recordDate: row.recordDate,
        recordText: row.recordText
      };
      this.dialogFormVisible = true;
    },

  }
}
</script>

<style>
.headerBg {
  background: #cccccc!important;
}
</style>

