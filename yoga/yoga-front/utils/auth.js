export const getToken = () => {
  return uni.getStorageSync('token') || ''
}

export const setToken = (token) => {
  uni.setStorageSync('token', token)
}

export const removeToken = () => {
  uni.removeStorageSync('token')
}

export const isLoggedIn = () => {
  return !!getToken()
}

export const checkLogin = () => {
  if (!isLoggedIn()) {
    uni.showToast({
      title: '请先登录',
      icon: 'none'
    })
    return false
  }
  return true
}
