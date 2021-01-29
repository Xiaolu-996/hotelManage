import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
                              routes: [
                                  {
                                      path: '/',
                                      redirect: '/dashboard'
                                  },
                                  {
                                      path: '/',
                                      component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
                                      meta: { title: '自述文件' },
                                      children: [
                                          {
                                              path: '/dashboard',
                                              component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
                                              meta: { title: '系统首页' }
                                          },
                                          {
                                              path: '/reserve',
                                              component: () => import(/* webpackChunkName: "table" */ '../components/page/ReserveTable.vue'),
                                              meta: { title: '预定列表' }
                                          },
                                          {
                                              path: '/table',
                                              component: () => import(/* webpackChunkName: "table" */ '../components/page/BaseTable.vue'),
                                              meta: { title: '客户列表' }
                                          },
                                          {
                                              path: '/room',
                                              component: () => import(/* webpackChunkName: "table" */ '../components/page/Room.vue'),
                                              meta: { title: '房间列表' }
                                          },
                                          {
                                              path: '/cost',
                                              component: () => import(/* webpackChunkName: "tabs" */ '../components/page/Cost.vue'),
                                              meta: { title: '消费管理' }
                                          },
                                          {
                                              // 权限页面
                                              path: '/permission',
                                              component: () => import(/* webpackChunkName: "permission" */ '../components/page/Permission.vue'),
                                              meta: { title: '前台管理', permission: true }
                                          }
                                      ]
                                  },
                                  {
                                      path: '/login',
                                      component: () => import(/* webpackChunkName: "login" */ '../components/page/Login.vue'),
                                      meta: { title: '登录' }
                                  },
                                  {
                                      path: '*',
                                      redirect: '/404'
                                  }
                              ]
                          });





