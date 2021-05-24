import Vue from 'vue'
import App from './view/App.vue'
import VueRouter from 'vue-router'
import router from './router/index'
import axios from 'axios'
import VueAxios from "vue-axios";
import 'bootstrap3/dist/css/bootstrap.min.css'
import 'bootstrap3/dist/js/bootstrap'
import * as echarts from 'echarts'

Vue.use(VueAxios, axios)
Vue.use(VueRouter)

Vue.config.productionTip = false
Vue.prototype.$echarts = echarts

Date.prototype.format = function(format) {
  var date = {
         "M+": this.getMonth() + 1,
         "d+": this.getDate(),
         "h+": this.getHours(),
         "m+": this.getMinutes(),
         "s+": this.getSeconds(),
         "q+": Math.floor((this.getMonth() + 3) / 3),
         "S+": this.getMilliseconds()
  };
  if (/(y+)/i.test(format)) {
         format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
  }
  for (var k in date) {
         if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1
                       ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
         }
  }
  return format;
}

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
