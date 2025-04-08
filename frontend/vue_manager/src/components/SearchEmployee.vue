<template>
    <div>
      <el-form inline class="searchBar">
          <el-dropdown @command="selectSearchType" class="dropdownTag">
              <span class="el-dropdown-link">
                  {{searchType==1?'员工名模式':'工号模式'}}<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command='1'>员工名</el-dropdown-item>
                  <el-dropdown-item command='2'>工号</el-dropdown-item>
              </el-dropdown-menu>
          </el-dropdown>
  
          <el-form-item>
              <el-input size="small" v-model="searchValue" maxlength="10" :placeholder="searchType==1?'输入员工名':'输入工号'" style="width:110px"></el-input>
          </el-form-item>
  
          <el-form-item label="部门：">
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
              <el-button size="small" type="primary" icon="el-icon-search" @click="searchEmployee(1)">搜索</el-button>
          </el-form-item>
      </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" size="mini"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
          <el-table-column prop="id" label="员工号" min-width="100"></el-table-column>
          <el-table-column prop="name" label="姓名" min-width="120"></el-table-column>
          <el-table-column prop="department" label="部门" min-width="100"></el-table-column>
          <el-table-column prop="position" label="职位" min-width="120"></el-table-column>
          <el-table-column prop="phoneNumber" label="联系电话" min-width="120"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" min-width="80">
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
      name:"SearchEmployee",
      data(){
          return {
              searchType: 1,
              searchValue:'',
              departments:[],
              departmentId:'',
              form:'',
              pageMsg: {
                  currentPage: 1,
                  pageSize: 0,
                  total: 0
              },
              tableData:[],
              userInfo:{}
          }
      },
      methods:{
          selectSearchType(command){
              this.searchType = Number(command)
          },
          handleSelect(row) {
              this.$emit('onEmployeeSeleted',row)
          },
          pageChangeHandle(param) {
            this.searchEmployee(param.currentPage)
          },
        departmentIdToString(){
            for(let i=0;i<this.tableData.length;i++) {
                for (let j=0;j<this.departments.length;j++) {
                    if (this.tableData[i].departmentId === this.departments[j].id) {
                        this.tableData[i].department = this.departments[j].name
                        break;
                    }
                }
            }
        },
        searchEmployee(currentPage){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/employee/searchEmployee'
            let params = {
                'currentPage': currentPage,
                'searchType': this.searchType,
                'searchValue': this.searchValue,
                'departmentId': this.departmentId
            }

            this.$http.get(url,{
                headers: {token:token},
                params: params
            }).then(function(res){
              let result = res.data
              if(result.status === 0){
                if(result.data.list.length == 0){
                    page.$message('搜索结果为空');
                } else {
                    page.tableData = result.data.list
                    page.pageMsg.total = result.data.total
                    page.pageMsg.pageSize = result.data.pageSize
                    page.pageMsg.currentPage = result.data.pageNum
                    //ID转化为部门字符串
                    page.departmentIdToString()
                }
              } else {
                page.$message.error(result.msg);
              }
            }).catch(err=>{
              console.log(err);
              alert("搜索失败")
            })
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