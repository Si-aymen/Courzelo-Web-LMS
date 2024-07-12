import { Component, OnInit } from '@angular/core';
import { FAQ } from 'src/app/shared/models/faq';
import { FAQService } from '../Services/faq.service';

@Component({
  selector: 'app-faq',
  templateUrl: './faq.component.html',
  styleUrls: ['./faq.component.scss']
})
export class FaqComponent implements OnInit {
  faqs: FAQ[];

  constructor(private faqService: FAQService) { }

  ngOnInit(): void {
    this.loadFAQs();
  }

  loadFAQs() {
    this.faqService.getAllFAQs().subscribe(data => {
      this.faqs = data;
    });
  }
  editFAQ(id:any){

  }
  deleteFAQ(id:any){

  }
}