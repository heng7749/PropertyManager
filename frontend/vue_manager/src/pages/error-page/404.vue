<template>
  <div class="container">
    <div class="box404">
      <div class="picture">
        <img class="picture-404" src="@/assets/404.png" alt="404">
      </div>

      <div class="message">
        <div class="message-head">NOT FUND</div>
        <div class="message-info">请确认您输入的网址是否正确, 或者点击下方按钮以回到首页。</div>
        <el-button type="primary" round style="margin-top:20px">返回首页</el-button>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Page404'
}
</script>

<style scoped>
  .message{
    float: right;
    width: 600px;
  }
  .message-head{
    font-size: 32px;
    font-weight: bold;
    line-height: 40px;
    color: #1482f0;
    padding: 10px;
  }
  .message-info{
    font-size: 20px;
    line-height: 24px;
    color: #222;
  }

  .container{
    width: 100%;
    background-color: #fff;
  }
  .box404{
    margin-top: 150px;
    padding: 0 50px;
  }
  .picture{
    float: left;
  }
  .picture .picture-404{
    width: 600px;
    display: inline-block;
  }
</style>
