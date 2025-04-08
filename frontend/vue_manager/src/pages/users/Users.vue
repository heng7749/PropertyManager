<template>
  <div style="padding:16px;">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">发布管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>

    <el-form inline class="userName-search">
        <el-form-item label="用户查询：">
            <el-input size="small" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" icon="el-icon-search">搜索</el-button>
        </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table stripe border :data="tableData" tooltip-effect="dark" style="width: 100%"
        :default-sort="{prop: 'registerDate', order: 'descending'}" @selection-change="handleSelectionChange">
        <el-table-column prop="id" label="ID" width="120"></el-table-column>
        <el-table-column prop="userName" label="用户名" width="120"></el-table-column>
        <el-table-column prop="gender" label="性别" width="120"></el-table-column>
        <el-table-column prop="phoneNumber" label="电话号码" width="120"></el-table-column>
        <el-table-column prop="registerDate" label="注册日期" width="120" sortable>
            <template slot-scope="scope">{{scope.row.registerDate}}</template>
        </el-table-column>
        <el-table-column prop="phoneNumber" label="电话号码" width="120"></el-table-column>
        <el-table-column prop="address" label="地址" show-overflow-tooltip></el-table-column>
        <!-- 操作 -->
        <el-table-column label="操作">
            <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleEdit(scope.$index, scope.row)">查看信息</el-button>
                <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
        </el-table-column>
    </el-table> 

    <Pagination class="pagination" v-bind:child-msg="pageMsg" @pageChange="pageChangeHandle"></Pagination>
    <el-dialog title="用户详细信息" :visible.sync="userInfoVisible">
        <el-descriptions class="margin-top" :column="3" border>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-user"></i>用户名
                </template>
                {{userInfo.userName}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-male"></i>性别
                </template>
                {{userInfo.gender}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-mobile-phone"></i>手机号
                </template>
                {{userInfo.phoneNumber}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-tickets"></i>注册日期
                </template>
                {{userInfo.registerDate}}
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <i class="el-icon-location-outline"></i>地址
                </template>
                {{userInfo.address}}
            </el-descriptions-item>
        </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '../../components/Pagination.vue'
export default {
    components: { Pagination },
    userName:"Users",
    data(){
        return {
            pageMsg: {
                currentPage: 1,
                pageSize: 10,
                total: 10
            },
            tableData:[],
            userInfoVisible:false,
            userInfo:{}
        }
    },
    methods:{
        handleEdit(index, row) {
            this.userInfoVisible = true;
            this.userInfo = row;
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
            id:1,
            gender:'男',
            phoneNumber:'12345678910',
            registerDate: '2016-05-03',
            userName: '王小虎',
            address: '上海市s 弄'
        }, {
            id:2,
            gender:'男',
            phoneNumber:'12345678910',
            registerDate: '2016-05-02',
            userName: '王小虎',
            address: '上海市普陀区金沙江路 1518 弄'
        }, {
            id:3,
            gender:'女',
            phoneNumber:'12345678910',
            registerDate: '2016-05-04',
            userName: '王小虎',
            address: '上海市普陀区金沙江路 1518 弄'
        }, {
            id:4,
            gender:'男',
            phoneNumber:'12345678910',
            registerDate: '2016-05-01',
            userName: '王小虎',
            address: '上海市普陀区金沙江路 1518 弄'
        }, {
            id:5,
            gender:'男',
            phoneNumber:'12345678910',
            registerDate: '2016-05-08',
            userName: '王小虎',
            address: '上海市普陀区金沙江路 1518 弄'
        }, {
            id:6,
            gender:'女',
            phoneNumber:'12345678910',
            registerDate: '2016-05-06',
            userName: '王小虎',
            address: '上海市普陀区金沙江路 1518 弄'
        }, {
            id:7,
            gender:'女',
            phoneNumber:'12345678910',
            registerDate: '2016-05-07',
            userName: '王小虎',
            address: '上海市普陀区金沙江路 1518 弄'
        }];
        this.pageMsg.total = this.tableData.length
    }
}
</script>

<style>
    .userName-search {
        float: right;
    }
    .pagination {
        margin-top: 20px;
        margin-right: 20px;
        float: right;
    }
</style>