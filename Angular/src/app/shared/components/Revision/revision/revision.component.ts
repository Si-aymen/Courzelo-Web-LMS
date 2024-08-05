import { Component, OnInit } from '@angular/core';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import { RevisionService } from 'src/app/shared/services/Revision/revision.service';


@Component({
  selector: 'app-revision',
  templateUrl: './revision.component.html',
  styleUrls: ['./revision.component.scss'],
  animations: [SharedAnimations]
})
export class RevisionComponent implements OnInit {
  viewMode: 'list' | 'grid' = 'list';
  allSelected: boolean;
  page = 1;
  pageSize = 8; //number of session in one page 
  products: any[] = [];

  constructor(
    private dl: RevisionService
  ) { }

  ngOnInit() {
    this.dl.getProducts()
    .subscribe((products: any[]) => {
      this.products = products;
    });
  }

  selectAll(e) {
    this.products = this.products.map(p => {
      p.isSelected = this.allSelected;
      return p;
    });

    if (this.allSelected) {

    }
    console.log(this.allSelected);
  }

}
