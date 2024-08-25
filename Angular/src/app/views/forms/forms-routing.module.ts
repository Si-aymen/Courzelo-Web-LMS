import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BasicFormComponent } from './basic-form/basic-form.component';
import { TagInputsComponent } from './tag-inputs/tag-inputs.component';
import { AppImgCropperComponent } from './img-cropper/img-cropper.component';
import { WizardComponent } from './wizard/wizard.component';
import { InputMaskComponent } from './input-mask/input-mask.component';
import { InputGroupsComponent } from './input-groups/input-groups.component';
import { FormLayoutsComponent } from './form-layouts/form-layouts.component';
import {ProfessorAvailabilityComponentComponent} from './professor-availability-component/professor-availability-component.component';
import {TimetableComponent} from './timetable/timetable.component';
import { CreateQuizComponent } from './Quiz/create-quiz/create-quiz.component';
import {TakeQuizComponent} from './Quiz/take-quiz/take-quiz.component';
import {QuizResultComponent} from './Quiz/quiz-result/quiz-result.component';
import {AuthGuard} from '../../shared/services/auth-guard.service';
import {EditQuizComponent} from './Quiz/edit-quiz/edit-quiz.component';

const routes: Routes = [
  {
    path: 'basic',
    component: BasicFormComponent
  },
  {
    path: 'layouts',
    component: FormLayoutsComponent
  },
  {
    path: 'input-group',
    component: InputGroupsComponent
  },
  {
    path: 'input-mask',
    component: InputMaskComponent
  },
  {
    path: 'tag-input',
    component: TagInputsComponent
  },
  {
    path: 'wizard',
    component: WizardComponent
  },
  {
    path: 'img-cropper',
    component: AppImgCropperComponent
  },
  {
    path: 'professor/:professorId/availability',
    component: ProfessorAvailabilityComponentComponent
  },
  {
    path: 'Timetable',
    component: TimetableComponent
  }
  {
    path: 'create-quiz',
    component: CreateQuizComponent,
    canActivate: [AuthGuard],
    data: { roles: ['TEACHER'] }
  },
  {
    path: 'take-quiz',
    component: TakeQuizComponent,
    canActivate: [AuthGuard],
    data: { roles: ['STUDENT'] }
  },
  {
    path: 'EditQuiz',
    component: EditQuizComponent
  },

  {
    path: 'QuizResult',
    component: QuizResultComponent,
    canActivate: [AuthGuard]
  }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FormsRoutingModule { }
