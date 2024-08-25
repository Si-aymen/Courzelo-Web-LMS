import { TestBed } from '@angular/core/testing';
import { TicketDataService } from './ticketdata.service';

describe('TicketdataService', () => {
  let service: TicketDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicketDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
