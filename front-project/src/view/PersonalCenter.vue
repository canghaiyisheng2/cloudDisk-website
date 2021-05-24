<template>
    <div id="backgroud">
        <div class="container" style="background-color: #f2f2f2;margin-top:0">
            <div class="panel">

                <div class="panel-body" style="padding: 0;margin-top: 20px;">

                    <div class="row">
                        <div class="col-md-3 col-sm-3 col-xs-3">
                            <img class="img img-responsive img-circle" :src="avatarPath" v-on:click="selectImg"/>
                            <h3 class="col-md-offset-1" style="color:#00000050; font-style:italic;" v-if="!isInputName" @dblclick="showInputEle">{{userName}}</h3>
                            <input type="text" id="name-modify" class="col-md-offset-1 form-control" v-model="userName" v-if="isInputName" @keyup.enter="modify('userName')" @blur="isInputName = !isInputName"/>
                            <button class="btn-default btn col-md-10 col-md-offset-1">编辑个人资料</button>
                            <input type="file" id="avatar-path" style="display: none;" @change="uploadAvatar"/>

                        </div>
                        <div class="col-md-9 col-sm-9 col-xs-9">
                            <div class="navbar" style="border: 1px #f2f2f2 solid;">
                                <ul class="nav navbar-nav" id="page-option">
                                    <li @click="curOp=0;" :class="{active:curOp==0}">
                                        <router-link :to="{path:'/getPersonalCenter/record'}">
                                            &nbsp;主页&nbsp;
                                        </router-link>
                                    </li>
                                    <li @click="curOp=1;" :class="{active:curOp==1}">
                                        <router-link :to="{path:'/getPersonalCenter/record'}">
                                            上传记录
                                        </router-link>
                                    </li>
                                </ul>
                            </div>
                            <div class="panel-body">
                                <router-view/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
</template>


<script>
export default{
    data() {
            return {
                option:["mainPage","uploadRecord"],
                curOp:0,
                isInputName:false,
            }
        },
    props:{
        userName:{
            type:String,
            default:'未登录'
        },
        avatarPath:{
            type:String,
            default:`${process.env.BASE_URL}UploadFile.svg`
        }
    },
    methods: {
        selectImg:function(){
            $("#avatar-path").click();
        },
        uploadAvatar:function(){
            var file = $("#avatar-path").get(0).files[0];
            if(!file){
                alert("上传头像不得为空");
            }
            else{
                //使用FormData上传文件
                var formData = new FormData();
                formData.append("file", file);
                this.axios({
                    url: '/CloudDisk/upavatar',
                    data: formData,
                    method: 'post',
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }).then((res)=>{
                    var data = res.data;
                    if(data.status == "fail"){
                        alert(data.msg);
                    }
                    else{
                        alert("头像上传成功");
                        location.reload();
                    }
                }).catch((err)=>{
                    alert("上传请求发生了一些错误……");
                })
            }
        },
        modify:function(attr){
            this.axios({
                url: '/CloudDisk/modify',
                    params:{
                        key:attr,
                        value:this.userName
                    },
                    method: 'post',
            }).then(function(res){
                var data = res.data;
                if(data.status == "fail")
                    alter(data.msg);
                else{
                    location.reload();
                }
            })
        },
        showInputEle:function(){
            this.isInputName = !this.isInputName;
            setTimeout(function(){
                $("#name-modify").focus();
            },50);
        }
    },
    watch: {
        curOp:function(val,oldVal){
            $("#panel-content").load("/CloudDisk/" + this.option[val]);
        }
    },
    created() {
        document.title = "个人中心";
    },
}
    
</script>

<style>
    #page-option > .active {
        border-bottom: 3px darkblue solid;
        background-color: #f8f8f8;
    }
    .img {
        border: 2px #f0f0f0 solid;
        margin: 10px;
    }
</style>