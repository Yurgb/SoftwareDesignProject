import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Manage.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: () => import('../views/Manage.vue'),//主界面
    redirect: "/login",//将/ 重定义为/home
    children: [//子路由展示主要界面，保证边框不变
      { path: 'home', name: '首页', component: () => import('../views/Course')},
      { path: 'user', name: '用户管理', component: () => import('../views/User.vue')},
      {path: 'person', name: '个人信息', component: () => import('../views/person')},
      {path: 'role', name: '权限管理', component: () => import('../views/role')},
      {path: 'Course', name: '课程管理', component: () => import('../views/Course')},
      {path: 'FileDownload/:id', name: '教学资料下载', component: () => import('../views/FileDownload')},
      {path: 'FileDownload/0', name: '教学资料下载', component: () => import('../views/FileDownload')},
      {path: 'project/:id', name: '查看实验', component: () => import('../views/Project')},
      {path: 'grade/:id', name: '批改实验', component: () => import('../views/Grade')},
      {path: 'myproject', name: '提交记录', component: () => import('../views/Myproject')},
      {path: 'grademanage', name: '成绩管理', component: () => import('../views/Grademanage')},
      {path: 'mygrade', name: '我的成绩', component: () => import('../views/Mygrade')}
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  }
]

const router = new VueRouter({
  //mode: 'history',必须注释，否则Vue部署之后页面刷新会404，不能采用history模式
  base: process.env.BASE_URL,
  routes
})

export default router
