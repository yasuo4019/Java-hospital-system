<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="Enter Procedure Name" suffix-icon="el-icon-search" class="ml-5" v-model="name"></el-input>
      <el-input style="width: 200px" placeholder="Enter Procedure Type" suffix-icon="el-icon-notebook-2" class="ml-5" v-model="type"></el-input>
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
      <el-button type="warning" @click="reset">Reset</el-button>
    </div>

    <div style="margin: 10px 0">
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


    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="name" label="Procedure Name" width="180"></el-table-column>
      <el-table-column prop="type" label="Procedure Type"></el-table-column>
      <el-table-column prop="description" label="Procedure Description"></el-table-column>
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
              @confirm="del(scope.row.procedureId)"
          >
            <el-button type="danger" slot="reference">Delete<i class="el-icon-remove-outline"></i></el-button>
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


    <el-dialog title="Procedure Info" :visible.sync="dialogFormVisible">
      <el-form label-width="100px" size="small">
        <el-form-item label="Procedure Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Procedure Type">
          <el-input v-model="form.type" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="form.description" autocomplete="off"></el-input>
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
  name: "ProcedureStore",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      type: "",
      description: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: []
    }
  },
  created() {
    this.load()
  },
  methods : {
    load() {
      this.request.get("/procedures/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          type: this.type
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },


    save() {
      this.request.post("/procedures", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Save Success")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("Save Failed")
        }
      })
    },


    del(procedureId) {
      this.request.delete("/procedures/" + procedureId).then(res => {
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


    delBatch() {
      let procedureIds = this.multipleSelection.map(v => v.procedureId)
      this.request.post("/procedures/del/batch", procedureIds).then(res => {
        if (res.code === '200') {
          this.$message.success("Batch Delete Success")
          this.load()
        } else {
          this.$message.error("Batch Delete Failed")
        }
      })
    },

    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
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
