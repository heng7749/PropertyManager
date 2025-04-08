<template>
    <div style="padding:16px;">
        <el-form inline class="searchBar">
            <el-dropdown @command="selectSearchType" class="dropdownTag" szie="small">
                <span class="el-dropdown-link">
                    {{searchType==1?'设备名模式':'应用场所模式'}}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command='1'>设备名</el-dropdown-item>
                    <el-dropdown-item command='2'>应用场所</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <el-form-item>
                <el-input size="small" v-model="searchValue" maxlength="10" :placeholder="searchType==1?'请输入设备名':'请输入应用场所'" style="width:110px"></el-input>
            </el-form-item>
            <el-form-item label="所属部门:">
                <el-select v-model="departmentId" placeholder="全部" size="small" style="width:85px">
                    <el-option
                        v-for="item in departments"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="类别:">
                <el-select v-model="type" placeholder="全部" size="small" style="width:95px">
                    <el-option label="电力设备" value="1"></el-option>
                    <el-option label="消防设备" value="2"></el-option>
                    <el-option label="普通设备" value="3"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="检修日期:">
                <el-date-picker
                    value-format="yyyy-MM-dd"
                    v-model="maintainDate"
                    type="date"
                    size="small"
                    style="width:130px;"
                    placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="状态:">
                <el-select v-model="status" placeholder="全部" size="small" style="width:85px">
                    <el-option label="未检修" :value="0"></el-option>
                    <el-option label="已指派" :value="1"></el-option>
                    <el-option label="已检修" :value="2"></el-option>
                    <el-option label="已逾期" :value="3"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
            </el-form-item>
        </el-form>
  
        <!-- 表格 -->
        <el-table stripe border :data="tableData" tooltip-effect="dark" size="small" v-loading="loading"
                :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
            <el-table-column prop="id" label="编号" min-width="120"></el-table-column>
            <el-table-column label="类别" min-width="120">
                <template slot-scope="scope">
                    {{typeName[scope.row.type]}}
                </template>>
            </el-table-column>
            <el-table-column prop="equipmentName" label="设备名" min-width="120" show-overflow-tooltip></el-table-column>
            <el-table-column prop="deployPlace" label="应用场所" min-width="120"></el-table-column>
            <el-table-column prop="departmentName" label="所属部门" min-width="120"></el-table-column>
            <el-table-column label="状态" min-width="100">
                <template slot-scope="scope">
                    {{statusName[scope.row.status]}}
                </template>>
            </el-table-column>
            <el-table-column prop="maintainer" label="检修人" widmin-widthth="120"></el-table-column>
            <el-table-column prop="maintainDate" label="检修日期" min-width="120"></el-table-column>
            <el-table-column prop="deployTime" label="投用时间" min-width="120"></el-table-column>
            <el-table-column prop="maintainTargetDate" label="定检日期" min-width="120"></el-table-column>
            <el-table-column prop="replaceTargetDate" label="更换日期" min-width="120"></el-table-column>
            <el-table-column prop="remark" label="说明" min-width="160"></el-table-column>
            <!-- 操作 -->
            <el-table-column label="操作" fixed="right" width="150">
                <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button @click="openAddStallView" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;float: left;">添加</el-button>
        <!-- 分页 -->
        <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
     
        <!-- 设备信息 -->
        <el-dialog title="设备检修信息" :visible.sync="maintenanceInfoVisible" width="60%" center style="position: absolute;">
            <el-descriptions>
                <el-descriptions-item label="编号">{{ editForm.id }}</el-descriptions-item>
                <el-descriptions-item label="设备名">{{ editForm.equipmentName }}</el-descriptions-item>
                <el-descriptions-item label="生产厂商">{{ editForm.producer }}</el-descriptions-item>
                <el-descriptions-item label="型号">{{ editForm.model }}</el-descriptions-item>
                <el-descriptions-item label="类型">{{ typeName[editForm.type] }}</el-descriptions-item>
                <el-descriptions-item label="应用场所">{{ editForm.deployPlace }}</el-descriptions-item>
                <el-descriptions-item label="所属部门">{{editForm.departmentName}}</el-descriptions-item>
                <el-descriptions-item label="购买时间">{{editForm.purchasingTime}}</el-descriptions-item>
                <el-descriptions-item label="投用时间">{{editForm.deployTime}}</el-descriptions-item>
                <el-descriptions-item label="定检周期">{{editForm.repairsPeriod}}周</el-descriptions-item>
                <el-descriptions-item label="更换周期">{{editForm.replacePeriod}}周</el-descriptions-item>
                <el-descriptions-item label="定检日期">{{editForm.maintainTargetDate}}</el-descriptions-item>
                <el-descriptions-item label="状态">
                    <el-select v-model="editForm.status"  placeholder="全部" size="mini" style="width:90px;">
                        <el-option label="未检修" :value="0"></el-option>
                        <el-option label="已指派" :value="1"></el-option>
                        <el-option label="已检修" :value="2"></el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="检修员">
                    <el-tag size="small">{{editForm.maintainer}}</el-tag>
                    <el-button @click="openSearchEmployeeView(1)" type="primary" size="mini" style="margin-left: 5px;height: 24px;line-height: 22px;font-size: 12px;font-weight: 400;padding: 0 8px;">选择</el-button>
                </el-descriptions-item>
                <el-descriptions-item label="检修日期" span="2">
                    <el-date-picker value-format="yyyy-MM-dd" type="date" placeholder="选择日期" v-model="editForm.maintainDate" style="width:125px;" size="mini"></el-date-picker>
                </el-descriptions-item>
                <el-descriptions-item label="备注">
                    <el-input
                        type="textarea"
                        placeholder="说明"
                        v-model="editForm.remark"
                        maxlength="30"
                        show-word-limit>
                    </el-input>
                </el-descriptions-item>
            </el-descriptions>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" @click="editMaintenance" size="mini">保存</el-button>
                <el-button @click="maintenanceInfoVisible=false" size="mini">取消</el-button>
            </div>
        </el-dialog>

        <!-- 添加检修信息 -->
        <el-dialog title="添加检修信息" :visible.sync="addViewVisible" width="60%" center style="position: absolute;">
            <el-descriptions>
                <el-descriptions-item label="编号">{{ seletedEquipmentInfo.id }}</el-descriptions-item>
                <el-descriptions-item label="设备名">{{ seletedEquipmentInfo.name }}</el-descriptions-item>
                <el-descriptions-item label="生产厂商">{{ seletedEquipmentInfo.producer }}</el-descriptions-item>
                <el-descriptions-item label="型号">{{ seletedEquipmentInfo.model }}</el-descriptions-item>
                <el-descriptions-item label="类型">{{ typeName[seletedEquipmentInfo.type] }}</el-descriptions-item>
                <el-descriptions-item label="应用场所">{{ seletedEquipmentInfo.deployPlace }}</el-descriptions-item>
                <el-descriptions-item label="所属部门">{{seletedEquipmentInfo.departmentName}}</el-descriptions-item>
                <el-descriptions-item label="购买时间">{{seletedEquipmentInfo.purchasingTime}}</el-descriptions-item>
                <el-descriptions-item label="投用时间">{{seletedEquipmentInfo.deployTime}}</el-descriptions-item>
                <el-descriptions-item label="定检周期">{{seletedEquipmentInfo.repairsPeriod}}周</el-descriptions-item>
                <el-descriptions-item label="更换周期" span="2">{{seletedEquipmentInfo.replacePeriod}}周
                    <el-button @click="searchEquipmentVisible=true" type="success" size="mini" style="margin-left: 60px;height: 24px;line-height: 22px;font-size: 12px;font-weight: 400;padding: 0 8px;">选择设备</el-button>
                </el-descriptions-item>
                <el-descriptions-item label="状态">
                    <el-select v-model="addForm.status" placeholder="全部" size="mini" style="width:90px;">
                        <el-option label="未检修" :value="0"></el-option>
                        <el-option label="已指派" :value="1"></el-option>
                        <el-option label="已检修" :value="2"></el-option>
                        <el-option label="已逾期" :value="3"></el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="检修员">
                    <el-tag size="small">{{addForm.maintainer}}</el-tag>
                    <el-button @click="openSearchEmployeeView(2)" type="primary" size="mini" style="margin-left: 5px;height: 24px;line-height: 22px;font-size: 12px;font-weight: 400;padding: 0 8px;">选择</el-button>
                </el-descriptions-item>
                <el-descriptions-item label="检修日期">
                    <el-date-picker value-format="yyyy-MM-dd" type="date" placeholder="选择日期" v-model="addForm.maintainDate" style="width:125px;" size="mini"></el-date-picker>
                </el-descriptions-item>
                <el-descriptions-item label="备注">
                    <el-input
                        type="textarea"
                        placeholder="说明"
                        v-model="addForm.remark"
                        maxlength="500"
                        show-word-limit>
                    </el-input>
                </el-descriptions-item>
            </el-descriptions>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" size="mini" @click="addMaintenance">添加</el-button>
                <el-button size="mini" @click="addViewVisible=false">取消</el-button>
            </div>
        </el-dialog>

        <el-dialog
            width="55%"
            title="员工查询"
            :visible.sync="searchEmployeeVisible"
            append-to-body top="4vh">
            <SearchEmployee @onEmployeeSeleted="setEmployeeInfo"></SearchEmployee>
        </el-dialog>

        <el-dialog
            width="55%"
            title="设备查询"
            :visible.sync="searchEquipmentVisible"
            append-to-body top="4vh">
            <SearchEquipment @onEquipmentSeleted="setEquipmentInfo"></SearchEquipment>
        </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  import SearchEmployee from '../../components/SearchEmployee.vue'
  import SearchEquipment from '../../components/SearchEquipment.vue'
  export default {
      components: {Pagination,SearchEmployee,SearchEquipment},
      name:"MaintenanceMsg",
      data(){
          return {
            value:'',
              searchType: 1,
              searchValue:'',
              departmentId:'',
              departments:[],
              tableData:[],
              userInfo:{},
              pageMsg: {
                currentPage: 1,
                pageSize: 0,
                total: 0
              },
              maintenanceInfoVisible:false,
              addViewVisible:false,
              form:'',
              maintainDate:'',
              textarea:'',
              status:'',
              type:'',
              types:[],
              loading:false,
              statusName:['未检修','已指派','已检修','已逾期'],
              typeName:['','电力设备','消防设备','普通设备'],
              editForm:{
                id:'',
                departmentId:'',
                name:'',
                type:'',
                status:'',
                deployTime:'',
                purchasingTime:'',
                repairsPeriod:'',
                replacePeriod:'',
                producer:'',
                remark:'',
                model:'',
                maintainDate:'',
                maintainerId:'',
                maintainer:''
              },
              addForm:{
                equipmentId:'',
                status:'',
                maintainerId:'',
                maintainer:'',
                maintainDate:'',
                remark:''
              },
              searchEmployeeVisible:false,
              seletedEmployeeInfo:{},
              searchEquipmentVisible:false,
              seletedEquipmentInfo:{},
              employeeSearchType:1
          }
      },
      methods:{
        selectSearchType(command){
            this.searchType = Number(command)
        },
          handleEdit(row) {
              this.maintenanceInfoVisible = true;
              Object.assign(this.editForm,row);
          },
          handleDelete(row) {
              this.$confirm('是否删除此检修信息?', '删除检修提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                this.deleteMaintenance(row.id)
              }).catch(() => {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  });          
              });
          },
          pageChangeHandle(param) {
            let currentPage = param.currentPage
            let equipmentName = ''
            let deployPlace  = ''
            if(this.searchType === 1){
                equipmentName = this.searchValue
            } else {
                deployPlace = this.searchValue
            }
            let departmentId = this.departmentId
            let type = this.type
            let maintainDate = this.maintainDate
            let status = this.status
            this.searchMaintenances(currentPage,equipmentName,deployPlace,departmentId,type,maintainDate,status)
          },
          openAddStallView(){
            this.addViewVisible = true
          },
        searchMaintenances(currentPage,equipmentName,deployPlace,departmentId,type,maintainDate,status){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/equipment/searchMaintenances"
            let params = {
                "equipmentName":equipmentName,
                "deployPlace": deployPlace,
                "departmentId": departmentId,
                "type": type,
                "maintainDate":maintainDate,
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

                        page.departmentIdToString()
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
            let equipmentName = ''
            let deployPlace  = ''
            if(this.searchType === 1){
                equipmentName = this.searchValue
            } else {
                deployPlace = this.searchValue
            }
            let departmentId = this.departmentId
            let type = this.type
            let maintainDate = this.maintainDate
            let status = this.status
            this.searchMaintenances(currentPage,equipmentName,deployPlace,departmentId,type,maintainDate,status)
          },
          deleteMaintenance(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/equipment/deleteMaintenance'
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
          editMaintenance(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/equipment/editMaintenance"
            let params = {
                "id": this.editForm.id,
                "status": this.editForm.status,
                "maintainerId": this.editForm.maintainerId,
                "maintainDate": this.editForm.maintainDate,
                "remark": this.editForm.remark,
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '修改成功',
                        type: 'success'
                    });
                    page.maintenanceInfoVisible = false
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          openSearchEmployeeView(type){
            this.searchEmployeeVisible = true
            this.employeeSearchType = type
          },
          setEmployeeInfo(employee){
            this.seletedEmployeeInfo = employee
            this.searchEmployeeVisible = false
            if(this.employeeSearchType == 1){
                this.editForm.maintainerId = employee.id
                this.editForm.maintainer = employee.name
            } else if(this.employeeSearchType == 2){
                this.addForm.maintainerId = employee.id
                this.addForm.maintainer = employee.name
            }
          },
          setEquipmentInfo(equipment){
            this.seletedEquipmentInfo = equipment
            this.searchEquipmentVisible = false
            this.addForm.equipmentId = equipment.id
          },
          addMaintenance(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/equipment/addMaintenance"
            let params = {
                "equipmentId": this.addForm.equipmentId,
                "status": this.addForm.status,
                "maintainerId": this.addForm.maintainerId,
                "maintainDate": this.addForm.maintainDate,
                "remark": this.addForm.remark
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '添加成功',
                        type: 'success'
                    });
                    page.addViewVisible = false
                    page.search()
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
        //获设备信息  
        this.searchMaintenances(1,'','','','','','','')
      }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  .pagination {
        margin-top: 20px;
        margin-right: 20px;
        float: right;
    }
    .dropdownTag{
    width: 110px;
    height: 40px;
    text-align: right;
    line-height: 40px;
    margin-right: 5px;
}
.el-dropdown-link {
        cursor: pointer;
        color: #409EFF;
        font-size: 14px;
    }
  </style>