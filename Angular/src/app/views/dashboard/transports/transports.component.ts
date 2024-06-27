import { Component, OnInit } from '@angular/core';
import { ProductService } from 'src/app/shared/services/product.service';


@Component({
  selector: 'app-transports',
  templateUrl: './transports.component.html',
  styleUrls: ['./transports.component.scss']
})
export class TransportsComponent implements OnInit{

  products$: any;
  constructor(
		private productService: ProductService
	) { }

  ngOnInit() {

    this.products$ = this.productService.getProducts();

  }






}
