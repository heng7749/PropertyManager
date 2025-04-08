<template>
  <el-container class="index-con">
    <el-aside :class="showclass">
      <LeftNavBar></LeftNavBar>
    </el-aside>
    <el-container class="main-con">
      <el-header class="index-header" style="padding:0px;">
        <NavBar></NavBar>
      </el-header>
      <el-main clss="index-main" style="padding:0px">
        <el-tabs :value="getStoreEditableTabsValue" type="card" closable @edit="handleTabsEdit" @tab-click="tabTag">
          <el-tab-pane
            :key="item.name"
            v-for="item in getStoreEditableTabs"
            :label="item.title"
            :name="item.name">
          </el-tab-pane>
        </el-tabs>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import LeftNavBar from '../components/LeftNavBar.vue'
import NavBar from '../components/NavBar.vue'
export default {
  name:'Index',
  components:{LeftNavBar,NavBar},
  data() {
    return {
      showclass: 'asideshow',
      showtype: false,
      tabIndex: 0
    }
  },
  created() {
    this.$bus.$on('toggle', value => {
      if (value) {
        this.showclass = 'asideshow';
      } else {
        this.showclass = 'aside';
      }
    })

  },
  methods:{
    tabTag(tab){
      this.$store.commit('SET_EDITABLETABSVALUE',tab.name)
      this.$router.push(tab.name)
    },
    handleTabsEdit(targetName, action) {
      if (action === 'remove') {
        let tabs = this.$store.state.editableTabs;
        let activeName = this.$store.state.editableTabsValue;
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              let nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                activeName = nextTab.name;
              }
            }
          });
        }
        this.$store.commit('SET_EDITABLETABSVALUE',activeName)
        this.$store.commit('REMOVE_TABS',targetName)
        this.$router.push(activeName)
      }
    }
  },
  computed:{
    getStoreEditableTabsValue(){
      return this.$store.state.editableTabsValue
    },
    getStoreEditableTabs(){
      return this.$store.state.editableTabs
    }
  }
}
</script>

<style>

.el-tabs {
  height: 41px!important;
  overflow: hidden!important;
}

.index-con {
  height: 100%;
  width: 100%;
  box-sizing: border-box;
}

.aside {
  width: 64px !important;
  height: 100%;
  background-color: #334157;
  margin: 0px;
}
.asideshow {
  width: 200px !important;
  height: 100%;
  background-color: #334157;
  margin: 0px;
}
.index-header,.index-main {
  padding: 0px;
  border-left: 2px solid #333;
}
</style>