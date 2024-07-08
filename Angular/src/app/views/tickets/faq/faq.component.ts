import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-faq',
  templateUrl: './faq.component.html',
  styleUrls: ['./faq.component.scss']
})
export class FaqComponent implements OnInit {

  faqList: any[] = [
    { question: 'What payment methods do you accept?', answer: 'We accept Visa, MasterCard, American Express, and PayPal.' },
    { question: 'How do I get a refund ?', answer: 'You can initiate a refund request through your account or contact our support team.' },
    // Add more FAQ items as needed
  ];

  constructor() { }

  ngOnInit(): void {
  }

}
