<template>
  <div class="wrapper">
    <!-- 视频背景 -->
    <video autoplay loop muted class="video-background">
      <source src="/video/why2.mp4" type="video/mp4">
      您的浏览器不支持视频标签。
    </video>
    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>LogIn</b></div>
      <el-form :model="user" :rules="rules" ref="userForm" @submit.prevent="login">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password" @keyup.enter="login"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">Register</el-button>
          <el-button type="primary" size="small" autocomplete="off" @click="login">Login</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import CryptoJS from 'crypto-js';
import {setRoutes} from "@/router";
export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: 'please enter user name', trigger: 'blur'},
          {min: 3, max: 10, message: '3 to 10 characters in length', trigger: 'blur'}
        ],
        password: [
          {required: true, message: 'Please enter password', trigger: 'blur'},
          {min: 1, max: 20, message: '1 to 20 characters in length', trigger: 'blur'},
          {validator: this.checkSpecialCharacters, trigger: 'blur'} // 新增的规则
        ],
      },
      loginAttempts: 0,
      lastAttemptTime: null,
      passwordErrorCount: 0,
      passwordErrorTimestamp: null,
    }
  },
  methods: {
    login() {
      let currentTime = new Date().getTime();
      // 检查密码错误冷却
      if (this.passwordErrorTimestamp && currentTime - this.passwordErrorTimestamp < 60000) {
        if (this.passwordErrorCount >= 3) {
          this.$message.error("You entered the wrong password more than three times, please try again after one minute.");
          return;
        }
      }
      if (this.lastAttemptTime && currentTime - this.lastAttemptTime < 60000 && this.loginAttempts >= 3) {
        this.$message.error("You can only try to log in three times in one minute, please try again later.");
        return;
      }
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.lastAttemptTime = currentTime;
          this.loginAttempts++; // 增加尝试次数
          // 将密码加密
          let encryptedPassword = CryptoJS.SHA256(this.user.password).toString();
          let loginData = {...this.user, password: encryptedPassword};
          this.request.post("/user/login", loginData).then(res => {
            if (res.code === '200') {
              // 登录成功，重置计数器和时间戳
              this.passwordErrorCount = 0;
              this.passwordErrorTimestamp = null;
              localStorage.setItem("user", JSON.stringify(res.data)) //存储用户信息到浏览器
              localStorage.setItem("menus", JSON.stringify(res.data.menus))//存储用户信息到浏览器
              //动态设置当前用户的路由
              setRoutes()
              this.$router.push("/")
              this.$message.success("login successful")
            } else {
              this.$message.error(res.msg)
              // 登录失败
              this.handleLoginFailure(currentTime);
            }
          })
        }
      });
    },

    checkSpecialCharacters(rule, value, callback) {
      if (value && /[!@#$%^&*(),.?":{}|<>=-]/.test(value)) {
        return callback(new Error('Password should not contain special characters'));
      }
      return callback();
    },

    handleLoginFailure(currentTime) {
      this.passwordErrorCount++;
      if (this.passwordErrorCount >= 3) {
        // 记录时间戳并通知用户
        this.passwordErrorTimestamp = currentTime;
        this.$message.error("You entered the wrong password more than three times, please try again after one minute.");
      } else {
        this.$message.error("Wrong password, please try again");
      }
    },
  }
}
</script>


<style>
.wrapper {
  position: relative;
  height: 100vh;
  overflow: hidden;
}

.video-background {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  min-width: 100%;
  min-height: 100%;
  width: auto;
  height: auto;
  z-index: -1;
}

.login-form {
  position: relative;
  margin: 200px auto;
  background-color: rgba(255, 255, 255, 0.8); /* 透明背景 */
  width: 350px;
  height: 300px;
  padding: 20px;
  border-radius: 10px;
  z-index: 1;
}
</style>
