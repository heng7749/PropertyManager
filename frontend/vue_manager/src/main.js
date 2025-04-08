import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false
import store from './store'
import router from './router'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI)
// 安装echarts（cnpm install echarts --save）
import axios from 'axios'
//引入完成之后需要挂载到vue上
Vue.prototype.$http = axios
//引入全局路由
import '@/router/permission.js'


new Vue({
  render: h => h(App),
  store,
  router,
  beforeCreate() {
    Vue.prototype.$bus = this  //在Vue的原型中添加全局总线
  }
}).$mount('#app')
