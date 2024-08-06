<template>
  <div class="center-container">
    <el-card style="width: 500px;">
      <el-form label-width="120px" size="small" :model="form" :rules="rules" ref="pass">
        <el-form-item label="Old Password" prop="password">
          <el-input v-model="form.password" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="New Password" prop="newPassword">
          <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="Confirm" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">SURE</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>


<script>

import CryptoJS from 'crypto-js';
export default {
  name: "Password",
  data() {
    return {
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        password: [
          {required: true, message: 'Please enter the original password', trigger: 'blur'},
          {min: 3, message: 'Length should not be less than 3 digits', trigger: 'blur'},
          {validator: this.checkSpecialCharacters, trigger: 'blur'}
        ],
        newPassword: [
          {required: true, message: 'Please enter a new password', trigger: 'blur'},
          {min: 3, message: 'Length should not be less than 3 digits', trigger: 'blur'},
          {validator: this.checkSpecialCharacters, trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: 'Please enter password', trigger: 'blur'},
          {min: 3, message: 'Length should not be less than 3 digits', trigger: 'blur'},
          {validator: this.checkSpecialCharacters, trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.form.username = this.user.username
  },
  methods: {
    save() {
      this.$refs.pass.validate((valid) => {
        if (valid) {
          if (this.form.newPassword !== this.form.confirmPassword) {
            this.$message.error("The new passwords entered twice are not the same");
            return false;
          } else {
            // 加密原密码和新密码
            let encryptedPassword = CryptoJS.SHA256(this.form.password).toString();
            let encryptedNewPassword = CryptoJS.SHA256(this.form.newPassword).toString();

            let passwordData = {
              username: this.form.username,
              password: encryptedPassword,
              newPassword: encryptedNewPassword
            };
            // 使用包含加密密码的 passwordData 对象
            this.request.post("/user/password", passwordData).then(res => {
              if (res.code === '200') {
                this.$message.success("success");
                this.$store.commit("logout");
              } else {
                this.$message.error(res.msg);
              }
            });
          }
        }
      });
    },
    checkSpecialCharacters(rule, value, callback) {
      if (value && /[!@#$%^&*(),.?":{}|<>=-]/.test(value)) {
        return callback(new Error('Password should not contain special characters'));
      }
      return callback();
    }
  }
}
</script>

<style>
.center-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: linear-gradient(to bottom right, #0000FF , #98FB98);
  overflow: hidden;
}
</style>
