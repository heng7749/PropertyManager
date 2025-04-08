<template>
    <div>
      <el-row>
        <!-- 右侧，周一到周五具体内容 -->
        <el-col :span="22">
          <!-- 第一行表头，周一到周日 -->
          <div class="top-con">
            <div class="top" v-for="item in top" :key="item">星期{{ item }}</div>
          </div>
          <!-- 日历号 -->
          <div class="date-con">
            <div class="date dutyMsg" :class="[item.thisMonth, item.isToday, item.afterToday]"
              v-for="(item, index) in visibleCalendar" :key="index"  @click="dutySelect(index)">
              <div>{{ item.day }}</div>
              <div v-if="dutiesData[format(item.date,'yyyy-MM-dd')] != undefined">
                <span>{{ (dutiesData[format(item.date,'yyyy-MM-dd')])[0].employee.name}}</span>
                <span style="margin-left: 5px;">{{ (dutiesData[format(item.date,'yyyy-MM-dd')])[0].shift}}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script>
  // import utils from './utils.js'
  export default {
    name:'Calendar',
    props: {
      time: {
        type: Object,
        default: () => {
          return {}
        }
      },
      dutiesData: {
        type: Object,
        default: () => {
          return {}
        }
      }
    },
    data() {
      return {
        top: ['一', '二', '三', '四', '五', '六', '日'],
        // visibleCalendar:[]
      }
    },
    created() {
    },
    methods: {
      // 重绘
      redrawCalendar(time){
        // 获取当前月份显示日历,共42天
        // 获取今天的年月日
        const today = new Date()
          today.setHours(0)
          today.setMinutes(0)
          today.setSeconds(0)
          today.setMilliseconds(0)
          
    
          let calendarArr = []
          // 获取当前月份第一天
          const currentFirstDay = new Date(time.year, time.month, 1)
          // 获取第一天是周几，注意周日的时候getDay()返回的是0，要做特殊处理
          const weekDay =
            currentFirstDay.getDay() === 0 ? 7 : currentFirstDay.getDay()
          // 用当前月份第一天减去周几前面几天，就是看见的日历的第一天
          const startDay = currentFirstDay - (weekDay - 1) * 24 * 3600 * 1000
          // 我们统一用42天来显示当前显示日历
          for (let i = 0; i < 42; i++) {
            const date = new Date(startDay + i * 24 * 3600 * 1000)
            calendarArr.push({
              date: new Date(startDay + i * 24 * 3600 * 1000),
              year: date.getFullYear(),
              month: date.getMonth(),
              day: date.getDate(),
              // 是否在当月
              thisMonth:
                date.getFullYear() === today.getFullYear() &&
                date.getMonth() === today.getMonth()
                  ? 'thisMonth'
                  : '',
              // 是否是今天
              isToday:
                date.getFullYear() === today.getFullYear() &&
                date.getMonth() === today.getMonth() &&
                date.getDate() === today.getDate()
                  ? 'isToday'
                  : '',
              // 是否在今天之后
              afterToday: date.getTime() >= today.getTime() ? 'afterToday' : ''
          })
        }
        //判断最后一行是否含有当月日期
        if (!calendarArr[35].thisMonth) {
          calendarArr = calendarArr.splice(0,35)
        }
        return calendarArr
      },
      format(date, fmt) {
        if (typeof date == 'string') {
          return date;
        }

        if (!fmt) fmt = "yyyy-MM-dd hh:mm:ss";
        var o = {
          "M+": date.getMonth() + 1, //月份
          "d+": date.getDate(), //日
          "H+": date.getHours(), //小时
          "m+": date.getMinutes(), //分
          "s+": date.getSeconds(), //秒
          "q+": Math.floor((date.getMonth() + 3) / 3), //季度
          "S": date.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
          if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
      },
      // 选中日期
      dutySelect(index){
        let clicked_date = this.format(this.visibleCalendar[index].date,'yyyy-MM-dd')
        //该组件自定义事件onItemClick
        this.$emit('onItemClick',clicked_date)
      }
    },
    computed: {
      visibleCalendar(){
        return this.redrawCalendar(this.time)
      }
    }
  }
  </script>
  <style scoped>
  .top-con {
    display: flex;
    align-items: center;
  }
  .top-con .top {
      width: 13%;
      background-color: rgb(242, 242, 242);
      padding: 10px 0;
      margin: 5px;
      text-align: center;
    }

  .date-con {
    display: flex;
    flex-wrap: wrap;
  }

  .date-con .date {
    width: 13%;
    text-align: center;
    padding: 10px 0;
    margin: 5px;
    background-color:#F2F6FC;
    opacity: 0.5;
  }

  .date-con .thisMonth {  
    background-color:#C0C4CC;
    opacity: 1;
  }

    .date-con  .isToday {
      font-weight: 700;
      background-color: #F56C6C;
    }

  .tip-con {
    margin-top: 51px;
  }
  
  .tip-con .tip {
      margin-top: 21px;
      width: 100%;
    }

    .dutyMsg{
      height: 42px;
      font-size: 15px;
      margin-top: 4px;
    }
  </style>
  
  