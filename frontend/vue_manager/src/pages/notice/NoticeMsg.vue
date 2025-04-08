<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
        <el-form-item label="通知主题:">
            <el-input size="small" v-model="searchValue" maxlength="10" placeholder="输入通知主题" style="width:110px;"></el-input>
        </el-form-item>
          <el-form-item label="通知类型:">
              <el-select v-model="type" placeholder="全部" size="small" style="width:100px">
                <el-option label="所有人" value="0"></el-option>
                <el-option label="全体业主" value="-2"></el-option>
                <el-option label="全体员工" value="-1"></el-option>
                <el-option label="经理室" value="1"></el-option>
                <el-option label="办公室" value="2"></el-option>
                <el-option label="财务部" value="3"></el-option>
                <el-option label="安保部" value="4"></el-option>
                <el-option label="工程部" value="5"></el-option>
                <el-option label="保洁部" value="6"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="状态:">
              <el-select v-model="status" placeholder="全部" size="small" style="width:100px">
                <el-option label="已发布" value="0"></el-option>
                <el-option label="已下架" value="1"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item>
              <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          </el-form-item>
      </el-form>
  
      <!-- 表格 -->
      <el-table stripe border :data="tableData" tooltip-effect="dark" style="width: 100%" size="small" v-loading="loading"
              :header-cell-style="{textAlign:'center'}" :cell-style="{textAlign:'center'}">
          <el-table-column prop="id" label="编号" min-width="120"></el-table-column>
          <el-table-column label="类型" min-width="120">
            <template slot-scope="scope">
                {{scope.row.type >= 0 ? typeName[scope.row.type] : (scope.row.type==-1 ? '全体员工' : '全体业主')}}
            </template>
          </el-table-column>
          <el-table-column prop="subject" label="主题" min-width="260" show-overflow-tooltip></el-table-column>
          <el-table-column prop="creator" label="发布者" min-width="120"></el-table-column>
          <el-table-column prop="createTime" label="提交时间" min-width="160"></el-table-column>
          <el-table-column label="状态" min-width="100">
            <template slot-scope="scope">
                {{statusName[scope.row.status]}}
            </template>
          </el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" fixed="right" width="230">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                  <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
          </el-table-column>
      </el-table> 
      <el-button @click="openAddView" type="primary" size="mini" icon="el-icon-plus" style="margin-top: 20px;margin-left: 10px;">发布</el-button>
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>

        <!-- 修改通知 -->
        <el-dialog title="通知信息" :visible.sync="noticeInfoVisible" width="60%" center style="position: absolute;" top="3vh">
            <el-form ref="editForm" :model="editForm" label-width="80px" size="mini" label-position="left"  style="height:480px;">
                <el-form-item label="主题:">
                    <el-input v-model="editForm.subject" placeholder="请输入通知主题"></el-input>
                </el-form-item>
                <el-form-item label="通知类型:" style="display: inline-block;">
                    <el-select v-model="editForm.type" placeholder="请选择通知类型">
                        <el-option label="所有人" value="0"></el-option>
                        <el-option label="全体业主" value="-2"></el-option>
                        <el-option label="全体员工" value="-1"></el-option>
                        <el-option label="经理室" value="1"></el-option>
                        <el-option label="办公室" value="2"></el-option>
                        <el-option label="财务部" value="3"></el-option>
                        <el-option label="安保部" value="4"></el-option>
                        <el-option label="工程部" value="5"></el-option>
                        <el-option label="保洁部" value="6"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="状态:" style="display: inline-block;margin-left: 100px;">
                    <el-select v-model="editForm.status" placeholder="全部" size="small" style="width:100px">
                        <el-option label="已发布" :value="0"></el-option>
                        <el-option label="已下架" :value="1"></el-option>
                    </el-select>
                </el-form-item>
                <quill-editor
                    ref="editFormQuillEditor"
                    v-model="editForm.contents"
                    :options="editorOption"
                    style="height: 300px;"
                    />
            </el-form>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" size="mini" @click="editNotice">保存</el-button>
                <el-button size="mini">取消</el-button>
            </div>
        </el-dialog>

        <!-- 发布通知 -->
        <el-dialog title="发布通知" :visible.sync="addViewVisible" width="60%" center style="position: absolute;" top="3vh">
            <el-form ref="form" :model="form" label-width="80px" size="mini" label-position="left"  style="height:480px;">
                <el-form-item label="主题:">
                    <el-input v-model="form.subject" placeholder="请输入通知主题"></el-input>
                </el-form-item>
                <el-form-item label="通知类型:">
                    <el-select v-model="form.type" placeholder="请选择通知类型">
                        <el-option label="全体业主" value="-2"></el-option>
                        <el-option label="全体员工" value="-1"></el-option>
                        <el-option label="经理室" value="1"></el-option>
                        <el-option label="办公室" value="2"></el-option>
                        <el-option label="财务部" value="3"></el-option>
                        <el-option label="安保部" value="4"></el-option>
                        <el-option label="工程部" value="5"></el-option>
                        <el-option label="保洁部" value="6"></el-option>
                    </el-select>
                </el-form-item>
                <quill-editor
                    ref="myQuillEditor"
                    v-model="form.contents"
                    :options="editorOption"
                    style="height: 300px;"
                    />
            </el-form>
            <div style="width: 150px;margin-left: auto;margin-right: auto;">
                <el-button type="primary" @click="publish" size="mini">提交</el-button>
                <el-button size="mini">取消</el-button>
            </div>
        </el-dialog>

        <!-- 查看通知 -->
        <el-dialog title="通知详情" :visible.sync="noticeDetialViewVisible" width="80%" center style="position: absolute;" top="3vh">
            <div style="border-bottom: 1px solid #E4E7ED;">
                <div style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">主题：{{noticeDetailInfos.subject}}</div>
                <div style="text-align: right;">
                    <span style="margin-right: 18px;">发布者：{{noticeDetailInfos.creator}}</span>
                    <span>发布时间：{{noticeDetailInfos.createTime}}</span>
                </div>
            </div>
            <div v-html="noticeDetailInfos.contents" class="ql-editor"></div>
        </el-dialog>

    </div>
  </template>
  
  <script>
  import Pagination from '../../components/Pagination.vue'
  import "quill/dist/quill.core.css";
  import "quill/dist/quill.snow.css";
  import "quill/dist/quill.bubble.css";
  import { quillEditor } from "vue-quill-editor";
  export default {
      components: {Pagination,quillEditor},
      name:"NoticeMsg",
      data(){
          return {
              searchValue:'',
              tableData:[],
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              noticeInfoVisible:false,
              addViewVisible:false,
              form:{
                subject:'',
                type:'',
                contents:''
              },
              editorOption: {
                placeholder: "请在这里输入",
                modules:{
                    toolbar:[
                        ['bold', 'italic', 'underline', 'strike'],    //加粗，斜体，下划线，删除线
                        ['blockquote', 'code-block'],     //引用，代码块
                        [{ 'header': 1 }, { 'header': 2 }],        // 标题，键值对的形式；1、2表示字体大小
                        [{ 'list': 'ordered'}, { 'list': 'bullet' }],     //列表
                        [{ 'script': 'sub'}, { 'script': 'super' }],   // 上下标
                        [{ 'indent': '-1'}, { 'indent': '+1' }],     // 缩进
                        [{ 'direction': 'rtl' }],             // 文本方向
                        [{ 'size': ['small', false, 'large', 'huge'] }], // 字体大小
                        [{ 'header': [1, 2, 3, 4, 5, 6, false] }],     //几级标题
                        [{ 'color': [] }, { 'background': [] }],     // 字体颜色，字体背景颜色
                        [{ 'font': [] }],     //字体
                        [{ 'align': [] }],    //对齐方式
                        ['clean'],    //清除字体样式
                        ['image','video']    //上传图片、上传视频
                    ]
                }
            },
            loading:false,
            type:'',
            status:'',
            noticeDetialViewVisible:false,
            noticeDetailInfos:{},
            statusName:['已发布','已下架'],
            typeName:['所有人','经理室','办公室','财务部','安保部','工程部','保洁部'],
            editForm:{
                subject:'',
                type:'',
                contents:'',
                status:''
            }
          }
      },
      methods:{
          handleView(row){
            this.noticeDetialViewVisible = true
            let noticeId = row.id
            let page = this
            let token = this.$store.getters.GET_TOKEN
            //获取公告消息详细信息
            this.$http.get("/heng/notice/noticeDetail",{
                headers:{token:token},
                params:{
                    "noticeId":noticeId
                }
            }).then(res=>{
                let result = res.data
                if(result.status === 0){
                    page.noticeDetailInfos = result.data[0]
                } else {
                    page.$message.error(result.msg)
                }
            },err=>{
                page.$message.error(err.msg)
            })
          },
          handleEdit(row) {
            this.noticeInfoVisible = true;
            let page = this
            let token = this.$store.getters.GET_TOKEN
            //获取公告消息详细信息
            this.$http.get("/heng/notice/noticeDetail",{
                headers:{token:token},
                params:{
                    "noticeId":row.id
                }
            }).then(res=>{
                let result = res.data
                if(result.status === 0){
                    page.editForm = result.data[0]
                } else {
                    page.$message.error(result.msg)
                }
            },err=>{
                page.$message.error(err.msg)
            })
          },
          handleDelete(row) {
              this.$confirm('是否删除此通知信息?', '删除通知提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                this.deleteNotice(row.id)
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
            this.searchNotices(currentPage,subject,type,status)
          },
          openAddView() {
              this.addViewVisible = true;
          },
          searchNotices(currentPage,subject,type,status){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/notice/searchNotices"
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
            this.searchNotices(currentPage,subject,type,status)
          },
          publish(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/notice/publish"
            let params = {
                "type": this.form.type,
                "subject": this.form.subject,
                "contents": this.form.contents,
                "status": this.form.status
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    if(result.data.list.length == 0){
                        page.$message('搜索结果为空');
                    } else {
                        page.$message({
                            message: result.msg,
                            type: 'success'
                        });
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
          deleteNotice(id){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = '/heng/notice/deleteNotice'
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
          editNotice(){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/notice/editNotice"
            let params = {
                "id": this.editForm.id,
                "subject": this.editForm.subject,
                "type": this.editForm.type,
                "contents": this.editForm.contents,
                "status": this.editForm.status
            }
            this.$http.post(url, params, {headers:{'token':token}}).then(function(res){
                let result = res.data
                if(result.status === 0){
                    page.$message({
                        message: '处理成功',
                        type: 'success'
                    });
                    page.noticeInfoVisible = false
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
        //获取通知公告信息
        this.searchNotices(1,'','','')
      }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
.ql-editor img{
    max-width: 100% !important;
}
  </style>