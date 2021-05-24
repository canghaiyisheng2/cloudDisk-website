
<template>
    <div :class="{container:isFullScreen}" :style="{width:activeWidth, marginTop:'0'}">
        <table class="table table-hover">
            <thead>
            <tr>
                <th>name</th>
                <th>date</th>
                <th>uploader</th>
                <th>msg</th>
            </tr>
            </thead>
            <tbody id="table-body">
            <tr v-for="(item, index) in items" :key="index">
                <td><a :href="'/CloudDisk/downloads?para=' + item.no">{{item.name}}</a></td>
                <td>{{item.date}}</td>
                <td>{{item.usr}}</td>
                <td>{{item.msg}}</td>
            </tr>
            <tr v-for="n in blankItemLen" :key="n">
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            </tbody>
        </table>
        <div id="pagination" class="text-center">
            <ul class="pagination">
                <li><a href="#" v-on:click="curPage==1?curPage=1:curPage--;">&laquo;</a></li>
                <li v-for="n in totalPage" :key="n" :class="{active:curPage==n}"><a href="#" v-on:click="curPage=n;">{{n}}</a></li>
                <li><a href="#" v-on:click="curPage==totalPage?curPage==totalPage:curPage++;">&raquo;</a></li>
            </ul>
        </div>
    </div>
</template>


<script>
export default{
    data() {
        return {
            isFullScreen:false,
            activeWidth:"inherit",
            items:[],
            curPage:1,
            totalPage:0,
            pageSize:10
        }
    },
    computed: {
        blankItemLen:function(){
            return this.pageSize - this.items.length;
        }
    },
    methods: {
        getFileList: function(){
            var vue = this;
            this.axios({
                url:'/CloudDisk/getFileList',
                method:'get',
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
                params: {
                    p:this.curPage-1,
                    size:this.pageSize
                },
            }).then((res)=>{
                var data = res.data;
                if(data.status=="fail")
                    alert(data.msg);
                else{
                    var fileList = JSON.parse(data.msg);
                    vue.items = fileList.content;
                    vue.curPage = fileList.curPage + 1;
                    vue.totalPage = fileList.totalPage;
                }

            }).catch((err)=>{
                console.log(err);
            })
        }
    },
    watch: {
        curPage:function(val, oldVal){
            this.getFileList();
        }
    },
    created() {
        this.getFileList();
        if(this.$router.history.current.fullPath == "/uploadRecord"){
            this.activeWidth = "";
            this.isFullScreen = true;
            this.pageSize=12
        }
    },
}
</script>

<style>
    tr {
        height:40px;
    }
    tr > th {
        text-align: center;
    }
</style>