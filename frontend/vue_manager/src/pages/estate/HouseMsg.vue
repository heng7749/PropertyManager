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
  
          <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search" @click="search()">搜索</el-button>
          </el-form-item>
      </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" style="width: 100%" size="small" v-loading="loading"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
          <el-table-column prop="id" label="编号" min-width="120"></el-table-column>
          <el-table-column prop="building" label="楼宇" min-width="120"></el-table-column>
          <el-table-column prop="unit" label="单元" min-width="80"></el-table-column>
          <el-table-column prop="floor" label="楼层" min-width="80"></el-table-column>
          <el-table-column prop="name" label="房号" min-width="120"></el-table-column>
          <el-table-column prop="houseType" label="户型" min-width="120"></el-table-column>
          <el-table-column prop="floorSpace" label="面积" min-width="120"></el-table-column>
          <el-table-column label="状态">
            <template slot-scope="scope">
                {{scope.row.status===1?"已售出":"未售出"}}
            </template>
         </el-table-column>
          <el-table-column label="业主" min-width="120">
            <template slot-scope="scope">
                {{scope.row.owner.name}}
            </template>
         </el-table-column>
          <el-table-column prop="remark" label="房屋信息" width="160"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" fixed="right" width="150">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">修改</el-button>
                  <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
          </el-table-column>
      </el-table> 
      <el-button @click="adddHouseVisible=true" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;float: left;">添加</el-button>
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
  
      <!-- 房屋信息 -->
    <el-dialog title="房屋信息" :visible.sync="houseInfoVisible" width="40%" center style="position: absolute;"  top="2vh">
        <el-form ref="editForm" :model="editForm" label-width="100px" size="mini">
            <el-form-item label="编号：">
                {{editForm.id}}
            </el-form-item>
            <el-form-item label="楼宇：">
                {{editForm.building}}
            </el-form-item>
            <el-form-item label="单元：">
                {{ editForm.unit }}
            </el-form-item>
            <el-form-item label="楼层：">
                {{ editForm.floor }}
            </el-form-item>
            <el-form-item label="房号：">
                <el-input v-model="editForm.name" style="width:100px;"></el-input>
            </el-form-item>
            <el-form-item label="户型：">
                <el-input v-model="editForm.houseType" style="width:100px;"></el-input>
            </el-form-item>
            <el-form-item label="面积：">
                <el-input v-model="editForm.floorSpace" style="width:100px;"></el-input>
                <span style="margin-left: 5px;">平米</span>
            </el-form-item>
            <el-form-item label="状态：">
                <el-radio-group v-model="editForm.status">
                    <el-radio :label="0">未售出</el-radio>
                    <el-radio :label="1">已售出</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="业主：">
                <div class="name-wrapper">
                    <el-tag size="medium" style="float: left;display: inline;">{{ editForm.owner.name }}</el-tag>
                    <el-button type="primary" icon="el-icon-edit" size="mini" circle style="margin-left: 20px;" @click="openSearchOwnerView(2)"></el-button>
                </div>
            </el-form-item>
            <el-form-item label="房屋信息：">
                <el-input v-model="editForm.remark" type="textarea" :rows="2" placeholder="请输入内容" maxlength="20" show-word-limit>
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="editHouse">保存</el-button>
                <el-button @click="houseInfoVisible=false">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
    <!-- 添加房屋 -->
    <el-dialog title="添加房屋" :visible.sync="adddHouseVisible" width="40%" center style="position: absolute;"  top="2vh">
        <el-form ref="addForm" :model="addForm" label-width="100px" size="mini">
            <el-form-item label="楼宇：">
                <el-select v-model="add_BuildingIndex"  @change="onAddBuildingIndexChange"  placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="(item,index) in buildingInfos"
                      :key="item.id"
                      :label="item.name"
                      :value="index">
                  </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="单元：">
                <el-select v-model="addForm.unit" :disabled="add_BuildingIndex===''" placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="index in add_units"
                      :key="index"
                      :label="index+'单元'"
                      :value="index">
                  </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="楼层：">
                <el-select v-model="addForm.floor"  :disabled="add_BuildingIndex===''" placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="index in add_floors"
                      :key="index"
                      :label="index+'层'"
                      :value="index">
                  </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="房号：">
                <el-input v-model="addForm.name" style="width:100px;"></el-input>
            </el-form-item>
            <el-form-item label="户型：">
                <el-input v-model="addForm.houseType" style="width:100px;"></el-input>
            </el-form-item>
            <el-form-item label="面积：">
                <el-input-number v-model="addForm.floorSpace" :precision="2" :step="1" :min="1" :max="1000" style="width:150px;"></el-input-number>
                <span style="margin-left: 5px;">平米</span>
            </el-form-item>
            <el-form-item label="状态：">
                <el-radio-group v-model="addForm.status">
                    <el-radio :label="0">未售出</el-radio>
                    <el-radio :label="1">已售出</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="业主：">
                <div class="name-wrapper">
                    <el-tag size="medium" style="float: left;display: inline;">{{ addForm.owner.name }}</el-tag>
                    <el-button type="primary" icon="el-icon-edit" size="mini" circle style="margin-left: 20px;" @click="openSearchOwnerView(1)"></el-button>
                </div>
            </el-form-item>
            <el-form-item label="房屋信息：">
                <el-input v-model="addForm.remark" type="textarea" :rows="2" placeholder="请输入内容" maxlength="20" show-word-limit>
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addHouse">添加</el-button>
                <el-button @click="adddHouseVisible=false">取消</el-button>
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
      name:"HouseMsg",
      data(){
          return {
              searchType: 1,
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
              houseInfoVisible:false,
              editForm:{
                id:'',
                ownerId:'',
                building:'',
                buildingId:'',
                unit:'',
                floor:'',
                name:'',
                houseType:'',
                floorSpace:'',
                status:'',
                owner:{
                    name:''
                },
                remark:''
              },
              adddHouseVisible:false,
              addForm:{
                id:'',
                ownerId:'',
                building:'',
                buildingId:'',
                unit:'',
                floor:'',
                name:'',
                houseType:'',
                floorSpace:'',
                status:'',
                owner:{
                    name:''
                },
                remark:''
              },
              add_BuildingIndex:'',
              add_units:'',
              add_floors:'',
              loading:true,
              searchOwnerVisible:false,
              ownerType:''
          }
      },
      methods:{
          selectSearchType(command){
              this.searchType = Number(command)
          },
          handleEdit(row) {
              this.houseInfoVisible = true;
              this.editForm = row
          },
          handleDelete(row) {
              this.$confirm('是否删除此房屋信息?', '删除房屋提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                this.deleteHouse(row.id)
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
            this.searchHouse(currentPage,buildingId,unit,floor,searchValue)
          },
          onBuildingIndexChange(){
            this.buildingId = this.buildingInfos[this.buildingIndex].id
            this.units = this.buildingInfos[this.buildingIndex].units
            this.floors = this.buildingInfos[this.buildingIndex].floors
          },
          onUnitChange(){
          },
          buildingIdToName(){
            for(let i=0;i<this.tableData.length;i++) {
                for (let j=0;j<this.buildingInfos.length;j++) {
                    if (this.tableData[i].buildingId === this.buildingInfos[j].id) {
                        this.tableData[i].building = this.buildingInfos[j].name
                        break;
                    }
                }
            }
          },
          searchHouse(currentPage,buildingId,unit,floor,searchValue){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/house/searchHouse"
            let params = {
                'buildingId': buildingId,
                'unit': unit,
                'floor': floor,
                'searchValue': searchValue,
                'params':{
                    'currentPage':currentPage
                }
            }
            
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                console.log(result)
                if(result.data.list.length == 0){
                    page.$message('搜索结果为空');
                } else {
                    page.tableData = result.data.list
                    page.pageMsg.total = result.data.total
                    page.pageMsg.pageSize = result.data.pageSize
                    page.pageMsg.currentPage = result.data.pageNum

                    page.buildingIdToName()
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
            this.searchHouse(currentPage,buildingId,unit,floor,searchValue)
          },
          editHouse(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/house/editHouse'
            let params = {
                'id': this.editForm.id,
                'ownerId': this.editForm.owner.id,
                'name': this.editForm.name,
                'houseType': this.editForm.houseType,
                'floorSpace': this.editForm.floorSpace,
                'status': this.editForm.status,
                'remark': this.editForm.remark
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.houseInfoVisible = false
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          deleteHouse(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/house/deleteHouse'
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
        onAddBuildingIndexChange(){
            this.addForm.buildingId = this.buildingInfos[this.add_BuildingIndex].id
            this.add_units = this.buildingInfos[this.add_BuildingIndex].units
            this.add_floors = this.buildingInfos[this.add_BuildingIndex].floors
          },
          openSearchOwnerView(ownerType){
            this.searchOwnerVisible = true;
            this.ownerType = ownerType;
          },
          setOwnerInfo(owner){
            if(this.ownerType===1) {
                this.addForm.owner.id = owner.id
                this.addForm.owner.name = owner.name
            }else{
                this.editForm.owner.id = owner.id
                this.editForm.owner.name = owner.name
            }
            this.searchOwnerVisible = false
          },
          addHouse(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/house/addHouse'
            let params = {
                'ownerId': this.addForm.owner.id,
                'buildingId': this.addForm.buildingId,
                'unit': this.addForm.unit,
                'floor': this.addForm.floor,
                'name': this.addForm.name,
                'houseType': this.addForm.houseType,
                'floorSpace': this.addForm.floorSpace,
                'status': this.addForm.status,
                'remark': this.addForm.remark
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.adddHouseVisible = false
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
        //获取房屋信息  
        this.searchHouse(1,'','','','')
    }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  </style>