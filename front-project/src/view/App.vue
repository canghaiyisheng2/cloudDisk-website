<template>
  <div id="app" :class="theme">
    <Nav :user-name="userName" :avatar-path="avatarPath" :isLogin="isLogin"/>
    <router-view :user-name="userName" :avatar-path="avatarPath"/>
  </div>
</template>

<script>
import Nav from '../components/Navigator'
export default {
  name: 'App',
  data() {
    return {
      userName: '未登录',
      avatarPath: `${process.env.BASE_URL}UploadFile.svg`,
      isLogin:false,
      theme:'day'
    }
  },
  components: {
    Nav,
  },
  methods: {
    checkLogin:function(){
      var vue = this;
      this.axios.get('/CloudDisk/checklogin')
        .then(function(response){
            var data = response.data;
            if(data.status == "success"){
                var user = JSON.parse(data.msg);
                vue.userName=user.usrName;
                if(user.avatar)
                  vue.avatarPath="/CloudDisk/avatar" + "/"  + user.uuid+ user.avatar;
                vue.isLogin = true;
            }
        }).catch(function(err){
            console.log(err);
        })
    }
  },
  updated() {
    this.checkLogin();
  },
  created() {
    this.checkLogin();
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* background-color: #f2f2f2; */
}
</style>
