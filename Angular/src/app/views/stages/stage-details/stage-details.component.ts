import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { Stages } from 'src/app/shared/models/stages/stages';
import { StagesService } from 'src/app/shared/services/stages/stages.service';

@Component({
  selector: 'app-stage-details',
  templateUrl: './stage-details.component.html',
  styleUrls: ['./stage-details.component.scss']
})
export class StageDetailsComponent implements OnInit, OnDestroy {
  stageId: string;
  stageName: string;
  stage$: Observable<Stages>;
  private subscription: Subscription;

  constructor(
    private stageService: StagesService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.subscription = this.route.params.subscribe(params => {
      this.stageId = params['id'];
      this.stageName = params['name'];
      this.getMyStage();
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  private getMyStage(): void {
    this.stage$ = this.stageService.getStageByID(this.stageId);
  }

  print() {
    if (window) {
        window.print();
    }
}
}