import { createStore } from 'vuex'

const store = createStore({
  state: {
    token: uni.getStorageSync('token') || '',
    userInfo: uni.getStorageSync('userInfo') || null
  },
  
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      uni.setStorageSync('token', token)
    },
    
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      uni.setStorageSync('userInfo', userInfo)
    },
    
    CLEAR_USER(state) {
      state.token = ''
      state.userInfo = null
      uni.removeStorageSync('token')
      uni.removeStorageSync('userInfo')
    }
  },
  
  actions: {
    login({ commit }, { token, userInfo }) {
      commit('SET_TOKEN', token)
      commit('SET_USER_INFO', userInfo)
    },
    
    logout({ commit }) {
      commit('CLEAR_USER')
    }
  },
  
  getters: {
    isLoggedIn: state => !!state.token,
    userInfo: state => state.userInfo,
    token: state => state.token
  }
})

export default store
