import { Component, OnInit } from '@angular/core';
import { Observable, of, Subject, throwError } from 'rxjs';
import { echartStyles } from 'src/app/shared/echart-styles';
import { Transports } from 'src/app/shared/models/transports/Transports';
import { TransportsService } from 'src/app/shared/services/transports/transports.service';
import { DataLayerService } from 'src/app/shared/services/data-layer.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { catchError, finalize, takeUntil } from 'rxjs/operators';




@Component({
  selector: 'app-transports',
  templateUrl: './transports.component.html',
  styleUrls: ['./transports.component.scss']
})
export class TransportsComponent implements OnInit {
  chartLineOption3: any;
  transports$: Observable<Transports[]>;  
  Buttons: string;
  count$: number;
  data : number[]
  items = ['Javascript', 'Typescript'];
  transportForm: FormGroup;
  private destroy$ = new Subject<void>();
  



  

  constructor(
    private transportsService: TransportsService,
    private dl: DataLayerService,
    private fb: FormBuilder
    

  ) 
    { }
    

  ngOnInit(): void {
    this.initForm();
    this.loadData();
  }


  private loadData(): void {
    this.initializeChart();
    this.loadTransports();
    this.loadCount();
    this.fetchTransports().pipe(
      takeUntil(this.destroy$),
      finalize(() => {
      })
    ).subscribe({
      next: (transports) => this.updateChart(transports),
      error: (error) => console.error('Error fetching transports:', error)
    });


  }

  private initForm(): void {
    this.transportForm = this.fb.group({
      departure: ['', Validators.required],
      destination: ['', Validators.required],
      time: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]]
    });
  }


  initializeChart(): void {
    this.chartLineOption3 = {
      ...echartStyles.lineNoAxis,
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      series: [{
        data: [50,10,20,10,50,70],
        lineStyle: {
          color: 'rgba(102, 51, 153, .86)',
          width: 3,
          shadowColor: 'rgba(0, 0, 0, .2)',
          shadowOffsetX: -1,
          shadowOffsetY: 8,
          shadowBlur: 10
        },
        label: { show: true, color: '#212121' },
        type: 'line',
        smooth: true,
        itemStyle: {
          borderColor: 'rgba(69, 86, 172, 0.86)'
        }
      }]
    };
  }
  
  updateChart(transports: Transports[]): void {
    console.log('Updating chart with transports:', transports);
    const prices = transports.map(transport => transport.price);
    console.log('Extracted prices:', prices);
    this.chartLineOption3.series[0].data = prices;
    console.log('Updated chart data:', this.chartLineOption3.series[0].data);
  }
  
    

  loadTransports(): void {
    this.transportsService.getTransports().subscribe({
      next: (transports: Transports[]) => {
       this.updateChart(transports);
        this.transports$ = of(transports); 
      },
      error: (error) => {
        console.error('There was an error loading transports:', error);
      }
    });
  }

  loadCount(): void {
    this.transportsService.getCount().subscribe({
      next: (data: number) => {
        this.count$ = data;
      },
      error: (error) => {
        console.error('There was an error!', error);
      }
    });
  }

  onSubmit(): void {
    if (this.transportForm.valid) {
      const newTransport: Transports = this.transportForm.value;
      this.transportsService.register(newTransport);
      this.loadTransports();
    } else {
      alert('Please fill in all required fields.');
    }
  }

  public onSelect(item) {
    console.log('tag selected: value is ' + item);
  }

  fetchTransports(): Observable<Transports[]> {
    return this.transportsService.getTransports().pipe(
      catchError(error => {
        console.error('Error fetching transports:', error);
        // You might want to show an error message to the user here
        return throwError(() => new Error('Failed to fetch transports. Please try again later.'));
      })
    );
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }


}
