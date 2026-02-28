import { createRouter, createWebHistory } from 'vue-router'
import store from '@/store'
import Cookies from 'js-cookie'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layouts/BasicLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Dashboard.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      {
        path: 'venue',
        name: 'Venue',
        component: () => import('@/views/venue/Venue.vue'),
        meta: { title: '场馆管理', icon: 'OfficeBuilding' }
      },
      {
        path: 'coach',
        name: 'Coach',
        component: () => import('@/views/coach/Coach.vue'),
        meta: { title: '教练管理', icon: 'User' }
      },
      {
        path: 'course',
        name: 'Course',
        component: () => import('@/views/course/Course.vue'),
        meta: { title: '课程管理', icon: 'Reading' }
      },
      {
        path: 'booking',
        name: 'Booking',
        component: () => import('@/views/booking/Booking.vue'),
        meta: { title: '预约管理', icon: 'Tickets' }
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('@/views/order/Order.vue'),
        meta: { title: '订单管理', icon: 'Wallet' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/User.vue'),
        meta: { title: '用户管理', icon: 'UserFilled' }
      },
      {
        path: 'system',
        name: 'System',
        component: () => import('@/views/system/System.vue'),
        meta: { title: '系统设置', icon: 'Setting' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = Cookies.get('accessToken')
  
  if (to.path === '/login') {
    next()
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
