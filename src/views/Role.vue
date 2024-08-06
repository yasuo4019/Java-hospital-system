<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="Enter Name" suffix-icon="el-icon-search" class="ml-5" v-model="name"></el-input>
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
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="Name"></el-table-column>
      <el-table-column prop="flag" label="Flag"></el-table-column>
      <el-table-column prop="description" label="Description"></el-table-column>
      <el-table-column label="Operation" width="600" align="center">
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row)">Assignment Menu<i class="el-icon-menu"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)">Edit<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Sure'
              cancel-button-text='Think again'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure to delete these data?"
              @confirm="del(scope.row.id)">
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

    <el-dialog title="Role Info" :visible.sync="dialogFormVisible">
      <el-form label-width="100px" size="small">
        <el-form-item label="Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Flag">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
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

    <el-dialog title="Assigment Menu" :visible.sync="menuDialogVis" width="30%">
      <el-tree
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="tree"
          :default-expanded-keys="expends"
          :default-checked-keys="checks">
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.icon"></i> {{ data.name }}</span>
          </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">Cancel</el-button>
        <el-button type="primary" @click="saveRoleMenu">Sure</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name:"",
      form: {},
      dialogFormVisible:false,
      menuDialogVis: false,
      multipleSelection: [],
      menuData: [],
      props: {
        label: 'name',
      },
      expends: [],
      checks: [],
      roleId: 0,
      roleFlag: ''
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods : {
    load() {
      this.request.get("/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res =>{
        this.tableData = res.data.records
        this.total = res.data.total
      })

    },
    save() {
      this.request.post("/role",this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Save Success")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("Save Failed")
        }
      })
    },
    saveRoleMenu() {
     this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
       console.log(res)
            if (res.code === '200') {
              this.$message.success("Binding Success")
              this.menuDialogVis = false

              //操作管理员角色后需要重新登录
              if (this.roleFlag === 'ROLE_ADMIN') {
                this.$store.commit("logout")
              }

            } else {
              this.$message.error(res.msg)
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
    del(id) {
      this.request.delete("/role/" + id).then(res => {
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
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1.2.3]
      this.request.post("/role/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("Batch Delete Success")
          this.load()
        } else {
          this.$message.error("Batch Delete Failed")
        }
      })
    },
    reset(){
      this.name = ""
      this.email = ""
      this.address = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    selectMenu(role) {
      this.menuDialogVis = true
      this.roleId = role.id
      this.roleFlag = role.flag

      //请求菜单数据
      this.request.get("/menu").then(res => {
        this.menuData = res.data

        // 把后台返回的菜单数据处理成 id数组
        this.expends = this.menuData.map(v => v.id)
      })

      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        this.checks = res.data

        this.request.get("/menu/ids").then(r => {
          const ids = r.data
          ids.forEach(id => {
            if (!this.checks.includes(id)) {
              // 可能会报错：Uncaught (in promise) TypeError: Cannot read properties of undefined (reading 'setChecked')
              this.$refs.tree.setChecked(id, false)
            }
          })

        })
        this.menuDialogVis = true
      })
    },
  }
}
</script>

<style>
headerBg {
  background: #cccccc!important;
}
</style>