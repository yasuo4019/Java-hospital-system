<template>
  <div class="wrapper">
    <div style="margin: 100px auto; background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>Register</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="Please Input Username" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="Please Enter password" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="Please Confirm Your password" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/login')">Return Login</el-button>
          <el-button type="primary" size="small"  autocomplete="off" @click="login">Register</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>

import CryptoJS from 'crypto-js';
export default {
  name: "Register",
  data() {
    return {
      user: {},
      rules: {
        username: [
          { required: true, message: 'please enter user name', trigger: 'blur' },
          { min: 3, max: 10, message: '3 to 5 characters in length', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 1, max: 20, message: '1 to 20 characters in length', trigger: 'blur' },
          {validator: this.checkSpecialCharacters, trigger: 'blur'} // 新增的规则
        ],
        confirmPassword: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 1, max: 20, message: '1 to 20 characters in length', trigger: 'blur' },
          {validator: this.checkSpecialCharacters, trigger: 'blur'} // 新增的规则
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("The passwords entered twice are inconsistent");
            return false;
          }

          // 对密码进行 MD5 加密
          let encryptedPassword = CryptoJS.SHA256(this.user.password).toString();
          // 添加默认用户身份
          let registerData = {
            ...this.user,
            password: encryptedPassword,
            role: 'ROLE_USER'
          };

          // 发送加密后的密码
          this.request.post("/user/register", registerData).then(res => {
            if(res.code === '200') {
              this.$message.success("success");
              // 注册成功后跳转到登录页面
              this.$router.push('/login');
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
    },
    checkSpecialCharacters(rule, value, callback) {
      if (value && /[!@#$%^&*(),.?":{}|<>=-]/.test(value)) {
        return callback(new Error('Password Should Not Contain Special Characters'));
      }
      return callback();
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #0000FF , #98FB98);
  overflow: hidden;
}
</style>