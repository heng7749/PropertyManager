<template>
    <div style="padding:16px;">
        <el-form inline class="searchBar">
            <el-dropdown @command="selectSearchType" class="dropdownTag">
                <span class="el-dropdown-link">
                搜索模式<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command='1'>设备名</el-dropdown-item>
                    <el-dropdown-item command='2'>应用场所</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <el-form-item>
                <el-input size="mini" v-model="searchValue" maxlength="10" :placeholder="searchType==1?'输入设备名':'输入应用场所'" style="width:110px"></el-input>
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
            <el-form-item label="购买日期:">
                <el-date-picker
                    value-format="yyyy-MM-dd"
                    v-model="purchasingTime"
                    type="date"
                    size="mini"
                    style="width:125px;"
                    placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="投用日期:">
                <el-date-picker
                    value-format="yyyy-MM-dd"
                    v-model="deployTime"
                    type="date"
                    size="mini"
                    style="width:125px;"
                    placeholder="选择日期">
                </el-date-picker>
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
            <el-table-column prop="purchasingTime" label="购买时间" min-width="160"></el-table-column>
            <el-table-column label="定检周期" min-width="100">
                <template slot-scope="scope">
                    {{scope.row.repairsPeriod}}周
                </template>
            </el-table-column>
            <el-table-column label="更换周期" min-width="100">
                <template slot-scope="scope">
                    {{scope.row.replacePeriod}}周
                </template>
            </el-table-column>
            <el-table-column prop="producer" label="生产厂商" min-width="120" show-overflow-tooltip></el-table-column>
            <el-table-column prop="creator" label="创建人" min-width="120"></el-table-column>
            <el-table-column prop="remark" label="说明" min-width="160" show-overflow-tooltip></el-table-column>
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
        <el-dialog title="设备信息" :visible.sync="equipmentInfoVisible" width="60%" center>
            <el-descriptions :column="2" style="display: inline-block;">
                <el-descriptions-item label="编号">{{ editForm.id }}</el-descriptions-item>
                <el-descriptions-item label="设备名">
                    <el-input v-model="editForm.name" placeholder="请输入设备名" maxlength="50" size="mini" style="width: 250px;"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="类别">
                    <el-select v-model="editForm.type" placeholder="请选择设备类别" size="mini" style="width:130px">
                        <el-option label="办公用品" :value="1"></el-option>
                        <el-option label="家具" :value="2"></el-option>
                        <el-option label="生活用品" :value="3"></el-option>
                        <el-option label="安全保障" :value="4"></el-option>
                        <el-option label="工具" :value="5"></el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="型号">
                    <el-input v-model="editForm.model" placeholder="请输入设备型号" maxlength="255" size="mini" style="width: 250px;"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="应用场所" span="2">
                    <el-input size="mini" v-model="editForm.deployPlace" maxlength="255" placeholder="请输入设备应用场所" style="width:560px"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="所属部门">
                    <el-select v-model="editForm.departmentId" placeholder="全部" size="mini" style="width:85px">
                        <el-option
                            v-for="item in departments"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="状态">
                    <el-select v-model="editForm.status" placeholder="全部" size="mini" style="width:85px">
                        <el-option label="正常" :value="0"></el-option>
                        <el-option label="异常" :value="1"></el-option>
                        <el-option label="已报废" :value="2"></el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="投用日期">
                    <el-date-picker
                        v-model="editForm.deployTime"
                        type="date"
                        size="mini" 
                        style="width: 130px;"
                        placeholder="选择日期">
                    </el-date-picker>
                </el-descriptions-item>
                <el-descriptions-item label="购买日期">
                    <el-date-picker
                        v-model="editForm.purchasingTime"
                        type="date"
                        size="mini" 
                        style="width: 130px;"
                        placeholder="选择日期">
                    </el-date-picker>
                </el-descriptions-item>
                <el-descriptions-item label="定检周期">
                    <el-input-number v-model="editForm.repairsPeriod" :precision="0" size="mini" :step="1" :min="0" :max="1000" style="width:130px;"></el-input-number>
                </el-descriptions-item>
                <el-descriptions-item label="更换周期">
                    <el-input-number v-model="editForm.replacePeriod" :precision="0" size="mini" :step="1" :min="0" :max="1000" style="width:130px;"></el-input-number>
                </el-descriptions-item>
                <el-descriptions-item label="生产厂商" span="2">
                    <el-input size="mini" v-model="editForm.producer" maxlength="255" placeholder="请输入设备生产厂商" style="width:560px"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="备注" span="2">
                    <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="editForm.remark"
                        maxlength="500"
                        show-word-limit
                        ></el-input>
                </el-descriptions-item>
            </el-descriptions>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" size="mini" @click="editEquipment">保存</el-button>
                <el-button size="mini" @click="equipmentInfoVisible=false">取消</el-button>
            </div>
        </el-dialog>

        <!-- 添加设备信息 -->
        <el-dialog title="添加设备信息" :visible.sync="addViewVisible" width="60%" center style="position: absolute;">
            <el-descriptions :column="2" style="display: inline-block;">
                <el-descriptions-item label="设备名" span="2">
                    <el-input v-model="addForm.name" placeholder="请输入设备名" maxlength="50" size="mini" style="width: 250px;"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="类别">
                    <el-select v-model="addForm.type" placeholder="请选择设备类别" size="mini" style="width:130px">
                        <el-option label="办公用品" :value="1"></el-option>
                        <el-option label="家具" :value="2"></el-option>
                        <el-option label="生活用品" :value="3"></el-option>
                        <el-option label="安全保障" :value="4"></el-option>
                        <el-option label="工具" :value="5"></el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="型号">
                    <el-input v-model="addForm.model" placeholder="请输入设备型号" maxlength="255" size="mini" style="width: 250px;"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="应用场所" span="2">
                    <el-input size="mini" v-model="addForm.deployPlace" maxlength="255" placeholder="请输入设备应用场所" style="width:560px"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="所属部门">
                    <el-select v-model="addForm.departmentId" placeholder="全部" size="mini" style="width:85px">
                        <el-option
                            v-for="item in departments"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="状态">
                    <el-select v-model="addForm.status" placeholder="全部" size="mini" style="width:85px">
                        <el-option label="正常" :value="0"></el-option>
                        <el-option label="异常" :value="1"></el-option>
                        <el-option label="已报废" :value="2"></el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="投用日期">
                    <el-date-picker
                        v-model="addForm.deployTime"
                        value-format="yyyy-MM-dd"
                        type="date"
                        size="mini" 
                        style="width: 130px;"
                        placeholder="选择日期">
                    </el-date-picker>
                </el-descriptions-item>
                <el-descriptions-item label="购买日期">
                    <el-date-picker
                        v-model="addForm.purchasingTime"
                        value-format="yyyy-MM-dd"
                        type="date"
                        size="mini" 
                        style="width: 130px;"
                        placeholder="选择日期">
                    </el-date-picker>
                </el-descriptions-item>
                <el-descriptions-item label="定检周期">
                    <el-input-number v-model="addForm.repairsPeriod" :precision="0" size="mini" :step="1" :min="0" :max="1000" style="width:130px;"></el-input-number>
                </el-descriptions-item>
                <el-descriptions-item label="更换周期">
                    <el-input-number v-model="addForm.replacePeriod" :precision="0" size="mini" :step="1" :min="0" :max="1000" style="width:130px;"></el-input-number>
                </el-descriptions-item>
                <el-descriptions-item label="生产厂商" span="2">
                    <el-input size="mini" v-model="addForm.producer" maxlength="255" placeholder="请输入设备生产厂商" style="width:560px"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="备注" span="2">
                    <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="addForm.remark"
                        maxlength="500"
                        show-word-limit
                        ></el-input>
                </el-descriptions-item>
            </el-descriptions>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" size="mini" @click="addEquipment">添加</el-button>
                <el-button size="mini" @click="addViewVisible=false">取消</el-button>
            </div>
        </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  export default {
      components: {Pagination},
      name:"EquipmentMsg",
      data(){
          return {
            value:'',
              searchType: 1,
              searchValue:'',
              departmentId:'',
              department:'',
              departments:[],
              type:'',
              types:[{
                id:1,
                name:'类型1'
              },{
                id:2,
                name:'类型2'
              }],
              purchasingTime:'',
              deployTime:'',
              status:'',
              tableData:[],
              userInfo:{},
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              equipmentInfoVisible:false,
              addViewVisible:false,
              form:'',
              loading:false,
              textarea:'',
              statusName:['正常','异常','已报废'],
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
                remark:''
              },
              addForm:{
                departmentId:'',
                name:'',
                type:'',
                status:'',
                deployTime:'',
                purchasingTime:'',
                repairsPeriod:'',
                replacePeriod:'',
                producer:'',
                remark:''
              }
          }
      },
      methods:{
        selectSearchType(command){
            this.searchType = Number(command)
        },
          handleEdit(row) {
              this.equipmentInfoVisible = true;
              Object.assign(this.editForm,row);
          },
          handleDelete(row) {
              this.$confirm('是否删除此设备信息?', '删除设备提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                this.deleteEquipment(row.id)
              }).catch(() => {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  });          
              });
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
          openAddStallView(){
            this.addViewVisible = true
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
          editEquipment(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/equipment/editEquipment"
            let params = {
                "id": this.editForm.id,
                "name": this.editForm.name,
                "type": this.editForm.type,
                "model": this.editForm.model,
                "status": this.editForm.status,
                "deployPlace": this.editForm.deployPlace,
                "departmentId": this.editForm.departmentId,
                "deployTime": this.editForm.deployTime,
                "purchasingTime": this.editForm.purchasingTime,
                "repairsPeriod": this.editForm.repairsPeriod,
                "replacePeriod": this.editForm.replacePeriod,
                "producer": this.editForm.producer,
                "remark": this.editForm.remark
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '修改成功',
                        type: 'success'
                    });
                    page.equipmentInfoVisible = false
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          deleteEquipment(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/equipment/deleteEquipment'
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
          addEquipment(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/equipment/addEquipment"
            let params = {
                "name": this.addForm.name,
                "type": this.addForm.type,
                "model": this.addForm.model,
                "status": this.addForm.status,
                "deployPlace": this.addForm.deployPlace,
                "departmentId": this.addForm.departmentId,
                "deployTime": this.addForm.deployTime,
                "purchasingTime": this.addForm.purchasingTime,
                "repairsPeriod": this.addForm.repairsPeriod,
                "replacePeriod": this.addForm.replacePeriod,
                "producer": this.addForm.producer,
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
        this.searchEquipments(1,'','','','','','','')
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
    width: 80px;
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