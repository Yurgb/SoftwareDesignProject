<template>
  <div>

    <div style="margin-bottom: 30px">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>批改实验</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">


<!--      <el-table-column prop="id" label="提交ID" width="80"></el-table-column>-->
      <el-table-column prop="projectid" label="实验ID"></el-table-column>
      <el-table-column prop="studentid" label="提交学生ID"></el-table-column>
      <el-table-column prop="grade" label="得分"></el-table-column>
      <el-table-column label="下载实验报告">
        <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row.file)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column label="打分" width="250" align="center">
        <template slot-scope="scope">
          <el-input-number v-model="scope.row.grade" :precision="2" :step="0.1" :max="max" :min="0"></el-input-number>
          <el-button type="primary" @click="grade1(scope.row)" style="margin-left: 5px">确定</el-button>
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

  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  name: "Grade",
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      projectID: '',
      num:0,
      max:100
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      if(this.$route.params.id!=0)
      {this.projectID=this.$route.params.id}
      request.get("/assignment/page?pageNum="+this.pageNum+"&pageSize=" + this.pageSize +
          "&projectID="+this.projectID).then(
          res => {
            console.log(res)
            this.tableData = res.records;
            this.total = res.total;
          }
      )
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
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
      this.$message.success("上传成功")
      this.load()
    },
    download(url) {
      window.open(url)
    },
    grade1(row){
      request.get("/assignment/setgrade?grade="+row.grade+"&id="+row.id).then(
          res => {
            console.log(res)
            this.$message.success("修改成功")
          }
      )
    }

  }
}
</script>

<style scoped>

</style>