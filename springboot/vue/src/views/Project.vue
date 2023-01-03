<template>
  <div>
    <div style="margin-bottom: 30px">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>实验详情</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div v-if="user.role!=3" style="margin: 10px 0">
      <el-button type="primary" @click="ShowaddFrom()">新增 <i class="el-icon-circle-plus-outline"></i></el-button>

      <el-button type="danger" @click="delBatch()">批量删除 <i class="el-icon-remove-outline"></i></el-button>

    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">

      <el-table-column type="selection" width="55"></el-table-column>
<!--      <el-table-column prop="id" label="ID" width="80"></el-table-column>-->
      <el-table-column prop="name" label="实验名称"></el-table-column>
      <el-table-column prop="content" label="实验描述"></el-table-column>
      <el-table-column prop="score" label="实验分数"></el-table-column>
      <el-table-column prop="time" label="截止时间"></el-table-column>
      <el-table-column v-if="user.role==3" label="提交实验" width="150">
        <template slot-scope="scope">
        <el-upload :action="'http://1.116.163.212:9091/assignment/upload?projectID='+scope.row.id+'&studentID='+user.id+'&courseID='+courseID" :show-file-list="false"
                   :on-success="handleFileUploadSuccess" style="display: inline-block">
                   <el-button type="primary" >上传实验报告<i class="el-icon-top"></i></el-button>
        </el-upload>
        </template>
      </el-table-column>
      <!--      <el-table-column label="启用">-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc"-->
      <!--                     @change="changeEnable(scope.row)"></el-switch>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column v-if="user.role!=3" label="操作" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="edit2(scope.row)">批改实验报告 <i class="el-icon-edit"></i></el-button>
          <el-button type="success" @click="edit1(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>

    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>


    <!--   新增实验的隐藏表单     -->
    <el-dialog title="添加新实验" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="id" :label-width="formLabelWidth">
          <el-input v-model="form.id" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="实验名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="实验分数" :label-width="formLabelWidth">
          <el-input v-model="form.score" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="实验描述" :label-width="formLabelWidth">
          <el-input v-model="form.content" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="截止时间" :label-width="formLabelWidth">
<!--          <el-input v-model="form.time" autocomplete="off"></el-input>-->
          <el-date-picker
              v-model="form.time"
              type="datetime"
              placeholder="选择日期时间">
          </el-date-picker>
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
  name: "Project",
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      courseID: -1,
      dialogFormVisible:false,
      formLabelWidth: '120px',
      form:{
        id:'',
        name:'',
        score:0,
        courseid:1,
        content:'',
        time:''
      },
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}

    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.courseID=this.$route.params.id
      request.get("/project/page?pageNum="+this.pageNum+"&pageSize=" + this.pageSize +
          "&name=" + this.name+"&courseID="+this.courseID).then(
          res => {
            console.log(res)
            this.tableData = res.records;
            this.total = res.total;
          }
      )
    },
    // changeEnable(row) {
    //   request.post("/project/update", row).then(res => {
    //
    //     this.$message.success("操作成功")
    //
    //   })
    // },
    del(id) {
      request.delete("/project/" + id).then(res => {

        this.$message.success("删除成功")
        this.load()

      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      request.post("/project/del/batch", ids).then(res => {

        this.$message.success("批量删除成功")
        this.load()

      })
    },
    reset() {
      this.name = ""
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
    handleFileUploadSuccess(res) {
      console.log(res)
      if(res==true)
      {this.$message.success("上传成功")
      this.load()}
      else{
        this.$message.error("实验已经截止")
      }
    },
    download(url) {
      window.open(url)
    },
    cancel(){
      this.dialogFormVisible = false
      this.load()
    },
    addcourse(){
      this.form.courseid=this.$route.params.id
      request.post("/project",this.form).then(
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
    edit1(row){
      this.form=row
      this.dialogFormVisible=true

    },
    edit2(row){
      this.$router.push({
        path: `/grade/${row.id}`
      })

    },
    ShowaddFrom(){
      this.form={}
      this.dialogFormVisible = true
    }

  }
}
</script>

<style scoped>

</style>