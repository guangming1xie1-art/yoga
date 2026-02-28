import { defineStore } from 'pinia'
import Cookies from 'js-cookie'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: Cookies.get('accessToken') || '',
    userInfo: null
  }),

  getters: {
    isLoggedIn: (state) => !!state.token
  },

  actions: {
    setToken(token) {
      this.token = token
      Cookies.set('accessToken', token, { expires: 7 })
    },

    setUserInfo(info) {
      this.userInfo = info
    },

    logout() {
      this.token = ''
      this.userInfo = null
      Cookies.remove('accessToken')
    }
  }
})
