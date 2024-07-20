import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MailExchange } from 'src/app/shared/models/Support/mailing';

@Injectable({
  providedIn: 'root'
})
export class MailexchangeService {

  private baseURL = '/tk/api/mails';

  constructor(private http: HttpClient) { }

  sendMail(mail: MailExchange): Observable<MailExchange> {
    return this.http.post<MailExchange>(this.baseURL, mail);
  }

  getAllMails(): Observable<MailExchange[]> {
    return this.http.get<MailExchange[]>(this.baseURL);
  }

  getMailsByRecipient(recipientId: string): Observable<MailExchange[]> {
    return this.http.get<MailExchange[]>(`${this.baseURL}/recipient/${recipientId}`);
  }

  getMailsBySender(senderId: string): Observable<MailExchange[]> {
    return this.http.get<MailExchange[]>(`${this.baseURL}/sender/${senderId}`);
  }
  deleteMail(mailId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseURL}/delete/${mailId}`);
  }
}