<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  }
}
</script>

<style>
page {
  background-color: #f8f8f8;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
}

.container {
  padding: 20rpx;
}

.text-primary {
  color: #00b894;
}

.text-secondary {
  color: #636e72;
}

.text-muted {
  color: #b2bec3;
}

.bg-primary {
  background-color: #00b894;
}

.card {
  background-color: #ffffff;
  border-radius: 12rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.btn-primary {
  background-color: #00b894;
  color: #ffffff;
  border-radius: 8rpx;
  padding: 20rpx 40rpx;
  text-align: center;
}

.btn-primary:active {
  background-color: #00a085;
}
</style>
