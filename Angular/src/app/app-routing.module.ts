import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthLayoutComponent } from './shared/components/layouts/auth-layout/auth-layout.component';
import { AuthGuard } from './shared/services/auth-guard.service';
import { BlankLayoutComponent } from './shared/components/layouts/blank-layout/blank-layout.component';
import { AdminLayoutSidebarCompactComponent } from './shared/components/layouts/admin-layout-sidebar-compact/admin-layout-sidebar-compact.component';
import {TakeQuizComponent} from './views/forms/Quiz/take-quiz/take-quiz.component';
import {CreateQuizComponent} from './views/forms/Quiz/create-quiz/create-quiz.component';

import { ProjectComponent } from 'src/app/shared/components/Project/User/project/project.component';
import { ProjectdetailsComponent } from './shared/components/Project/User/projectdetails/projectdetails.component';
import { ProgressDashboardComponent } from './shared/components/Project/User/progress-dashboard/progress-dashboard.component';
import { DashboardProjectComponent } from './shared/components/Project/Admin/dashboard-project/dashboard-project.component';
import { AddProjectComponent } from './shared/components/Project/Admin/add-project/add-project.component';
import { ViewdetailsComponent } from './shared/components/Project/Admin/viewdetails/viewdetails.component';
import { PdfComponent } from './shared/components/Project/User/pdf/pdf.component';

import {NoAuthGuard} from './shared/services/no-auth.guard';


const userRoutes: Routes = [
    {
      path: 'dashboard',
      loadChildren: () => import('./views/dashboard/dashboard.module').then(m => m.DashboardModule)
    },
    {
      path: 'tickets',
      loadChildren: () => import('./views/tickets/tickets.module').then(m => m.TicketsModule)
    },
    {
      path: 'mailing',
      loadChildren: () => import('./views/Mail/mail.module').then(m => m.MailModule)
    },
    {
      path: 'ticketsStudent',
      loadChildren: () => import('./views/ticketsStudent/ticket-student.module').then(m => m.TicketStudentModule)
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
    { path: 'getallprojects', component: ProjectComponent},
    { path: 'projectdetails', component: ProjectdetailsComponent},
    { path: 'ProgressDashboard', component: ProgressDashboardComponent},
    { path: 'projects', component: DashboardProjectComponent, canActivate: [AuthGuard], data: { roles: ['TEACHER'] }},
    { path: 'addprojects', component: AddProjectComponent, canActivate: [AuthGuard], data: { roles: ['TEACHER'] }},
    { path: 'project/:id', component: ViewdetailsComponent, canActivate: [AuthGuard], data: { roles: ['TEACHER'] }},
    { path: 'pdf', component: PdfComponent },
    {
        path: 'settings',
        loadChildren: () => import('./views/settings/settings.module').then(m => m.SettingsModule)
    },
    {
        path: 'tools',
        loadChildren: () => import('./views/tools/tools.module').then(m => m.ToolsModule),
        canActivate: [AuthGuard],
        data: { roles: ['SUPERADMIN'] }
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
      data: { roles: ['STUDENT', 'TEACHER', 'ADMIN' , 'SUPERADMIN'] }
  },

  {
    path: '**',
    redirectTo: 'others/404'
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
