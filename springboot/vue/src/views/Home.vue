<template>
  <div>

    <!-- 每一页的页签   -->
    <div style="margin-bottom: 30px">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>首页</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <h2>这是主页</h2>
  </div>

</template>

<script>
export default {
  name: "Home"
}
</script>

<style scoped>

</style>