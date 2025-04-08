<template>
  <el-pagination class="page-box" @current-change="handleCurrentChange" 
    background :current-page="childMsg.currentPage"
        :page-size="childMsg.pageSize" layout="total, prev, pager, next, jumper" :total="childMsg.total">
  </el-pagination>
</template>
<script>
export default {
  name: 'Pagination',
  props: ['childMsg'],
  data() {
    return {
      pageparm: {
        currentPage: this.childMsg.currentPage,
        pageSize: this.childMsg.pageSize
      }
    }
  },
  created() {},
  methods: {
    handleCurrentChange(val) {
      /**
       * 子传父
       * 参数1 父元素方法
       * 参数2 数据
       */
      this.pageparm.currentPage = val
      this.$emit('pageChange', this.pageparm)
    }
  }
}
</script>

<style>
.page-box {
  margin: 10px auto;
}
</style>
