import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { echartStyles } from 'src/app/shared/echart-styles';
import { Transports } from 'src/app/shared/models/transports/Transports';
import { TransportsService } from 'src/app/shared/services/transports/transports.service';
import { DataLayerService } from 'src/app/shared/services/data-layer.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TagInputsComponent } from '../../forms/tag-inputs/tag-inputs.component';




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
  autocompletes$;
  tagsCtrl2 = new FormControl([{display: 'Bangladesh', value: 'BD'}]);
  transportForm: FormGroup;



  

  constructor(
    private transportsService: TransportsService,
    private dl: DataLayerService

  ) 
    { }
    

  ngOnInit(): void {

    this.transportForm = new FormGroup({
      departure: new FormControl('', Validators.required),
      destination: new FormControl('', Validators.required),
      time: new FormControl('', Validators.required),
      price: new FormControl('', Validators.required)


    });
    this.loadTransports();
    this.loadCount();
    this.initializeChart();
    this.autocompletes$ = this.dl.getCountries();

  }

  initializeChart(): void {
    this.chartLineOption3 = {
			...echartStyles.lineNoAxis, ...{
				series: [{
					data: [0,0],
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
			}
		};

		this.chartLineOption3.xAxis.data = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

  }

  updateChart(transports: Transports[]): void {
    console.log('Updating chart with transports:', transports);
    const data = transports.map(transport => transport.price); 
    this.chartLineOption3.series[0].data = data;		

    this.data = data;
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

  



}
