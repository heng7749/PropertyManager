<template>
    <div style="padding:16px;">
      <el-form inline class="searchBar">
        <el-form-item label="通知主题:">
            <el-input size="small" v-model="searchValue" maxlength="10" placeholder="输入通知主题" style="width:110px;"></el-input>
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
                {{scope.row.type >= 0 ? typeName[scope.row.type] : (scope.row.type==-1 ? '全体员工' : '全体业主')}}
            </template>
          </el-table-column>
          <el-table-column prop="subject" label="主题" min-width="260" show-overflow-tooltip></el-table-column>
          <el-table-column prop="creator" label="发布者" min-width="120"></el-table-column>
          <el-table-column prop="createTime" label="发布时间" min-width="160"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" fixed="right" width="100">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">查看</el-button>
              </template>
          </el-table-column>
      </el-table> 
      <!-- 分页 -->
      <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>

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
  export default {
      components: {Pagination},
      name:"Notices",
      data(){
          return {
              searchValue:'',
              tableData:[],
              pageMsg: {
                currentPage: 1,
                pageSize: 5,
                total: 1
              },
              form:'',
              content:'',
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
            noticeDetailInfos:{},
            typeName:['所有人','经理室','办公室','财务部','安保部','工程部','保洁部'],
            noticeDetialViewVisible:false
          }
      },
      methods:{
          handleEdit(row) {
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
          pageChangeHandle(param) {
            let currentPage = param.currentPage
            let subject = this.searchValue
            this.searchUserNotices(currentPage,subject)
          },
          searchUserNotices(currentPage,subject){
            let page = this
            let token = this.$store.getters.GET_TOKEN
            let url = "/heng/notice/searchUserNotices"
            let params = {
                "subject": subject,
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
            this.searchUserNotices(currentPage,subject)
          }
      },
      created(){
        //获取通知信息
        this.searchUserNotices(1,'')
      }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  </style>