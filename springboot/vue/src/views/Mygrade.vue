<template>
  <div>

    <!-- 每一页的页签   -->
    <div style="margin-bottom: 30px">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>我的成绩</el-breadcrumb-item>
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
      <el-table-column label="查看成绩"  width="250" align="center">
        <template slot-scope="scope">
        <el-button type="primary" @click="grade1(scope.row)">点击查看成绩</el-button>
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

    <!--   新增课程的隐藏表单     -->
    <el-dialog title="我的成绩" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="出勤成绩(按占比计算)" :label-width="formLabelWidth">
          <el-input v-model="form.attendence" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="实验成绩(按占比计算)" :label-width="formLabelWidth">
          <el-input v-model="form.project" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程总成绩(满分100)" :label-width="formLabelWidth">
          <el-input v-model="form.totalgrade" disabled autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cancel()">确 定</el-button>
      </div>
    </el-dialog>



  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  name: "Mygrade",
  data() {

    return {
      num:-1,
      form:{
        attendence: 0,
        project:0,
        totalgrade:0
      },
      tableData: 0,
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
      dialogFormVisible:false,
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

      if(this.user.role!=3){
        request.get("/course/page1?pageNum="+this.pageNum+"&pageSize=" + this.pageSize +
            "&name=" + this.coursename+"&teacherID="+this.user.id).then(
            res => {
              console.log(res)
              this.tableData = res.records;
              this.total = res.total;
            }
        )}
      else
      {
        request.get("/course/page2?pageNum="+this.pageNum+"&pageSize=" + this.pageSize +
            "&name=" + this.coursename+"&studentID="+this.user.id).then(
            res => {
              console.log(res)
              this.tableData = res.records;
              this.total = res.total;
            }
        )
      }

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
    },
    grade1(row){

      this.form.totalgrade=0
      request.get("/project/getattdence?courseID="+row.id+"&studentID="+this.user.id).then(
          res=>{
            this.form.attendence=res;
            if(res!='')
            {this.form.totalgrade=this.form.totalgrade+res;}
          }
      )
      request.get("/project/getproject?courseID="+row.id+"&studentID="+this.user.id).then(
          res=>{
            this.form.project=res;
            if(res!='')
            {this.form.totalgrade=this.form.totalgrade+res;}
          }
      )
     // this.form.totalgrade=this.form.attendence+this.form.project;
      this.dialogFormVisible = true;
    },
    cancel(){
      this.dialogFormVisible = false

      this.load()
    }



  }
}
</script>

<style scoped>

</style>