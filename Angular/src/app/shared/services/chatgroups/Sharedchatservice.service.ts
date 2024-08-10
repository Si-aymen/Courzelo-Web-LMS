import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SharedChatserviceService {
  private groupsSource = new BehaviorSubject<any[]>([]);
  currentGroups = this.groupsSource.asObservable();

  constructor() { }

  updateGroups(groups: any[]) {
    this.groupsSource.next(groups);
  }
  
}
