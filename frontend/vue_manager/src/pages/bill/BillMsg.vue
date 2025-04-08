<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
          <el-form-item label="业主：">
              <el-input size="small" v-model="searchValue" maxlength="10" placeholder="请输入业主名" style="width:110px"></el-input>
          </el-form-item>
  
          <el-form-item label="楼宇：">
              <el-select v-model="buildingIndex"  @change="onBuildingIndexChange"  placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="(item,index) in buildingInfos"
                      :key="item.id"
                      :label="item.name"
                      :value="index">
                  </el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="单元：">
              <el-select v-model="unit" :disabled="buildingIndex===''" placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="index in units"
                      :key="index"
                      :label="index+'单元'"
                      :value="index">
                  </el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="楼层：">
              <el-select v-model="floor"  :disabled="buildingIndex===''" placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="index in floors"
                      :key="index"
                      :label="index+'层'"
                      :value="index">
                  </el-option>
              </el-select>
          </el-form-item>

          <el-form-item label="缴费类型：">
              <el-select v-model="billTypeId" placeholder="全部" size="small" style="width:100px">
                <el-option
                    v-for="item in billTypes"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
          </el-form-item>
        
          <el-form-item label="状态：">
              <el-select v-model="status" placeholder="全部" size="small" style="width:90px">
                <el-option :value="0" label="未缴费"></el-option>
                <el-option :value="1" label="已缴费"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          </el-form-item>
      </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" style="width: 100%" size="small" v-loading="loading"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
          <el-table-column prop="id" label="单号" min-width="100"></el-table-column>
          <el-table-column prop="ownerName" label="业主" min-width="120"></el-table-column>
          <el-table-column prop="address" label="住址" min-width="160"></el-table-column>
          <el-table-column prop="phoneNumber" label="联系电话" min-width="120"></el-table-column>
          <el-table-column prop="billTypeName" label="类型" min-width="120"></el-table-column>
          <el-table-column prop="count" label="数值" min-width="120"></el-table-column>
          <el-table-column prop="money" label="金额" min-width="120"></el-table-column>
          <el-table-column label="状态" min-width="100">
            <template slot-scope="scope">
                {{statusName[scope.row.status]}}
            </template>>
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间" min-width="160"></el-table-column>
          <el-table-column prop="endTime" label="结束时间" min-width="160"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" fixed="right" min-width="150">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">修改</el-button>
                  <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
          </el-table-column>
      </el-table> 
      <el-button @click="addBillVisible=true" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;float: left;">添加</el-button>
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
    <!-- 缴费信息 -->
    <el-dialog title="缴费信息" :visible.sync="billInfoVisible" width="40%" center style="position: absolute;" top="10vh">
        <el-descriptions class="margin-top" title="" :column="2">
            <el-descriptions-item label="单号" span="2">{{ editForm.id }}</el-descriptions-item>
            <el-descriptions-item label="业主">{{ editForm.ownerName }}</el-descriptions-item>
            <el-descriptions-item label="住址">{{ editForm.address }}</el-descriptions-item>
            <el-descriptions-item label="类型">{{ editForm.billTypeName }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{editForm.phoneNumber}}</el-descriptions-item>
            <el-descriptions-item label="数值">{{ editForm.count }}</el-descriptions-item>
            <el-descriptions-item label="上次抄表日期">{{ editForm.startTime }}</el-descriptions-item>
            <el-descriptions-item label="金额">{{ editForm.money }}</el-descriptions-item>
            <el-descriptions-item label="本次抄表日期">{{ editForm.endTime }}</el-descriptions-item>
            <el-descriptions-item label="状态">
                <el-select v-model="editForm.status" placeholder="全部" size="mini" style="width:90px">
                    <el-option :value="0" label="未缴费"></el-option>
                    <el-option :value="1" label="已缴费"></el-option>
                </el-select>
            </el-descriptions-item>
        </el-descriptions>
        <div>
            <el-button type="primary" size="mini" @click="editBill">保存</el-button>
            <el-button size="mini" @click="billInfoVisible=false">取消</el-button>
        </div>
    </el-dialog>

     <!-- 添加缴费 -->
     <el-dialog title="添加缴费" :visible.sync="addBillVisible" width="40%" center style="position: absolute;">
        <el-form ref="addForm" :model="addForm" size="mini">
            <el-form-item label="房屋：">
                <el-tag size="medium" style="float: left;display: inline;" v-if="addForm.house!=null">{{ addForm.house.building + addForm.house.unit + '单元' + addForm.house.floor + '层' + addForm.house.name}}</el-tag>
                <el-button type="primary" icon="el-icon-edit" size="mini" circle style="margin-left: 20px;" @click="searchHouseVisible=true"></el-button>
            </el-form-item>
            <el-form-item label="缴费类型：">
                <el-select v-model="addForm.billType" placeholder="全部" size="mini" style="width:90px">
                    <el-option
                        v-for="item in billTypes"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="抄表日期：">
                <el-date-picker
                    v-model="addForm.endTime"
                    type="datetime"
                    format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择日期时间">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="数值：">
                <el-input-number v-model="addForm.count" :precision="2" :step="1" :min="1" :max="1000" style="width:150px;"></el-input-number>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addBill">提交</el-button>
                <el-button @click="addBillVisible=false">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
    <el-dialog
        width="65%"
        title="房屋查询"
        :visible.sync="searchHouseVisible"
        append-to-body top="3vh">
        <SearchHouse @onHouseSeleted="setHouseInfo"></SearchHouse>
    </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  import SearchHouse from '../../components/SearchHouse.vue'
  export default {
      components: {Pagination,SearchHouse},
      name:"BillMsg",
      data(){
          return {
              searchValue:'',
              buildingIndex:'',
              buildingId:'',
              unit:'',
              units:[],
              floor:'',
              floors:'',
              buildingInfos:[],
              tableData:[],
              userInfo:{},
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              editForm:{
                id:'',
                ownerName:'',
                address:'',
                phoneNumber:'',
                count:'',
                endTime:'',
                status:''
              },
              loading:false,
              billType:'',
              billTypeId:'',
              statusName:['未缴费','已缴费'],
              billInfoVisible:false,
              billTypes:[],
              status:'',
              addBillVisible:false,
              addForm:{
                hosueId:'',
                billType:'',
                count:'',
                endTime:'',
                house:null
             },
             searchHouseVisible:false
          }
      },
      methods:{
          handleEdit(row) {
              this.billInfoVisible = true;
              this.editForm = row;
          },
          handleDelete(row) {
              this.$confirm('是否删除此缴费信息?', '删除缴费提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                this.deleteBill(row.id)
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
            let searchValue = this.searchValue
            let billTypeId = this.billTypeId
            let status = this.status
            this.searchBill(currentPage,buildingId,unit,floor,searchValue,billTypeId,status)
          },
          onBuildingIndexChange(){
            this.buildingId = this.buildingInfos[this.buildingIndex].id
            this.units = this.buildingInfos[this.buildingIndex].units
            this.floors = this.buildingInfos[this.buildingIndex].floors
          },
          searchBill(currentPage,buildingId,unit,floor,searchValue,billTypeId,status){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/bill/searchBill"
            let params = {
                "ownerName":searchValue,
                "buildingId": buildingId,
                "unit": unit,
                "floor": floor,
                "billTypeId":billTypeId,
                "status":status,
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
            let searchValue = this.searchValue
            let billTypeId = this.billTypeId
            let status = this.status
            this.searchBill(currentPage,buildingId,unit,floor,searchValue,billTypeId,status)
          },
          editBill(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/bill/editBill'
            let params = {
                'id': this.editForm.id,
                'status': this.editForm.status
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.billInfoVisible = false
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          deleteBill(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/bill/deleteBill'
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
          setHouseInfo(house){
            this.addForm.house = house
            this.searchHouseVisible = false
          },
          addBill(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/bill/addBill'
            let params = {
                'houseId': this.addForm.house.id,
                'billTypeId': this.addForm.billType,
                'endTime': this.addForm.endTime,
                'count': this.addForm.count,
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.addBillVisible = false
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
        //获取缴费类型信息
        this.$http.get("/heng/common/billTypes",{
            headers:{token:token}
        }).then(res=>{
            let result = res.data
            if(result.status === 0){
                page.billTypes = result.data
            } else {
                page.$message.error(result.msg)
            }
        },err=>{
            page.$message.error(err.msg)
        })
        //获取缴费信息  
        this.searchBill(1,'','','','','','')
      }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  </style>