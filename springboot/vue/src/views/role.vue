<template>
  <div>
    <!-- 每一页的页签   -->
    <div style="margin-bottom: 30px">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <!-- 搜索框   -->
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
<!--      <el-input style="width: 200px" placeholder="请输入昵称" suffix-icon="el-icon-message" v-model="nickname" class="ml-5"></el-input>-->
<!--      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" v-model="address" class="ml-5"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load()">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="resetPage()">重置</el-button>
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
      <el-table-column prop="role" label="身份">
      </el-table-column>
      <el-table-column prop="url" label="头像">
      </el-table-column>
      <el-table-column label="操作"  width="200" align="center">
        <template slot-scope="scope">

          <el-button type="success" @click="edit(scope.row)">权限管理 <i class="el-icon-s-custom"></i></el-button>
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
    <el-dialog title="选择用户权限" :visible.sync="dialogFormVisible">
      <el-tree

          :data="checkList"
          show-checkbox
          node-key="id"
          ref="tree"
          >

      </el-tree>


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
        password:"123456",
        nickname: '',
        email: '',
        phone: '',
        address: ''
      },
      formLabelWidth: '120px',
      checkList: [{
        id: 4,
        label: '主页',
      }, {
        id: 5,
        label: '用户管理',
      }, {
        id: 6,
        label: '权限管理',
      },
        {
          id: 22,
          label: '课程',
        },
        {
          id: 33,
          label: '我的提交',
        },
        {
          id:44,
          label: '成绩管理',
        }
        ,{
          id: 55,
          label: '我的成绩',
        }],
      selectId:0

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

    cancel(){
      this.dialogFormVisible = false
      this.load()
    },
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection=val

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
      request.get("/rolemenu/change?roleList="+this.$refs.tree.getCheckedKeys()+"&id="+this.selectId).then(
          res=>{
            console.log(res);
            this.$message({
                    message: '修改成功',
                    type: 'success'
                  })
          }
      )
      console.log(this.$refs.tree.getCheckedKeys());
      this.dialogFormVisible = false
    },
    edit(row){
      this.selectId=row.id
      request.get("rolemenu/selectrole?id="+row.id).then(
          res=>{
            console.log(res)
            this.$refs.tree.setCheckedKeys(res);
          }
      )
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