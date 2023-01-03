<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden"
           background-color="rgb(48, 65, 86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px; right: 5px">
      <b style="color: white" v-show="logoTextShow">实验教学管理系统</b>
    </div>
    <div v-for="item in menus">
      <div v-if="item==4">
        <el-menu-item index="/Course">
          <i class="el-icon-s-home"></i>
          <template slot="title">
            <span slot="title">主页</span>
          </template>
        </el-menu-item>
      </div>
      <div v-else-if="item==5">
        <el-menu-item index="/user">
          <i class="el-icon-menu"></i>
          <template slot="title">
            <span slot="title">用户管理</span>
          </template>
        </el-menu-item>
      </div>
      <div v-else-if="item==6">
        <el-menu-item index="/role">
          <i class="el-icon-s-custom"></i>
          <template slot="title">
            <span slot="title">权限管理</span>
          </template>
        </el-menu-item>
      </div>

      <div v-else-if="item==22">
        <el-menu-item index="/Course">
          <i class="el-icon-notebook-1"></i>
          <template slot="title">
            <span slot="title">课程</span>
          </template>
        </el-menu-item>
      </div>
      <div v-else-if="item==33">
        <el-menu-item index="/myproject">
          <i class="el-icon-document"></i>
          <template slot="title">
            <span slot="title">我的提交</span>
          </template>
        </el-menu-item>
      </div>
      <div v-else-if="item==44">
        <el-menu-item index="/grademanage">
          <i class="el-icon-bangzhu"></i>
          <template slot="title">
            <span slot="title">成绩管理</span>
          </template>
        </el-menu-item>
      </div>
      <div v-else-if="item==55">
        <el-menu-item index="/mygrade">
          <i class="el-icon-s-data"></i>
          <template slot="title">
            <span slot="title">我的成绩</span>
          </template>
        </el-menu-item>
      </div>

    </div>
    <div >
      <el-menu-item index="/FileDownload/0">
        <i class="el-icon-folder-opened"></i>
        <template slot="title">
          <span slot="title">教学资料库</span>
        </template>
      </el-menu-item>
    </div>

<!--    <el-submenu index="/user">-->
<!--      <template slot="title">-->
<!--        <i class="el-icon-menu"></i>-->
<!--        <span slot="title">用户管理</span>-->
<!--      </template>-->
<!--&lt;!&ndash;      <el-menu-item index="/user">&ndash;&gt;-->
<!--&lt;!&ndash;        <i class="el-icon-s-custom"></i>el-submenu&ndash;&gt;-->
<!--&lt;!&ndash;        <span slot="title">用户管理</span>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-menu-item>&ndash;&gt;-->
<!--    </el-submenu>-->
  </el-menu>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
  data(){
    return{
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      menus:[]
    }
  },
  created() {
    request.get("rolemenu/selectrole?id="+this.user.id).then(
        res=>{
          this.menus=res;
          console.log(this.menus)
        }
    )
  },
  methods:{


  }
}
</script>

<style scoped>

</style>