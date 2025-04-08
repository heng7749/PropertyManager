<template>
  <el-menu class="el-menu-demo" mode="horizontal" background-color="#334157" text-color="#fff" active-text-color="#fff">
    <el-button class="buttonimg" style="background-color: transparent;">
      <img class="showimg" :src="isCollapse?imgsq:imgshow" @click="toggle(isCollapse)">
    </el-button>
    <el-submenu index="2" class="submenu" style="float:right;">
      <template slot="title">超级管理员</template>
      <el-menu-item index="2-2">个人中心</el-menu-item>
      <el-menu-item index="2-3" @click="logout()">退出</el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name:'NavBar',
  data(){
    return {
      isCollapse: true,
      imgshow: require('../assets/img/show.png'),
      imgsq: require('../assets/img/sq.png'),
      user: {}
    }
  },
  methods:{
    toggle(value) {
      this.isCollapse = !value
      this.$bus.$emit('toggle', this.isCollapse)
    },
    logout() {
      let Vue = this
      let token = Vue.$store.getters.GET_TOKEN
      this.$http.get("/heng/user/logout",{
        headers:{token:token}
      }).then(res=>{
        let result = res.data
        if(result.status === 0){
          Vue.$store.dispatch('setToken','')
          Vue.$message({
              message: result.msg,
              type: 'success'
          })
          Vue.$store.commit("RESET_TABS")
          setTimeout(function(){
            Vue.$router.push("/login")
          },"500")
        } else 
          Vue.$message.error(result.msg)
      },error=>{
        console.log('请求失败',error.message)
      })
    }
  }
}
</script>

<style scoped>
  .el-menu-vertical-demo:not(.el-menu--collapse) {
  border: none;
  }
  .submenu {
  float: right;
  }
  .buttonimg {
  height: 60px;
  
  border: none;
  }
  .showimg {
  width: 26px;
  height: 26px;
  position: absolute;
  top: 17px;
  left: 17px;
  }
  .showimg:active {
  border: none;
  }
</style>