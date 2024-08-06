import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css'
import request from "@/utils/request";
import locale from 'element-ui/lib/locale/lang/en' // 引入英文语言包

// 配置 Element UI 使用英文
Vue.use(ElementUI, { locale });


Vue.config.productionTip = false

Vue.use(ElementUI,{ size: "mini" });

Vue.prototype.request=request

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
