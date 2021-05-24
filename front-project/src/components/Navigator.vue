<template>
   
<div class="navbar navbar-inverse navbar-static-top" role="navigation">
    <div class="container" style="margin-top:0; padding:1px">
        <div class="navbar-header">
            <router-link class="navbar-brand" :to="{name:'Index'}">在线云盘系统</router-link>
        </div>
        <ul class="nav navbar-nav">
            <li>
                <router-link class="show-after-login" style="font-size:16px" :to="{name:'Upload'}">文档上传</router-link>
            </li>
        </ul>

        <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
                <a href="" class="dropdown-toggle"
                    data-toggle="dropdown"
                    style="height: 50px;padding: 5px;">
                    <img alt="" class="img-circle" :src="avatarPath" width="38px" height="38px"/>
                    <span style="color: #FFFFFF;font-size: 15px" id="username">
                        <i>{{userName}}</i>
                    </span>
                </a>
                <div class="dropdown-menu dropdown-menu-right"
                        style="background: #FFFFFF;width: 220px;overflow: hidden;">
                    <div style="margin-top: 16px;">
                        <div class="text-center">
                            <img class="img-circle" :src="avatarPath" width="38px" height="38px"/>
                        </div>
                        <div class="text-center" style="line-height: 36px;font-size: 15px">
                            <i>{{userName}}</i>
                        </div>
                    </div>

                    <hr/>

                    <ul class="nav nav-pills nav-stacked" style="font-size:15px" v-if="!isLogin">
                        <li>
                            <router-link :to="{name:'Login'}">
                                <span class="glyphicon glyphicon-log-in" aria-hidden="true"></span>
                                登录
                            </router-link>
                        </li>
                        <li>
                            <router-link :to="{name:'Signup'}">
                                <span class="glyphicon glyphicon-log-in" aria-hidden="true"></span>
                                注册
                            </router-link>
                        </li>
                    </ul>

                    <ul class="nav nav-pills nav-stacked" style="font-size:15px" v-if="isLogin">
                        <li>
                            <router-link :to="'/getPersonalCenter/record'">
                                <span class="glyphicon glyphicon-user" aria-hidden="true"></span>
                                个人中心
                            </router-link>
                        </li>
                        <li>
                            <router-link :to="{name:'Record'}">
                                <span class="glyphicon glyphicon-open-file" aria-hidden="true"></span>
                                我的上传记录
                            </router-link>
                        </li>
                        <li role="separator" class="divider"></li>
                        <li id="exit" v-on:click="exitLogin">
                            <a href="">
                                <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>
                                退出
                            </a>
                        </li>
                    </ul>


                </div>
            </li>
        </ul>
    </div>
    
</div>

</template>

<script>

export default {
    props:{
        userName:{
            type:String,
            default:'未登录'
        },
        avatarPath:{
            type:String,
            default:`${process.env.BASE_URL}UploadFile.svg`
        },
        isLogin:{
            type:Boolean,
            default:false
        }
    },
    methods: {
        exitLogin:function(){
            var vue = this;
            this.axios.get('/CloudDisk/ExitLogin')
                .then(function(response){
                    var data = response.data;
                    console.log(data);
                    if(data.status=="success"){
                        alert("退出成功");
                        // location.href = "/CloudDisk/getlogin";
                        vue.$router.push('/getlogin');
                    }
                }).catch(function(err){
                    alert("退出登录请求发生了一些错误……");
                })
        }
    },
}
</script>