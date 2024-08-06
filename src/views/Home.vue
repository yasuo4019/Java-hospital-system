<template>
  <div class="home">
    <el-row :gutter="20">
      <!-- 主要内容区域 -->
      <el-col :span="18">
        <!-- 欢迎信息 -->
        <div class="header-background">
          <div class="welcome-message">
            Welcome to the hospital management system!
          </div>
        </div>
        <!-- 功能区域, 如果这些圆圈代表特定功能 -->
        <div class="function-area">
          <div class="circle-button" @click="goToRoute('/Patient')">Patients</div>
          <div class="circle-button" @click="goToRoute('/Doctor')">Doctors</div>
          <div class="circle-button" @click="goToRoute('/DoctorLocation')">DoctorLocation</div>
          <div class="circle-button" @click="goToRoute('/Treatment')">Treatment</div>
          <div class="circle-button" @click="goToRoute('/Drugsstore')">DrugsStore</div>
        </div>

        <!-- 通知区域 -->
        <div class="notification-area">
          <el-carousel interval="4000" type="card" arrow="always">
            <el-carousel-item v-for="n in 5" :key="n">
              <img :src="'/photos/image' + n + '.jpg'" alt="通知内容"/>
            </el-carousel-item>
          </el-carousel>
        </div>

      </el-col>
      <!-- 状态信息区域 -->
      <el-col :span="6">
        <el-card class="status-card">
          <!-- 状态信息区域的内容 -->
          <div class="status-info">
            Today's date: {{ currentDate }}
            <br>
            Current time: {{ currentTime }}
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 底部快速链接或联系信息 -->
    <div class="footer-info">
      Address: No. 01, XX Road, Minsk<br>
      Contact us:131-2131-3131
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      temperature: '0°',
      currentDate: '',
      currentTime: '',
      notifications: [
        { img: '/photos/image1.jpg', text: '通知1' },
        { img: '/photos/image2.jpg', text: '通知2' },
        { img: '/photos/image3.jpg', text: '通知3' },
        { img: '/photos/image4.jpg', text: '通知4' },
        { img: '/photos/image5.jpg', text: '通知5' },
      ]
    }
  },

  created() {
    this.fetchTemperature();
    this.updateDateTime();
    // 每分钟更新时间和温度
    setInterval(this.updateDateTime, 60000);
    setInterval(this.fetchTemperature, 3600000); // Assuming you want to update the temperature every hour
  },

  mounted() {
  },

  methods: {
    fetchTemperature() {
      // 温度获取方法
    },
    goToRoute(path) {
      this.$router.push(path);
    },
    updateDateTime() {
      const now = new Date();
      this.currentDate = now.toLocaleDateString('en-CN'); // Adjusted for Chinese locale
      this.currentTime = now.toLocaleTimeString('en-CN', {hour12: false}); // 24小时格式
    },
  },
}
</script>

<style scoped>
.header-background {
  background: url('~@/assets/why.jpg') no-repeat center center;
  background-size: cover;
  padding-top: 130px;
  height: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
}
.welcome-message {
  font-size: 45px;
  margin-bottom: 30px;
  color: #fff;
  width: 100%;
  text-align: left;
}


.home {
  position: relative;
}
.status-card {
  position: absolute;
  bottom: 200px; /* 不是紧贴底部，根据需要调整 */
  right: 50px; /* 不是紧贴右边，根据需要调整 */
  /* 如果您的卡片需要有固定的宽度或高度，也可以在这里设置 */
}
.footer-info {
  position: absolute;
  right: 35px; /* 不是紧贴右侧，根据需要调整 */
  bottom: 120px; /* 不是紧贴底部，根据需要调整 */
  /* 其他必要的样式 */
}



.function-area {
  display: flex;
  justify-content: space-around; /* 按需调整 */
  /* 其他样式 */
}
.circle-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 130px; /* 圆形大小 */
  height: 90px; /* 圆形大小 */
  border-radius: 50%; /* 使按钮成为圆形 */
  background-color: #e6e6fa; /* 背景颜色 */
  color: white; /* 设置字体颜色为白色 */
  text-align: center;
  margin-top: 20px; /* 向下移动圆圈，根据需要调整数值 */
  margin-left: 20px; /* 调整为所需的间距 */
  margin-right: 10px; /* 调整为所需的间距 */
  background-image: linear-gradient(to right, #4facfe 100%, #00f2fe 0%); /* 添加渐变背景 */
  transition: transform 0.3s ease, box-shadow 0.3s ease; /* 添加过渡效果 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}
.circle-button:hover {
  transform: scale(1.1); /* 悬停时放大 */
  box-shadow: 0 6px 8px rgba(0, 0, 0, 0.2); /* 悬停时增强阴影 */
}



.notification-area {
  width: 80%; /* 或者根据实际情况调整 */
  height: 500px; /* 根据您的轮播图内容调整高度 */
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative; /* 添加相对定位 */
  left: 50px; /* 根据需求调整这个值来向右移动 */
}
.el-carousel {
  width: 80%; /* 或者根据需要设置一个具体的宽度 */
  /* 如果需要，可以添加最小高度来确保图片显示不会太小 */
  min-height: 350px; /* 或根据设计调整 */
  /* 如果只想移动轮播图而不是整个区域，可以将margin-left添加到这里 */
}
.el-carousel .el-carousel__item img {
  width: 130%; /* 使图片宽度填满轮播项容器 */
  height: auto; /* 保持图片的宽高比 */
  object-fit: cover; /* 保持图片覆盖整个容器 */
}


</style>
