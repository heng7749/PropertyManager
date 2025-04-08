<template>
  <div style="padding:16px;">
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
    <el-table stripe border :data="tableData" tooltip-effect="dark" style="width: 100%" size="small"
            :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}" @selection-change="handleSelectionChange">
        <el-table-column prop="id" label="员工号" width="100"></el-table-column>
        <el-table-column prop="name" label="姓名" width="120"></el-table-column> 
        <el-table-column label="性别">
            <template slot-scope="scope">
                {{scope.row.gender===1?'男':'女'}}
            </template>>
        </el-table-column>
        <el-table-column prop="department" label="部门" width="100"></el-table-column>
        <el-table-column prop="position" label="职位" width="120"></el-table-column>
        <el-table-column prop="phoneNumber" label="联系电话" width="120"></el-table-column>
        <el-table-column prop="email" label="联系邮箱" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="birthday" label="出生年月日" width="100"></el-table-column>
        <el-table-column prop="identification" label="身份证号" width="150"></el-table-column>
        <el-table-column prop="address" label="住址" width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="salary" label="工资" width="100"></el-table-column>
        <!-- 操作 -->
        <el-table-column label="操作" fixed="right" width="150">
            <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleEdit(scope.row)">修改</el-button>
                <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-button @click="addEmployeeVisible=true" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;float: left;">添加</el-button>
    <!-- 分页 -->
    <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>

    <!-- 修改员工信息 -->
    <el-dialog title="员工信息" :visible.sync="userInfoVisible" width="40%" center style="position: absolute;" top="2vh">
        <el-form ref="editEmployeeForm" :model="editEmployeeForm" label-width="100px" size="mini">
            <el-form-item label="姓名">
                <el-input v-model="editEmployeeForm.name" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="性别">
                <el-radio-group v-model="editEmployeeForm.gender">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="0">女</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="部门">
                <el-select v-model="editEmployeeForm.departmentId" placeholder="请选择部门">
                    <el-option
                        v-for="item in departments"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="职位">
                <el-input v-model="editEmployeeForm.position" maxlength="20"></el-input>
            </el-form-item>
            <el-form-item label="联系电话">
                <el-input v-model="editEmployeeForm.phoneNumber" maxlength="20" type="tel"></el-input>
            </el-form-item>
            <el-form-item label="联系邮箱">
                <el-input v-model="editEmployeeForm.email" maxlength="50" type="email"></el-input>
            </el-form-item>
            <el-form-item label="出生年月日">
                <el-date-picker type="date" placeholder="选择日期" value-format="yyyy-MM-dd" v-model="editEmployeeForm.birthday" style="width: 100%;"></el-date-picker>
            </el-form-item>
            <el-form-item label="住址">
                <el-input v-model="editEmployeeForm.address"></el-input>
            </el-form-item>
            <el-form-item label="身份证号">
                <el-input v-model="addEmployeeForm.identification" maxlength="18"></el-input>
            </el-form-item>
            <el-form-item label="工资">
                <el-input v-model="editEmployeeForm.salary" type="number"></el-input>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="editEmployeeMsg">保存</el-button>
                <el-button @click="userInfoVisible=false">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>

    <!-- 添加员工 -->
    <el-dialog title="添加员工" :visible.sync="addEmployeeVisible" width="40%" center style="position: absolute;" top="2vh">
        <el-form ref="addEmployeeForm" :model="addEmployeeForm" label-width="100px" size="mini">
            <el-form-item label="姓名">
                <el-input v-model="addEmployeeForm.name" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="性别">
                <el-radio-group v-model="addEmployeeForm.gender">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="0">女</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="部门">
                <el-select v-model="addEmployeeForm.departmentId" placeholder="请选择部门">
                    <el-option
                        v-for="item in departments"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="职位">
                <el-input v-model="addEmployeeForm.position" maxlength="20"></el-input>
            </el-form-item>
            <el-form-item label="联系电话">
                <el-input v-model="addEmployeeForm.phoneNumber" maxlength="20" type="tel"></el-input>
            </el-form-item>
            <el-form-item label="联系邮箱">
                <el-input v-model="addEmployeeForm.email" maxlength="50" type="email"></el-input>
            </el-form-item>
            <el-form-item label="出生年月日">
                <el-date-picker value-format="yyyy-MM-dd" type="date" placeholder="选择日期" v-model="addEmployeeForm.birthday" style="width: 100%;"></el-date-picker>
            </el-form-item>
            <el-form-item label="身份证号">
                <el-input v-model="addEmployeeForm.identification" maxlength="18"></el-input>
            </el-form-item>
            <el-form-item label="住址">
                <el-input v-model="addEmployeeForm.address" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="工资">
                <el-input v-model="addEmployeeForm.salary" maxlength="10" type="number"></el-input>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="addEmployeeMsg">添加</el-button>
                <el-button @click="addEmployeeVisible=false">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '../../components/Pagination.vue'
export default {
    components: {Pagination},
    name:"EmployeeMsg",
    data(){
        return {
            searchType: 1,
            searchValue:'',
            departments:'',
            addEmployeeForm:{
                name:'',
                gender:'',
                departmentId:'',
                position:'',
                phoneNumber:'',
                email:'',
                birthday:'',
                identification:'',
                address:'',
                salary:''
            },
            editEmployeeForm:{
                id:'',
                name:'',
                gender:'',
                departmentId:'',
                position:'',
                phoneNumber:'',
                email:'',
                birthday:'',
                identification:'',
                address:'',
                salary:''
            },
            pageMsg: {
                currentPage: 1,
                pageSize: 10,
                total: 10
            },
            tableData:[],
            userInfoVisible:false,
            userInfo:{},
            departmentId:'',
            addEmployeeVisible:false
        }
    },
    methods:{
        selectSearchType(command){
            this.searchType = Number(command)
        },
        handleEdit(row) {
            this.editEmployeeForm.id = row.id
            this.editEmployeeForm.name = row.name
            this.editEmployeeForm.gender = row.gender
            this.editEmployeeForm.departmentId = row.departmentId
            this.editEmployeeForm.position = row.position
            this.editEmployeeForm.phoneNumber = row.phoneNumber
            this.editEmployeeForm.email = row.email
            this.editEmployeeForm.birthday = row.birthday
            this.editEmployeeForm.identification = row.identification
            this.editEmployeeForm.address = row.address
            this.editEmployeeForm.salary = row.salary
            this.userInfoVisible = true;
            this.userInfo = row;
        },
        handleDelete(row) {
            this.$confirm('是否删除此员工信息?', '员工信息删除提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteEmployeeMsg(row.id)
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });          
            });
        },
        //换页请求
        pageChangeHandle(param) {
            this.searchEmployee(param.currentPage)
        },
        handleSelectionChange(selections) {
            console.log(selections);
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
        addEmployeeMsg(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/employee/addEmployee'
            let params = {
                'name': this.addEmployeeForm.name,
                'gender': this.addEmployeeForm.gender,
                'departmentId': this.addEmployeeForm.departmentId,
                'position': this.addEmployeeForm.position,
                'phoneNumber': this.addEmployeeForm.phoneNumber,
                'email': this.addEmployeeForm.email,
                'birthday': this.addEmployeeForm.birthday,
                'identification': this.addEmployeeForm.identification,
                'address': this.addEmployeeForm.address,
                'salary': this.addEmployeeForm.salary
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.addEmployeeVisible = false
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
        },
        editEmployeeMsg(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/employee/editEmployee'
            let params = {
                'id': this.editEmployeeForm.id,
                'name': this.editEmployeeForm.name,
                'gender': this.editEmployeeForm.gender,
                'departmentId': this.editEmployeeForm.departmentId,
                'position': this.editEmployeeForm.position,
                'phoneNumber': this.editEmployeeForm.phoneNumber,
                'email': this.editEmployeeForm.email,
                'birthday': this.editEmployeeForm.birthday,
                'identification': this.editEmployeeForm.identification,
                'address': this.editEmployeeForm.address,
                'salary': this.editEmployeeForm.salary
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.userInfoVisible = false
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
        },
        deleteEmployeeMsg(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/employee/deleteEmployee'
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
                    page.searchEmployee(1)
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
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

        //获取表格数据
        this.$http.get("/heng/employee/current",{
            headers:{token:token},
            params:{
                currentPage:page.pageMsg.currentPage
            }
        }).then(res=>{
            let result = res.data
            if(result.status === 0){
                page.tableData = result.data.list
                page.pageMsg.total = result.data.total
                page.pageMsg.pageSize = result.data.pageSize
                page.pageMsg.currentPage = result.data.pageNum
                //ID转化为部门字符串
                page.departmentIdToString()
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