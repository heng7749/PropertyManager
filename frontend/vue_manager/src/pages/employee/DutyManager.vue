<template>
    <div style="padding:16px;">
      <div>
        <el-card>
            <!-- 操作栏 -->
            <el-row type="flex" justify="space-between" :gutte="15">
                <el-col :span="5">
                    <!-- 月份选择器 -->
                    <monthpicker @changeMonth="changeMonth"></monthpicker>
                </el-col>
                <el-col :span="5">{{department}}值班表</el-col>
                <el-col :span="5">
                    <el-select v-model="departmentId" @change="onDepartmentChange" placeholder="选择部门" size="small" style="width:100px">
                    <el-option
                        v-for="item in departments"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                </el-select>
                </el-col>
            </el-row>
            <!-- 日历栏 -->
            <el-row class="calendar-con">
                <calendar :time="time" :dutiesData="dutiesData" @onItemClick="editDutyMsg"></calendar>
            </el-row>
        </el-card>
      </div>

      <!-- 员工信息 -->
    <el-dialog :title="dutyDay+' 值班信息'" :visible.sync="dutyInfoVisible" width="70%" center top="10vh">
        <!-- 表格 -->
        <el-table stripe :data="showDuties" tooltip-effect="dark" style="width: 100%" size="mini"
                :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
            <el-table-column prop="id" label="员工号" width="100"></el-table-column>
            <el-table-column label="姓名">
                <template slot-scope="scope">
                    {{scope.row.employee.name}}
                </template>
            </el-table-column>
            <el-table-column prop="department" label="部门" width="120"></el-table-column>
            <el-table-column label="职位">
                <template slot-scope="scope">
                    {{scope.row.employee.position}}
                </template>
            </el-table-column>
            <el-table-column prop="shift" label="班次" width="120"></el-table-column>
            <el-table-column prop="job" label="工作内容" width="190" show-overflow-tooltip></el-table-column>
            <el-table-column label="联系电话">
                <template slot-scope="scope">
                    {{scope.row.employee.phoneNumber}}
                </template>
            </el-table-column>
            <!-- 操作 -->
            <el-table-column label="操作" fixed="right">
                <template slot-scope="scope">
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table> 
        
        <div style="margin-top: 10px;margin-left: auto;margin-right: 20px;width: 150px;">
            <el-button type="primary" size="mini" @click="addDutyViewVisible=true">添加员工</el-button>
            <el-button size="mini" @click="dutyInfoVisible=false">取消</el-button>
        </div>

        <el-dialog
            width="55%"
            title="员工查询"
            :visible.sync="searchEmployeeVisible"
            append-to-body top="4vh">
            <SearchEmployee @onEmployeeSeleted="setEmployeeInfo"></SearchEmployee>
        </el-dialog>
    </el-dialog>

    <!-- 添加员工值班信息 -->
    <el-dialog title="添加值班信息" :visible.sync="addDutyViewVisible" width="40%" center style="position: absolute;">
        <el-form ref="form" :model="form" label-width="100px" size="mini">
            <el-form-item label="部门">
                {{ seletedEmployeeInfo.department }}
            </el-form-item>
            <el-form-item label="员工">
                <div class="name-wrapper">
                    <el-tag size="medium " style="float: left;display: inline;">{{ seletedEmployeeInfo.name }}</el-tag>
                    <el-button type="primary" icon="el-icon-edit" size="mini" circle style="margin-left: 20px;" @click="openSearchEmployeeView"></el-button>
                </div>
            </el-form-item>
            <el-form-item label="班次">
                <el-input v-model="form.shift" maxlength="20"></el-input>
            </el-form-item>
            <el-form-item label="工作内容">
                <el-input v-model="form.job" maxlength="20" type="tel"></el-input>
            </el-form-item>
            <el-form-item label="值班日期">
                {{ dutyDay }}
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addDutyMsg">添加</el-button>
                <el-button @click="addEmployeeVisible=false">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
    </div>
  </template>
  
  <script>
  // 月份选择器
  import Monthpicker from '../../components/Monthpicker.vue'
  // 日历组件
  import Calendar from '../../components/Calendar.vue'
  import SearchEmployee from '../../components/SearchEmployee.vue'
  export default {
      components: { Monthpicker,Calendar,SearchEmployee },
      name:"DutyManager",
      data(){
        return {
            tableData:[],
            time: {},
            departments:[],
            dutyInfoVisible:false,
            searchEmployeeVisible:false,
            addDutyViewVisible:false,
            form:{
                employeeId:'',
                shift:'',
                job:'',
                attendedDate:'',
            },
            seletedEmployeeInfo:{},
            dutyMonth:'',
            departmentId:'',
            dutiesData:{},
            showDuties:{},
            dutyDay:'',
            department:'员工'
        }
      },
      methods:{
        // 修改月份
        changeMonth(time) {
            this.time = time
        },
        // 修改值班信息
        editDutyMsg(date) {
            this.dutyInfoVisible = true
            this.showDuties = this.dutiesData[date]
            this.dutyDay = date
        },
        handleDelete(row) {
            this.$confirm('是否删除此员工信息?', '删除商品提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteDutyMsg(row.id)
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });          
            });
        },
        openSearchEmployeeView(){
            this.searchEmployeeVisible = true;
        },
        searchDuties(dutyMonth,departmentId){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/duty/searchDuties"
            let params = {
                "dutyMonth":dutyMonth,
                "departmentId": departmentId
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                page.dutiesData = res.data.data
            }).catch(err=>{
                console.log(err);
            })
          },
          search(){
            let dutyMonth = this.dutyMonth
            let departmentId = this.departmentId
            this.searchDuties(dutyMonth,departmentId)
          },
          setEmployeeInfo(employee){
            this.seletedEmployeeInfo = employee
            this.searchEmployeeVisible = false
            this.form.employeeId = employee.id
            this.form.attendedDate = this.dutyDay
          },
          addDutyMsg(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/duty/addDuty'
            let params = {
                'employeeId': this.form.employeeId,
                'shift': this.form.shift,
                'job': this.form.job,
                'attendedDate': this.form.attendedDate,
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.searchDutyByAttendedDate()
                    page.addDutyViewVisible = false
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
        deleteDutyMsg(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/duty/deleteDuty'
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
                    page.searchDutyByAttendedDate()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
        },
        searchDutyByAttendedDate(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/duty/searchDutyByAttendedDate"
            let params = {
                "attendedDate":this.dutyDay,
                "departmentId": this.departmentId
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                page.showDuties = res.data.data
                page.dutiesData[page.dutyDay] = page.showDuties
            }).catch(err=>{
                console.log(err);
            })
        },
        onDepartmentChange(departmentId){
            let arr = this.departments
            let result = arr.filter(e=>e.id==departmentId)[0]
            this.department = result.name
            this.search()
        }
      },
      watch:{
        time:{
            handler(newValue){
                this.dutyMonth = newValue.year+ '-' + (parseInt(newValue.month)+1) + '-1'
                this.search()
            },
            deep:true,
            immediate:true
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
        this.time = {
            year: new Date().getFullYear(),
            month: new Date().getMonth()
        }
      }
  }
  </script>
  
  <style scoped>
  .calendar-con {
    margin-top: 20px;
  }
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
  .pagination {
      margin-top: 20px;
      margin-right: 20px;
      float: right;
  }
  
  </style>