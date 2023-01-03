<template>
  <div>
  <el-card style="width: 500px;">
    <el-form label-width="80px" size="small">
      <el-upload
          class="avatar-uploader"
          action=" http://1.116.163.212:9091/file/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
      >
        <img v-if="user.url" :src="user.url" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>

      <el-form-item label="用户名">
        <el-input v-model="user.username" disabled autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="user.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="user.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="user.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="user.address" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="头像">
        <el-input v-model="user.url" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="return1()">确 定</el-button>
        <el-button type="primary" @click="explode()" style="margin-left: 240px">重置密码</el-button>
      </el-form-item>
    </el-form>
  </el-card>


  <!--   重置密码的隐藏表单     -->
  <el-dialog title="重置密码" :visible.sync="dialogFormVisible" style="width: 1100px">
    <el-form :model="kk">
      <el-form-item label="邮箱" :label-width="formLabelWidth">
        <el-input v-model="kk.email" autocomplete="off" style="width: 316px"></el-input>
      </el-form-item>
      <el-form-item label="验证码" :label-width="formLabelWidth">
        <el-input v-model="kk.code" autocomplete="off" style="width: 220px"></el-input>
        <el-button type="primary" class="ml-5" :disabled="disabled" @click="send">{{valiBtn}}</el-button>
      </el-form-item>
      <el-form-item label="新密码" :label-width="formLabelWidth">
        <el-input v-model="kk.password" autocomplete="off" style="width: 316px"></el-input>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel()">取 消</el-button>
      <el-button type="primary" @click="reset()">确 定</el-button>
    </div>
  </el-dialog>
  </div>
</template>


<script>
import request from "@/utils/request";
export default {
  name: "Person",
  data() {
    return {
      from:{},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      dialogFormVisible: false,
      kk: {
        password:'',
        email: '',
        code:''
      },
      formLabelWidth: '120px',
      disabled:false,
      valiBtn:'获取验证码',
    }

  },
  methods:{
    return1(){
      this.save();
    },
    tackBtn(){       //验证码倒数60秒
      let time = 60;
      let timer = setInterval(() => {
        if(time == 0){
          clearInterval(timer);
          this.valiBtn = '获取验证码';
          this.disabled = false;
        }else{
          this.disabled = true;
          this.valiBtn = time + '秒后重试';
          time--;
        }
      }, 1000);
    },
    send(){
      let email=this.kk.email;
      if(!email) {
        this.$message.warning("请输入邮箱账号")
        return
      }
      if(!/^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/.test(email)) {
        this.$message.warning("请输入正确的邮箱账号")
        return
      }
      // 发送邮箱验证码
      request.get("/reset/email/" + email ).then(res => {

         console.log(res)

      })
      this.$message.success("发送成功")
      this.tackBtn();   //验证码倒数60秒
    },
    explode(){
      this.dialogFormVisible = true
      this.load()
    },
    reset(){

      let email =this.kk.email
      let password=this.kk.password
      let code=this.kk.code
      let id=this.user.id
      if(!email) {
        this.$message.warning("请输入邮箱账号")
        return
      }
      if(!password) {
        this.$message.warning("请输入新的密码")
        return
      }
      request.post("/reset/resetPassword?email="+email+"&password="+password+"&code="+code+"&id="+id).then(
          res=>{
            console.log(res)
               if(res==false)
                 this.$message.warning("验证码不正确，重置失败")
               else
               {
                 this.$message.success("重置成功")
                 this.$router.push("/login")
               }

          }
      )

    },
    cancel(){
      this.dialogFormVisible = false
      this.load()
    },
    handleAvatarSuccess(res) {
      this.user.url = res
      this.$message.success("修改头像成功")

    },
    save(){
      request.post("/user", this.user).then(
          res=>{
            //同步更新信息到浏览器
            localStorage.setItem("user", JSON.stringify(this.user))
            this.$message.success("保存成功");
          }
      )
    }

  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>