<template>
    <div>
      <el-form inline class="searchBar">
          <el-form-item label="用户账号:">
              <el-input size="small" v-model="searchValue" maxlength="10" placeholder="输入用户账号" style="width:110px" @click="search"></el-input>
          </el-form-item>
  
          <el-form-item label="类型：">
              <el-select v-model="type" placeholder="请选择用户类型" size="small" style="width:140px">
                <el-option label="业主" value="0"></el-option>
                <el-option label="物业员工" value="1"></el-option>
              </el-select>
          </el-form-item>
          
          <el-form-item label="部门：" v-show="type==1">
              <el-select v-model="departmentId" placeholder="全部" size="small" style="width:100px">
                <el-option
                    v-for="item in departments"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
          </el-form-item>
          <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          </el-form-item>
      </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" size="mini"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}" @selection-change="handleSelectionChange" >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="name" label="用户账号" min-width="100"></el-table-column>
          <el-table-column label="用户名称" min-width="120">
            <template slot-scope="scope">
                {{scope.row.type===0?scope.row.owner.name:scope.row.employeeVo.name}}
            </template>>
          </el-table-column>
          <el-table-column label="类型" min-width="100">
            <template slot-scope="scope">
                {{scope.row.type===0?'业主':'物业员工'}}
            </template>>
          </el-table-column>
          <el-table-column label="部门" min-width="100">
            <template slot-scope="scope">
                {{scope.row.type===0?'无':scope.row.employeeVo.department}}
            </template>>
          </el-table-column>
          <el-table-column label="联系电话" min-width="120">
            <template slot-scope="scope">
                {{scope.row.type===0?scope.row.owner.phoneNumber:scope.row.employeeVo.phoneNumber}}
            </template>>
          </el-table-column>
          <el-table-column label="邮箱" min-width="120" show-overflow-tooltip>
            <template slot-scope="scope">
                {{scope.row.type===0?scope.row.owner.email:scope.row.employeeVo.email}}
            </template>>
          </el-table-column>
      </el-table> 
      <!-- 分页 -->
      <Pagination v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
      
      <div style="border-top: 1px solid #e8e8e8;padding-top: 6px;height: 20px;">
        <el-button type="primary" size="small" style="float: right;" @click="comfirm">确定</el-button>
      </div>
    </div>
  </template>
  
  <script>
  import Pagination from './Pagination.vue'
  export default {
      components: {Pagination},
      name:"SearchUser",
      data(){
          return {
              searchValue:'',
              type:'',
              departmentId:'',
              departments:[],
              pageMsg: {
                  currentPage: 1,
                  pageSize: 10,
                  total: 10
              },
              tableData:[],
              userInfo:{},
              selections:[]
          }
      },
      methods:{
          pageChangeHandle(param) {
            let currentPage = param.currentPage
            let name = this.searchValue
            let type = this.type
            let departmentId = this.departmentId
            if(type==0){
                departmentId = ''
            }
            this.searchUser(currentPage,name,type,departmentId)
          },
          handleSelectionChange(selections) {
            this.selections = selections
          },
          search(){
            let currentPage = this.pageMsg.currentPage
            let name = this.searchValue
            let type = this.type
            let departmentId = this.departmentId
            if(type==0){
                departmentId = ''
            }
            this.searchUser(currentPage,name,type,departmentId)
          },
          searchUser(currentPage,name,type,departmentId){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/user/searchUsers"
            let params = {
                'name': name,
                'type':type,
                'departmentId':departmentId,
                'params':{
                    'currentPage':currentPage
                }
            }
            page.loading = true
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
          comfirm(){
            this.$emit('onUsersSeleted',this.selections)
          }
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
  .dropdownTag{
      width: 100px;
      height: 40px;
      text-align: right;
      line-height: 40px;
  }
  .el-dropdown-link {
      cursor: pointer;
      color: #409EFF;
      font-size: 14px;
      padding: 0 12px 0 0;
  }
  .el-icon-arrow-down {
      font-size: 12px;
  }
  </style>