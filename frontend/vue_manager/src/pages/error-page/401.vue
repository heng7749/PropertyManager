<template>
  <div class="errPage-container">
    <el-button icon="el-icon-arrow-left" class="pan-back-btn" @click="back">
      返回
    </el-button>
    <el-row>
      <el-col :span="12">
        <h1 class="text-jumbo text-ginormous">
          401错误!
        </h1>
        <h2>你没有权限进入该页面</h2>
      </el-col>
      <el-col :span="12">
        <img :src="errGif" width="313" height="428" alt="Girl has dropped her ice cream.">
      </el-col>
    </el-row>
  </div>
</template>

<script>
import errGif from '@/assets/401.gif'

export default {
  name: 'Page401',
  data() {
    return {
      errGif: errGif + '?' + +new Date(),
      dialogVisible: false
    }
  },
  methods: {
    back() {
      
    }
  }
}
</script>

<style scoped>
  .errPage-container {
    width: 800px;
    max-width: 100%;
    margin: 100px auto;
  }

  .errPage-container .pan-back-btn {
    background: #008489;
    color: #fff;
    border: none!important;
  }
  .errPage-container .pan-gif {
    margin: 0 auto;
    display: block;
  }
  .errPage-container .pan-img {
    display: block;
    margin: 0 auto;
    width: 100%;
  }
  .errPage-container .text-jumbo {
    font-size: 60px;
    font-weight: 700;
    color: #484848;
  }
  .errPage-container .list-unstyled {
    font-size: 14px;

  }
  .errPage-container .list-unstyled li {
    padding-bottom: 5px;
  }
  .errPage-container .list-unstyled a {
    color: #008489;
    text-decoration: none;
  }
  .errPage-container .list-unstyled a:hover{
    text-decoration: underline;
  }
</style>
