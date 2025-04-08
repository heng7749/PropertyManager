<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
          <el-form-item label="业主名:">
              <el-input size="small" v-model="searchValue" maxlength="10" placeholder="请输入业主名" style="width:110px"></el-input>
          </el-form-item>
  
          <el-form-item label="楼宇:">
              <el-select v-model="buildingIndex"  @change="onBuildingIndexChange"  placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="(item,index) in buildingInfos"
                      :key="item.id"
                      :label="item.name"
                      :value="index">
                  </el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="单元:">
              <el-select v-model="unit" :disabled="buildingIndex===''" placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="index in units"
                      :key="index"
                      :label="index+'单元'"
                      :value="index">
                  </el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="楼层:">
              <el-select v-model="floor"  :disabled="buildingIndex===''" placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="index in floors"
                      :key="index"
                      :label="index+'层'"
                      :value="index">
                  </el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="类型:">
              <el-select v-model="type" placeholder="全部" size="small" style="width:100px">
                <el-option label="房屋漏水" value="1"></el-option>
                <el-option label="开裂" value="2"></el-option>
                <el-option label="管道堵塞" value="3"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="状态:">
              <el-select v-model="status" placeholder="全部" size="small" style="width:100px">
                <el-option label="未处理" value="0"></el-option>
                <el-option label="已指派" value="1"></el-option>
                <el-option label="已处理" value="2"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          </el-form-item>
      </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" style="width: 100%" size="small" v-loading="loading"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}" @selection-change="handleSelectionChange">
          <el-table-column prop="id" label="编号" width="120"></el-table-column>
          <el-table-column label="地址" width="160">
            <template slot-scope="scope">
                {{scope.row.buildingName + scope.row.unit}}单元{{scope.row.floor}}层{{scope.row.houseName}}
            </template>>
          </el-table-column>
          <el-table-column prop="ownerName" label="业主" min-width="120"></el-table-column>
          <el-table-column prop="phoneNumber" label="预留电话" min-width="120"></el-table-column>
          <el-table-column label="类型" min-width="120">
                <template slot-scope="scope">
                    {{typeName[scope.row.type]}}
                </template>>
            </el-table-column>
          <el-table-column prop="contents" label="报修内容" min-width="260" show-overflow-tooltip></el-table-column>
          <el-table-column prop="createTime" label="申报时间" min-width="160"></el-table-column>
            <el-table-column label="状态" min-width="100">
                <template slot-scope="scope">
                    {{statusName[scope.row.status]}}
                </template>>
            </el-table-column>
          <el-table-column prop="repairer" label="维修员" min-width="120"></el-table-column>
          <el-table-column prop="updateTime" label="处理时间" min-width="160"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" fixed="right" min-width="150">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                  <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
          </el-table-column>
      </el-table> 
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>

        <!-- 报修信息 -->
        <el-dialog title="报修信息" :visible.sync="repairInfoVisible" width="750px" center style="position: absolute;">
            <el-image
                style="width: 178px; height: 178px;background-color: #C0C4CC;margin-right: 10px;"
                :src="editForm.photo"
                fit="contain">
            </el-image>
            <el-descriptions style="display: inline-block;width: 500px;">
                    <el-descriptions-item label="业主">{{editForm.ownerName}}</el-descriptions-item>
                    <el-descriptions-item label="住址" span="2">{{editForm.buildingName + editForm.unit}}单元{{editForm.floor}}层{{editForm.houseName}}</el-descriptions-item>
                    <el-descriptions-item label="预留电话">{{ editForm.phoneNumber }}</el-descriptions-item>
                    <el-descriptions-item label="申报时间" span="2">{{editForm.createTime}}</el-descriptions-item>
                    <el-descriptions-item label="类型">{{ typeName[editForm.type] }}</el-descriptions-item>
                    <el-descriptions-item label="处理时间" span="2">{{editForm.updateTime}}</el-descriptions-item>
                    <el-descriptions-item label="报修内容" span="3">{{ editForm.contents }}</el-descriptions-item>
                    <el-descriptions-item label="状态">
                        <el-select v-model="editForm.status" placeholder="全部" size="mini" style="width:90px;">
                            <el-option label="未处理" :value="0"></el-option>
                            <el-option label="已指派" :value="1"></el-option>
                            <el-option label="已处理" :value="2"></el-option>
                        </el-select>
                    </el-descriptions-item>
                    <el-descriptions-item label="检修员" span="2">
                        <el-tag size="small">{{ editForm.repairer }}</el-tag>
                        <el-button @click="openSearchEmployeeView" type="success" size="mini" style="margin-left: 5px;height: 24px;line-height: 22px;font-size: 12px;font-weight: 400;padding: 0 8px;">选择</el-button>
                    </el-descriptions-item>
            </el-descriptions>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" size="mini" @click="conductRepair">保存</el-button>
                <el-button size="mini">取消</el-button>
            </div>
        </el-dialog>

        <el-dialog
            width="55%"
            title="员工查询"
            :visible.sync="searchEmployeeVisible"
            append-to-body top="4vh">
            <SearchEmployee @onEmployeeSeleted="setEmployeeInfo"></SearchEmployee>
        </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  import SearchEmployee from '../../components/SearchEmployee.vue'
  export default {
      components: {Pagination,SearchEmployee},
      name:"RepairMsg",
      data(){
          return {
              searchValue:'',
              buildingIndex:'',
              buildingId:'',
              unit:'',
              units:'',
              floor:'',
              floors:'',
              buildingInfos:[],
              tableData:[],
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              repairInfoVisible:false,
              type:'',
              status:'',
              value:'',
              loading:'',
              statusName:['未处理','已指派','已处理'],
              typeName:['','房屋漏水','开裂','管道堵塞'],
              editForm:{
                buildingName:'',
                unit:'',
                floor:'',
                name:'',
                phoneNumber:'',
                type:'',
                contents:'',
                status:0,
                photo:'',
                repairer:'',
                ownerName:'',
                createTime:'',
                updateTime:'',
                repairerId:''
              },
              seletedEmployeeInfo:{},
              searchEmployeeVisible:false
          }
      },
      methods:{
          handleEdit(row) {
              this.repairInfoVisible = true;
              Object.assign(this.editForm,row);
          },
          handleDelete(row) {
              this.$confirm('是否删除此报修信息?', '删除报修提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                this.deleteRepair(row.id)
              }).catch(() => {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  });          
              });
          },
          pageChangeHandle(param) {
            let currentPage = param.currentPage
            let buildingId = this.buildingId
            let unit = this.unit
            let floor = this.floor
            let type = this.type
            let status = this.status
            let searchValue = this.searchValue
            this.searchRepairs(currentPage,buildingId,unit,floor,type,status,searchValue)
          },
          handleSelectionChange(selections) {
              console.log(selections);
              if(selections.length!==0){
                  this.delSelectItemsBtnShow = true
              }else {
                  this.delSelectItemsBtnShow = false
              }
          },
          onBuildingIndexChange(){
            this.buildingId = this.buildingInfos[this.buildingIndex].id
            this.units = this.buildingInfos[this.buildingIndex].units
            this.floors = this.buildingInfos[this.buildingIndex].floors
          },
          searchRepairs(currentPage,buildingId,unit,floor,type,status,searchValue){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/repair/searchRepairs"
            let params = {
                "ownerName":searchValue,
                "buildingId": buildingId,
                "unit": unit,
                "floor": floor,
                "type": type,
                "status": status,
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
            let buildingId = this.buildingId
            let unit = this.unit
            let floor = this.floor
            let type = this.type
            let status = this.status
            let searchValue = this.searchValue
            this.searchRepairs(currentPage,buildingId,unit,floor,type,status,searchValue)
          },
          deleteRepair(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/repair/deleteRepair'
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
          openSearchEmployeeView(){
            this.searchEmployeeVisible = true;
          },
          setEmployeeInfo(employee){
            this.seletedEmployeeInfo = employee
            this.searchEmployeeVisible = false
            this.editForm.repairerId = employee.id
            this.editForm.repairer = employee.name
          },
          conductRepair(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/repair/conductRepair"
            let params = {
                "id": this.editForm.id,
                "status": this.editForm.status,
                "repairerId": this.editForm.repairerId,
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '处理成功',
                        type: 'success'
                    });
                    page.repairInfoVisible = false
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
        //获取楼宇信息
        this.$http.get("/heng/common/buildings",{
            headers:{token:token}
        }).then(res=>{
            let result = res.data
            if(result.status === 0){
                page.buildingInfos = result.data
            } else {
                page.$message.error(result.msg)
            }
        },err=>{
            page.$message.error(err.msg)
        })
        //获取业主报修信息  
        this.searchRepairs(1,'','','','','','')
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
  </style>