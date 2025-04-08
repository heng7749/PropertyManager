<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
          <el-form-item label="缴费类型：">
              <el-select v-model="billTypeId" placeholder="全部" size="small" style="width:100px">
                <el-option
                    v-for="item in billTypes"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
          </el-form-item>
        
          <el-form-item label="状态：">
              <el-select v-model="status" placeholder="全部" size="small" style="width:90px">
                <el-option :value="0" label="未缴费"></el-option>
                <el-option :value="1" label="已缴费"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          </el-form-item>
      </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" style="width: 100%" size="small" v-loading="loading"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
          <el-table-column prop="id" label="单号" min-width="100"></el-table-column>
          <el-table-column prop="address" label="住址" min-width="160"></el-table-column>
          <el-table-column prop="billTypeName" label="类型" min-width="120"></el-table-column>
          <el-table-column prop="count" label="数值" min-width="120"></el-table-column>
          <el-table-column prop="money" label="金额" min-width="120"></el-table-column>
          <el-table-column label="状态" min-width="100">
            <template slot-scope="scope">
                {{statusName[scope.row.status]}}
            </template>>
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间" min-width="160"></el-table-column>
          <el-table-column prop="endTime" label="结束时间" min-width="160"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" fixed="right" min-width="80">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">查看</el-button>
              </template>
          </el-table-column>
      </el-table> 
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
    <!-- 缴费信息 -->
    <el-dialog title="缴费信息" :visible.sync="billInfoVisible" width="40%" center style="position: absolute;" top="10vh">
        <el-descriptions class="margin-top" title="" :column="2">
            <el-descriptions-item label="单号">{{ editForm.id }}</el-descriptions-item>
            <el-descriptions-item label="状态">{{ statusName[editForm.status] }}</el-descriptions-item>
            <el-descriptions-item label="类型">{{ editForm.billTypeName }}</el-descriptions-item>
            <el-descriptions-item label="住址">{{ editForm.address }}</el-descriptions-item>
            <el-descriptions-item label="数值">{{ editForm.count }}</el-descriptions-item>
            <el-descriptions-item label="上次抄表日期">{{ editForm.startTime }}</el-descriptions-item>
            <el-descriptions-item label="金额">{{ editForm.money }}</el-descriptions-item>
            <el-descriptions-item label="本次抄表日期">{{ editForm.endTime }}</el-descriptions-item>
        </el-descriptions>
    </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  export default {
      components: {Pagination},
      name:"BillRecords",
      data(){
          return {
              tableData:[],
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              editForm:{
                id:'',
                ownerName:'',
                address:'',
                phoneNumber:'',
                count:'',
                endTime:'',
                status:''
              },
              loading:false,
              billType:'',
              billTypeId:'',
              statusName:['未缴费','已缴费'],
              billInfoVisible:false,
              billTypes:[],
              status:''
          }
      },
      methods:{
          handleEdit(row) {
              this.billInfoVisible = true;
              this.editForm = row;
          },
          pageChangeHandle(param) {
            let currentPage = param.currentPage
            let billTypeId = this.billTypeId
            let status = this.status
            this.searchBill(currentPage,billTypeId,status)
          },
          searchBill(currentPage,billTypeId,status){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/bill/myBills"
            let params = {
                "billTypeId":billTypeId,
                "status":status,
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
          },
          search(){
            let currentPage = this.pageMsg.currentPage
            let billTypeId = this.billTypeId
            let status = this.status
            this.searchBill(currentPage,billTypeId,status)
          }
      },
      created(){
        let page = this
        let token = this.$store.getters.GET_TOKEN
        //获取缴费类型信息
        this.$http.get("/heng/common/billTypes",{
            headers:{token:token}
        }).then(res=>{
            let result = res.data
            if(result.status === 0){
                page.billTypes = result.data
            } else {
                page.$message.error(result.msg)
            }
        },err=>{
            page.$message.error(err.msg)
        })
        //获取缴费信息  
        this.searchBill(1,'','')
      }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  </style>