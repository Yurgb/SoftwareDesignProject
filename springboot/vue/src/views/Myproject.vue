<template>
  <div>
    <!-- 每一页的页签   -->
    <div style="margin-bottom: 30px">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>我的提交</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">
<!--      <el-table-column prop="id" label="ID" width="80"></el-table-column>-->
      <el-table-column prop="projectid" label="实验id"></el-table-column>
      <el-table-column prop="grade" label="我的实验分数"></el-table-column>
      <el-table-column prop="time" label="提交时间"></el-table-column>
      <el-table-column label="下载实验报告" width="150">
        <template slot-scope="scope">
              <el-button type="primary" @click="download(scope.row.file)">下载</el-button>
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
  name: "Myproject",
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      request.get("/assignment/page2?pageNum="+this.pageNum+"&pageSize=" + this.pageSize +
          "&studentID="+this.user.id).then(
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
    download(url) {
      window.open(url)
    }

  }
}
</script>

<style scoped>

</style>