import request from './request'

// 认证
export const authApi = {
  login: (data) => request.post('/api/backend/auth/login', data),
  logout: () => request.post('/api/backend/auth/logout'),
  getUserInfo: () => request.get('/api/backend/auth/user-info')
}

// 场馆
export const venueApi = {
  list: (params) => request.get('/api/backend/venue/list', { params }),
  create: (data) => request.post('/api/backend/venue/create', data),
  update: (data) => request.put('/api/backend/venue/update', data),
  delete: (id) => request.delete(`/api/backend/venue/${id}`),
  detail: (id) => request.get(`/api/backend/venue/${id}`)
}

// 教练
export const coachApi = {
  list: (params) => request.get('/api/backend/coach/list', { params }),
  create: (data) => request.post('/api/backend/coach/create', data),
  update: (data) => request.put('/api/backend/coach/update', data),
  delete: (id) => request.delete(`/api/backend/coach/${id}`),
  detail: (id) => request.get(`/api/backend/coach/${id}`)
}

// 课程模板
export const courseApi = {
  list: (params) => request.get('/api/backend/course/list', { params }),
  create: (data) => request.post('/api/backend/course/create', data),
  update: (data) => request.put('/api/backend/course/update', data),
  delete: (id) => request.delete(`/api/backend/course/${id}`),
  detail: (id) => request.get(`/api/backend/course/${id}`)
}

// 排课
export const scheduleApi = {
  list: (params) => request.get('/api/backend/schedule/list', { params }),
  create: (data) => request.post('/api/backend/schedule/create', data),
  update: (data) => request.put('/api/backend/schedule/update', data),
  delete: (id) => request.delete(`/api/backend/schedule/${id}`),
  generate: (id) => request.post(`/api/backend/schedule/${id}/generate`)
}

// 预约
export const bookingApi = {
  list: (params) => request.get('/api/backend/booking/list', { params }),
  cancel: (id, reason) => request.post(`/api/backend/booking/${id}/cancel`, { reason }),
  detail: (id) => request.get(`/api/backend/booking/${id}`)
}

// 订单
export const orderApi = {
  list: (params) => request.get('/api/backend/order/list', { params }),
  detail: (id) => request.get(`/api/backend/order/${id}`),
  refund: (id, reason) => request.post(`/api/backend/order/${id}/refund`, { reason })
}

// 用户
export const userApi = {
  list: (params) => request.get('/api/backend/user/list', { params }),
  disable: (id) => request.post(`/api/backend/user/${id}/disable`),
  enable: (id) => request.post(`/api/backend/user/${id}/enable`)
}

// 统计
export const statisticsApi = {
  dashboard: () => request.get('/api/backend/statistics/dashboard'),
  revenue: (params) => request.get('/api/backend/statistics/revenue', { params })
}
