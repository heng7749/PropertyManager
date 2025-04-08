<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
            <el-form-item>
                <el-dropdown @command="selectSearchType"  class="dropdownTag">
                    <span class="el-dropdown-link">
                    {{searchType==1?'物品名模式':'申请人模式'}}<i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command='1'>物品名</el-dropdown-item>
                        <el-dropdown-item command='2'>申请人</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-form-item>
            <el-form-item>
                <el-input size="small" v-model="searchValue" maxlength="10" :placeholder="searchType==1?'请输入物品名':'请输入申请人'" style="width:110px;"></el-input>
            </el-form-item>
          <el-form-item label="申请部门:">
              <el-select v-model="departmentId" placeholder="全部" size="small" style="width:100px">
                  <el-option
                      v-for="item in departments"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="类型:">
              <el-select v-model="type" placeholder="全部" size="small" style="width:100px">
                  <el-option label="办公用品" value="1"></el-option>
                  <el-option label="家具" value="2"></el-option>
                  <el-option label="生活用品" value="3"></el-option>
                  <el-option label="安全保障" value="4"></el-option>
                  <el-option label="工具" value="5"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="状态:">
              <el-select v-model="status" placeholder="全部" size="small" style="width:100px">
                <el-option label="未审批" value="0"></el-option>
                <el-option label="已通过" value="1"></el-option>
                <el-option label="未通过" value="2"></el-option>
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
                </template>>
          </el-table-column>
          <el-table-column prop="name" label="物品名" min-width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="model" label="型号" min-width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="producer" label="厂商" min-width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="number" label="数量" min-width="100"></el-table-column>
          <el-table-column prop="price" label="价格" min-width="120"></el-table-column>
          <el-table-column prop="applicant" label="申请人" min-width="120"></el-table-column>
          <el-table-column prop="departmentName" label="申请部门" min-width="120"></el-table-column>
          <el-table-column prop="createTime" label="申请时间" min-width="160"></el-table-column>
          <el-table-column label="状态" min-width="100">
                <template slot-scope="scope">
                    {{statusName[scope.row.status]}}
                </template>>
          </el-table-column>
          <el-table-column prop="conductor" label="处理人" min-width="160"></el-table-column>
          <el-table-column prop="updateTime" label="处理时间" min-width="160"></el-table-column>
          <el-table-column prop="remark" label="备注" min-width="260" show-overflow-tooltip></el-table-column>
          <el-table-column prop="feedback" label="反馈" min-width="260" show-overflow-tooltip></el-table-column>
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

        <!-- 采购信息 -->
        <el-dialog title="采购信息" :visible.sync="purchaseInfoVisible" width="60%" center>
            <el-descriptions column="2" style="display: inline-block;">
                <el-descriptions-item label="物品名">{{editForm.name}}</el-descriptions-item>
                <el-descriptions-item label="类型">{{ typeName[editForm.type] }}</el-descriptions-item>
                <el-descriptions-item label="型号">{{ editForm.model }}</el-descriptions-item>
                <el-descriptions-item label="厂商">{{ editForm.producer }}</el-descriptions-item>
                <el-descriptions-item label="数量">{{editForm.number}}</el-descriptions-item>
                <el-descriptions-item label="价格">{{editForm.price}}</el-descriptions-item>
                <el-descriptions-item label="申请人">{{editForm.applicant}}</el-descriptions-item>
                <el-descriptions-item label="申请时间">{{editForm.createTime}}</el-descriptions-item>
                <el-descriptions-item label="申请部门">{{editForm.departmentName}}</el-descriptions-item>
                <el-descriptions-item label="处理人">{{editForm.conductor}}</el-descriptions-item>
                <el-descriptions-item label="处理时间" span="2">{{editForm.updateTime}}</el-descriptions-item>
                <el-descriptions-item label="备注" span="2">{{editForm.remark}}</el-descriptions-item>
                <el-descriptions-item label="状态" span="2">
                    <el-select v-model="editForm.status" placeholder="请选择状态" size="mini" style="width:90px;">
                        <el-option label="未审批" :value="0"></el-option>
                        <el-option label="已通过" :value="1"></el-option>
                        <el-option label="未通过" :value="2"></el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="处理反馈" span="2">
                    <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="editForm.feedback"
                        maxlength="500"
                        show-word-limit
                        ></el-input>
                </el-descriptions-item>
            </el-descriptions>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" size="mini" @click="conductPurchase">保存</el-button>
                <el-button size="mini" @click="purchaseInfoVisible=false">取消</el-button>
            </div>
        </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  export default {
      components: {Pagination},
      name:"PurchasingMsg",
      data(){
          return {
              searchType: 1,
              searchValue:'',
              departmentId:'',
              departments:[],
              tableData:[],
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              purchaseInfoVisible:false,
              loading:false,
              status:'',
              type:'',
              statusName:['未审批','已通过','未通过'],
              typeName:['','办公用品','家具','生活用品','安全保障','工具'],
              editForm:{
                id:'',
                name:'',
                model:'',
                producer:'',
                price:'',
                departmentName:'',
                createTime:'',
                applicant:'',
                remark:'',
                status:'',
                type:'',
                feedback:'',
                number:'',
                updateTime:''
              }
          }
      },
      methods:{
          selectSearchType(command){
              this.searchType = Number(command)
          },
          handleEdit(row) {
              this.purchaseInfoVisible = true;
              Object.assign(this.editForm,row);
          },
          handleDelete(row) {
              this.$confirm('是否删除此采购信息?', '删除采购提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                this.deletePurchase(row.id)
              }).catch(() => {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  });          
              });
          },
          pageChangeHandle(param) {
            let currentPage = param.currentPage
            let name = ''
            let applicant = ''
            if(this.searchType === 1){
                name = this.searchValue
            } else {
                applicant = this.searchValue
            }
            let departmentId = this.departmentId
            let type = this.type
            let status = this.status
            this.searchPurchases(currentPage,name,applicant,departmentId,type,status)
          },
          searchPurchases(currentPage,name,applicant,departmentId,type,status){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/purchase/searchPurchases"
            let params = {
                "name": name,
                "applicant": applicant,
                "departmentId": departmentId,
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
            let name = ''
            let applicant = ''
            if(this.searchType === 1){
                name = this.searchValue
            } else {
                applicant = this.searchValue
            }
            let departmentId = this.departmentId
            let type = this.type
            let status = this.status
            this.searchPurchases(currentPage,name,applicant,departmentId,type,status)
          },
          conductPurchase(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/purchase/conductPurchase"
            let params = {
                "id": this.editForm.id,
                "status": this.editForm.status,
                "feedback": this.editForm.feedback,
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '处理成功',
                        type: 'success'
                    });
                    page.purchaseInfoVisible = false
                    page.search()
                } else {
                    page.$message.error(result.msg);
                }
            }).catch(err=>{
                console.log(err);
            })
          },
          deletePurchase(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/purchase/deletePurchase'
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
        //获取采购信息  
        this.searchPurchases(1,'','')
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