<template>
  <div style="padding:16px;">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">发布管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户发布</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 搜索筛选 -->
    <el-form inline class="user-search">
        <el-form-item>
            <el-dropdown @command="selectSearchType">
                <span class="el-dropdown-link">
                {{searchType==1?'用户名模式':'商品名模式'}}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command='1'>用户名</el-dropdown-item>
                    <el-dropdown-item command='2'>商品名</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </el-form-item>
        <el-form-item>
            <el-input size="small" :placeholder="searchType==1?'输入用户名':'输入商品名'"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" icon="el-icon-search">搜索</el-button>
        </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table stripe border :data="tableData" tooltip-effect="dark" style="width: 100%"
        :default-sort="{prop: 'date', order: 'descending'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>

        <el-table-column prop="date" label="日期" width="120" sortable>
            <template slot-scope="scope">{{scope.row.date}}</template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="120"></el-table-column>
        <el-table-column prop="address" label="地址" show-overflow-tooltip></el-table-column>
        <!-- 操作 -->
        <el-table-column label="操作">
            <template slot-scope="scope">
                <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
        </el-table-column>
    </el-table> 
    <el-button v-show="delSelectItemsBtnShow" size="mini" type="danger" style="margin-top:10px;" @click="setCurrent()">删除所有选中</el-button>
    <!-- 分页 -->
    <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
  </div>
</template>

<script>
import Pagination from '../../components/Pagination.vue'
export default {
    name:'Goods',
    components:{
        Pagination
    },
    data(){
        return {
            delSelectItemsBtnShow:false,
            pageMsg: {
                currentPage: 1,
                pageSize: 10,
                total: 10
            },
            searchType: 1,
            tableData:[]
        }
    },
    methods:{
        selectSearchType(command){
            this.searchType = Number(command)
        },
        handleEdit(index, row) {
            console.log(index, row);
        },
        handleDelete(index, row) {
            console.log(index, row);
            this.$confirm('是否删除此商品?', '删除商品提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                });
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
            if(selections.length!==0){
                this.delSelectItemsBtnShow = true
            }else {
                this.delSelectItemsBtnShow = false
            }
        }
    },
    created(){
        this.tableData =  [{
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市s 弄'
        }, {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-08',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-06',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-07',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }];
        this.pageMsg.total = this.tableData.length
    }
}
</script>

<style scoped>
    .el-dropdown-link {
        cursor: pointer;
        color: #409EFF;
        font-size: 14px;
        padding: 0 12px 0 0;
    }
    .el-icon-arrow-down {
        font-size: 12px;
    }
    .user-search {
        float: right;
    }
    .pagination {
        margin-top: 20px;
        margin-right: 20px;
        float: right;
    }
</style>