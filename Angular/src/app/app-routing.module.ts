import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthLayoutComponent } from './shared/components/layouts/auth-layout/auth-layout.component';
import { AuthGuard } from './shared/services/auth-guard.service';
import { BlankLayoutComponent } from './shared/components/layouts/blank-layout/blank-layout.component';
import { AdminLayoutSidebarCompactComponent } from './shared/components/layouts/admin-layout-sidebar-compact/admin-layout-sidebar-compact.component';

import { ProjectComponent } from 'src/app/shared/components/Project/User/project/project.component';
import { ProjectDetailsComponent } from './shared/components/Project/User/projectdetails/projectdetails.component';
import { ProgressDashboardComponent } from './shared/components/Project/User/progress-dashboard/progress-dashboard.component';
import { DashboardProjectComponent } from './shared/components/Project/Admin/dashboard-project/dashboard-project.component';
import { AddProjectComponent } from './shared/components/Project/Admin/add-project/add-project.component';
import { ViewdetailsComponent } from './shared/components/Project/Admin/viewdetails/viewdetails.component';
import { PdfComponent } from './shared/components/Project/User/pdf/pdf.component';

import {NoAuthGuard} from './shared/services/no-auth.guard';
import { ProjectCalendarComponent } from './shared/components/Project/User/project-calendar/project-calendar.component';
import { PublicationComponent } from './shared/components/Project/User/publication/publication.component';
import { RevisionComponent } from './shared/components/Revision/revision/revision.component';

const userRoutes: Routes = [
    {
      path: 'dashboard',
      loadChildren: () => import('./views/dashboard/dashboard.module').then(m => m.DashboardModule)
    },
    {
      path: 'uikits',
      loadChildren: () => import('./views/ui-kits/ui-kits.module').then(m => m.UiKitsModule)
    },
    {
      path: 'forms',
      loadChildren: () => import('./views/forms/forms.module').then(m => m.AppFormsModule)
    },
    {
      path: 'invoice',
      loadChildren: () => import('./views/invoice/invoice.module').then(m => m.InvoiceModule)
    },
    {
      path: 'inbox',
      loadChildren: () => import('./views/inbox/inbox.module').then(m => m.InboxModule)
    },
    {
      path: 'calendar',
      loadChildren: () => import('./views/calendar/calendar.module').then(m => m.CalendarAppModule)
    },
    {
      path: 'chat',
      loadChildren: () => import('./views/chat/chat.module').then(m => m.ChatModule)
    },
    {
      path: 'contacts',
      loadChildren: () => import('./views/contacts/contacts.module').then(m => m.ContactsModule)
    },
    {
      path: 'tables',
      loadChildren: () => import('./views/data-tables/data-tables.module').then(m => m.DataTablesModule)
    },
    {
      path: 'pages',
      loadChildren: () => import('./views/pages/pages.module').then(m => m.PagesModule)
    },
    {
        path: 'icons',
        loadChildren: () => import('./views/icons/icons.module').then(m => m.IconsModule)

    } ,
    
    
    { path: 'projects', component: DashboardProjectComponent},
    { path: 'addprojects', component: AddProjectComponent},
    { path: 'project/:id', component: ViewdetailsComponent },

    { path: 'pdf', component: PdfComponent },
    { path: 'projectcalendar/:id', component: ProjectCalendarComponent },
    { path: 'getallprojects', component: ProjectComponent},
    { path: 'projectdetails/:id', component: ProjectDetailsComponent},
    { path: 'ProgressDashboard/:id', component: ProgressDashboardComponent},
    { path: 'publication/:id', component: PublicationComponent},
   /////////////////////////////////////////////////////////////////é
   { path: 'revision', component: RevisionComponent },
    {
        path: 'settings',
        loadChildren: () => import('./views/settings/settings.module').then(m => m.SettingsModule)
    },
    {
        path: 'tools',
        loadChildren: () => import('./views/tools/tools.module').then(m => m.ToolsModule)
    }

  ];

const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard/v1',
    pathMatch: 'full'
  },
  {
    path: '',
    component: AuthLayoutComponent,
    children: [
      {
        path: 'sessions',
          canLoad: [NoAuthGuard],
          loadChildren: () => import('./views/sessions/sessions.module').then(m => m.SessionsModule)
      }
    ]
  },
  {
    path: '',
    component: BlankLayoutComponent,
    children: [
      {
        path: 'others',
        loadChildren: () => import('./views/others/others.module').then(m => m.OthersModule)
      }
    ]
  },
  {
    path: '',
    component: AdminLayoutSidebarCompactComponent,
    canActivate: [AuthGuard],
    children: userRoutes,
      data: { roles: ['STUDENT', 'TEACHER', 'ADMIN' , 'SUPER_ADMIN'] }
  },
  {
    path: '**',
    redirectTo: 'others/404'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
