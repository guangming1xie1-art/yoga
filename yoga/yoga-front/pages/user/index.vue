<template>
  <view class="container">
    <view class="user-card" v-if="isLoggedIn">
      <image class="avatar" :src="userInfo?.avatarUrl || '/static/default-avatar.png'" />
      <text class="nickname">{{ userInfo?.nickname || '用户' }}</text>
    </view>
    
    <view class="user-card" v-else>
      <button class="login-btn" @click="handleLogin">微信登录</button>
    </view>
    
    <view class="menu-list">
      <view class="menu-item" @click="goTo('/pages/booking/index')">
        <text class="menu-text">我的预约</text>
        <text class="menu-arrow">></text>
      </view>
      <view class="menu-item" @click="handleLogout" v-if="isLoggedIn">
        <text class="menu-text">退出登录</text>
        <text class="menu-arrow">></text>
      </view>
    </view>
  </view>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import { wxLogin, logout } from '@/api'

export default {
  computed: {
    ...mapGetters(['isLoggedIn', 'userInfo'])
  },
  methods: {
    ...mapActions(['login', 'logout']),
    
    async handleLogin() {
      try {
        const { code } = await uni.login({ provider: 'weixin' })
        const res = await wxLogin(code)
        this.login({
          token: res.accessToken,
          userInfo: {}
        })
        uni.showToast({ title: '登录成功', icon: 'success' })
      } catch (e) {
        console.error('登录失败', e)
      }
    },
    
    async handleLogout() {
      try {
        await logout()
        this.logout()
        uni.showToast({ title: '已退出', icon: 'success' })
      } catch (e) {
        console.error('退出失败', e)
      }
    },
    
    goTo(url) {
      uni.navigateTo({ url })
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20rpx;
}

.user-card {
  background: #ffffff;
  border-radius: 12rpx;
  padding: 40rpx;
  text-align: center;
  margin-bottom: 20rpx;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  margin-bottom: 20rpx;
}

.nickname {
  display: block;
  font-size: 32rpx;
  color: #2d3436;
}

.login-btn {
  background: #00b894;
  color: #ffffff;
  border: none;
  padding: 20rpx 60rpx;
  border-radius: 8rpx;
}

.menu-list {
  background: #ffffff;
  border-radius: 12rpx;
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-text {
  font-size: 28rpx;
  color: #2d3436;
}

.menu-arrow {
  font-size: 28rpx;
  color: #b2bec3;
}
</style>
