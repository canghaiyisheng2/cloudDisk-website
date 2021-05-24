<template>

<div class="container">
    <div class="row text-center"><h2>账号注册</h2></div>
    <hr/>
    <div class="row">
        <div class="col-md-4 col-md-offset-4 form">
            <div class="input-group">
                <span class="input-group-addon" id="user-name">账号: </span>
                <input type="text" id="name" class="form-control" placeholder="1-30字符，不得为空" aria-describedby="user-name" v-model="userName" v-on:keyup="CheckUNFormat"/>
            </div>
            <br/>
            <div class="input-group">
                <span class="input-group-addon" id="passwd">密码: </span>
                <input type="password" id="password" class="form-control" placeholder="长度为6-20位，区分大小写" aria-describedby="passwd" v-model="password" v-on:keyup="CheckPwDFormat" v-on:keyup.enter="signup"/>
            </div>
            <div id="errMsg" style="color: #ff0000;">{{errMsg}}</div>
            <br/>
            <div class="text-center"><button class="btn-default btn btn-lg" id="signup" v-on:click="signup">注册</button></div>
        </div>
    </div>


</div>

</template>

<script>
    export default{
        data() {
            return {
                userName:'',
                password:'',
                errMsg:''
            }
        },
        methods: {
            signup:function(){
                if(this.userName.length > 0 && this.userName.length <= 30){
                    this.axios.post('signup',{
                        name: this.userName,
                        password: this.password
                    }).then(response=>{
                        var data = response.data;
                        if(data.status == "fail") app.errMsg=data.msg;
                        else{
                            // location.href = "getupload";
                            this.$router.push('/getupload');
                            alert("注册成功");
                        }
                    }).catch(err=>{
                        console.log(err);
                    })
                }
            },
            CheckUNFormat:function(){
                if(this.userName.length ==0) this.errMsg="用户名不得为空";
                else if(this.userName.length > 30) this.errMsg="用户名过长";
                else this.errMsg="";
            },
            CheckPwDFormat:function(){
                if(this.password.length < 6) this.errMsg="密码过短";
                else if(this.password.length > 20) this.errMsg="密码过长";
                else this.errMsg="";
            }
        },
        created() {
            document.title = "注册";
        },
    }
</script>