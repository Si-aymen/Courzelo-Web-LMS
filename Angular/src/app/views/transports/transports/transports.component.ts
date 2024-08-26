import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Observable, of, Subject, throwError } from 'rxjs';
import { echartStyles } from 'src/app/shared/echart-styles';
import { Transports } from 'src/app/shared/models/transports/Transports';
import { TransportsService } from 'src/app/shared/services/transports/transports.service';
import { DataLayerService } from 'src/app/shared/services/data-layer.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { catchError, finalize, takeUntil } from 'rxjs/operators';

import Swal from 'sweetalert2';


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
  newData : number[]
  items = ['Javascript', 'Typescript'];
  transportForm: FormGroup;
  private destroy$ = new Subject<void>();
  
  

  constructor(
    private transportsService: TransportsService,
    private dl: DataLayerService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
    

  ) 
    { }
    

  ngOnInit(): void {

    this.initForm();
    this.loadData();

  }



  private loadData(): void {
    this.updateChart2();
    this.initializeChart();
    this.loadTransports();
    this.loadCount();

  }

  private initForm(): void {
    this.transportForm = this.fb.group({
      departure: ['', Validators.required],
      destination: ['', Validators.required],
      time: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]]
    });
  }



  updateChart2(): void {

    this.transportsService.getTransports().subscribe({
      next: (transports: Transports[]) => {
        this.transports$ = of(transports); 
        const prices = transports.map(transport => transport.price);
        this.chartLineOption3.series[0].data = prices;
        this.newData=prices
        console.log(' chart data on init in chart option 2   :', this.chartLineOption3.series[0].data);
        this.initializeChart();
        this.cdr.detectChanges();
        

      },
      error: (error) => {
        console.error('There was an error loading transports:', error);
      }
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
        data :this.newData,
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
    
    console.log(' chart data on init in chart option   :', this.chartLineOption3.series[0].data);

  }
  
  
  loadTransports(): void {
    this.transportsService.getTransports().subscribe({
      next: (transports: Transports[]) => {
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
        return throwError(() => new Error('Failed to fetch transports. Please try again later.'));
      })
    );
  }


  delete(id:any){
    Swal.fire({
      title: 'are you sur ?',
      text: "Delete this Transportation ?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes Delete!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.transportsService.deleteTransports(id).subscribe((res:any) =>{
          if (res.message){
            Swal.fire({
              icon: 'success',
              title: 'Success...',
              text: 'Transportation Deleted !',
            })
            this.ngOnInit();
          }
          else{
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: "Error !",
            })
          }
        },
        err =>{
          Swal.fire({
            icon: 'warning',
            title: 'Error in deleting Transportation',
            text: err.error.message,
          })
        }
        )
      }
      this.ngOnInit();
    }
    )
  }


  refresh(): void {
    window.location.reload();
}

}
