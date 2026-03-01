import { createRouter, createWebHistory } from 'vue-router'
import BasicLayout from '@/layouts/BasicLayout.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/dashboard/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: BasicLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '仪表盘', icon: 'Odometer' }
      },
      {
        path: 'venue',
        name: 'Venue',
        component: () => import('@/views/venue/index.vue'),
        meta: { title: '场馆管理', icon: 'OfficeBuilding' }
      },
      {
        path: 'coach',
        name: 'Coach',
        component: () => import('@/views/coach/index.vue'),
        meta: { title: '教练管理', icon: 'User' }
      },
      {
        path: 'template',
        name: 'Template',
        component: () => import('@/views/template/index.vue'),
        meta: { title: '课程模板', icon: 'Document' }
      },
      {
        path: 'schedule',
        name: 'Schedule',
        component: () => import('@/views/schedule/index.vue'),
        meta: { title: '排课管理', icon: 'Calendar' }
      },
      {
        path: 'session',
        name: 'Session',
        component: () => import('@/views/session/index.vue'),
        meta: { title: '课程排期', icon: 'Timer' }
      },
      {
        path: 'booking',
        name: 'Booking',
        component: () => import('@/views/booking/index.vue'),
        meta: { title: '预约管理', icon: 'Tickets' }
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('@/views/order/index.vue'),
        meta: { title: '订单管理', icon: 'Wallet' }
      },
      {
        path: 'review',
        name: 'Review',
        component: () => import('@/views/review/index.vue'),
        meta: { title: '评价管理', icon: 'ChatDotSquare' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/index.vue'),
        meta: { title: '用户管理', icon: 'Avatar' }
      },
      {
        path: 'notification',
        name: 'Notification',
        component: () => import('@/views/notification/index.vue'),
        meta: { title: '通知管理', icon: 'Bell' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
