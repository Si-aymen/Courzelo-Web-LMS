import { Component, OnInit } from '@angular/core';
import { FAQService } from '../../tickets/Services/FaqService/faq.service';
import { FAQ } from 'src/app/shared/models/faq';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-list-faq',
  templateUrl: './list-faq.component.html',
  styleUrls: ['./list-faq.component.scss']
})
export class ListFaqComponent implements OnInit {
  faqs: FAQ[];

  constructor(private faqService: FAQService,
    public dialog: MatDialog
    ) { }

  ngOnInit(): void {
    this.loadFAQs();
  }

  loadFAQs() {
    this.faqService.getAllFAQs().subscribe(data => {
      this.faqs = data;
    });
  }
  }