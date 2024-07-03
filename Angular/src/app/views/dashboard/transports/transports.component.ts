import { Component, OnInit } from '@angular/core';
import { ProductService } from 'src/app/shared/services/product.service';
import { TransportsService } from 'src/app/shared/services/transports.service';


@Component({
  selector: 'app-transports',
  templateUrl: './transports.component.html',
  styleUrls: ['./transports.component.scss']
})
export class TransportsComponent implements OnInit{

  products$: any;
  clientTransports$: any ; 
  combinedList: any[] = [];

  transports: any[] = [
    { id: 1, Depature: 'Product 1', TimeOfDep: 10 },
    { id: 2, Depature: 'Product 2', TimeOfDep: 20 }
  ];


  Lines$: any = [
    { id: 1,Sector: 'Sud', Depature: 'Product 1', TimeOfDep: 10 },
    { id: 2,Sector: 'Sud', Depature: 'Product 1', TimeOfDep: 30 },
    { id: 3,Sector: 'Nord', Depature: 'Product 2', TimeOfDep: 20 }
  ];

  

  constructor(
		private productService: ProductService,
		private transportsService: TransportsService
	) { }

  ngOnInit() {

    this.products$ = this.productService.getProducts();
    this.clientTransports$ = this.transportsService.getUsers();
    //this.combineLists();
    

  }

  /*
  combineLists() {
    this.transports.forEach(transports => {
      this.combinedList.push({
        idT: transports.id,
        typeT: 'product',
        Depature: transports.Depature,
        TimeOfDep: transports.TimeOfDep
      });
    });

    this.clientTransports$.forEach(clientTransports => {
      this.combinedList.push({
        idC: clientTransports.id,
        typeC: 'user',
        avatar: clientTransports.avatar,
        name: clientTransports.name

        });
    });
  }

  */s
}








