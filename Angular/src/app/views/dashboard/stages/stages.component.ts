import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EChartOption } from 'echarts';
import { Observable, of } from 'rxjs';
import { Stages } from 'src/app/shared/models/stages/stages';
import { DataLayerService } from 'src/app/shared/services/data-layer.service';
import { StagesService } from 'src/app/shared/services/stages/stages.service';


@Component({
  selector: 'app-stages',
  templateUrl: './stages.component.html',
  styleUrls: ['./stages.component.scss']
})
export class StagesComponent {

  salesChartBar: EChartOption;
  salesChartPie: EChartOption;
  stages$ :   Observable<Stages[]>;   ;
  stageForm: FormGroup;


  constructor(
    private dl: DataLayerService , 
    private stageService : StagesService

  ) { }

  ngOnInit() {

    this.stageForm = new FormGroup({
      name: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      location: new FormControl('', Validators.required),
      type: new FormControl('', Validators.required),
      status: new FormControl('', Validators.required),
      entName: new FormControl('', Validators.required),
      duration: new FormControl('', Validators.required)


    });


    this.loadStages();
    this.Chart1();


  }

  loadStages(): void {
    this.stageService.getStages().subscribe({
      next: (stages: Stages[]) => {
        this.stages$ = of(stages); 

      },
      error: (error) => {
        console.error('There was an error loading internships:', error);
      }
    });
  }

  generateData(stages: Stages[]): void {
    const nameCounts: { [name: string]: number } = {};
    stages.forEach(stage => {
      const name = stage.location;
      nameCounts[name] = (nameCounts[name] || 0) + 1;
    });

    console.log(nameCounts); 
  }

  onSubmit(): void {
    if (this.stageForm.valid) {
      const newStage: Stages = this.stageForm.value;
      this.stageService.register(newStage);
      this.loadStages();
    } else {
      alert('Please fill in all required fields.');
    }
  }


  Chart1():void{

    this.salesChartPie = {
      color: ['#62549c', '#7566b5', '#7d6cbb', '#8877bd', '#9181bd', '#6957af'],
      tooltip: {
          show: true,
          backgroundColor: 'rgba(0, 0, 0, .8)'
      },

      xAxis: [{
              axisLine: {
                  show: false
              },
              splitLine: {
                  show: false
              }
          }

      ],
      yAxis: [{
              axisLine: {
                  show: false
              },
              splitLine: {
                  show: false
              }
          }
      ],
      series: [{
              name: 'Sales by Country',
              type: 'pie',
              radius: '75%',
              center: ['50%', '50%'],
              data: [
                  { value: 535, name: 'USA' },
                  { value: 310, name: 'Brazil' },
                  { value: 234, name: 'France' },
                  { value: 155, name: 'Germany' },
                  { value: 130, name: 'UK' },
                  { value: 348, name: 'India' }
              ],
              itemStyle: {
                  emphasis: {
                      shadowBlur: 10,
                      shadowOffsetX: 0,
                      shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
              }
          }
      ]
  };


  }




}
