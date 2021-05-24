<template>
    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4 form">
                <div class="form-group">
                    <label for="filepath" class="control-label">上传文件(点击下图选择): </label>
                    <div id="labels">
                        <div class="label label-primary" id="fileName">{{fileName}}</div>
                        <div class="label label-danger" v-if="!isUpload">未上传</div>
                        <div class="label label-success" v-if="isUpload">已上传</div>
                    </div>
                    <img :src="`${publicPath}UploadFile.svg`" alt="" id="uploadImg" v-on:click="select"/>
                    <div class="input-group msg-box">
                        <span class="input-group-addon" id="add-msg">附加信息：</span>
                        <input type="text" id="msg-input" class="form-control" aria-describedby="add-msg" v-model="msg" v-on:keyup.enter="upload"/>
                    </div>
                    <input type="file" id="filepath" style="display: none;" v-on:change="getName"/>
                </div>
                <div class="row">
                    <button class="btn-default btn btn-lg col-md-offset-5 text-center" v-on:click="upload">上传</button>
                    <button class="btn-danger btn btn-xs col-md-offset-3 text-center" v-on:click="withdraw" style="margin-top:20px">撤回</button>
                </div>

            </div>
        </div>
    </div>
</template>

<script>
    export default{
		data() {
			return {
                publicPath: process.env.BASE_URL,
				fileName:'',
				fileDate:'',
				msg:'',
				isUpload:false,
				no:-1
			}
		},
		methods: {
			select:function(){
				this.isUpload = false;
				this.fileDate = '';
				this.no = -1;
				$("#filepath").click();
			},
			getName:function(){
			    var file = $("#filepath").get(0).files[0];
			    if(!file)
			        this.fileName='';
				else
				    this.fileName=file.name;
				console.log();
			},
			upload:function(){
				var file = $("#filepath").get(0).files[0];
				if(!file){
					alert("上传文件不得为空");
				}
				else{
					//使用FormData上传文件
					var formData = new FormData();
					var date = Date()
					this.fileDate = date;
					var vue = this;
					formData.append("file", file);
					formData.append("date", date);
					formData.append("msg",vue.msg);
					this.axios({
						url: 'upload',
						data: formData,
						method: 'post',
						headers: {
							'Content-Type': 'multipart/form-data'
						}
					}).then((res)=>{
						var data = res.data;
						if(data.status == "fail"){
							vue.isUpload = false;
							alert(data.msg);
						}
						else if(data.status == "success"){
							vue.isUpload = true;
                            vue.no = data.msg;
							alert(file.name + "上传成功");
						}
					}).catch((err)=>{
					    if(err.response){
						    if(err.response.status == 501){
						        alert("该功能需要先登录!");
								vue.$router.push({name:'Login'})
						    }
					    }
					    else{
					        console.log('Error', err.message);
					    }
					})
				}
			},
			withdraw:function(){
			    if(this.no == -1){
			        alert("无可撤回");
			        return;
			    }
				var vue = this;
			    this.axios({
						url: 'withdraw',
						params:{
						    para:vue.no
						},
						method: 'post',
						headers: {
							'Content-Type': 'multipart/form-data'
						}
					}).then(()=>{
					    vue.isUpload = false;
						alert("撤回成功");
					}).catch((err)=>{
						alert("请求发生了一些错误……");
					})
			}
		},
		created() {
			document.title = "上传";
		},
	}
</script>

<style>
    .container {
        margin-top: 70px;
    }
</style>