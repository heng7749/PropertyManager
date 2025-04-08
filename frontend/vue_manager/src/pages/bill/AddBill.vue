<template>
    <div style="padding:16px;">
        <!-- 面包屑导航 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/' }">物业缴费管理</el-breadcrumb-item>
            <el-breadcrumb-item>添加缴费</el-breadcrumb-item>
        </el-breadcrumb>

        <el-card style="margin-top: 5px;padding: 10px 20px;">
            <el-form ref="form" :model="form" size="mini">
                <el-form-item label="编号：">xxxx<el-button @click="openSearchHouseView" type="primary" size="mini" style="margin-left: 160px;">选择缴费单位</el-button></el-form-item>
                <el-form-item label="楼号：">1栋</el-form-item>
                <el-form-item label="单元：">2单元</el-form-item>
                <el-form-item label="楼层：">3楼</el-form-item>
                <el-form-item label="房号：">402</el-form-item>
                <el-form-item label="业主：">业主</el-form-item>
                <el-form-item label="应缴费用类型：">
                    <el-select v-model="stairs" placeholder="请选择缴费类型" size="small" style="width:100px">
                        <el-option label="物业费" value="0"></el-option>
                        <el-option label="水费" value="1"></el-option>
                        <el-option label="电费" value="2"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="数值：">
                    <el-input v-model="form.houseNumber" style="width:100px;"></el-input>
                </el-form-item>
                <el-form-item label="抄表日期：">
                    <el-date-picker v-model="form.date" type="date" placeholder="选择日期"></el-date-picker>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="onSubmit">提交</el-button>
                    <el-button>取消</el-button>
                </el-form-item>
            </el-form>
        </el-card>
        
        <!-- 房屋信息 -->
        <el-dialog title="搜索房屋" :visible.sync="searchHouseVisible" width="65%" center style="position: absolute;">
            <SearchHouse></SearchHouse>
        </el-dialog>
    </div>
  </template>
  
  <script>
  import SearchHouse from '../../components/SearchHouse.vue'
  export default {
      components: {SearchHouse},
      name:"AddBill",
      data(){
          return {
              searchType: 1,
              searchValue:'',
              departments:[{
                  value: '1',
                  label: '经理室'
              },
              {
                  value: '2',
                  label: '办公室'
              },
              {
                  value: '3',
                  label: '财务部'
              },{
                  value: '4',
                  label: '安保部'
              },{
                  value: '5',
                  label: '工程部'
              },{
                  value: '6',
                  label: '保洁部'
              }],
              form:'',
              pageMsg: {
                  currentPage: 1,
                  pageSize: 10,
                  total: 10
              },
              tableData:[],
              searchHouseVisible:false,
              userInfo:{}
          }
      },
      methods:{
          openSearchHouseView(){
            this.searchHouseVisible = true;
        },
      },
      created(){
          this.tableData =  [{
              employeeId:'00000001',
              name:'王小虎',
              gender:'男',
              department:'安保部',
              duty:'保安队长',
              phoneNumber:'12345678910',
              email:'keionn@163.com',
              birthday: '2016-05-03',
              address: '上海市s 弄',
              salary:1200
          },{
              employeeId:'00000002',
              name:'王小虎',
              gender:'男',
              department:'安保部',
              duty:'保安队长',
              phoneNumber:'12345678910',
              email:'keionn@163.com',
              birthday: '2016-05-03',
              address: '上海市s 弄',
              salary:1200
          },{
              employeeId:'00000003',
              name:'王小虎',
              gender:'男',
              department:'安保部',
              duty:'保安队长',
              phoneNumber:'12345678910',
              email:'keionn@163.com',
              birthday: '2016-05-03',
              address: '上海市s 弄',
              salary:1200
          },];
          this.pageMsg.total = this.tableData.length
      }
  }
  </script>
  
  <style scoped>
  .searchBar{
      align-content: center;
      align-items: center;
  }
  .dropdownTag{
      width: 100px;
      height: 40px;
      text-align: right;
      line-height: 40px;
  }
  .el-dropdown-link {
      cursor: pointer;
      color: #409EFF;
      font-size: 14px;
      padding: 0 12px 0 0;
  }
  .el-icon-arrow-down {
      font-size: 12px;
  }
  </style>