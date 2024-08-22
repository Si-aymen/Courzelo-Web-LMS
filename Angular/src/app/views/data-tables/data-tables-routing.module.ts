import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FullscreenTableComponent } from './fullscreen-table/fullscreen-table.component';
import { PagingTableComponent } from './paging-table/paging-table.component';
import { FilterTableComponent } from './filter-table/filter-table.component';
import { ListPaginationComponent } from './list-pagination/list-pagination.component';
import {QuizTableComponent} from './Quiz/quiz-table/quiz-table.component';
import {QuizListComponent} from './Quiz/quiz-list/quiz-list.component';
import {EditQuizComponent} from '../forms/Quiz/edit-quiz/edit-quiz.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListPaginationComponent
  },
  {
    path: 'full',
    component: FullscreenTableComponent
  },
  {
    path: 'paging',
    component: PagingTableComponent
  },
  {
    path: 'filter',
    component: FilterTableComponent
  },
  {
    path: 'QuizTable',
    component: QuizTableComponent
  },
  {
    path: 'QuizList',
    component: QuizListComponent},

  { path: 'EditQuiz',
    component: EditQuizComponent }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DataTablesRoutingModule { }
