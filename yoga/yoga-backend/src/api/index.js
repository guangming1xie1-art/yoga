import request from './request'

// 认证
export const login = (data) => request.post('/auth/login', data)
export const logout = () => request.post('/auth/logout')
export const refreshToken = (token) => request.post('/auth/refresh', { refreshToken: token })

// 场馆管理
export const getVenueList = (params) => request.get('/venue/list', { params })
export const getVenueDetail = (id) => request.get(`/venue/${id}`)
export const createVenue = (data) => request.post('/venue', data)
export const updateVenue = (id, data) => request.put(`/venue/${id}`, data)
export const deleteVenue = (id) => request.delete(`/venue/${id}`)

// 教练管理
export const getCoachList = (params) => request.get('/coach/list', { params })
export const createCoach = (data) => request.post('/coach', data)
export const updateCoach = (id, data) => request.put(`/coach/${id}`, data)

// 课程模板
export const getTemplateList = (params) => request.get('/template/list', { params })
export const createTemplate = (data) => request.post('/template', data)

// 排课管理
export const getScheduleList = (params) => request.get('/schedule/list', { params })
export const createSchedule = (data) => request.post('/schedule', data)

// 课程排期
export const getSessionList = (params) => request.get('/session/list', { params })

// 预约管理
export const getBookingList = (params) => request.get('/booking/list', { params })

// 订单管理
export const getOrderList = (params) => request.get('/order/list', { params })

// 用户管理
export const getUserList = (params) => request.get('/user/list', { params })

export default {
  login,
  logout,
  refreshToken,
  getVenueList,
  getVenueDetail,
  createVenue,
  updateVenue,
  deleteVenue,
  getCoachList,
  createCoach,
  updateCoach,
  getTemplateList,
  createTemplate,
  getScheduleList,
  createSchedule,
  getSessionList,
  getBookingList,
  getOrderList,
  getUserList
}
