<template>
  <div>
    <!-- 每一页的页签   -->
    <div style="margin-bottom: 30px">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
<!-- 搜索框   -->
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-input style="width: 200px" placeholder="请输入昵称" suffix-icon="el-icon-message" v-model="nickname" class="ml-5"></el-input>
      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" v-model="address" class="ml-5"></el-input>
      <el-button class="ml-5" type="primary" @click="load()">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="resetPage()">重置</el-button>
    </div>
<!--管理员操作-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="ShowaddFrom()">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="danger" @click="delBatch()">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      <el-upload action="http://1.116.163.212:9091/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>
<!--主体表单-->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column prop="id" label="ID" width="140">
      </el-table-column>
      <el-table-column prop="username" label="用户名" width="140">
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120">
      </el-table-column>
      <el-table-column prop="phone" label="电话" width="120">
      </el-table-column>
      <el-table-column prop="email" label="邮箱" width="120">
      </el-table-column>
      <el-table-column prop="address" label="地址">
      </el-table-column>
<!--      <span v-if="tableData.role==1">-->
      <el-table-column  prop="role" label="身份">
      </el-table-column>
<!--      </span>-->

<!--      <el-table-column prop="url" label="头像">-->
<!--      </el-table-column>-->
      <el-table-column label="操作"  width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="edit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-button type="danger" @click="userDelete(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
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



    <!--   新增用户的隐藏表单     -->
    <el-dialog title="添加新用户" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="名称" :label-width="formLabelWidth">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="昵称" :label-width="formLabelWidth">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮件" :label-width="formLabelWidth">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" :label-width="formLabelWidth">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址" :label-width="formLabelWidth">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="身份" :label-width="formLabelWidth">
          <el-input v-model="form.role" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取 消</el-button>
        <el-button type="primary" @click="addUser()">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "User",
  data() {

    return {
      tableData: [],
      total: 1,
      username:"",
      nickname:"",
      address:"",
      pageNum:1,
      pageSize:5,
      multipleSelection:[],
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      headerBg: 'headerBg',
      dialogTableVisible: false,
      dialogFormVisible: false,
      form: {
        username:'',
        nickname: '',
        email: '',
        phone: '',
        address: '',
        role:''
      },
      formLabelWidth: '120px'
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
      request.get("/user/page?pageNum="+this.pageNum+"&pageSize=" + this.pageSize +
          "&username=" + this.username+"&nickname="+this.nickname+"&address="+this.address).then(
          res => {
            console.log(res)

            this.tableData = res.records;
            this.total = res.total;
          }
      )

    },
    resetPage(){
      this.username=""
      this.nickname=""
      this.address=""
      this.load()
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    exp(){
      window.open("http://1.116.163.212:9091/user/export")
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
    ShowaddFrom(){
      this.form={}
      this.dialogFormVisible = true
    },
    cancel(){
      this.dialogFormVisible = false
      this.load()
    },
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection=val

    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.$confirm('此操作将永久删除这批用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then
      (() => {
        request.post("/user/del/batch", ids).then(res =>{
          console.log(res)
          if(res) {
            this.$message({
              message: '批量删除成功',
              type: 'success'
            });
            this.load()
          }
          else {
            this.$message.error('批量删除失败');
          }})
      }).catch
      (() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });

    },
    userDelete(id)
    {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.delete("/user/"+id).then(
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
    addUser(){
      request.post("/user",this.form).then(
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
    edit(row){
      this.form=row
      this.dialogFormVisible=true

    }
  }
}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>