<template>
  <div>
    <div style="padding: 10px 0">
      <!-- 搜索栏 -->
      <el-input style="width: 200px" placeholder="Enter Drugs Name" suffix-icon="el-icon-search" class="ml-5" v-model="name"></el-input>
      <el-input style="width: 200px" placeholder="Enter Drugs Type" suffix-icon="el-icon-notebook-2" class="ml-5" v-model="type"></el-input>
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button type="warning" @click="reset">Reset</el-button>
    </div>

    <div style="margin: 10px 0">
      <!-- 操作按钮 -->
      <el-button type="primary" @click="handleAdd">Add<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='Sure'
          cancel-button-text='Think again'
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure to delete these data?"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">Batch Delete<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <!-- 表格显示药品数据 -->
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="100"></el-table-column>
      <el-table-column prop="name" label="Drugs Name" width="350"></el-table-column>
      <el-table-column prop="type" label="Drugs Type"></el-table-column>
      <el-table-column prop="number" label="Number" width="250"></el-table-column>
      <el-table-column label="Operation" width="350" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">Edit<i class="el-icon-edit"></i></el-button>
          <!-- 单个删除药品数据 -->
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Sure'
              cancel-button-text='Think again'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure to delete these data?"
              @confirm="del(scope.row.drugId)"
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
    <el-dialog title="Drugs Info" :visible.sync="dialogFormVisible">
      <el-form label-width="100px" size="small">
        <el-form-item label="Drugs Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Drugs Type">
          <el-input v-model="form.type" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Number">
          <el-input v-model="form.number" autocomplete="off"></el-input>
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
  name: "DrugsStore",
  data() {
    return {
      // 初始化数据
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      type: "",
      number: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: []
    }
  },
  created() {
    this.load()
  },
  methods : {
    // 加载药品数据
    load() {
      this.request.get("/drugsstore/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          type: this.type,
          number: this.number,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },

    // 保存药品信息
    save() {
      this.request.post("/drugsstore", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Save Success")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("Save Failed")
        }
      })
    },

    // 单个删除药品信息
    del(drugId) {
      this.request.delete("/drugsstore/" + drugId).then(res => {
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
      console.log(val)
      this.multipleSelection = val
    },

    // 批量删除药品信息
    delBatch() {
      let drugIds = this.multipleSelection.map(v => v.drugId)  // 从选中的药品记录中获取ID
      this.request.post("/drugsstore/del/batch", drugIds).then(res => {
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
      this.form = row
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
      this.type = ""
      this.load()
    },
  }
}
</script>

<style>
.headerBg {
  background: #cccccc!important;
}
</style>
