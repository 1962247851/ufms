import Vue from 'vue'
import VueRouter from 'vue-router'

import Header from "@/components/Header";
import NavigationDrawer from "@/components/NavigationDrawer";

Vue.use(VueRouter)

export const constantRouters = [
    {
        path: "",
        redirect: "/index",
    },
    {
        path: "/index",
        components: {
            header: Header,
            default: () => import("@/components/Index"),
        }
    },
    {
        path: '/index/register',
        components: {
            default: () => import("@/components/index/Register"),
        }
    },
    {
        path: '/index/login',
        components: {
            default: () => import("@/components/index/Login"),
        }
    },
    {
        path: '/dashboard',
        components: {
            header: Header,
            default: () => import("@/components/dashboard/Dashboard"),
        },
        children: [
            {
                path: "",
                redirect: "/dashboard/home",
            },
            {
                path: "/dashboard/home",
                components: {
                    drawer: NavigationDrawer,
                    default: () => import("@/components/dashboard/Home"),
                },
            },
            {
                path: "/dashboard/product",
                components: {
                    drawer: NavigationDrawer,
                    default: () => import("@/components/dashboard/Product"),
                }
            },
            {
                path: "/dashboard/new-product",
                components: {
                    drawer: NavigationDrawer,
                    default: () => import("@/components/dashboard/NewProduct"),
                },
            },
            {
                path: "/dashboard/manage",
                components: {
                    drawer: NavigationDrawer,
                    default: () => import("@/components/dashboard/Manage"),
                },
            },
            {
                path: "/dashboard/feedbacks",
                components: {
                    drawer: NavigationDrawer,
                    default: () => import("@/components/dashboard/Feedbacks"),
                },
            },
            {
                path: "/dashboard/settings",
                components: {
                    drawer: NavigationDrawer,
                    default: () => import("@/components/dashboard/Settings"),
                },
            },
            {
                path: "/dashboard/products",
                components: {
                    drawer: NavigationDrawer,
                    default: () => import("@/components/dashboard/Products"),
                },
            },
            {
                path: "/dashboard/analytics",
                components: {
                    drawer: NavigationDrawer,
                    default: () => import("@/components/dashboard/Analytics"),
                },
            }
        ]
    },
    {
        path: "*",
        component: () => import("@/components/error/NotFound"),
    },
]

export default new VueRouter({
    routes: constantRouters,
    scrollBehavior(to, from, saveTop) {
        if (saveTop) {
            return saveTop;
        } else {
            return {x: 0, y: 0}
        }
    }
    // scrollBehavior: (to, from, savedPosition) => {
    //     let scrollTo = 0
    //     if (to.hash) {
    //         scrollTo = to.hash
    //     } else if (savedPosition) {
    //         scrollTo = savedPosition.y
    //     }
    //     return goTo(scrollTo)
    // },
})
