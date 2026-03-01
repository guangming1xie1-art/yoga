<template>
  <view class="container">
    <view class="header">
      <text class="title">瑜伽预约</text>
      <text class="subtitle">发现你的身心平衡</text>
    </view>
    
    <view class="card">
      <text class="card-title">热门课程</text>
      <text class="card-desc">即将上线...</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {}
  },
  onLoad() {
    this.init()
  },
  methods: {
    async init() {
      // 初始化首页数据
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20rpx;
}

.header {
  padding: 40rpx 0;
  text-align: center;
}

.title {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #00b894;
  margin-bottom: 16rpx;
}

.subtitle {
  display: block;
  font-size: 28rpx;
  color: #636e72;
}

.card {
  background: #ffffff;
  border-radius: 12rpx;
  padding: 24rpx;
  margin-top: 20rpx;
}

.card-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #2d3436;
  margin-bottom: 12rpx;
}

.card-desc {
  display: block;
  font-size: 26rpx;
  color: #b2bec3;
}
</style>
