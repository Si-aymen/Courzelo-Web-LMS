import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { DashboadDefaultComponent } from "./dashboad-default/dashboad-default.component";
import { DashboardV2Component } from "./dashboard-v2/dashboard-v2.component";
import { DashboardV3Component } from "./dashboard-v3/dashboard-v3.component";
import { DashboardV4Component } from "./dashboard-v4/dashboard-v4.component";
import { JobofferComponent } from "./RecruitementComponents/joboffer/joboffer.component";

const routes: Routes = [
  {
    path: "v1",
    component: DashboadDefaultComponent,
  },
  {
    path: "v2",
    component: DashboardV2Component,
  },
  {
    path: "v3",
    component: DashboardV3Component,
  },
  {
    path: "v4",
    component: DashboardV4Component,
  },
  {
    path: "joboffers",
    component: JobofferComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {}
