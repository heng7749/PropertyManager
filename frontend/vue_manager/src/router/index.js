import Vue from 'vue'
import  VueRouter from 'vue-router'

import Index from '../pages/Index.vue'
import Users from '../pages/users/Users.vue'
import FittingPublish from '../pages/goods/FittingPublish.vue'
import SystemData from '../pages/SystemData/SystemData.vue'
import Page401 from '../pages/error-page/401.vue'
import Page404 from '../pages/error-page/404.vue'
import EmployeeMsg from '../pages/employee/EmployeeMsg.vue'
import DutyManager from '../pages/employee/DutyManager.vue'
import HouseMsg from '../pages/estate/HouseMsg.vue'
import OwnerMsg from '../pages/estate/OwnerMsg.vue'
import BillMsg from '../pages/bill/BillMsg.vue'
import BillRecords from '../pages/bill/BillRecords.vue'
import StallMsg from '../pages/parking/StallMsg.vue'
import ParkingMsg from '../pages/parking/ParkingMsg.vue'
import EquipmentMsg from '../pages/equipment/EquipmentMsg.vue'
import MaintenanceMsg from '../pages/equipment/MaintenanceMsg.vue'
import RepairMsg from '../pages/repair/RepairMsg.vue'
import RepairRecords from '../pages/repair/RepairRecords.vue'
import PurchasingMsg from '../pages/purchasing/PurchasingMsg.vue'
import ApplyRecords from '../pages/purchasing/ApplyRecords.vue'
import ComplaintMsg from '../pages/feedback/ComplaintMsg.vue'
import ComplaintRecords from '../pages/feedback/ComplaintRecords.vue'
import FeedbackMsg from '../pages/feedback/FeedbackMsg.vue'
import FeedbackRecords from '../pages/feedback/FeedbackRecords.vue'
import NoticeMsg from '../pages/notice/NoticeMsg.vue'
import Notices from '../pages/notice/Notices.vue'
import RoleManager from '../pages/system/RoleManager.vue'
import UserManager from '../pages/system/UserManager.vue'
import BuildingManager from '../pages/system/BuildingManager.vue'
import DepartmentManager from '../pages/system/DepartmentManager.vue'
import BillTypeManager from '../pages/system/BillTypeManager.vue'
import Login from '../pages/users/Login.vue'



Vue.use(VueRouter)
const router = new VueRouter({
    mode:'history',
    routes:[{
        path:'/',
        name:"index",
        redirect:'/systemData',
        component:Index,
        children:[
            {
                path:'employee/EmployeeMsg',
                name:"employee/EmployeeMsg",
                component:EmployeeMsg
            },
            {
                path:'employee/DutyManager',
                name:"employee/DutyManager",
                component:DutyManager
            },
            {
                path:'estate/HouseMsg',
                name:"estate/HouseMsg",
                component:HouseMsg
            },
            {
                path:'estate/OwnerMsg',
                name:"estate/OwnerMsg",
                component:OwnerMsg
            },
            {
                path:'bill/BillMsg',
                name:"bill/BillMsg",
                component:BillMsg
            },
            {
                path:'bill/BillRecords',
                name:"bill/BillRecords",
                component:BillRecords
            },
            {
                path:'parking/StallMsg',
                name:"parking/StallMsg",
                component:StallMsg
            },
            {
                path:'parking/ParkingMsg',
                name:"parking/ParkingMsg",
                component:ParkingMsg
            },
            {
                path:'/equipment/EquipmentMsg',
                name:"/equipment/EquipmentMsg",
                component:EquipmentMsg
            },
            {
                path:'/equipment/MaintenanceMsg',
                name:"/equipment/MaintenanceMsg",
                component:MaintenanceMsg
            },
            {
                path:'/repair/RepairMsg',
                name:"/repair/RepairMsg",
                component:RepairMsg
            },
            {
                path:'/repair/RepairRecords',
                name:"/repair/RepairRecords",
                component:RepairRecords
            },
            {
                path:'/purchasing/PurchasingMsg',
                name:"/purchasing/PurchasingMsg",
                component:PurchasingMsg
            },
            {
                path:'/purchasing/ApplyRecords',
                name:"/purchasing/ApplyRecords",
                component:ApplyRecords
            },
            {
                path:'/feedback/ComplaintMsg',
                name:"/feedback/ComplaintMsg",
                component:ComplaintMsg
            },
            {
                path:'/feedback/ComplaintRecords',
                name:"/feedback/ComplaintRecords",
                component:ComplaintRecords
            },
            {
                path:'/feedback/FeedbackMsg',
                name:"/feedback/FeedbackMsg",
                component:FeedbackMsg
            },
            {
                path:'/feedback/FeedbackRecords',
                name:"/feedback/FeedbackRecords",
                component:FeedbackRecords
            },
            {
                path:'/notice/NoticeMsg',
                name:"/notice/NoticeMsg",
                component:NoticeMsg
            },
            {
                path:'/notice/Notices',
                name:"/notice/Notices",
                component:Notices
            },
            {
                path:'/system/RoleManager',
                name:"/system/RoleManager",
                component:RoleManager
            },
            {
                path:'/system/UserManager',
                name:"/system/UserManager",
                component:UserManager
            },
            {
                path:'/system/BuildingManager',
                name:"/system/BuildingManager",
                component:BuildingManager
            },
            {
                path:'/system/DepartmentManager',
                name:"/system/DepartmentManager",
                component:DepartmentManager
            },
            {
                path:'/system/BillTypeManager',
                name:"/system/BillTypeManager",
                component:BillTypeManager
            },
            {
                path:'users',
                name:"users",
                component:Users
            },{
                path:'fittingPublish',
                name:"fittingPublish",
                component:FittingPublish
            },{
                path:'systemData',
                name:"systemData",
                component:SystemData
            }
        ]
    },{
        path:'/error/401',
        name:'Page401',
        component:Page401
    },{
        path:'/error/404',
        name:'Page404',
        component:Page404
    },{
        path:'/login',
        name:'Login',
        component:Login
    }]
})

export default router