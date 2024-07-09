import { Component, OnInit } from '@angular/core';
import { echartStyles } from 'src/app/shared/echart-styles';
import { ActivityService } from 'src/app/shared/services/activity.service';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-dashboard-v5',
  templateUrl: './dashboard-v5.component.html',
  styleUrls: ['./dashboard-v5.component.scss']
})
export class DashboardV5Component implements OnInit {
  chartLineSmall1: any;
  lineChart1: any;
  activities: any[] = [];
  activityForm: FormGroup;

  constructor(
    private activityService: ActivityService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.chartLineSmall1 = {
      ...echartStyles.defaultOptions,
      grid: echartStyles.gridAlignLeft,
      series: [{
        data: [30, 40, 20, 50, 40, 80, 90, 40],
        ...echartStyles.smoothLine,
        lineStyle: {
          color: '#4CAF50',
          ...echartStyles.lineShadow
        },
        itemStyle: {
          color: '#4CAF50'
        }
      }]
    };

    this.lineChart1 = {
      ...echartStyles.lineFullWidth,
      series: [{
        data: [80, 40, 90, 20, 80, 30, 90, 30, 80, 10, 70, 30, 90],
        ...echartStyles.smoothLine,
        markArea: {
          label: {
            show: true
          }
        },
        areaStyle: {
          color: 'rgba(102, 51, 153, .15)',
          origin: 'start'
        },
        lineStyle: {
          color: 'rgba(102, 51, 153, 0.68)',
        },
        itemStyle: {
          color: '#663399'
        }
      }, {
        data: [20, 80, 40, 90, 20, 80, 30, 90, 30, 80, 10, 70, 30],
        ...echartStyles.smoothLine,
        markArea: {
          label: {
            show: true
          }
        },
        areaStyle: {
          color: 'rgba(255, 152, 0, 0.15)',
          origin: 'start'
        },
        lineStyle: {
          color: 'rgba(255, 152, 0, .6)',
        },
        itemStyle: {
          color: 'rgba(255, 152, 0, 1)'
        }
      }]
    };

    this.activityForm = this.fb.group({
      name: [''],
      description: [''],
      category: [''],
      date: [''],
      time: [''],
      location: [''],
      responsible: this.fb.group({
        name: [''],
        contact: ['']
      }),
      participants: this.fb.array([]),
      maxCapacity: [''],
      registrations: this.fb.group({
        formUrl: [''],
        deadline: ['']
      }),
      requiredEquipment: this.fb.array([]),
      budget: [''],
      objectives: this.fb.array([]),
      evaluation: this.fb.array([]),
      feedback: this.fb.array([]),
      photos: this.fb.array([]),
      videos: this.fb.array([]),
      sponsors: this.fb.array([]),
      resources: this.fb.array([]),
      status: [''],
      notes: ['']
    });

    this.loadActivities();
  }

  loadActivities(): void {
    this.activityService.getActivities().subscribe((data: any[]) => {
      this.activities = data;
    });
  }

  addActivity(): void {
    const newActivity = this.activityForm.value;
    this.activityService.addActivity(newActivity).subscribe(() => {
      this.loadActivities(); // Refresh the list
    });
  }

  update(row: any): void {
    this.activityService.updateActivity(row).subscribe(() => {
      this.loadActivities(); // Refresh the list
    });
  }

  delete(row: any): void {
    this.activityService.deleteActivity(row.id).subscribe(() => {
      this.loadActivities(); // Refresh the list
    });
  }

  getRowClass(row: any): string {
    switch (row.status) {
      case 'Planifiée':
        return 'status-planifiee';
      case 'En cours':
        return 'status-en-cours';
      case 'Terminée':
        return 'status-terminee';
      case 'Annulée':
        return 'status-annulee';
      default:
        return '';
    }
  }
}
