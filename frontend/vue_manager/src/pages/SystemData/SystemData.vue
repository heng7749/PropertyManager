<template>
    <div class="container">
        <el-row :gutter="40">
            <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
                <div class="card-panel">
                    <div class="card-panel-left icon-person">
                        <i class="el-icon-s-custom"></i>
                    </div>
                    <div class="card-panel-description">
                    <div class="card-panel-text">
                        用户数
                    </div>
                    <div class="card-panel-num">{{ systemData.systemUserCount }}</div>
                    </div>
                </div>
            </el-col>
            <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
                <div class="card-panel">
                    <div class="card-panel-left icon-order">
                        <i class="el-icon-data-analysis"></i>
                    </div>
                    <div class="card-panel-description">
                    <div class="card-panel-text">
                        物业员工数
                    </div>
                    <div class="card-panel-num">{{ systemData.employeeCount }}</div>
                    </div>
                </div>
            </el-col>
            <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
                <div class="card-panel">
                    <div class="card-panel-left icon-goods">
                        <i class="el-icon-office-building"></i>
                    </div>
                    <div class="card-panel-description">
                    <div class="card-panel-text">
                        小区房屋数
                    </div>
                    <div class="card-panel-num">{{ systemData.houseCount }}</div>
                    </div>
                </div>
            </el-col>
            <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
                <div class="card-panel">
                    <div class="card-panel-left icon-fitting">
                        <i class="el-icon-postcard"></i>
                    </div>
                    <div class="card-panel-description">
                    <div class="card-panel-text">
                        小区业主数
                    </div>
                    <div class="card-panel-num">{{ systemData.ownerCount }}</div>
                    </div>
                </div>
            </el-col>
        </el-row>

        <!-- 折线图 -->
        <el-row :gutter="32">
            <el-col :xs="16" :sm="24" :lg="24">
                <div ref="lineChart" class="lineChart"></div>
            </el-col>
        </el-row>

        <!-- 图表 -->
        <el-row :gutter="32">
            <el-col :xs="24" :sm="24" :lg="8">
                <div class="chart-wrapper">
                    <div ref='echart1' class="echarts"></div>
                </div>
            </el-col>
            <el-col :xs="24" :sm="24" :lg="8">
                <div class="chart-wrapper">
                    <div ref='echart2' class="echarts"></div>
                </div>
            </el-col>
            <el-col :xs="24" :sm="24" :lg="8">
                <div class="chart-wrapper">
                    <div ref='echart3' class="echarts"></div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import * as echarts from 'echarts'
import resize from './resize'
export default {
    name:'SystemData',
    mixins:[resize],
    components: {
    },
    data(){
        return {
            lineChart:null,
            echart1:null,
            echart2:null,
            echart3:null,
            systemData:{}
        }
    },
    methods: {
        getRecentMonth(){
            //创建现在的时间
            var data=new Date();
            //获取年
            var year=data.getFullYear();
            //获取月
            var mon=data.getMonth()+1;
            var arry=new Array();
            if(mon<10){
                mon="0"+mon;
            }
            for(var i=0;i<6;i++){
                arry[i]=year+"-"+mon;
                mon=mon-1;
                if(mon<=0){
                    year=year-1;
                    mon=mon+12;
                }
                if(mon<10){
                    mon="0"+mon;
                }
 
            }
            return arry.reverse();
        },
        redrawLineChart(systemData){
            this.lineChart = echarts.init(this.$refs.lineChart);
            let data = this.getRecentMonth()
        
            // let repairData = [0,0,0,0,0,0]
            // let complaintData = [0,0,0,0,0,0]
            // let noticeData = [0,0,0,0,0,0]
            // for (let i=0;i<data.length;i++) {
            //     repairData[i] = systemData.lastSixRepairCount[data[i]]
            //     complaintData[i] = systemData.lastSixComplaintCount[data[i]]
            //     noticeData[i] = systemData.lastFourNoticeCount[data[i]]
                
            //     repairData[i] = repairData[i] == undefined ? 0 : repairData[i]
            //     complaintData[i] = complaintData[i] == undefined ? 0 : complaintData[i]
            //     noticeData[i] = noticeData[i] == undefined ? 0 : noticeData[i]
            // }
            this.lineChart.setOption({
                xAxis: {
                    data: data,
                    boundaryGap: false,
                    axisTick: {
                        show: false
                    }
                },
                grid: {
                    left: 10,
                    right: 10,
                    bottom: 20,
                    top: 30,
                    containLabel: true
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross'
                    },
                    padding: [5, 10]
                },
                yAxis: {
                    axisTick: {
                        show: false
                    }
                },
                legend: {
                    data: ['报修申报', '业主投诉']
                },
                series: [{
                    name: '报修申报', itemStyle: {
                        normal: {
                            color: '#FF005A',
                            lineStyle: {
                                color: '#FF005A',
                                width: 2
                            }
                        }
                    },
                    smooth: true,
                    type: 'line',
                    // data: repairData,
                    data: [15,20,15,15,30,42],
                    animationDuration: 2800,
                    animationEasing: 'cubicInOut'
                },
                {
                    name: '业主投诉',
                    smooth: true,
                    type: 'line',
                    itemStyle: {
                        normal: {
                            color: '#3888fa',
                            lineStyle: {
                                color: '#3888fa',
                                width: 2
                            },
                            areaStyle: {
                                color: '#f3f8ff'
                            }
                        }
                    },
                    // data: complaintData,
                    data:[20,30,35,50,32,29],
                    animationDuration: 2800,
                    animationEasing: 'quadraticOut'
                }]
            })

            this.echart1 = echarts.init(this.$refs.echart1);
            this.echart1.setOption({
                title: {
                    text: '物业员工男女比例',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    top: 'bottom',
                    left: 'center'
                },
                color: ['#478df5', '#eb60d3'],
                series: [{
                    type: 'pie',
                    radius: ['40%', '70%'],
                    roseType: 'radius',
                    avoidLabelOverlap: false,
                    itemStyle: {
                        borderRadius: 10,
                        borderColor: '#fff',
                        borderWidth: 2
                    },
                    label: {
                        show: false
                    },
                    data: [
                        { value: systemData.employeeMaleCount, name: '男性'},
                        { value: systemData.employeeCount - systemData.employeeMaleCount, name: '女性' }
                    ]
                }]
            });
            
            this.echart2 = echarts.init(this.$refs.echart2);
            this.echart2.setOption({
                title: {
                    text: '通知发布数',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                xAxis: {
                    type: 'category',
                    data: data.slice(2)
                },
                yAxis: {
                    type: 'value'
                },
                
                series: [{
                    // data: noticeData.slice(2),
                    data: [5,4,3,6],
                    type: 'bar'
                }]
            });

            this.echart3 = echarts.init(this.$refs.echart3);
            this.echart3.setOption({
                title: {
                    text: '小区业主男女比例',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    top: 'bottom',
                    left: 'center'
                },
                color: ['#478df5', '#eb60d3'],
                series: [{
                    name:'Access From',
                    type: 'pie',
                    radius: ['50%'],
                    avoidLabelOverlap: false,
                    itemStyle: {
                        borderRadius: 10,
                        borderColor: '#fff',
                        borderWidth: 2
                    },
                    label: {
                        show: false
                    },
                    data: [
                        { value: systemData.ownerMaleCount, name: '男性'},
                        { value: systemData.ownerCount - systemData.ownerMaleCount, name: '女性' }
                    ],
                    emphasis: {
                        itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }]
            });

        }
    },
    created(){
        // let page = this
        // let token = this.$store.getters.GET_TOKEN
        this.systemData = {
            systemUserCount:80,
            employeeCount:45,
            employeeMaleCount:20,
            houseCount:980,
            ownerCount:961,
            ownerMaleCount:500
        }
        //获取系统数据信息
        // this.$http.get("/heng/dataAnalysis/systemData",{
        //     headers:{token:token}
        // }).then(res=>{
        //     let result = res.data
        //     if(result.status === 0){
        //         page.systemData = result.data
        //     } else {
        //         page.$message.error(result.msg)
        //     }
        // },err=>{
        //     page.$message.error(err.msg)
        // })
    },
    activated(){
        this.lineChart.resize();
    },
    mounted(){
        
    },
    watch:{
        systemData:{
            handler(newValue){
                this.redrawLineChart(newValue)
            },
            deep:true
        }
    }
}
</script>

<style scoped>
    .lineChart {
        height: 350px;
        background-color: #fff;
        margin-bottom:32px;
        padding: 16px 16px 0;
    }
    .chart-wrapper {
        background: #fff;
        padding: 16px 16px 0;
        margin-bottom: 32px;
    }
    .bottom-echarts{
        background-color: #fff;
    }
    .echarts{
        width: 300px;
        height: 300px;
        margin-left: auto;
        margin-right: auto;
    }
    .container{
        background-color:#f0f2f5;
        padding: 0 30px;
    }
    .card-panel-col{
        margin-top: 32px;
        margin-bottom: 32px;
    }
    .card-panel {
        cursor: pointer;
        font-size: 12px;
        position: relative;
        overflow: hidden;
        color: #666;
        background: #fff;
        box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
        border-color: rgba(0, 0, 0, .05);
    }

    .card-panel-left{
        float: left;
        padding:8px;
        font-size: 50px;
        border-radius: 6px;
        margin-left: 18px;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
    }

    .icon-person{
        color: #40c9c6;
    }
    .icon-order{
        color: #f4516c;
    }
    .icon-goods{
        color: #34bfa3;
    }
    .icon-fitting{
        color: #36a3f7;
    }


    .card-panel:hover > .icon-person{
        background-color: #40c9c6;
        color: #fff;
    }
    .card-panel:hover > .icon-order{
        background-color: #f4516c;
        color: #fff;
    }
    .card-panel:hover > .icon-goods{
        background-color: #34bfa3;
        color: #fff;
    }
    .card-panel:hover > .icon-fitting{
        background-color: #36a3f7;
        color: #fff;
    }

    .card-panel-description{
        float: right;
        font-weight: bold;
        margin: 26px;
        margin-left: 0px;
    }
    .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
    }
    .card-panel-num {
        font-size: 20px;
    }
</style>
