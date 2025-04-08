<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
          <el-form-item label="车牌号：">
              <el-input size="small" v-model="searchValue" maxlength="10" placeholder="请输入车位号" style="width:110px"></el-input>
          </el-form-item>
  
          <el-form-item label="停车场：">
              <el-select v-model="parkingLotIndex"  @change="onParkingLotIndexChange"  placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="(item,index) in parkingLotInfos"
                      :key="item.id"
                      :label="item.name"
                      :value="index">
                  </el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="区域：">
              <el-select v-model="area" :disabled="parkingLotIndex===''" placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="index in areas"
                      :key="index"
                      :label="index+'区'"
                      :value="index">
                  </el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="状态：">
                <el-select v-model="status" placeholder="全部" size="small" style="width:100px">
                  <el-option label="未缴费" value="0"></el-option>
                  <el-option label="已缴费" value="1"></el-option>
                  <el-option label="使用中" value="2"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          </el-form-item>
      </el-form>
  
         <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" size="small"  v-loading="loading"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}" @selection-change="handleSelectionChange">
          <el-table-column prop="carNumber" label="车牌号" fixed="left" min-width="120"></el-table-column>
          <el-table-column prop="ownerName" label="车主" min-width="120"></el-table-column>
          <el-table-column prop="parkingLotName" label="停车场" min-width="120"></el-table-column>
          <el-table-column label="区域" min-width="120">
            <template slot-scope="scope">
                {{scope.row.area}}区
            </template>>
          </el-table-column>
          <el-table-column prop="stallNumber" label="车位号" min-width="120"></el-table-column>
          <el-table-column prop="startTime" label="进场时间" min-width="160"></el-table-column>
          <el-table-column prop="endTime" label="出场时间" min-width="160"></el-table-column>
          <el-table-column prop="money" label="金额" min-width="120"></el-table-column>
          <el-table-column prop="status" label="状态" min-width="120"></el-table-column>
          <el-table-column prop="remark" label="说明" min-width="160"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" fixed="right" width="210">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row,1)">修改</el-button>
                  <el-button v-show="scope.row.endTime==null" size="mini" type="warning" @click="handleEdit(scope.row,2)">出场</el-button>
                  <el-button v-show="scope.row.endTime!=null" size="mini" :disabled="scope.row.status==1" type="info" @click="handleEdit(scope.row,3)">缴费</el-button>
                  <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
          </el-table-column>
      </el-table>
      <el-button @click="openAddParkingView" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 15px;margin-left: 10px;">添加</el-button>
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
     
  
    <!-- 停车信息 -->
    <el-dialog title="车位信息" :visible.sync="parkingInfoVisible" width="28%" center style="position: absolute;">
        <el-form ref="editForm" :model="editForm" label-width="100px" size="mini">
            <el-form-item label="停车场：">{{ editForm.parkingLotName }}</el-form-item>
            <el-form-item label="区域：">{{ editForm.area }}</el-form-item>
            <el-form-item label="车位号：">{{ editForm.stallNumber }}</el-form-item>
            <el-form-item label="车牌号：">{{ editForm.carNumber }}</el-form-item>
            <el-form-item label="入场时间">
                <el-date-picker
                    v-model="editForm.startTime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    type="datetime"
                    placeholder="选择日期时间"
                    align="right" style="width:180px">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="出场时间">
                <el-date-picker
                    v-model="editForm.endTime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    type="datetime"
                    placeholder="选择日期时间"
                    align="right" style="width:180px">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="缴费类型：">
                    <el-select v-model="editForm.billTypeId" placeholder="全部" size="small" style="width:100px">
                    <el-option label="停车费" :value="0"></el-option>
                    <el-option label="免费" :value="1"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="状态：">
                    <el-select v-model="editForm.status" placeholder="全部" size="small" style="width:100px">
                    <el-option label="未缴费" :value="0"></el-option>
                    <el-option label="已缴费" :value="1"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="editStallDialog">保存</el-button>
                <el-button @click="parkingInfoVisible=false">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
    <!-- 添加停车信息 -->
    <el-dialog title="添加停车信息" :visible.sync="addParkingVisible" width="28%" center style="position: absolute;">
        <el-form ref="addForm" :model="addForm" label-width="100px" size="mini">
            <el-form-item label="停车场：">
                <el-select v-model="addForm.parkingLotIndex"  @change="onAddParkingLotIndexChange"  placeholder="全部" size="small" style="width:150px">
                    <el-option
                      v-for="(item,index) in parkingLotInfos"
                      :key="item.id"
                      :label="item.name"
                      :value="index">
                  </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="区域：">
                <el-select v-model="addForm.area" @change="onAddAreaChange"  :disabled="addForm.parkingLotIndex===''" placeholder="全部" size="small" style="width:150px">
                    <el-option
                      v-for="index in addForm.areas"
                      :key="index"
                      :label="index+'区'"
                      :value="index">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="车位号：">
                <el-select v-model="addForm.stallId" :disabled="addForm.area===''" placeholder="全部" size="small" style="width:150px">
                    <el-option
                        v-for="item in addForm.stalls"
                        :key="item.id"
                        :label="item.stallNumber"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="车牌号：">
                <el-input size="small" v-model="addForm.carNumber" maxlength="10" placeholder="请输入车牌号" style="width:150px"></el-input>
            </el-form-item>
            <el-form-item label="车主：">
                <el-input size="small" v-model="addForm.ownerName" maxlength="20" placeholder="请输入车牌号" style="width:150px"></el-input>
            </el-form-item>
            <el-form-item label="联系电话：">
                <el-input size="small" v-model="addForm.phoneNumber" maxlength="20" placeholder="请输入车牌号" style="width:150px"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addParking">添加</el-button>
                <el-button>取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  export default {
      components: {Pagination},
      name:"ParkingMsg",
      data(){
          return {
            searchValue:'',
              parkingLotIndex:'',
              parkingLotId:'',
              status:'',
              area:'',
              areas:[],
              parkingLotInfos:[],
              tableData:[],
              userInfo:{},
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              parkingInfoVisible:false,
              addParkingVisible:false,
              form:'',
              loading:'',
              value1:'',
              editForm:{
                parkingLotName:'',
                area:'',
                stallNumber:'',
                carNumber:'',
                startTime:'',
                endTime:'',
                billTypeId:'',
                status:''
              },
              freeStall:[],
              addForm:{
                parkingLotIndex:'',
                areas:[],
                area:'',
                stallNumber:'',
                ownerName:'',
                phoneNumber:'',
                carNumber:'',
                stalls:''
              }
          }
      },
      methods:{
          handleEdit(row,type) {
            if(type==1){
                Object.assign(this.editForm,row);
                this.parkingInfoVisible = true;
            }else if(type==2){  //出场
                let endTime = this.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                this.editStall(row.id,row.startTime,endTime,row.billTypeId,row.status)
            }else if(type==3){ //缴费
                this.editStall(row.id,row.startTime,row.endTime,row.billTypeId,1)
            }
          },
          handleDelete(row) {
              this.$confirm('是否删除此停车信息?', '删除停车提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                this.deleteParking(row.id)
              }).catch(() => {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  });          
              });
          },
          pageChangeHandle(param) {
            let currentPage = param.currentPage
            let parkingLotId = this.parkingLotId
            let area = this.area
            let status = this.status
            let searchValue = this.searchValue
            this.searchParkingRecords(currentPage,parkingLotId,area,status,searchValue)
          },
          handleSelectionChange(selections) {
              console.log(selections);
              if(selections.length!==0){
                  this.delSelectItemsBtnShow = true
              }else {
                  this.delSelectItemsBtnShow = false
              }
          },
          onParkingLotIndexChange(){
            this.parkingLotId = this.parkingLotInfos[this.parkingLotIndex].id
            this.areas = this.parkingLotInfos[this.parkingLotIndex].areas
          },
          onAddParkingLotIndexChange(){
            this.addForm.areas = this.parkingLotInfos[this.addForm.parkingLotIndex].areas
          },
          onAddAreaChange(){
            let page = this
            let result = []
            this.freeStall.forEach((e)=>{
                if(e.parkingLotId == page.parkingLotInfos[page.addForm.parkingLotIndex].id && page.addForm.area == e.area) {
                    result.push(e)
                }
            })
            this.addForm.stalls = result
          },
          openAddParkingView(){
            this.addParkingVisible = true
            this.addForm = {
                parkingLotIndex:'',
                areas:[],
                area:'',
                stallNumber:'',
                ownerName:'',
                phoneNumber:'',
                carNumber:'',
                stalls:''
              }
            this.getFreeStall()
          },
          searchParkingRecords(currentPage,parkingLotId,area,status,searchValue){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/parking/searchParkingRecords"
            let params = {
                "carNumber":searchValue,
                "parkingLotId": parkingLotId,
                "area": area,
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
            let parkingLotId = this.parkingLotId
            let area = this.area
            let status = this.status
            let searchValue = this.searchValue
            this.searchParkingRecords(currentPage,parkingLotId,area,status,searchValue)
        },
        editStall(id,startTime,endTime,billTypeId,status){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/parking/editParking'
            let params = {
                'id': id,
                'startTime': startTime,
                'endTime': endTime,
                'status': status,
                'billTypeId': billTypeId
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.parkingInfoVisible = false
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
        },
        deleteParking(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/parking/deleteParking'
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
        editStallDialog(){
            let id = this.editForm.id
            let startTime = this.editForm.startTime
            let endTime = this.editForm.endTime
            let billTypeId = this.editForm.billTypeId
            let status = this.editForm.status
            this.editStall(id,startTime,endTime,billTypeId,status)
        },
        format(date, fmt) {
            if (typeof date == 'string') {
            return date;
            }

            if (!fmt) fmt = "yyyy-MM-dd hh:mm:ss";
            var o = {
            "M+": date.getMonth() + 1, //月份
            "d+": date.getDate(), //日
            "H+": date.getHours(), //小时
            "m+": date.getMinutes(), //分
            "s+": date.getSeconds(), //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                return fmt;
        },
        getFreeStall(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            //获取停车场信息
            this.$http.get("/heng/common/parkingLots",{
                headers:{token:token}
            }).then(res=>{
                let result = res.data
                if(result.status === 0){
                    page.parkingLotInfos = result.data
                } else {
                    page.$message.error(result.msg)
                }
            },err=>{
                page.$message.error(err.msg)
            })
        },
        addParking(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/parking/addParking'
            let params = {
                'carNumber': this.addForm.carNumber,
                'ownerName': this.addForm.ownerName,
                'phoneNumber': this.addForm.phoneNumber,
                'stallId': this.addForm.stallId,
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.addParkingVisible = false
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
        //获取停车场信息
        this.$http.get("/heng/parking/freeStall",{
            headers:{token:token}
        }).then(res=>{
            let result = res.data
            if(result.status === 0){
                page.freeStall = result.data
            } else {
                page.$message.error(result.msg)
            }
        },err=>{
            page.$message.error(err.msg)
        })
        //获取停车记录信息  
        this.searchParkingRecords(1,'','','','')
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