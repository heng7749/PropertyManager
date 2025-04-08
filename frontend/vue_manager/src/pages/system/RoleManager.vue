<template>
    <div style="padding:16px;">
      <el-row style="width: 100%;" :gutter="6">
        <el-col :span="userViewHidden?24:12">
            <!-- 角色 -->
            <el-card class="box-card" style="margin-top: 8px;">
                <el-form inline class="searchBar">
                    <el-form-item label="角色名称:">
                        <el-input size="small" v-model="searchValue" maxlength="10" placeholder="请输入角色名" style="width:300px"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                    </el-form-item>
                </el-form>
                <el-table border :data="tableData" tooltip-effect="dark" size="small" v-loading="loading"
                    :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}" @row-click="handleSelect">
                <el-table-column width="30">
                    <template slot-scope="scope">
                        <el-radio :label="scope.row.id" v-model="radioId">
                            <span></span>
                        </el-radio>
                    </template>
                </el-table-column>
                <el-table-column prop="id" label="编号"></el-table-column>
                <el-table-column prop="code" label="角色编码"></el-table-column>
                <el-table-column prop="name" label="角色名称" show-overflow-tooltip></el-table-column>
                <el-table-column prop="createTime" label="创建时间"></el-table-column>
                <el-table-column prop="remark" label="描述" show-overflow-tooltip></el-table-column>
                <!-- 操作 -->
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-link :underline="false" type="success" @click="handleAuthorization(scope.$index, scope.row)">授权</el-link>
                        <span style="margin: 0 6px;color: #e8e8e8;">|</span>
                        <el-link :underline="false" type="primary" @click="handleEdit(scope.row)">编辑</el-link>
                        <span style="margin: 0 6px;color: #e8e8e8;">|</span>
                        <el-link :underline="false" type="danger" @click="handleDelete(scope.row)">删除</el-link>
                    </template>
                </el-table-column>
                </el-table> 
                <el-button @click="openAddView" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;margin-left: 10px;">新建</el-button>
                <!-- 分页 -->
                <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
            </el-card>
        </el-col>
        <el-col :hidden="userViewHidden" :span="12">
            <!-- 用户 -->
            <el-card class="box-card" style="margin-top: 8px;">
                <el-button icon="el-icon-circle-close" type="text" @click="radioId=''" style="float: right;margin-top: -18px;margin-right: -6px;font-size: 18px;"></el-button>
                <el-form inline class="searchBar">
                    <el-form-item label="用户账号:">
                        <el-input size="small" v-model="userName" maxlength="10" placeholder="请输入用户账号" style="width:130px"></el-input>
                    </el-form-item>
                    <el-form-item label="类型:">
                        <el-select v-model="userType" placeholder="全部" size="small" style="width:100px">
                            <el-option label="业主" value="0"></el-option>
                            <el-option label="管理者" value="1"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" icon="el-icon-search" @click="searchUsers">搜索</el-button>
                    </el-form-item>
                </el-form>
                <el-table border :data="roleUsers" tooltip-effect="dark" size="small"
                    :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}" @selection-change="handleSelectionChange">
                <el-table-column prop="name" label="用户账号"></el-table-column>
                <el-table-column label="用户名称">
                    <template slot-scope="scope">
                        {{scope.row.type==0? scope.row.owner.name: scope.row.employeeVo.name}}
                    </template>>
                </el-table-column>
                <el-table-column label="类型">
                    <template slot-scope="scope">
                        {{scope.row.type==0?'业主':'管理者'}}
                    </template>>
                </el-table-column>
                <!-- 操作 -->
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-link :underline="false" type="primary" @click="unbindRole(scope.row)">取消关联</el-link>
                    </template>
                </el-table-column>
                </el-table> 
                <el-button @click="openSearchUserView" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;margin-left: 10px;">关联</el-button>
                <!-- 分页 -->
                <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle2"></Pagination>
            </el-card>
        </el-col>
      </el-row>
        <!-- 角色信息 -->
        <el-dialog title="角色信息" :visible.sync="roleInfoVisible" width="60%" center>
            <el-form ref="editForm" :model="editForm" label-width="90px" size="mini">
                <el-form-item label="角色编码:" required>
                    <el-input v-model="editForm.code" style="width:80%"></el-input>
                </el-form-item>
                <el-form-item label="角色名称:" required>
                    <el-input v-model="editForm.name" style="width:80%"></el-input>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input
                            type="textarea"
                            placeholder="请输入内容"
                            v-model="editForm.remark"
                            maxlength="30"
                            show-word-limit
                            style="width:80%;line-height: 1.5">
                    </el-input>
                </el-form-item>
            </el-form>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" size="small" @click="editRole">提交</el-button>
                <el-button size="small" @click="roleInfoVisible=false">取消</el-button>
            </div>
        </el-dialog>

        <!-- 新建角色 -->
        <el-dialog title="新增" :visible.sync="addViewVisible" width="40%" center>
            <el-form ref="addForm" :model="addForm" label-width="90px" size="mini">
                <el-form-item label="角色编码:" required>
                    <el-input v-model="addForm.code" style="width:80%"></el-input>
                </el-form-item>
                <el-form-item label="角色名称:" required>
                    <el-input v-model="addForm.name" style="width:80%"></el-input>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input
                            type="textarea"
                            placeholder="请输入内容"
                            v-model="addForm.remark"
                            maxlength="30"
                            show-word-limit
                            style="width:80%;line-height: 1.5">
                    </el-input>
                </el-form-item>
            </el-form>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" size="small" @click="addRole">提交</el-button>
                <el-button size="small" @click="addViewVisible=false">取消</el-button>
            </div>
        </el-dialog>

        <!-- 授权 -->
        <el-drawer title="角色权限配置" :visible.sync="drawer">
            <el-container style="height: 100%;">
                <el-header height="10px" style="border-top: 1px solid #e8e8e8;padding-top: 6px;">
                    <span>所拥有权限:</span>
                </el-header>
                <el-main>
                    <el-tree
                        :data="perms"
                        show-checkbox
                        node-key="id"
                        :default-expanded-keys="[2, 3]"
                        :default-checked-keys="[5]"
                        :props="defaultProps">
                    </el-tree>
                </el-main>
                <el-footer height="50px" style="border-top: 1px solid #e8e8e8;padding: 10px 16px;">
                    <el-button type="primary" size="small" style="float: right;">保存</el-button>
                </el-footer>
            </el-container>
        </el-drawer>

        <!-- 关联 -->
        <el-dialog title="关联用户" :visible.sync="searchUserVisible" width="60%" center>
            <SearchUser @onUsersSeleted="bindUsersToRole"></SearchUser>
        </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  import SearchUser from '../../components/SearchUser.vue'
  export default {
      components: {Pagination,SearchUser},
      name:"RoleManager",
      data(){
          return {
              searchValue:'',
              tableData:[],
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              roleInfoVisible:false,
              addViewVisible:false,
            radioId:'',
            drawer:false,
            defaultProps: {
                children: 'children',
                label: 'name'
            },
            searchUserVisible:false,
            textarea:'',
            type:'',
            roleId:'',
            roleUsers:[],
            userName:'',
            userType:'',
            addForm:{
                name:'',
                code:'',
                remark:''
            },
            editForm:{
                id:'',
                name:'',
                code:'',
                remark:''
            },
            data: [{
                    id: 1,
                    label: '系统数据'
                }, {
                    id: 2,
                    label: '员工管理',
                    children: [{
                        id: 5,
                        label: '员工信息'
                    }, {
                        id: 6,
                        label: '值班管理'
                    }]
                }, {
                    id: 3,
                    label: '房屋管理',
                    children: [{
                        id: 7,
                        label: '房屋信息'
                    }, {
                        id: 8,
                        label: '业主信息'
                    }]
                }, {
                    id: 4,
                    label: '业主缴费管理',
                    children: [{
                        id: 7,
                        label: '缴费信息'
                    }, {
                        id: 8,
                        label: '添加缴费'
                    }]
                }, {
                    id: 4,
                    label: '停车管理',
                    children: [{
                        id: 7,
                        label: '车位信息'
                    }, {
                        id: 8,
                        label: '停车信息'
                    }]
                }, {
                    id: 4,
                    label: '设备管理',
                    children: [{
                        id: 7,
                        label: '设备信息'
                    }, {
                        id: 8,
                        label: '设备检修'
                    }]
                }, {
                    id: 4,
                    label: '报修管理',
                    children: [{
                        id: 7,
                        label: '报修信息'
                    }, {
                        id: 8,
                        label: '报修记录'
                    }]
                }, {
                    id: 4,
                    label: '采购管理',
                    children: [{
                        id: 7,
                        label: '采购信息'
                    }, {
                        id: 8,
                        label: '申请记录'
                    }]
                }, {
                    id: 4,
                    label: '业主反馈管理',
                    children: [{
                        id: 7,
                        label: '投诉信息'
                    }, {
                        id: 8,
                        label: '投诉记录'
                    },{
                        id: 7,
                        label: '建议信息'
                    }, {
                        id: 8,
                        label: '建议记录'
                    }]
                }, {
                    id: 4,
                    label: '公告管理',
                    children: [{
                        id: 7,
                        label: '公告信息'
                    }, {
                        id: 8,
                        label: '所有公告'
                    }]
                }, {
                    id: 4,
                    label: '系统管理',
                    children: [{
                        id: 7,
                        label: '角色管理'
                    }, {
                        id: 8,
                        label: '权限管理'
                    }]
                }],
            perms:[]
          }
      },
      computed:{
        userViewHidden(){
            return this.radioId == ''
        }
      },
      methods:{
          handleAuthorization(index, row) {
                this.drawer = true;
                console.log(index, row);
          },
          handleSelect(row) {
            this.radioId = row.id
            this.roleId = row.id
            this.searchRoleUsers(1,this.roleId,'','')
          },
          handleEdit(row) {
              this.roleInfoVisible = true;
              Object.assign(this.editForm,row);
          },
          handleDelete(row) {
              this.$confirm('是否删除此角色信息?', '删除角色提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                this.deleteRole(row.id)
              }).catch(() => {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  });          
              });
          },
          pageChangeHandle(param) {
            let currentPage = param.currentPage
            let searchValue = this.searchValue
            this.searchRoles(currentPage,searchValue)
          },
          pageChangeHandle2(param) {
            let currentPage = param.currentPage
            let roleId = this.roleId
            let userName = this.userName
            let userType = this.userType
            this.searchRoleUsers(currentPage,roleId,userName,userType)
          },
          handleSelectionChange(selections) {
              console.log(selections);
              if(selections.length!==0){
                  this.delSelectItemsBtnShow = true
              }else {
                  this.delSelectItemsBtnShow = false
              }
          },
          openAddView() {
              this.addViewVisible = true;
          },
          openSearchUserView() {
              this.searchUserVisible = true;
          },
          searchRoles(currentPage,name){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/user/searchRoles"
            let params = {
                "name": name,
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
          searchRoleUsers(currentPage,roleId,userName,userType){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/user/searchRoleUsers"
            let params = {
                "id": roleId,
                "userName": userName,
                "userType": userType,
                "params":{
                    "currentPage": currentPage,
                }
            }
            // this.loading = true
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.roleUsers = result.data.list
                } else {
                    page.$message.error(result.msg);
                }
                // page.loading = false
            }).catch(err=>{
                console.log(err);
                // page.loading = false
            })
          },
          deleteRole(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/role/deleteRole'
            let params = {
                'id': id
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.radioId = ''
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          addRole(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/role/addRole"
            let params = {
                "name": this.addForm.name,
                "code": this.addForm.code,
                "remark": this.addForm.remark,
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '添加成功',
                        type: 'success'
                    });
                    page.addViewVisible = false
                    page.radioId = ''
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          editRole(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/role/editRole"
            let params = {
                "id": this.editForm.id,
                "name": this.editForm.name,
                "code": this.editForm.code,
                "remark": this.editForm.remark
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '修改成功',
                        type: 'success'
                    });
                    page.roleInfoVisible = false
                    page.radioId = ''
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          search(){
            let currentPage = this.pageMsg.currentPage
            let searchValue = this.searchValue
            this.searchRoles(currentPage,searchValue)
          },
          bindUsersToRole(selections){
            this.searchUserVisible = false
            let usersId = []
            selections.forEach(element => {
                usersId.push(element.id)
            });

            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/role/bindUsersToRole"
            let params = new URLSearchParams();
            params.append('roleId', this.roleId);
            params.append('usersId', usersId);
            this.$http.post(url, params, {headers:{'token':token,'Content-Type':'application/x-www-form-urlencoded '}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '操作成功',
                        type: 'success'
                    });
                    page.roleInfoVisible = false
                    page.radioId = ''
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          unbindRole(row){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/role/unbindRole"
            let params = new URLSearchParams();
            params.append('id', row.surId);
            this.$http.post(url, params, {headers:{'token':token,'Content-Type':'application/x-www-form-urlencoded '}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '操作成功',
                        type: 'success'
                    });
                    page.roleInfoVisible = false
                    page.radioId = ''
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          searchUsers(){
            let currentPage = this.pageMsg.currentPage
            let roleId = this.roleId
            let userName = this.userName
            let userType = this.userType
            this.searchRoleUsers(currentPage,roleId,userName,userType)
          }
      },
      created(){
        this.searchRoles(1,'')

        let page = this
        let token = this.$store.getters.GET_TOKEN
        //获取授权列表信息
        this.$http.get("/heng/role/permsList",{
            headers:{token:token}
        }).then(res=>{
            let result = res.data
            if(result.status === 0){
                page.perms = result.data
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
  .el-dropdown-link {
        cursor: pointer;
        color: #409EFF;
        font-size: 14px;
        padding: 0 12px 0 0;
    }
    .dropdownTag{
    width: 110px;
    height: 40px;
    text-align: right;
    line-height: 40px;
}
  </style>