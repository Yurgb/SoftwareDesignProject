<template>
  <div>

    <!-- 每一页的页签   -->
    <div style="margin-bottom: 30px">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>课程</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <!-- 搜索框   -->
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入课程名称" suffix-icon="el-icon-search" v-model="coursename"></el-input>
      <el-button class="ml-5" type="primary" @click="load()">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="resetPage()">重置</el-button>
    </div>

    <div v-if="user.role!=3" style="margin: 10px 0">
      <el-button type="primary" @click="ShowaddFrom()">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="danger" @click="delBatch()">批量删除 <i class="el-icon-remove-outline"></i></el-button>

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
      <el-table-column v-if="user.role!=3" label="教师操作"  width="250" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="edit1(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>

          <el-button type="danger" @click="courseDelete(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
          <el-upload action="http://1.116.163.212:9091/takes/import" :show-file-list="false" accept="xlsx" :on-success="addstudent" style="display: inline-block">
            <el-button type="primary" style="margin-top: 5px">添加学生选课名单   <i class="el-icon-bottom"></i></el-button>
          </el-upload>

        </template>
      </el-table-column>
      <el-table-column label="操作"  width="250" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="tiao(scope.row)">查看实验 <i class="el-icon-view"></i></el-button>
          <el-button type="success" @click="edit(scope.row)">查看教学资料 <i class="el-icon-s-custom"></i></el-button>

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
    <el-dialog title="添加新用户" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="id" :label-width="formLabelWidth">
          <el-input v-model="form.id" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程分数" :label-width="formLabelWidth">
          <el-input v-model="form.score" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="教师编号" :label-width="formLabelWidth">
          <el-input v-model="form.teacherid" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取 消</el-button>
        <el-button type="primary" @click="addcourse()">确 定</el-button>
      </div>
    </el-dialog>




  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  name: "Files",
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
      dialogFormVisible:false,
      form:{
        id:'',
        name:'',
        score:0,
        teacherid:67
      },
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
    edit1(row){
      this.form=row
      this.dialogFormVisible=true

    },
    ShowaddFrom(){
      this.form={}
      this.dialogFormVisible = true
    },
    addstudent(res){
      console.log(res)
      this.$message.success("上传成功")


    },
    courseDelete(id)
    {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.delete("/course/"+id).then(
            res =>{
              console.log(res)
              if(res)
              {
                this.$message({
                  message: '删除成功',
                  type: 'success'
                });
                this.load()

              }
              else
              {
                this.$message.error('删除失败');
              }})

      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });

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
    addcourse(){
      request.post("/course",this.form).then(
          res =>{
            console.log(res)
            if(res)
            {
              this.$message({
                message: '保存成功',
                type: 'success'
              });
              this.load()

            }
            else
            {
              this.$message.error('保存失败');
            }
          }
      )
      this.dialogFormVisible = false
    },
    cancel(){
      this.dialogFormVisible = false
      this.load()
    },
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection=val

    },
    handleFileUploadSuccess(res) {
      console.log(res)
      this.$message.success(res)
    },
    edit(row){
      this.$router.push({
        path: `/FileDownload/${row.id}`
      })

    },
    tiao(row){
      this.$router.push({
        path: `/project/${row.id}`
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      request.post("/course/del/batch", ids).then(res => {

        this.$message.success("批量删除成功")
        this.load()

      })
    }

  }
}
</script>

<style scoped>

</style>