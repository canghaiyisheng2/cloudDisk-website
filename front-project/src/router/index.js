import Vue from 'vue'
import Router from 'vue-router'
import Login from '../view/Login.vue'
import Index from '../view/index.vue'
import Signup from '../view/Signup.vue'
import Upload from '../view/Upload.vue'
import PersonalCenter from '../view/PersonalCenter.vue'
import Record from '../components/UploadRecord.vue'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/getlogin',
            name: 'Login',
            component:Login
        },
        {
            path: '/getsignup',
            name: 'Signup',
            component:Signup
        },
        {
            path: '/getupload',
            name: 'Upload',
            component:Upload
        },
        {
            path: '/uploadRecord',
            name: 'Record',
            component:Record
        },
        {
            path: '/getPersonalCenter',
            name: 'PersonalCenter',
            component: PersonalCenter,
            children: [
                {
                    path: 'record',
                    component:Record
                },
            ]
        },
        {
            path: '/',
            name: 'Index',
            component:Index
        }
    ]
})