import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

//准备actions   用于响应组件中的动作    
const actions  = {
    setToken(context,value){
        context.commit('SETTOKEN',value)
    }
}

//准备mutations 用于操作数据(state)
const mutations = {
    SETTOKEN(state,value){
        state.token = value
    },
    ADD_TABS:(state,tab)=>{
        if(state.editableTabs.findIndex(e=>e.name===tab.path)===-1){
            state.editableTabs.push({
                title:tab.name,
                name:tab.path
            })
        }
        state.editableTabsValue = tab.path
    },
    REMOVE_TABS(state,targetName){
        let tabs = state.editableTabs
        state.editableTabs = tabs.filter(tab => tab.name !== targetName);
    },
    SET_EDITABLETABSVALUE(state,activeName){
        state.editableTabsValue = activeName
    },
    RESET_TABS:(state)=>{
        state.editableTabsValue = '/'
        state.editableTabs = [{
            title:'系统数据',
            name:'/'
        }]
    }
}

//准备state 用于存储数据
const state = {
    token:'',
    editableTabsValue:'/',
    editableTabs:[{
        title:'系统数据',
        name:'/'
    }]
}
// 准备getter用于将state中的数据进行加工
const getters = {
    GET_TOKEN(state){
        return state.token
    }
}

// 创建并暴露store
export default new Vuex.Store({
    actions,
    mutations,
    state,
    getters
})
