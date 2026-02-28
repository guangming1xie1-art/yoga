<script setup>
import { onLaunch, onShow, onHide } from '@dcloudio/uni-app'

onLaunch(() => {
  console.log('App Launch')
  // 检查登录状态
  const token = uni.getStorageSync('accessToken')
  if (!token) {
    // 未登录，跳转到登录页
    // uni.reLaunch({ url: '/pages/login/login' })
  }
})

onShow(() => {
  console.log('App Show')
})

onHide(() => {
  console.log('App Hide')
})
</script>

<style lang="scss">
@import '@/common/styles/index.scss';
</style>
