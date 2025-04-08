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
              <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          </el-form-item>
      </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" size="small" v-loading="loading"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
          <el-table-column prop="id" label="编号" width="120"></el-table-column>
          <el-table-column label="地址" width="160">
            <template slot-scope="scope">
                {{scope.row.address}}
            </template>>
          </el-table-column>
          <el-table-column prop="name" label="业主" width="120"></el-table-column>
          <el-table-column label="性别">
            <template slot-scope="scope">
                {{scope.row.gender===1?'男':'女'}}
            </template>>
          </el-table-column>
          <el-table-column prop="phoneNumber" label="电话" width="150"></el-table-column>
          <el-table-column prop="email" label="邮箱" width="150"></el-table-column>
          <el-table-column prop="birthday" label="生日" width="100"></el-table-column>
          <el-table-column prop="identification" label="身份证号" width="150"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" width="160" fixed="right">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">修改</el-button>
                  <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
          </el-table-column>
      </el-table> 
      <el-button @click="addOwnerVisible=true" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;float: left;">添加</el-button>
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
  
      <!-- 业主信息 -->
    <el-dialog title="业主信息" :visible.sync="ownerInfoVisible" width="40%" center style="position: absolute;" top="10vh">
        <el-form ref="editForm" :model="editForm" label-width="100px" size="mini">
            <el-form-item label="编号：">
                {{ editForm.id }}
            </el-form-item>
            <el-form-item label="住址：">
                {{ editForm.address }}
            </el-form-item>
            <el-form-item label="业主：">
                <el-input v-model="editForm.name" style="width:150px;"></el-input>
            </el-form-item>
            <el-form-item label="性别：">
                <el-radio-group v-model="editForm.gender">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="0">女</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="电话：">
                <el-input v-model="editForm.phoneNumber" style="width:200px;"></el-input>
            </el-form-item>
            <el-form-item label="邮箱：">
                <el-input v-model="editForm.email" style="width:200px;"></el-input>
            </el-form-item>
            <el-form-item label="出生日期：">
                <el-date-picker type="date" placeholder="选择日期" value-format="yyyy-MM-dd" v-model="editForm.birthday" style="width:130px"></el-date-picker>
            </el-form-item>
            <el-form-item label="身份证号：">
                <el-input v-model="editForm.identification" style="width:200px;"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="editOwner">保存</el-button>
                <el-button @click="ownerInfoVisible=false">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>

    <!-- 添加业主 -->
    <el-dialog title="添加业主" :visible.sync="addOwnerVisible" width="40%" center style="position: absolute;" top="10vh">
        <el-form ref="addForm" :model="addForm" label-width="100px" size="mini">
            <el-form-item label="业主：">
                <el-input v-model="addForm.name" style="width:150px;"></el-input>
            </el-form-item>
            <el-form-item label="性别：">
                <el-radio-group v-model="addForm.gender">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="0">女</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="电话：">
                <el-input v-model="addForm.phoneNumber" style="width:200px;"></el-input>
            </el-form-item>
            <el-form-item label="邮箱：">
                <el-input v-model="addForm.email" style="width:200px;"></el-input>
            </el-form-item>
            <el-form-item label="出生日期：">
                <el-date-picker type="date" placeholder="选择日期" value-format="yyyy-MM-dd" v-model="addForm.birthday" style="width:130px"></el-date-picker>
            </el-form-item>
            <el-form-item label="身份证号：">
                <el-input v-model="addForm.identification" style="width:200px;"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addOwner">添加</el-button>
                <el-button @click="addOwnerVisible=false">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  export default {
      components: {Pagination},
      name:"OwnerMsg",
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
              ownerInfoVisible:false,
              loading:true,
              editForm:{
                id:'',
                address:'',
                name:'',
                gender:'',
                phoneNumber:'',
                email:'',
                birthday:'',
                identification:''
              },
              addOwnerVisible:false,
              addForm:{
                name:'',
                gender:'',
                phoneNumber:'',
                email:'',
                birthday:'',
                identification:''
              }
          }
      },
      methods:{
          handleEdit(row) {
              this.ownerInfoVisible = true;
              this.editForm = row
          },
          handleDelete(row) {
              this.$confirm('是否删除此业主信息?', '删除业主提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                  this.deleteOwner(row.id)
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
            this.searchOwner(currentPage,buildingId,unit,floor,searchValue)
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
          searchOwner(currentPage,buildingId,unit,floor,searchValue){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/owner/searchOwner"
            let params = {
                "name":searchValue,
                "buildingId": buildingId,
                "unit": unit,
                "floor": floor,
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
            this.searchOwner(currentPage,buildingId,unit,floor,searchValue)
          },
          editOwner(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/owner/editOwner'
            let params = {
                'id': this.editForm.id,
                'name': this.editForm.name,
                'gender': this.editForm.gender,
                'phoneNumber': this.editForm.phoneNumber,
                'email': this.editForm.email,
                'birthday': this.editForm.birthday,
                'identification': this.editForm.identification
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.ownerInfoVisible = false
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          deleteOwner(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/owner/deleteOwner'
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
          addOwner(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/owner/addOwner'
            let params = {
                'name': this.addForm.name,
                'gender': this.addForm.gender,
                'phoneNumber': this.addForm.phoneNumber,
                'email': this.addForm.email,
                'birthday': this.addForm.birthday,
                'identification': this.addForm.identification
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: result.msg,
                        type: 'success'
                    });
                    page.addOwnerVisible = false
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
        //获取业主信息  
        this.searchOwner(1,'','','','')
      }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  </style>