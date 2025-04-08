<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
          <el-form-item label="车位号：">
              <el-input size="small" v-model="searchValue" maxlength="10" placeholder="请输入车位号" style="width:110px"></el-input>
          </el-form-item>
  
          <el-form-item label="停车场：">
              <el-select v-model="parkingLotIndex"  @change="onParkingLotIndexChange"  placeholder="全部" size="small" style="width:120px">
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
                  <el-option label="未售出" value="0"></el-option>
                  <el-option label="已售出" value="1"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          </el-form-item>
      </el-form>
      
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" size="small" v-loading="loading"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
          <el-table-column prop="id" label="编号" min-width="100"></el-table-column>
          <el-table-column prop="parkingLotName" label="停车场" min-width="120"></el-table-column>
          <el-table-column label="区域" min-width="120">
            <template slot-scope="scope">
                {{scope.row.area}}区
            </template>
          </el-table-column>
          <el-table-column prop="stallNumber" label="车位号" min-width="120"></el-table-column>
          <el-table-column label="状态">
            <template slot-scope="scope">
                {{statusName[scope.row.status]}}
            </template>
          </el-table-column>
          <el-table-column prop="ownerName" label="业主" min-width="120"></el-table-column>
          <el-table-column prop="phoneNumber" label="联系电话" min-width="120"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" width="160">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">修改</el-button>
                  <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
          </el-table-column>
      </el-table>
      <el-button @click="openAddStallView" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;margin-left: 10px;">添加</el-button>
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
      
    <!-- 车位信息 -->
    <el-dialog title="车位信息" :visible.sync="stallInfoVisible" width="28%" center style="position: absolute;">
        <el-form ref="editForm" :model="editForm" label-width="100px" size="mini">
            <el-form-item label="停车场：">{{ editForm.parkingLotName }}</el-form-item>
            <el-form-item label="区域：">{{ editForm.area }}区</el-form-item>
            <el-form-item label="车位号：">{{ editForm.stallNumber }}</el-form-item>
            <el-form-item label="状态：">
                <el-select v-model="editForm.status" placeholder="全部" size="small" style="width:100px">
                    <el-option label="未售出" :value="0"></el-option>
                    <el-option label="已售出" :value="1"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="业主：">
                <div class="name-wrapper">
                    <el-tag size="medium " style="float: left;display: inline;">{{ editForm.ownerName }}</el-tag>
                    <el-button type="primary" icon="el-icon-edit" size="mini" circle style="margin-left: 20px;" @click="searchOwnerVisible=true"></el-button>
                </div>
            </el-form-item>
            <el-form-item label="关联车辆：" v-loading="carLoading">
                <el-tag v-show="car.delMark!=1" v-for="(car,index) in editForm.cars" :key="index" size="mini"  closable :disable-transitions="false" @close="handleClose(index)">
                    {{car.carNumber}}
                </el-tag>
                <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" 
                    ref="saveTagInput" size="mini" @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
                </el-input>
                <el-button v-else class="button-new-tag" size="mini" @click="showInput">+添加</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="editStall">保存</el-button>
                <el-button>取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
    <!-- 添加车位 -->
    <el-dialog title="添加车位" :visible.sync="addStallVisible" width="28%" center style="position: absolute;">
        <el-form ref="addForm" :model="addForm" label-width="100px" size="mini">
            <el-form-item label="停车场：">
                <el-select v-model="addForm.parkingLotIndex"  @change="onAddParkingLotIndexChange"  placeholder="全部" size="small" style="width:120px">
                    <el-option
                      v-for="(item,index) in parkingLotInfos"
                      :key="item.id"
                      :label="item.name"
                      :value="index">
                  </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="区域：">
                <el-select v-model="addForm.area" :disabled="addForm.parkingLotIndex===''" placeholder="全部" size="small" style="width:120px">
                    <el-option
                      v-for="index in addForm.areas"
                      :key="index"
                      :label="index+'区'"
                      :value="index">
                  </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="车位号：">
                <el-input size="small" v-model="addForm.stallNumber" maxlength="10" placeholder="请输入车位号" style="width:120px"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addStall">添加</el-button>
                <el-button @click="addStallVisible=false">取消</el-button>
            </el-form-item>
        </el-form>
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
  import SearchOwner from '../../components/SearchOwner.vue'
  export default {
      components: {Pagination,SearchOwner},
      name:"StallMsg",
      data(){
          return {
              searchValue:'',
              parkingLotIndex:'',
              parkingLotId:'',
              status:'',
              areas:[],
              parkingLotInfos:[],
              tableData:[],
              editForm:{
                id:'',
                status:'',
                ownerId:'',
                ownerName:'',
                cars:[],
                backupCars:[]
              },
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              stallInfoVisible:false,
              addStallVisible:false,
              inputVisible: false,
              inputValue: '',
              area:'',
              loading:'',
              statusName:['未售出','已售出'],
              addForm:{
                parkingLotId:'',
                parkingLotIndex:'',
                area:'',
                areas:[],
                stallNumber:''
              },
              searchOwnerVisible:false,
              carLoading:false
          }
      },
      methods:{
          handleEdit(row) {
              Object.assign(this.editForm, row)
              this.stallInfoVisible = true;
              this.getStallCars(row.id)
          },
          handleDelete(row) {
              this.$confirm('是否删除此车位信息?', '删除车位提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                    this.deleteStall(row.id)
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
            this.searchStall(currentPage,parkingLotId,area,status,searchValue)
          },
          onParkingLotIndexChange(){
            this.parkingLotId = this.parkingLotInfos[this.parkingLotIndex].id
            this.areas = this.parkingLotInfos[this.parkingLotIndex].areas
          },
          onAddParkingLotIndexChange(){
            this.addForm.parkingLotId = this.parkingLotInfos[this.addForm.parkingLotIndex].id
            this.addForm.areas = this.parkingLotInfos[this.addForm.parkingLotIndex].areas
          },
          openAddStallView(){
            this.addStallVisible = true
          },
          handleClose(index) {
            this.editForm.cars[index].delMark = 1
            this.editForm.backupCars.push(this.editForm.cars[index])
            this.editForm.cars.splice(index,1);
          },
          showInput() {
            this.inputVisible = true;
            this.$nextTick(() => {
            this.$refs.saveTagInput.$refs.input.focus();
            });
          },
          handleInputConfirm() {
            let inputValue = this.inputValue;
            if (inputValue) {
                this.editForm.cars.push({'carNumber':inputValue});
            }
            this.inputVisible = false;
            this.inputValue = '';
          },
          searchStall(currentPage,parkingLotId,area,status,searchValue){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/parking/searchStall"
            let params = {
                "stallNumber":searchValue,
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
            this.searchStall(currentPage,parkingLotId,area,status,searchValue)
        },
        editStall(){
            let cars = this.editForm.cars.concat(this.editForm.backupCars)
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/parking/editStall'
            let params = {
                'id': this.editForm.id,
                'status': this.editForm.status,
                'ownerId': this.editForm.ownerId,
                'ownerName': this.editForm.ownerName,
                'phoneNumber': this.editForm.phoneNumber,
                'cars':cars
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.stallInfoVisible = false
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
        },
        addStall(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/parking/addStall'
            let params = {
                'parkingLotId': this.addForm.parkingLotId,
                'area': this.addForm.area,
                'stallNumber': this.addForm.stallNumber
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.addStallVisible = false
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
        },
        setOwnerInfo(owner){
            this.editForm.ownerId = owner.id
            this.editForm.ownerName = owner.name
            this.editForm.phoneNumber = owner.phoneNumber
            this.searchOwnerVisible = false
        },
        getStallCars(stallId){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/parking/stallBindCars"
            let params = {
                "id":stallId,
            }
            this.carLoading = true
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.editForm.cars = result.data
                }
                page.carLoading = false
            }).catch(err=>{
                console.log(err);
                page.carLoading = false
            })
        },
        deleteStall(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/parking/deleteStall'
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
        }
      },
      created(){
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
        //获取业主信息  
        this.searchStall(1,'','','','')
      },
      computed:{
        showCarList(){
            return this.editForm.cars.filter(car=>car.delMark!=1)
        }
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
    .el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
  </style>