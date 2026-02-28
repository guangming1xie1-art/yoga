import request from './request'

// 认证相关
export const authApi = {
  wxLogin: (code) => request.post('/api/front/auth/wx-login', { code }),
  bindPhone: (phone, code) => request.post('/api/front/auth/bind-phone', { phone, code }),
  refreshToken: (refreshToken) => request.post('/api/front/auth/refresh', { refreshToken }),
  logout: () => request.post('/api/front/auth/logout')
}

// 首页
export const homeApi = {
  getBanners: () => request.get('/api/front/home/banners'),
  getNotices: () => request.get('/api/front/home/notices'),
  getRecommendedCourses: () => request.get('/api/front/home/recommended')
}

// 课程排期
export const sessionApi = {
  list: (params) => request.get('/api/front/session/list', params),
  detail: (id) => request.get(`/api/front/session/${id}`),
  search: (params) => request.get('/api/front/session/search', params),
  reviews: (id, params) => request.get(`/api/front/session/${id}/reviews`, params)
}

// 预约
export const bookingApi = {
  create: (sessionId) => request.post('/api/front/booking/create', { sessionId }),
  cancel: (id, reason) => request.post('/api/front/booking/cancel', { id, reason }),
  myList: (params) => request.get('/api/front/booking/my-list', params),
  detail: (id) => request.get(`/api/front/booking/${id}`)
}

// 订单
export const orderApi = {
  create: (bookingId) => request.post('/api/front/order/create', { bookingId }),
  pay: (orderId) => request.post('/api/front/order/pay', { orderId }),
  cancel: (orderId, reason) => request.post('/api/front/order/cancel', { orderId, reason }),
  myList: (params) => request.get('/api/front/order/my-list', params),
  detail: (id) => request.get(`/api/front/order/${id}`)
}

// 用户
export const userApi = {
  profile: () => request.get('/api/front/user/profile'),
  updateProfile: (data) => request.put('/api/front/user/profile', data),
  checkin: (qrCode) => request.post('/api/front/user/checkin', { qrCode })
}

// 场馆
export const venueApi = {
  list: (params) => request.get('/api/front/venue/list', params),
  detail: (id) => request.get(`/api/front/venue/${id}`)
}
