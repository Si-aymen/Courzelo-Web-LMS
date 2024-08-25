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

  getMailsByRecipient(email: string): Observable<MailExchange[]> {
    return this.http.get<MailExchange[]>(`${this.baseURL}/recipient`, {
      params: { email }
    });
  }

  getMailsBySender(email: string): Observable<MailExchange[]> {
    return this.http.get<MailExchange[]>(`${this.baseURL}/sender`, {
      params: { email }
    });
  }
  deleteMail(mailId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseURL}/delete/${mailId}`);
  }
}