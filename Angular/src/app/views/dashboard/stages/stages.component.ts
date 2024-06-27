import { Component,OnInit } from '@angular/core';
import { echartStyles } from 'src/app/shared/echart-styles';


@Component({
  selector: 'app-stages',
  templateUrl: './stages.component.html',
  styleUrls: ['./stages.component.scss']
})
export class StagesComponent implements OnInit {
  chartLineOption3: any;
  chartLineOption2: any;
  
    constructor(
    ) { }
  
    ngOnInit() {
      this.chartLineOption3 = {
        ...echartStyles.lineNoAxis, ...{
          series: [{
            data: [40, 30, 20, 50, 30, 10, 40],
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
      this.chartLineOption3.xAxis.data = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

      this.chartLineOption2 = {
        ...echartStyles.lineNoAxis, ...{
          series: [{
            data: [5, 11, 16, 8, 16, 10, 13],
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
      this.chartLineOption2.xAxis.data = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  
    }
  
}
  

