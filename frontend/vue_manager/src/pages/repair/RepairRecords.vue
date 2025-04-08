<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
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
          <el-table-column prop="id" label="编号" min-width="120"></el-table-column>
          <el-table-column label="地址" min-width="160">
            <template slot-scope="scope">
                {{scope.row.buildingName + scope.row.unit}}单元{{scope.row.floor}}层{{scope.row.houseName}}
            </template>>
          </el-table-column>
          <el-table-column label="类型" min-width="120">
            <template slot-scope="scope">
                {{typeName[scope.row.type]}}
            </template>>
          </el-table-column>
          <el-table-column prop="contents" label="报修内容" min-width="260" show-overflow-tooltip></el-table-column>
          <el-table-column prop="phoneNumber" label="预留电话" min-width="120"></el-table-column>
          <el-table-column prop="createTime" label="申报时间" min-width="160"></el-table-column>
          <el-table-column label="状态" min-width="100">
            <template slot-scope="scope">
                {{statusName[scope.row.status]}}
            </template>>
          </el-table-column>
          <el-table-column prop="repairer" label="维修员" min-width="120"></el-table-column>
          <el-table-column prop="updateTime" label="处理时间" min-width="160"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" fixed="right" width="150">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                  <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
          </el-table-column>
      </el-table> 
      <el-button @click="openAddView" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;margin-left: 10px;">我要报修</el-button>
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>

        <!-- 报修信息 -->
        <el-dialog title="报修信息" :visible.sync="repairInfoVisible" width="750px" center style="position: absolute;">
            <el-upload
                class="el-upload"
                :disabled="editForm.status!=0"
                style="margin-right: 10px;vertical-align: top;display: inline-block;"
                action=""
                :show-file-list="false"
                :on-change="handleEditPhotoChange"
                accept="image/jpeg,image/jpg,image/png"
                list-type="picture"
                :auto-upload="false"
                :before-upload="beforeAvatarUpload">
                <img v-if="editForm.imageUrl" :src="editForm.imageUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <el-descriptions style="display: inline-block;width: 500px;">
                    <el-descriptions-item label="地址" span="3">{{editForm.buildingName + editForm.unit}}单元{{editForm.floor}}层{{editForm.houseName}}</el-descriptions-item>
                    <el-descriptions-item label="状态">{{statusName[editForm.status]}}</el-descriptions-item>
                    <el-descriptions-item label="申报时间" span="2">{{editForm.createTime}}</el-descriptions-item>
                    <el-descriptions-item label="检修员">{{editForm.repairer}} </el-descriptions-item>
                    <el-descriptions-item label="处理时间" span="2">{{editForm.updateTime}}</el-descriptions-item>
                    <el-descriptions-item label="类型">
                        <el-select v-if="editForm.status==0" v-model="editForm.type" placeholder="全部" size="mini" style="width:100px">
                            <el-option label="房屋漏水" :value="1"></el-option>
                            <el-option label="开裂" :value="2"></el-option>
                            <el-option label="管道堵塞" :value="3"></el-option>
                        </el-select>
                        <span v-if="editForm.status!=0">{{typeName[editForm.type]}}</span>
                    </el-descriptions-item>
                    <el-descriptions-item label="预留电话" span="2">
                        <el-input v-if="editForm.status==0" v-model="editForm.phoneNumber" placeholder="请输入内容" size="mini" style="width:110px;"></el-input>
                        <span v-if="editForm.status!=0">{{editForm.phoneNumber}}</span>
                    </el-descriptions-item>
                    <el-descriptions-item label="报修内容" span="3">
                        <el-input
                            v-if="editForm.status==0"
                            type="textarea"
                            placeholder="请输入内容"
                            v-model="editForm.contents"
                            maxlength="30"
                            show-word-limit
                            ></el-input>
                        <span v-if="editForm.status!=0">{{editForm.contents}}</span>
                        </el-descriptions-item>
                    </el-descriptions>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" size="mini" :disabled="editForm.status!=0" @click="editRepair">保存</el-button>
                <el-button size="mini">取消</el-button>
            </div>
        </el-dialog>

        <!-- 添加报修信息 -->
        <el-dialog title="添加报修信息" :visible.sync="addViewVisible" width="750px" center style="position: absolute;">
            <el-upload
                class="el-upload"
                ref="upload"
                :data="addForm"
                style="margin-right: 10px;vertical-align: top;display: inline-block;"
                action=""
                :show-file-list="false"
                :on-change="handleAvatarSuccess"
                accept="image/jpeg,image/jpg,image/png"
                list-type="picture"
                :auto-upload="false"
                :before-upload="beforeAvatarUpload">
                <img v-if="imageUrl" :src="imageUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <el-descriptions style="display: inline-block;width: 500px;">
                <el-descriptions-item label="地址" span="3">
                    <el-select v-model="addForm.houseId" placeholder="全部" size="mini" style="width:200px">
                        <el-option
                            v-for="item in ownerHouses"
                            :key="item.id"
                            :label="item.buildingName + item.unit + '单元' + item.floor + '层' + item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="类型">
                    <el-select v-model="addForm.type" placeholder="全部" size="mini" style="width:100px">
                        <el-option label="物业费" value="0"></el-option>
                        <el-option label="水费" value="1"></el-option>
                        <el-option label="电费" value="2"></el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="预留电话" span="2">
                    <el-input v-model="addForm.phoneNumber" placeholder="请输入内容" size="mini" style="width:110px;"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="报修内容" span="3">
                    <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="addForm.contents"
                        maxlength="30"
                        show-word-limit
                        ></el-input>
                    </el-descriptions-item>
            </el-descriptions>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" size="mini" @click="addRepair">提交</el-button>
                <el-button size="mini">取消</el-button>
            </div>
        </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  export default {
      components: {Pagination},
      name:"RepairRecords",
      data(){
          return {
              tableData:[],
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              repairInfoVisible:false,
              imageUrl: '',
              addViewVisible:false,
              loading:false,
              type:'',
              status:'',
              textarea:'',
              addForm:{
                houseId:'',
                type:'',
                phoneNumber:'',
                contents:''
              },
              photo:'',
              ownerHouses:[],
              file:'',
              editForm:{
                phoneNumber:'',
                type:'',
                contents:'',
                status:0,
                photo:'',
                repairer:'',
                ownerName:'',
                imageUrl:'',
                file:null
              },
              statusName:['未处理','已指派','已处理'],
              typeName:['','房屋漏水','开裂','管道堵塞']
          }
      },
      methods:{
          selectSearchType(command){
              this.searchType = Number(command)
          },
          handleEdit(row) {
              this.repairInfoVisible = true;
              Object.assign(this.editForm,row);
              this.editForm.imageUrl = this.editForm.photo
              this.editForm.file = null
          },
          openAddView() {
              this.addViewVisible = true;

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
              console.log(param)
          },
          handleSelectionChange(selections) {
              console.log(selections);
          },
          handleEditPhotoChange(file){
            this.editForm.imageUrl = URL.createObjectURL(file.raw);
            this.editForm.file = file
          },
          handleAvatarSuccess(file) {
                this.imageUrl = URL.createObjectURL(file.raw);
                this.file = file
            },
            beforeAvatarUpload(file) {
                const isLt2M = file.size / 1024 / 1024 < 2;
                if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
                }
                return isLt2M;
            },
          myRepairRecords(currentPage,type,status){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/repair/myRepairRecords"
            let params = {
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
            let type = this.type
            let status = this.status
            this.myRepairRecords(currentPage,type,status)
          },
          getOwnerHouse(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            //获取楼宇信息
            this.$http.get("/heng/house/ownerHouses",{
                headers:{token:token}
            }).then(res=>{
                let result = res.data
                if(result.status === 0){
                    page.ownerHouses = result.data
                } else {
                    page.$message.error(result.msg)
                }
            },err=>{
                page.$message.error(err.msg)
            })
          },
          addRepair(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/repair/addRepair"
            let formData = new FormData();
            formData.append('houseId', this.addForm.houseId);
            formData.append('type', this.addForm.type);
            formData.append('phoneNumber', this.addForm.phoneNumber);
            formData.append('contents', this.addForm.contents);
            formData.append('photo', this.file.raw,this.file.raw.name);
            this.$http.post(url, formData, {headers:{'token':token, 'Content-Type':'multipart/form-data'}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '提交成功',
                        type: 'success'
                    });
                    page.addViewVisible = false
                    page.myRepairRecords(1,'','')
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          editRepair(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/repair/editRepair"
            let formData = new FormData();
            formData.append('id', this.editForm.id);
            formData.append('type', this.editForm.type);
            formData.append('phoneNumber', this.editForm.phoneNumber);
            formData.append('contents', this.editForm.contents);
            console.log(this.editForm.file)
            if(this.editForm.file != null){
                console.log(1)
                formData.append('photo', this.editForm.file.raw,this.editForm.file.raw.name);
            }
            this.$http.post(url, formData, {headers:{'token':token, 'Content-Type':'multipart/form-data'}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '修改成功',
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
        //获取报修信息  
        this.myRepairRecords(1,'','')
        //获取用户房屋信息
        this.getOwnerHouse()
      }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
  </style>