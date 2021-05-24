<template>
    <div>
        <div class="container">
            <div class="row text-center"><h2>登录</h2></div>
            <hr/>
            <div class="row">
                <div class="col-md-4 col-md-offset-4 form">
                    <div class="input-group">
                        <span class="input-group-addon" id="user-name">账号: </span>
                        <input type="text" id="name" class="form-control" aria-describedby="user-name" v-model="userName"/>
                    </div>
                    <br/>
                    <div class="input-group">
                        <span class="input-group-addon" id="passwd">密码:</span>
                        <input type="password" id="password" class="form-control" aria-describedby="passwd" v-model="password" v-on:keyup.enter="login" />
                    </div>
                    <div id="errMsg">{{errMsg}}</div>
                    <br/>
                    <div class="text-center"><button v-on:click="login" class="btn-default btn btn-lg">登录</button></div>
                </div>
            </div>

        </div>
    </div>

</template>

<script>
export default{
    data(){
        return{
            userName:'',
            password:'',
            errMsg:'',
        }
    },
    methods: {
        login: function(){
            if(this.userName=='') errorMsg='用户名不能为空！';
            var vue = this;
            this.axios.post('login',{
                name: this.userName,
                password: this.password
            }).then(function(response){
                var data = response.data;
                console.log(data);
                if(data.status == "fail")
                    vue.errMsg=data.msg;
                else
                    // location.href = "getupload";
                    vue.$router.push('/getupload');
            }).catch(function(err){
                console.log(err);
                alert("登录请求发生了一些错误……");
            })
        }
    },
    created() {
        document.title = "登录";
    },
}
</script>

<style>
    #errMsg {
        color: #ff0000;
    }
</style>