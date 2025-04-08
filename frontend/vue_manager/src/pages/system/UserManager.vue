<template>
    <div style="padding:16px;">
      <el-row style="width: 100%;" :gutter="6">
        <el-col :span="16">
            <el-card class="box-card" style="margin-top: 8px;">
                <el-form inline class="searchBar">
                    <el-form-item label="用户账号:">
                        <el-input size="small" v-model="searchValue" maxlength="10" placeholder="请输入账号名" style="width:110px"></el-input>
                    </el-form-item>
            
                    <el-form-item label="类型：">
                        <el-select v-model="type" placeholder="全部" size="small" style="width:100px">
                            <el-option label="业主" value="0"></el-option>
                            <el-option label="管理者" value="1"></el-option>
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
                <el-table stripe border :data="tableData" tooltip-effect="dark" size="small" v-loading="loading"
                        :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}" @row-click="handleSelect" style="width: 850px;">
                    <el-table-column width="30">
                        <template slot-scope="scope">
                            <el-radio :label="scope.row.id" v-model="radioId">
                                <span></span>
                            </el-radio>
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="用户账号" width="120"></el-table-column>
                    <el-table-column prop="name" label="用户名称" width="120"></el-table-column>
                    <el-table-column label="类型" width="100">
                        <template slot-scope="scope">
                            {{scope.row.type==0?'业主':'管理者'}}
                        </template>>
                    </el-table-column>
                    <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
                    <!-- 操作 -->
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-link :underline="false" type="primary" @click="handleEdit(scope.row)">编辑</el-link>
                            <span style="margin: 0 6px;color: #e8e8e8;">|</span>
                            <el-link :underline="false" type="danger" @click="handleDelete(scope.row)">删除</el-link>
                        </template>
                    </el-table-column>
                </el-table> 
                <el-button @click="openAddView" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;margin-left: 10px;">添加</el-button>
                <!-- 分页 -->
                <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
            </el-card>
        </el-col>
        <el-col :span="8">
            <el-card class="box-card" style="margin-top: 8px;">
                <div slot="header" class="clearfix">
                    <span>用户详细信息</span>
                </div>
                <div :hidden="userMsgEmptyHidden">
                    <el-empty description="用户信息为空"></el-empty>
                </div>
                <div :hidden="!userMsgEmptyHidden">
                    <el-descriptions :column="1" title="账户信息">
                        <el-descriptions-item label="用户账号">{{userInfos.name}}</el-descriptions-item>
                        <el-descriptions-item label="用户名称">{{userInfos.owner != null ? userInfos.owner.name: userInfos.employeeVo.name}} </el-descriptions-item>
                        <el-descriptions-item label="类型">{{userInfos.type==0?'业主':'管理者'}}</el-descriptions-item>
                        <el-descriptions-item label="创建时间">{{userInfos.createTime}}</el-descriptions-item>
                    </el-descriptions>
                    <el-descriptions :column="1" title="关联业主信息" v-if="userInfos.owner != null">
                        <el-descriptions-item label="业主编号">{{userInfos.owner.id}}</el-descriptions-item>
                        <el-descriptions-item label="姓名">{{userInfos.owner.name}}</el-descriptions-item>
                        <el-descriptions-item label="性别">{{userInfos.owner.gender===1?'男':'女'}}</el-descriptions-item>
                        <el-descriptions-item label="联系电话">{{userInfos.owner.phoneNumber}}</el-descriptions-item>
                        <el-descriptions-item label="联系邮箱">{{userInfos.owner.email}}</el-descriptions-item>
                        <el-descriptions-item label="出生年月日">{{userInfos.owner.birthday}}</el-descriptions-item>
                        <el-descriptions-item label="身份证号码">{{userInfos.owner.identification}}</el-descriptions-item>
                    </el-descriptions>
                    <el-descriptions :column="1" title="关联员工信息" v-if="userInfos.employeeVo != null">
                        <el-descriptions-item label="员工号">{{userInfos.employeeVo.id}}</el-descriptions-item>
                        <el-descriptions-item label="姓名">{{userInfos.employeeVo.name}}</el-descriptions-item>
                        <el-descriptions-item label="性别">{{userInfos.employeeVo.gender===1?'男':'女'}}</el-descriptions-item>
                        <el-descriptions-item label="部门">{{userInfos.employeeVo.department}}</el-descriptions-item>
                        <el-descriptions-item label="职位">{{userInfos.employeeVo.position}}</el-descriptions-item>
                        <el-descriptions-item label="联系电话">{{userInfos.employeeVo.phoneNumber}}</el-descriptions-item>
                        <el-descriptions-item label="联系邮箱">{{userInfos.employeeVo.email}}</el-descriptions-item>
                        <el-descriptions-item label="出生年月日">{{userInfos.employeeVo.birthday}}</el-descriptions-item>
                        <el-descriptions-item label="身份证号码">{{userInfos.employeeVo.identification}}</el-descriptions-item>
                        <el-descriptions-item label="住址">{{userInfos.employeeVo.address}}</el-descriptions-item>
                        <el-descriptions-item label="工资">{{userInfos.employeeVo.salary}}</el-descriptions-item>
                    </el-descriptions>
                </div>
            </el-card>
        </el-col>
      </el-row>

      <!-- 添加用户 -->
      <el-drawer title="添加用户" :visible.sync="addViewVisible" size="45%">
            <el-container style="height: 100%;" direction="vertical">
                <el-form ref="addForm" :rules="rules" :model="addForm" label-width="90px" size="mini">
                    <el-main style="border-top: 1px solid #e8e8e8;width: 100%;">
                        <el-form-item label="用户账号:" required prop="name">
                            <el-input v-model="addForm.name" style="width:90%"></el-input>
                        </el-form-item>
                        <el-form-item label="登录密码:" required prop="password">
                            <el-input v-model="addForm.password" style="width:90%" type="password" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="确认密码:" required prop="comfirmPassword">
                            <el-input v-model="addForm.comfirmPassword" style="width:90%" type="password" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="用户类型" required prop="type">
                            <el-radio-group v-model="addForm.type">
                                <el-radio label="0">业主</el-radio>
                                <el-radio label="1">物业员工</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-descriptions :column="2" title="关联信息" v-if="addForm.type!=null">
                            <template slot="extra">
                                <el-button @click="selectInfo(0)" type="primary" size="small">关联用户信息</el-button>
                            </template>
                            <template v-if="addForm.type == 0">
                                <el-descriptions-item label="业主编号">{{seletedOwnerInfo.id}}</el-descriptions-item>
                                <el-descriptions-item label="姓名">{{seletedOwnerInfo.name}}</el-descriptions-item>
                                <el-descriptions-item label="性别">{{seletedOwnerInfo===1?'男':'女'}}</el-descriptions-item>
                                <el-descriptions-item label="联系电话">{{seletedOwnerInfo.phoneNumber}}</el-descriptions-item>
                                <el-descriptions-item label="联系邮箱">{{seletedOwnerInfo.email}}</el-descriptions-item>
                                <el-descriptions-item label="出生年月日">{{seletedOwnerInfo.birthday}}</el-descriptions-item>
                                <el-descriptions-item label="身份证号码">{{seletedOwnerInfo.identification}}</el-descriptions-item>
                            </template>
                            <template v-if="addForm.type == 1">
                                <el-descriptions-item label="员工号">{{seletedEmployeeInfo.id}}</el-descriptions-item>
                                <el-descriptions-item label="姓名">{{seletedEmployeeInfo.name}}</el-descriptions-item>
                                <el-descriptions-item label="性别">{{seletedEmployeeInfo.gender===1?'男':'女'}}</el-descriptions-item>
                                <el-descriptions-item label="部门">{{seletedEmployeeInfo.department}}</el-descriptions-item>
                                <el-descriptions-item label="职位">{{seletedEmployeeInfo.position}}</el-descriptions-item>
                                <el-descriptions-item label="联系电话">{{seletedEmployeeInfo.phoneNumber}}</el-descriptions-item>
                                <el-descriptions-item label="联系邮箱">{{seletedEmployeeInfo.email}}</el-descriptions-item>
                                <el-descriptions-item label="出生年月日">{{seletedEmployeeInfo.birthday}}</el-descriptions-item>
                                <el-descriptions-item label="身份证号码">{{seletedEmployeeInfo.identification}}</el-descriptions-item>
                                <el-descriptions-item label="住址">{{seletedEmployeeInfo.address}}</el-descriptions-item>
                                <el-descriptions-item label="工资">{{seletedEmployeeInfo.salary}}</el-descriptions-item>
                            </template>
                        </el-descriptions>
                    </el-main>
                    <el-footer height="50px" style="border-top: 1px solid #e8e8e8;padding: 10px 16px;">
                        <el-button type="primary" size="small" style="float: right;" @click="userRegister('addForm')">提交</el-button>
                        <el-button size="small" style="float: right;margin-right: 6px;" @click="addViewVisible=false">取消</el-button>
                    </el-footer>
                </el-form>
            </el-container>
      </el-drawer>
      <!-- 编辑用户 -->
      <el-drawer title="编辑用户" :visible.sync="editViewVisible" size="45%">
            <el-container style="height: 100%;" direction="vertical">
                <el-form ref="editForm" :rules="editRules" :model="editForm" label-width="90px" size="mini">
                    <el-main style="border-top: 1px solid #e8e8e8;width: 100%;">
                        <el-form-item label="用户账号:" prop="name">
                            {{ editForm.name }}
                        </el-form-item>
                        <el-form-item label="更改密码:" prop="name">
                            <el-switch
                                v-model="editForm.changePassword"
                                @change="passwordChange"
                                active-color="#13ce66"
                                inactive-color="#ff4949">
                            </el-switch>
                        </el-form-item>
                        <el-form-item v-if="editForm.changePassword" label="登录密码:" required prop="password">
                            <el-input v-model="editForm.password" style="width:90%" type="password" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item v-if="editForm.changePassword" label="确认密码:" required prop="comfirmPassword">
                            <el-input v-model="editForm.comfirmPassword" style="width:90%" type="password" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="用户类型" prop="type">
                            <el-radio-group v-model="editForm.type" @input="editRadioChange">
                                <el-radio :label="0">业主</el-radio>
                                <el-radio :label="1">物业员工</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-descriptions :column="2" title="关联信息" v-if="editForm.type!=null">
                            <template slot="extra">
                                <el-button @click="selectInfo(1)" type="primary" size="small">关联用户信息</el-button>
                            </template>
                            <template v-if="editForm.type == 0">
                                <el-descriptions-item label="业主编号">{{seletedOwnerInfo.id}}</el-descriptions-item>
                                <el-descriptions-item label="姓名">{{seletedOwnerInfo.name}}</el-descriptions-item>
                                <el-descriptions-item label="性别">{{seletedOwnerInfo===1?'男':'女'}}</el-descriptions-item>
                                <el-descriptions-item label="联系电话">{{seletedOwnerInfo.phoneNumber}}</el-descriptions-item>
                                <el-descriptions-item label="联系邮箱">{{seletedOwnerInfo.email}}</el-descriptions-item>
                                <el-descriptions-item label="出生年月日">{{seletedOwnerInfo.birthday}}</el-descriptions-item>
                                <el-descriptions-item label="身份证号码">{{seletedOwnerInfo.identification}}</el-descriptions-item>
                            </template>
                            <template v-if="editForm.type == 1">
                                <el-descriptions-item label="员工号">{{seletedEmployeeInfo.id}}</el-descriptions-item>
                                <el-descriptions-item label="姓名">{{seletedEmployeeInfo.name}}</el-descriptions-item>
                                <el-descriptions-item label="性别">{{seletedEmployeeInfo.gender===1?'男':'女'}}</el-descriptions-item>
                                <el-descriptions-item label="部门">{{seletedEmployeeInfo.department}}</el-descriptions-item>
                                <el-descriptions-item label="职位">{{seletedEmployeeInfo.position}}</el-descriptions-item>
                                <el-descriptions-item label="联系电话">{{seletedEmployeeInfo.phoneNumber}}</el-descriptions-item>
                                <el-descriptions-item label="联系邮箱">{{seletedEmployeeInfo.email}}</el-descriptions-item>
                                <el-descriptions-item label="出生年月日">{{seletedEmployeeInfo.birthday}}</el-descriptions-item>
                                <el-descriptions-item label="身份证号码">{{seletedEmployeeInfo.identification}}</el-descriptions-item>
                                <el-descriptions-item label="住址">{{seletedEmployeeInfo.address}}</el-descriptions-item>
                                <el-descriptions-item label="工资">{{seletedEmployeeInfo.salary}}</el-descriptions-item>
                            </template>
                        </el-descriptions>
                    </el-main>
                    <el-footer height="50px" style="border-top: 1px solid #e8e8e8;padding: 10px 16px;">
                        <el-button type="primary" size="small" style="float: right;" @click="editUserMsg('editForm')">提交</el-button>
                        <el-button size="small" style="float: right;margin-right: 6px;" @click="editViewVisible=false">取消</el-button>
                    </el-footer>
                </el-form>
            </el-container>
      </el-drawer>
      <el-dialog
            width="55%"
            title="员工查询"
            :visible.sync="searchEmployeeVisible"
            append-to-body top="4vh">
            <SearchEmployee @onEmployeeSeleted="setEmployeeInfo"></SearchEmployee>
      </el-dialog>
      <el-dialog
        width="60%"
        title="业主查询"
        :visible.sync="searchOwnerVisible"
        append-to-body top="4vh">
        <SearchOwner @onOwnerSeleted="setOwnerInfo"></SearchOwner>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  import SearchEmployee from '../../components/SearchEmployee.vue'
  import SearchOwner from '../../components/SearchOwner.vue'
  export default {
      components: {Pagination,SearchEmployee,SearchOwner},
      name:"UserManager",
      data(){
          return {
              searchValue:'',
              departments:[],
              form:'',
              pageMsg: {
                  currentPage: 1,
                  pageSize: 10,
                  total: 10
              },
              tableData:[],
              userInfo:{},
              radioId:'',
              addViewVisible:false,
              loading:false,
              type:'',
              departmentId:'',
              userInfos:{
                "searchValue": null,
                "createBy": null,
                "createTime": null,
                "updateBy": null,
                "updateTime": null,
                "remark": null,
                "params": null,
                "id": null,
                "name": null,
                "password": null,
                "type": null,
                "bindId": null,
                "avatar": null,
                "owner": {
                    "searchValue": null,
                    "createBy": null,
                    "createTime": null,
                    "updateBy": null,
                    "updateTime": null,
                    "remark": null,
                    "params": null,
                    "id": null,
                    "name": null,
                    "gender": null,
                    "phoneNumber": null,
                    "email": null,
                    "birthday": null,
                    "identification": null
                },
                "employeeVo": {
                    "searchValue": null,
                    "createBy": null,
                    "createTime": null,
                    "updateBy": null,
                    "updateTime": null,
                    "remark": null,
                    "params": null,
                    "id": null,
                    "name": null,
                    "gender": null,
                    "departmentId": null,
                    "position": null,
                    "phoneNumber": null,
                    "email": null,
                    "birthday": null,
                    "identification": null,
                    "address": null,
                    "salary": null,
                    "department": null
                },
                "departmentId": null
              },
              addForm:{
                name:'',
                password:'',
                comfirmPassword:'',
                type:null,
                bindId:''
              },
              rules:{
                name: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 3, max: 25, message: '长度在 3 到 25 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, max: 25, message: '长度在 6 到 25 个字符', trigger: 'blur' }
                ],
                comfirmPassword: [
                    {validator:(rule, value, callback) => {
                        if (value === '') {
                            callback(new Error('请再次输入密码'));
                        } else if (value !== this.addForm.password) {
                            callback(new Error('两次输入密码不一致!'));
                        } else {
                            callback();
                        }
                    }},
                    { required: true, message: '请输入确认密码', trigger: 'blur' },
                    { min: 6, max: 25, message: '长度在 6 到 25 个字符', trigger: 'blur' }
                ],
                type: [
                    { required: true, message: '请选择用户类型', trigger: 'change' }
                ]
              },
              seletedEmployeeInfo:{},
              searchEmployeeVisible:false,
              searchOwnerVisible:false,
              seletedOwnerInfo:{},
              editViewVisible:false,
              editForm:{
                name:'',
                password:'',
                comfirmPassword:'',
                type:null,
                bindId:'',
                changePassword:false
              },
              editRules:{
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, max: 25, message: '长度在 6 到 25 个字符', trigger: 'blur' }
                ],
                comfirmPassword: [
                    {validator:(rule, value, callback) => {
                        if (value === '') {
                            callback(new Error('请再次输入密码'));
                        } else if (value !== this.editForm.password) {
                            callback(new Error('两次输入密码不一致!'));
                        } else {
                            callback();
                        }
                    }},
                    { required: true, message: '请输入确认密码', trigger: 'blur' },
                    { min: 6, max: 25, message: '长度在 6 到 25 个字符', trigger: 'blur' }
                ]
              }
          }
      },
      computed:{
        userMsgEmptyHidden() {
            return this.radioId !== ''
        }
      },
      methods:{
          selectSearchType(command){
              this.searchType = Number(command)
          },
          handleSelect(row) {
            this.radioId = row.id
            this.userInfos = row
          },
          pageChangeHandle(param) {
              console.log(param)
          },
          openAddView() {
              this.addViewVisible = true;
          },
          searchUsers(currentPage,name,type,departmentId){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/user/searchUsers"
            let params = {
                "name": name,
                "type": type,
                "departmentId": departmentId,
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
            let name = this.searchValue
            let type = this.type
            let departmentId = this.departmentId
            this.searchUsers(currentPage,name,type,departmentId)
          },
          userRegister(formName){
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    let page = this
                    let token = this.$store.getters.GET_TOKEN
                    let url = '/heng/user/addUser'
                    let params = {
                        'name': this.addForm.name,
                        'password': this.addForm.password,
                        'type': this.addForm.type,
                        'bindId': this.addForm.bindId
                    }
                    this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                        let result = res.data
                        if(result.status === 0){
                            page.$message({
                                message: result.msg,
                                type: 'success'
                            });
                            page.addViewVisible = false
                            page.search()
                            page.addForm = {}
                        } else {
                            page.$message.error(result.msg);
                        }
                    }).catch(err=>{
                        console.log(err);
                    })
                } else {
                    console.log('error submit!!');
                    return false;
                }
            })
          },
          setEmployeeInfo(employee){
            this.seletedEmployeeInfo = employee
            this.searchEmployeeVisible = false
            this.addForm.bindId = employee.id
            this.editForm.bindId = employee.id
          },
          setOwnerInfo(owner){
            this.seletedOwnerInfo = owner
            this.searchOwnerVisible = false
            this.addForm.bindId = owner.id
            this.editForm.bindId = owner.id
          },
          selectInfo(tag){
            if(tag == 0){
                if(this.addForm.type == 0){
                    this.searchOwnerVisible = true
                } else if(this.addForm.type == 1){
                    this.searchEmployeeVisible = true
                }
            }else{
                if(this.editForm.type == 0){
                    this.searchOwnerVisible = true
                } else if(this.editForm.type == 1){
                    this.searchEmployeeVisible = true
                }
            }
          },
          handleDelete(row){
            this.$confirm('是否删除此用户信息?', '删除用户提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                this.deleteUser(row.id)
              }).catch(() => {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  });          
              });
          },
          deleteUser(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/user/deleteUser'
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
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          handleEdit(row) {
              this.editViewVisible = true;
              Object.assign(this.editForm,row);
              this.seletedOwnerInfo = {}
              this.seletedEmployeeInfo = {}
              if(this.editForm.type == 0){
                this.seletedOwnerInfo = this.userInfos.owner
              }else{
                this.seletedEmployeeInfo = this.userInfos.employeeVo
              }
          },
          editRadioChange(value){
            this.seletedOwnerInfo = {}
            this.seletedEmployeeInfo = {}
            return value
          },
          editUserMsg(formName){
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    let page = this
                    let token = this.$store.getters.GET_TOKEN
                    let url = '/heng/user/editUser'
                    let params = {
                        'id': this.editForm.id,
                        'password': this.editForm.password,
                        'type': this.editForm.type,
                        'bindId': this.editForm.bindId
                    }
                    this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                        let result = res.data
                        if(result.status === 0){
                            page.$message({
                                message: result.msg,
                                type: 'success'
                            });
                            page.editViewVisible = false
                            page.search()
                            page.radioId = ''
                        } else {
                            page.$message.error(result.msg);
                        }
                    }).catch(err=>{
                        console.log(err);
                    })
                } else {
                    console.log('error submit!!');
                    return false;
                }
            })
          },
          passwordChange(value){
            if(!value){
                this.editForm.password = null
                this.editForm.comfirmPassword = null
            }
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
        //获取用户信息  
        this.searchUsers(1,'','','')
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