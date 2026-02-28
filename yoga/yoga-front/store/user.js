import { defineStore } from 'pinia'
import { authApi } from '@/api/index'

export const useUserStore = defineStore('user', {
  state: () => ({
    accessToken: uni.getStorageSync('accessToken') || '',
    refreshToken: uni.getStorageSync('refreshToken') || '',
    userInfo: null
  }),

  getters: {
    isLoggedIn: (state) => !!state.accessToken
  },

  actions: {
    async login(code) {
      try {
        const res = await authApi.wxLogin(code)
        this.accessToken = res.accessToken
        this.refreshToken = res.refreshToken
        uni.setStorageSync('accessToken', res.accessToken)
        uni.setStorageSync('refreshToken', res.refreshToken)
        return true
      } catch (e) {
        console.error('登录失败', e)
        return false
      }
    },

    async logout() {
      try {
        await authApi.logout()
      } catch (e) {
        // 忽略登出接口错误
      }
      this.accessToken = ''
      this.refreshToken = ''
      this.userInfo = null
      uni.removeStorageSync('accessToken')
      uni.removeStorageSync('refreshToken')
    },

    setUserInfo(info) {
      this.userInfo = info
    }
  }
})
