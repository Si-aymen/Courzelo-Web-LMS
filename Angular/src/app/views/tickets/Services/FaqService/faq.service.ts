// faq.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FAQ } from 'src/app/shared/models/faq';

@Injectable({
  providedIn: 'root'
})
export class FAQService {
  private baseUrl = '/tk/api/v1/faq/';

  constructor(private http: HttpClient) { }

  getAllFAQs(): Observable<FAQ[]> {
    return this.http.get<FAQ[]>(`${this.baseUrl}/all`);
  }

  getFAQById(id: string): Observable<FAQ> {
    return this.http.get<FAQ>(`${this.baseUrl}/get/${id}`);
  }

  addFAQ(faq: FAQ): Observable<FAQ> {
    return this.http.post<FAQ>(`${this.baseUrl}/add`, faq);
  }

  updateFAQ(id:any,faq: FAQ): Observable<FAQ> {
    return this.http.put<FAQ>(`${this.baseUrl}/update/${id}`, faq);
  }

  deleteFAQ(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }
}
