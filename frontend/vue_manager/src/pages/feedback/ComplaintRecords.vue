<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
        <el-form-item label="投诉主题:">
                <el-input size="small" v-model="searchValue" maxlength="10" placeholder="输入投诉主题" style="width:110px;"></el-input>
        </el-form-item>
        <el-form-item label="类型:">
              <el-select v-model="type" placeholder="全部" size="small" style="width:100px">
                <el-option label="保洁卫生" value="1"></el-option>
                <el-option label="安保" value="2"></el-option>
                <el-option label="质量" value="3"></el-option>
                <el-option label="服务态度" value="4"></el-option>
                <el-option label="停车管理" value="5"></el-option>
                <el-option label="物业管理" value="6"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="状态:">
              <el-select v-model="status" placeholder="全部" size="small" style="width:100px">
                <el-option label="未处理" value="0"></el-option>
                <el-option label="已处理" value="1"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          </el-form-item>
      </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" style="width: 100%" size="mini" v-loading="loading"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
          <el-table-column prop="id" label="编号" min-width="120"></el-table-column>
          <el-table-column label="类型" min-width="120">
            <template slot-scope="scope">
                {{typeName[scope.row.type]}}
            </template>
          </el-table-column>
          <el-table-column prop="subject" label="投诉主题" min-width="180" show-overflow-tooltip></el-table-column>
          <el-table-column prop="contents" label="投诉内容" min-width="260" show-overflow-tooltip></el-table-column>
          <el-table-column prop="phoneNumber" label="联系电话" min-width="120"></el-table-column>
          <el-table-column label="状态" min-width="100">
            <template slot-scope="scope">
                {{statusName[scope.row.status]}}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="投诉时间" min-width="160"></el-table-column>
          <el-table-column prop="conductor" label="处理人" min-width="120"></el-table-column>
          <el-table-column prop="updateTime" label="处理时间" min-width="160"></el-table-column>
          <el-table-column prop="feedback" label="处理反馈" min-width="260" show-overflow-tooltip></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" fixed="right" width="150">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                  <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
          </el-table-column>
      </el-table> 
      <el-button @click="openAddView" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;float: left;">投诉</el-button>
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>

        <!-- 投诉信息 -->
        <el-dialog title="投诉信息" :visible.sync="complaintInfoVisible" width="60%" center>
            <el-descriptions :column="2" style="display: inline-block;">
                <el-descriptions-item label="投诉主题">
                    <el-input v-if="editForm.status==0" v-model="editForm.subject" placeholder="请输入投诉主题" maxlength="50" size="mini" style="width: 250px;"></el-input>
                    <span v-if="editForm.status!=0">{{ editForm.subject }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="投诉类型">
                    <el-select v-if="editForm.status==0"  v-model="editForm.type" placeholder="请选择投诉类型" size="mini" style="width:100px">
                        <el-option label="保洁卫生" :value="1"></el-option>
                        <el-option label="安保" :value="2"></el-option>
                        <el-option label="质量" :value="3"></el-option>
                        <el-option label="服务态度" :value="4"></el-option>
                        <el-option label="停车管理" :value="5"></el-option>
                        <el-option label="物业管理" :value="6"></el-option>
                    </el-select>
                    <span v-if="editForm.status!=0">{{typeName[editForm.type]}}</span>
                </el-descriptions-item>
                <el-descriptions-item label="联系电话">
                    <el-input v-if="editForm.status==0" v-model="editForm.phoneNumber" placeholder="请输入联系电话" maxlength="50" size="mini" style="width: 150px;"></el-input>
                    <span v-if="editForm.status!=0">{{ editForm.phoneNumber }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="投诉时间">{{editForm.createTime}}</el-descriptions-item>
                <el-descriptions-item label="状态">{{statusName[editForm.status]}}</el-descriptions-item>
                <el-descriptions-item label="处理时间">{{ editForm.updateTime }}</el-descriptions-item>
                <el-descriptions-item label="投诉内容" span="2">
                    <el-input
                        v-if="editForm.status==0"
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="editForm.contents"
                        maxlength="500"
                        show-word-limit
                        ></el-input>
                    <span v-if="editForm.status!=0">{{editForm.contents}}</span>
                </el-descriptions-item>
                <el-descriptions-item v-if="editForm.status!=0" label="处理反馈" span="2">{{ editForm.feedback }}</el-descriptions-item>
            </el-descriptions>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button :disabled="editForm.status!=0" type="primary" size="mini" @click="editComplaint">保存</el-button>
                <el-button size="mini" @click="complaintInfoVisible=false">取消</el-button>
            </div>
        </el-dialog>

        <!-- 添加投诉 -->
        <el-dialog title="我要投诉" :visible.sync="addViewVisible" width="60%" center>
            <el-form ref="addForm" :model="addForm" label-width="80px" size="mini">
                <el-form-item label="投诉主题">
                    <el-input v-model="addForm.subject" maxlength="50" placeholder="请输入投诉主题"></el-input>
                </el-form-item>
                <el-form-item label="投诉类型">
                    <el-select v-model="addForm.type" placeholder="请选择投诉类型">
                        <el-option label="保洁卫生" :value="1"></el-option>
                        <el-option label="安保" :value="2"></el-option>
                        <el-option label="质量" :value="3"></el-option>
                        <el-option label="服务态度" :value="4"></el-option>
                        <el-option label="停车管理" :value="5"></el-option>
                        <el-option label="物业管理" :value="6"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="联系电话">
                    <el-input v-model="addForm.phoneNumber" style="width:200px" maxlength="50"></el-input>
                </el-form-item>
                <el-form-item label="投诉内容">
                    <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="addForm.contents"
                        maxlength="500"
                        show-word-limit
                        ></el-input>
                </el-form-item>
                <el-form-item>
                    <div style="width: 150px;margin-left: auto;margin-right: auto;">
                        <el-button type="primary" @click="addComplaint">提交</el-button>
                        <el-button @click="addViewVisible=false">取消</el-button>
                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  export default {
      components: {Pagination},
      name:"ComplaintRecords",
      data(){
          return {
              searchValue:'',
              tableData:[],
              userInfoVisible:false,
              userInfo:{},
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              complaintInfoVisible:false,
              addViewVisible:false,
              textarea:'',
              type:'',
              status:'',
              loading:false,
              editForm:{
                id:'',
                ownerName:'',
                phoneNumber:'',
                address:'',
                createTime:'',
                status:'',
                updateTime:'',
                subject:'',
                type:'',
                contents:'',
                feedback:''
              },
              addForm:{
                subject:'',
                phoneNumber:'',
                type:'',
                contents:''
              },
              statusName:['未处理','已处理'],
              typeName:['','保洁卫生','安保','质量','服务态度','停车管理','物业管理'],
          }
      },
      methods:{
          handleEdit(row) {
              this.complaintInfoVisible = true;
              Object.assign(this.editForm,row);
          },
          handleDelete(row) {
              this.$confirm('是否删除此投诉信息?', '删除投诉提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                this.deleteComplaint(row.id)
              }).catch(() => {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  });          
              });
          },
          pageChangeHandle(param) {
            let currentPage = param.currentPage
            let subject = this.searchValue
            let type = this.type
            let status = this.status
            this.myComplaints(currentPage,subject,type,status)
          },
          openAddView() {
              this.addViewVisible = true;
          },
          myComplaints(currentPage,subject,type,status){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/complaint/myComplaints"
            let params = {
                "subject": subject,
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
            let subject = this.searchValue
            let type = this.type
            let status = this.status
            this.myComplaints(currentPage,subject,type,status)
          },
          deleteComplaint(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/complaint/deleteComplaint'
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
          editComplaint(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/complaint/editComplaint"
            let params = {
                "id": this.editForm.id,
                "subject": this.editForm.subject,
                "phoneNumber": this.editForm.phoneNumber,
                "type": this.editForm.type,
                "contents": this.editForm.contents,
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '修改成功',
                        type: 'success'
                    });
                    page.complaintInfoVisible = false
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          addComplaint(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/complaint/addComplaint"
            let params = {
                "subject": this.addForm.subject,
                "phoneNumber": this.addForm.phoneNumber,
                "type": this.addForm.type,
                "contents": this.addForm.contents,
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
        //获取投诉信息  
        this.myComplaints(1,'','','','')
      }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  .el-dropdown-link {
        cursor: pointer;
        color: #409EFF;
        font-size: 14px;
        padding: 0 12px 0 0;
    }
    .dropdownTag{
    width: 110px;
    height: 40px;
    text-align: right;
    line-height: 40px;
}
  </style>