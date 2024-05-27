/* prettier-ignore-start */

/* eslint-disable */

// @ts-nocheck

// noinspection JSUnusedGlobalSymbols

// This file is auto-generated by TanStack Router

import { createFileRoute } from '@tanstack/react-router'

// Import Routes

import { Route as rootRoute } from './routes/__root'
import { Route as LoginImport } from './routes/login'
import { Route as DashboardImport } from './routes/dashboard'
import { Route as IndexImport } from './routes/index'

// Create Virtual Routes

const DashboardSupplierLazyImport = createFileRoute('/dashboard/supplier')()
const DashboardProductLazyImport = createFileRoute('/dashboard/product')()
const DashboardHomeLazyImport = createFileRoute('/dashboard/home')()
const DashboardCategoryLazyImport = createFileRoute('/dashboard/category')()

// Create/Update Routes

const LoginRoute = LoginImport.update({
  path: '/login',
  getParentRoute: () => rootRoute,
} as any)

const DashboardRoute = DashboardImport.update({
  path: '/dashboard',
  getParentRoute: () => rootRoute,
} as any)

const IndexRoute = IndexImport.update({
  path: '/',
  getParentRoute: () => rootRoute,
} as any)

const DashboardSupplierLazyRoute = DashboardSupplierLazyImport.update({
  path: '/supplier',
  getParentRoute: () => DashboardRoute,
} as any).lazy(() =>
  import('./routes/dashboard.supplier.lazy').then((d) => d.Route),
)

const DashboardProductLazyRoute = DashboardProductLazyImport.update({
  path: '/product',
  getParentRoute: () => DashboardRoute,
} as any).lazy(() =>
  import('./routes/dashboard.product.lazy').then((d) => d.Route),
)

const DashboardHomeLazyRoute = DashboardHomeLazyImport.update({
  path: '/home',
  getParentRoute: () => DashboardRoute,
} as any).lazy(() =>
  import('./routes/dashboard.home.lazy').then((d) => d.Route),
)

const DashboardCategoryLazyRoute = DashboardCategoryLazyImport.update({
  path: '/category',
  getParentRoute: () => DashboardRoute,
} as any).lazy(() =>
  import('./routes/dashboard.category.lazy').then((d) => d.Route),
)

// Populate the FileRoutesByPath interface

declare module '@tanstack/react-router' {
  interface FileRoutesByPath {
    '/': {
      preLoaderRoute: typeof IndexImport
      parentRoute: typeof rootRoute
    }
    '/dashboard': {
      preLoaderRoute: typeof DashboardImport
      parentRoute: typeof rootRoute
    }
    '/login': {
      preLoaderRoute: typeof LoginImport
      parentRoute: typeof rootRoute
    }
    '/dashboard/category': {
      preLoaderRoute: typeof DashboardCategoryLazyImport
      parentRoute: typeof DashboardImport
    }
    '/dashboard/home': {
      preLoaderRoute: typeof DashboardHomeLazyImport
      parentRoute: typeof DashboardImport
    }
    '/dashboard/product': {
      preLoaderRoute: typeof DashboardProductLazyImport
      parentRoute: typeof DashboardImport
    }
    '/dashboard/supplier': {
      preLoaderRoute: typeof DashboardSupplierLazyImport
      parentRoute: typeof DashboardImport
    }
  }
}

// Create and export the route tree

export const routeTree = rootRoute.addChildren([
  IndexRoute,
  DashboardRoute.addChildren([
    DashboardCategoryLazyRoute,
    DashboardHomeLazyRoute,
    DashboardProductLazyRoute,
    DashboardSupplierLazyRoute,
  ]),
  LoginRoute,
])

/* prettier-ignore-end */
