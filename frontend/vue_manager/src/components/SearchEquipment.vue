<template>
    <div>
        <el-form inline class="searchBar" size="mini">
            <el-form-item label="设备名:">
                <el-input size="mini" v-model="searchValue" maxlength="10" placeholder="输入设备名" style="width:110px"></el-input>
            </el-form-item>
            <el-form-item label="所属部门:">
                <el-select v-model="departmentId" placeholder="全部" size="mini" style="width:85px">
                    <el-option
                        v-for="item in departments"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="类别:">
                <el-select v-model="type" placeholder="全部" size="mini" style="width:95px">
                    <el-option label="电力设备" value="1"></el-option>
                    <el-option label="消防设备" value="2"></el-option>
                    <el-option label="普通设备" value="3"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="状态:">
                <el-select v-model="status" placeholder="全部" size="mini" style="width:85px">
                    <el-option label="正常" value="0"></el-option>
                    <el-option label="异常" value="1"></el-option>
                    <el-option label="已报废" value="2"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button size="mini" type="primary" icon="el-icon-search" @click="search"></el-button>
            </el-form-item>
        </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" size="mini" v-loading="loading"
                :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
            <el-table-column prop="id" label="编号" min-width="120"></el-table-column>
            <el-table-column label="类别" min-width="120">
                <template slot-scope="scope">
                    {{typeName[scope.row.type]}}
                </template>
            </el-table-column>
            <el-table-column prop="name" label="设备名" min-width="120" show-overflow-tooltip></el-table-column>
            <el-table-column prop="model" label="型号" min-width="120" show-overflow-tooltip></el-table-column>
            <el-table-column prop="deployPlace" label="应用场所" min-width="120"></el-table-column>
            <el-table-column prop="departmentName" label="所属部门" min-width="120"></el-table-column>
            <el-table-column label="状态" min-width="100">
                <template slot-scope="scope">
                    {{statusName[scope.row.status]}}
                </template>
            </el-table-column>
            <el-table-column prop="deployTime" label="投用时间" min-width="160"></el-table-column>
            <!-- 操作 -->
            <el-table-column label="操作" fixed="right" width="80">
                <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="handleSelect(scope.row)">选择</el-button>
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
      name:"SearchEquipment",
      data(){
          return {
              searchValue:'',
              departments:[],
              departmentId:'',
              form:'',
              pageMsg: {
                  currentPage: 1,
                  pageSize: 1,
                  total: 0
              },
              tableData:[],
              userInfo:{},
              typeName:['','电力设备','消防设备','普通设备'],
              statusName:['正常','异常','已报废'],
              loading:false,
              status:'',
              type:''
          }
      },
      methods:{
          handleSelect(row) {
            this.$emit('onEquipmentSeleted',row)
          },
          pageChangeHandle(param) {
            let currentPage = param.currentPage
            let name = this.searchValue
            let deployPlace = this.deployPlace
            let departmentId = this.departmentId
            let type = this.type
            let deployTime = this.deployTime
            let purchasingTime = this.purchasingTime
            let status = this.status
            this.searchEquipments(currentPage,name,deployPlace,departmentId,type,deployTime,purchasingTime,status)
          },
          searchEquipments(currentPage,name,deployPlace,departmentId,type,deployTime,purchasingTime,status){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/equipment/searchEquipments"
            let params = {
                "name":name,
                "deployPlace": deployPlace,
                "departmentId": departmentId,
                "type": type,
                "deployTime":deployTime,
                "purchasingTime":purchasingTime,
                "status":status,
                "params":{
                    "currentPage": currentPage,
                }
            }
            this.loading = true
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                page.loading = false
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
            }).catch(err=>{
                console.log(err);
                page.loading = false
            })
          },
          search(){
            let currentPage = this.pageMsg.currentPage
            let name = this.searchValue
            let deployPlace = this.deployPlace
            let departmentId = this.departmentId
            let type = this.type
            let deployTime = this.deployTime
            let purchasingTime = this.purchasingTime
            let status = this.status
            this.searchEquipments(currentPage,name,deployPlace,departmentId,type,deployTime,purchasingTime,status)
          },
      },
      created(){
        let page = this
        let token = this.$store.getters.GET_TOKEN
        //获取部门信息
        this.$http.get("/heng/common/departments",{
            headers:{token:token}
        }).then(res=>{
            let result = res.data
            if(result.status === 0){
                page.departments = result.data
            } else {
                page.$message.error(result.msg)
            }
        },err=>{
            page.$message.error(err.msg)
        })

      }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  </style>