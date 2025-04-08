<template>
    <div>
      <el-form inline class="searchBar">
          <el-form-item label="业主：">
              <el-input size="mini" v-model="searchValue" maxlength="10" placeholder="请输入业主名" style="width:110px"></el-input>
          </el-form-item>
          <el-form-item>
              <el-button size="mini" type="primary" icon="el-icon-search" @click="search(1)">搜索</el-button>
          </el-form-item>
      </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" size="mini" v-loading="loading"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
          <el-table-column prop="id" label="编号" min-width="100"></el-table-column>
          <el-table-column prop="name" label="业主" min-width="120"></el-table-column>
          <el-table-column label="性别">
            <template slot-scope="scope">
                {{scope.row.gender===1?'男':'女'}}
            </template>>
         </el-table-column>
          <el-table-column prop="phoneNumber" label="电话" min-width="120"></el-table-column>
          <el-table-column prop="identification" label="身份证号" min-width="180"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" min-width="80">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">选择</el-button>
              </template>
          </el-table-column>
      </el-table> 
      <!-- 分页 -->
      <Pagination v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
    </div>
  </template>
  
  <script>
  import Pagination from './Pagination.vue'
  export default {
      components: {Pagination},
      name:"SearchOwner",
      data(){
          return {
              searchValue:'',
              tableData:[],
              userInfo:{},
              pageMsg: {
                currentPage: 1,
                pageSize: 8,
                total: 1
              },
              loading:false
          }
      },
      methods:{
          handleEdit(row) {
            this.$emit('onOwnerSeleted',row)
          },
          pageChangeHandle(param) {
            this.search(param.currentPage)
          },
          search(currentPage){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/owner/searchOwnerByName"
            let params = {
                "name":this.searchValue,
                "params":{
                    "currentPage": currentPage,
                }
            }
            this.loading = true
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    if(result.data.list.length == 0){
                        page.$message('搜索结果为空');
                    } else {
                        page.tableData = result.data.list
                        page.pageMsg.total = result.data.total
                        page.pageMsg.pageSize = result.data.pageSize
                        page.pageMsg.currentPage = result.data.pageNum
                    }
                    } else {
                        page.$message.error(result.msg);
                    }
                    page.loading = false
            }).catch(err=>{
                console.log(err);
                page.loading = false
            })
          }
      },
      created(){
      }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  </style>