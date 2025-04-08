<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
        <el-form-item label="业主：">
            <el-input size="mini" v-model="searchValue" maxlength="10" placeholder="请输入业主名" style="width:110px"></el-input>
        </el-form-item>

        <el-form-item label="楼宇：">
            <el-select v-model="buildingIndex"  @change="onBuildingIndexChange"  placeholder="全部" size="mini" style="width:100px">
                <el-option
                    v-for="(item,index) in buildingInfos"
                    :key="item.id"
                    :label="item.name"
                    :value="index">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="单元：">
            <el-select v-model="unit" :disabled="buildingIndex===''" placeholder="全部" size="mini" style="width:100px">
                <el-option
                    v-for="index in units"
                    :key="index"
                    :label="index+'单元'"
                    :value="index">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="楼层：">
            <el-select v-model="floor"  :disabled="buildingIndex===''" placeholder="全部" size="mini" style="width:100px">
                <el-option
                    v-for="index in floors"
                    :key="index"
                    :label="index+'层'"
                    :value="index">
                </el-option>
            </el-select>
        </el-form-item>
  
        <el-form-item>
            <el-button size="mini" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
        </el-form-item>
      </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" style="width: 100%" size="mini" v-loading="loading"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
          <el-table-column prop="id" label="编号" width="120"></el-table-column>
          <el-table-column label="地址" min-width="160">
            <template slot-scope="scope">
                {{scope.row.building + scope.row.unit + '单元' + scope.row.floor + '层' + scope.row.name}}
            </template>
          </el-table-column>
          <el-table-column label="业主" min-width="120">
            <template slot-scope="scope">
                {{scope.row.owner.name}}
            </template>
          </el-table-column>
          <el-table-column label="状态">
            <template slot-scope="scope">
                {{scope.row.status===1?"已售出":"未售出"}}
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="房屋信息" min-width="160"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" fixed="right" width="80">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleSelect(scope.row)">选择</el-button>
              </template>
          </el-table-column>
      </el-table>
      <!-- 分页 -->
      <Pagination v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
    </div>
  </template>
  
  <script>
  import Pagination from './Pagination.vue'
  export default {
      components: {Pagination},
      name:"SearchHouse",
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
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              loading:false
          }
      },
      methods:{
        handleSelect(row) {
            this.$emit('onHouseSeleted',row)
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
    }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  </style>