<template>
  <div class="container"> 
    <el-menu :collapse="isCollapse" collapse-transition router unique-opened 
      class="el-menu-vertical-demo" background-color="#334157" text-color="#fff" active-text-color="#ffd04b" :default-active="$route.path">

      <el-menu-item index="/systemData">
        <i class="el-icon-menu el-icon-s-data"></i>
        <span slot="title">系统数据</span>
      </el-menu-item>
      
      <el-submenu v-for="menu in allmenu" :key="'/'+menu.menuid" :index="menu.name">
        <template slot="title">
          <i class="iconfont" :class="menu.icon" style="font-size:20px;margin-right:2px"></i>
          <span>{{menu.name}}</span>
        </template>
        <el-menu-item-group>
          <el-menu-item v-for="chmenu in menu.menus" :index="chmenu.path" :key="chmenu.menuid" @click="openTab(chmenu)">
            <i class="iconfont" :class="chmenu.icon" style="font-size:20px;margin-right:2px"></i>
            <span>{{chmenu.name}}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>

    </el-menu>
  </div>
</template>

<script>
export default {
  name:'LeftNavBar',
  data(){
    return {
      isCollapse: false,
      allmenu: []
    }
  },
  created(){
    let res = {
      success: true,
      data: [
        {
          menuid: '2',
          icon: 'el-icon-user-solid',
          name: '员工管理',
          menus: [
            {
              menuid: '2-1',
              icon: 'el-icon-user',
              name: '员工信息',
              path: '/employee/EmployeeMsg',
              menus: null
            },
            {
              menuid: '2-2',
              icon: 'el-icon-date',
              name: '值班管理',
              path: '/employee/DutyManager',
              menus: null
            }
          ]
        },
        {
          menuid: '3',
          icon: 'el-icon-office-building',
          name: '房屋管理',
          menus: [
            {
              menuid: '3-1',
              icon: 'el-icon-office-building',
              name: '房屋信息',
              path: '/estate/HouseMsg',
              menus: null
            },
            {
              menuid: '3-2',
              icon: 'el-icon-user',
              name: '业主信息',
              path: '/estate/OwnerMsg',
              menus: null
            }
          ]
        },
        {
          menuid: '4',
          icon: 'el-icon-s-finance',
          name: '物业缴费管理',
          menus: [
            {
              menuid: '4-1',
              icon: 'el-icon-tickets',
              name: '缴费信息',
              path: '/bill/BillMsg',
              menus: null
            },
            {
              menuid: '4-2',
              icon: 'el-icon-time',
              name: '缴费记录',
              path: '/bill/BillRecords',
              menus: null
            }
          ]
        },
        {
          menuid: '5',
          icon: 'el-icon-truck',
          name: '停车管理',
          menus: [
            {
              menuid: '5-1',
              icon: 'el-icon-position',
              name: '车位信息',
              path: '/parking/StallMsg',
              menus: null
            },
            {
              menuid: '5-2',
              icon: 'el-icon-map-location',
              name: '停车信息',
              path: '/parking/ParkingMsg',
              menus: null
            }
          ]
        },
        {
          menuid: '6',
          icon: 'el-icon-s-platform',
          name: '设备管理',
          menus: [
            {
              menuid: '6-1',
              icon: 'el-icon-table-lamp',
              name: '设备信息',
              path: '/equipment/EquipmentMsg',
              menus: null
            },
            {
              menuid: '6-2',
              icon: 'el-icon-document-checked',
              name: '设备检修',
              path: '/equipment/MaintenanceMsg',
              menus: null
            }
          ]
        },
        {
          menuid: '7',
          icon: 'el-icon-s-opportunity',
          name: '报修管理',
          menus: [
            {
              menuid: '7-1',
              icon: 'el-icon-tickets',
              name: '报修信息',
              path: '/repair/RepairMsg',
              menus: null
            },
            {
              menuid: '7-2',
              icon: 'el-icon-time',
              name: '报修记录',
              path: '/repair/RepairRecords',
              menus: null
            }
          ]
        },
        {
          menuid: '8',
          icon: 'el-icon-s-goods',
          name: '采购管理',
          menus: [
            {
              menuid: '8-1',
              icon: 'el-icon-tickets',
              name: '采购信息',
              path: '/purchasing/PurchasingMsg',
              menus: null
            },
            {
              menuid: '8-2',
              icon: 'el-icon-time',
              name: '申请记录',
              path: '/purchasing/ApplyRecords',
              menus: null
            }
          ]
        },
        {
          menuid: '9',
          icon: 'el-icon-s-management',
          name: '业主反馈管理',
          menus: [
            {
              menuid: '9-1',
              icon: 'el-icon-tickets',
              name: '投诉信息',
              path: '/feedback/ComplaintMsg',
              menus: null
            },
            {
              menuid: '9-2',
              icon: 'el-icon-time',
              name: '投诉记录',
              path: '/feedback/ComplaintRecords',
              menus: null
            },
            {
              menuid: '9-3',
              icon: 'el-icon-tickets',
              name: '建议信息',
              path: '/feedback/FeedbackMsg',
              menus: null
            },
            {
              menuid: '9-4',
              icon: 'el-icon-time',
              name: '建议记录',
              path: '/feedback/FeedbackRecords',
              menus: null
            }
          ]
        },
        {
          menuid: '10',
          icon: 'el-icon-s-comment',
          name: '通知管理',
          menus: [
            {
              menuid: '10-1',
              icon: 'el-icon-tickets',
              name: '通知信息',
              path: '/notice/NoticeMsg',
              menus: null
            },
            {
              menuid: '10-2',
              icon: 'el-icon-document-copy',
              name: '所有通知',
              path: '/notice/Notices',
              menus: null
            }
          ]
        },
        {
          menuid: '11',
          icon: 'el-icon-s-tools',
          name: '系统管理',
          menus: [
            {
              menuid: '11-1',
              icon: 'el-icon-view',
              name: '角色管理',
              path: '/system/RoleManager',
              menus: null
            },
            {
              menuid: '11-2',
              icon: 'el-icon-user',
              name: '用户管理',
              path: '/system/UserManager',
              menus: null
            },
            {
              menuid: '11-2',
              icon: 'el-icon-school',
              name: '楼宇管理',
              path: '/system/BuildingManager',
              menus: null
            },
            {
              menuid: '11-2',
              icon: 'el-icon-coin',
              name: '收费项管理',
              path: '/system/BillTypeManager',
              menus: null
            },
            {
              menuid: '11-2',
              icon: 'el-icon-user',
              name: '部门管理',
              path: "/system/DepartmentManager",
              menus: null
            }
          ]
        }
      ],
      msg: 'success'
    };
    this.allmenu = res.data;

    this.$bus.$on('toggle', value => {
      this.isCollapse = !value;
    })
  },
  methods: {
    openTab(item){
      this.$store.commit('ADD_TABS',item)
    }
  }
}
</script>

<style scoped>
.container{
  width: 200px;
  height: 100%;
  overflow-x: hidden;
  overflow-y: visible;
}
::-webkit-scrollbar {
  width: 3px;
}
::-webkit-scrollbar-track {
  background-color:  #fdfdfdd7;
}
::-webkit-scrollbar-thumb {
  box-shadow: inset 0 0 6px rgb(1, 51, 85);
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
  border: none;
  text-align: left;
}
.el-menu-item-group__title {
  padding: 0px;
}
.el-menu-bg {
  background-color: #1f2d3d !important;
}
.el-menu {
  border: none;
}
</style>