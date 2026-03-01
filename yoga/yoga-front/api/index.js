import { get, post } from './request'

// 认证相关
export const wxLogin = (code) => post('/auth/wx-login', { code })
export const refreshToken = (refreshToken) => post('/auth/refresh', { refreshToken })
export const bindPhone = (phone, code) => post('/auth/bind-phone', { phone, code })
export const logout = () => post('/auth/logout')

// 课程相关
export const getSessionList = (params) => get('/session/list', params)
export const getSessionDetail = (id) => get(`/session/${id}`)
export const searchSessions = (keyword, params) => get('/session/search', { keyword, ...params })
export const getSessionReviews = (id, params) => get(`/session/${id}/reviews`, params)

// 预约相关
export const createBooking = (sessionId) => post('/booking', { sessionId })
export const cancelBooking = (id) => post(`/booking/${id}/cancel`)
export const getMyBookings = (params) => get('/booking/my', params)
export const getBookingDetail = (id) => get(`/booking/${id}`)

// 用户相关
export const getUserInfo = () => get('/user/info')
export const updateUserInfo = (data) => put('/user/info', data)

export default {
  wxLogin,
  refreshToken,
  bindPhone,
  logout,
  getSessionList,
  getSessionDetail,
  searchSessions,
  getSessionReviews,
  createBooking,
  cancelBooking,
  getMyBookings,
  getBookingDetail,
  getUserInfo,
  updateUserInfo
}
