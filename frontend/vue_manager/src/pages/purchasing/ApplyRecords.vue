<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
            <el-form-item label="物品名:">
                <el-input size="small" v-model="searchValue" maxlength="10" placeholder="请输入物品名" style="width:110px;"></el-input>
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
      <el-button @click="openAddView" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;float: left;">申请</el-button>
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>

        <!-- 采购信息 -->
        <el-dialog title="采购信息" :visible.sync="purchaseInfoVisible" width="60%" center>
            <el-descriptions column="2" style="display: inline-block;">
                <el-descriptions-item label="物品名">
                    <el-input v-if="editForm.status==0" v-model="editForm.name" placeholder="请输入物品名" maxlength="100" size="mini" style="width: 250px;"></el-input>
                    <span v-if="editForm.status!=0">{{editForm.name}}</span>
                </el-descriptions-item>
                <el-descriptions-item label="类型">
                    <el-select v-if="editForm.status==0"  v-model="editForm.type" placeholder="请选择物品类型" size="mini" style="width:100px">
                        <el-option label="办公用品" :value="1"></el-option>
                        <el-option label="家具" :value="2"></el-option>
                        <el-option label="生活用品" :value="3"></el-option>
                        <el-option label="安全保障" :value="4"></el-option>
                        <el-option label="工具" :value="5"></el-option>
                    </el-select>
                    <span v-if="editForm.status!=0">{{typeName[editForm.type]}}</span>
                </el-descriptions-item>
                <el-descriptions-item label="型号">
                    <el-input v-if="editForm.status==0" v-model="editForm.model" placeholder="请输入物品型号" maxlength="255" size="mini" style="width: 250px;"></el-input>
                    <span v-if="editForm.status!=0">{{editForm.model}}</span>
                </el-descriptions-item>
                <el-descriptions-item label="厂商">
                    <el-input v-if="editForm.status==0" v-model="editForm.producer" placeholder="请输入厂商名" maxlength="255" size="mini" style="width: 250px;"></el-input>
                    <span v-if="editForm.status!=0">{{editForm.producer}}</span>
                </el-descriptions-item>
                <el-descriptions-item label="数量">
                    <el-input-number v-if="editForm.status==0" v-model="editForm.number" :precision="0" size="mini" :step="1" :min="1" :max="1000" style="width:130px;"></el-input-number>
                    <span v-if="editForm.status!=0">{{editForm.number}}</span>
                </el-descriptions-item>
                <el-descriptions-item label="价格"> 
                    <el-input-number v-if="editForm.status==0" v-model="editForm.price" :precision="2" size="mini" :step="1" :min="1" style="width:160px;" :controls="false"></el-input-number>
                    <span v-if="editForm.status!=0">{{editForm.price}}</span>
                </el-descriptions-item>
                <el-descriptions-item label="申请时间">{{editForm.createTime}}</el-descriptions-item>
                <el-descriptions-item label="状态">{{ statusName[editForm.status] }}</el-descriptions-item>
                <el-descriptions-item label="备注" span="2">
                    <el-input
                        v-if="editForm.status==0"
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="editForm.remark"
                        maxlength="500"
                        show-word-limit
                        ></el-input>
                    <span v-if="editForm.status!=0">{{editForm.remark}}</span>
                </el-descriptions-item>
                <el-descriptions-item v-if="editForm.status!=0"  label="处理人">{{editForm.conductor}}</el-descriptions-item>
                <el-descriptions-item v-if="editForm.status!=0"  label="处理时间">{{editForm.updateTime}}</el-descriptions-item>
                <el-descriptions-item v-if="editForm.status!=0"  label="处理反馈" span="2">{{ editForm.feedback }}</el-descriptions-item>
            </el-descriptions>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button :disabled="editForm.status!=0" type="primary" size="mini" @click="editPurchase">保存</el-button>
                <el-button size="mini" @click="purchaseInfoVisible=false">取消</el-button>
            </div>
        </el-dialog>

        <!-- 采购申请 -->
        <el-dialog title="采购申请" :visible.sync="addViewVisible" width="30%" center>
            <el-descriptions column="1" style="display: inline-block;">
                <el-descriptions-item label="物品名">
                    <el-input v-model="addForm.name" placeholder="请输入物品名" maxlength="100" size="mini"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="类型">
                    <el-select v-model="addForm.type" placeholder="请选择物品类型" size="mini" style="width:130px">
                        <el-option label="办公用品" :value="1"></el-option>
                        <el-option label="家具" :value="2"></el-option>
                        <el-option label="生活用品" :value="3"></el-option>
                        <el-option label="安全保障" :value="4"></el-option>
                        <el-option label="工具" :value="5"></el-option>
                    </el-select>
                </el-descriptions-item>
                <el-descriptions-item label="型号">
                    <el-input v-model="addForm.model" placeholder="请输入物品型号" maxlength="255" size="mini"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="厂商">
                    <el-input v-model="addForm.producer" placeholder="请输入厂商名" maxlength="255" size="mini"></el-input>
                </el-descriptions-item>
                <el-descriptions-item label="数量">
                    <el-input-number v-model="addForm.number" :precision="0" size="mini" :step="1" :min="1" :max="1000" style="width:130px;"></el-input-number>
                </el-descriptions-item>
                <el-descriptions-item label="价格"> 
                    <el-input-number v-model="addForm.price" :precision="2" size="mini" :step="1" :min="0" style="width:160px;" :controls="false"></el-input-number>
                </el-descriptions-item>
                <el-descriptions-item label="备注" span="2">
                    <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="addForm.remark"
                        maxlength="500"
                        show-word-limit
                        ></el-input>
                </el-descriptions-item>
            </el-descriptions>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" size="mini" @click="addPurchase">提交</el-button>
                <el-button size="mini" @click="addViewVisible=false">取消</el-button>
            </div>
        </el-dialog>
    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  export default {
      components: {Pagination},
      name:"ApplyRecords",
      data(){
          return {
              searchValue:'',
              tableData:[],
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              purchaseInfoVisible:false,
              addViewVisible:false,
              type:'',
              status:'',
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
              },
              statusName:['未审批','已通过','未通过'],
              typeName:['','办公用品','家具','生活用品','安全保障','工具'],
              addForm:{
                name:'',
                model:'',
                producer:'',
                price:'',
                remark:'',
                type:'',
                number:'',
              }
          }
      },
      methods:{
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
            let name = this.searchValue
            let type = this.type
            let status = this.status
            this.myApplyRecords(currentPage,name,type,status)
          },
          openAddView() {
              this.addViewVisible = true;
          },
        myApplyRecords(currentPage,name,type,status){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/purchase/myApplyRecords"
            let params = {
                "name": name,
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
            let name = this.searchValue
            let type = this.type
            let status = this.status
            this.myApplyRecords(currentPage,name,type,status)
          },
          editPurchase(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/purchase/editPurchase"
            let params = {
                "id": this.editForm.id,
                "name": this.editForm.name,
                "type": this.editForm.type,
                "model": this.editForm.model,
                "producer": this.editForm.producer,
                "price": this.editForm.price,
                "number": this.editForm.number,
                "remark": this.editForm.remark,
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '修改成功',
                        type: 'success'
                    });
                    page.editPurchase = false
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
          },
          addPurchase(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/purchase/addPurchase"
            let params = {
                "name": this.addForm.name,
                "model": this.addForm.model,
                "producer": this.addForm.producer,
                "price": this.addForm.price,
                "remark": this.addForm.remark,
                "type": this.addForm.type,
                "number": this.addForm.number
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
        //获取采购信息  
        this.myApplyRecords(1,'','','','')
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