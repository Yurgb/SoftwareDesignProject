<template>
  <div>

    <!-- 每一页的页签   -->
    <div style="margin-bottom: 30px">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>成绩管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <!-- 搜索框   -->
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入课程名称" suffix-icon="el-icon-search" v-model="coursename"></el-input>
      <el-button class="ml-5" type="primary" @click="load()">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="resetPage()">重置</el-button>
    </div>

    <!--主体表单width="200"-->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column prop="id" label="课程ID" width="140">
      </el-table-column>
      <el-table-column prop="name" label="课程名称" width="200">
      </el-table-column>
      <el-table-column prop="score" label="分数" width="140">
      </el-table-column>
      <el-table-column prop="teacherid" label="教师编号" width="140">
      </el-table-column>
      <el-table-column label="设置实验权重(剩下为出席权重)"  width="250" align="center">
        <template slot-scope="scope">
          <el-slider v-model="scope.row.projectper"> </el-slider>
          <el-button type="success" @click="edit(scope.row)">确认</el-button>
      </template>
      </el-table-column>
    </el-table>

    <!--  分页查询  -->
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>





  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  name: "Grademanage",
  data() {

    return {
      tableData: [],
      total: 1,
      coursename:"",
      pageNum:1,
      pageSize:5,
      multipleSelection:[],
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      headerBg: 'headerBg',
      formLabelWidth: '120px',
      selectId:0,
      value:0,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}

    }

  },
  created() {
    this.load()
  },
  methods: {
    collapse() {  // 点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {  // 收缩
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {   // 展开
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    load() {

        request.get("/course/page1?pageNum="+this.pageNum+"&pageSize=" + this.pageSize +
            "&name=" + this.coursename+"&teacherID="+this.user.id).then(
            res => {
              console.log(res)
              this.tableData = res.records;
              this.total = res.total;
            }
        )

    },
    edit(row){
      request.get("/course/setper?courseID="+row.id+"&per="+row.projectper).then(
          this.$message.success("修改成功")
      )

    },
    resetPage(){
      this.coursename=""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    }



  }
}
</script>

<style scoped>

</style>