import {Component, OnInit} from '@angular/core';
import {DataLayerService} from '../../../../shared/services/data-layer.service';
import {QuizService} from '../../../../shared/services/quiz.service';
import {Quiz} from '../../../../shared/models/Quiz';

@Component({
  selector: 'app-quiz-table',
  templateUrl: './quiz-table.component.html',
  styleUrls: ['./quiz-table.component.scss']
})
export class QuizTableComponent implements OnInit {
  viewMode: 'list' | 'grid' = 'list';
  allSelected: boolean;
  page = 1;
  pageSize = 8;
  quizzes: Quiz[] = [];
  constructor(private QS: QuizService) { }
    ngOnInit(): void {
      this.QS.getQuizzesByTeacherId('ddsd')
          .subscribe((quizzes: any[]) => {
            this.quizzes = quizzes;
          } );
    }
  selectAll(e) {
    this.quizzes = this.quizzes.map(q => {
      q.isSelected = this.allSelected;
      return q;
    });

    if (this.allSelected) {

    }
    console.log(this.allSelected);
  }


}
