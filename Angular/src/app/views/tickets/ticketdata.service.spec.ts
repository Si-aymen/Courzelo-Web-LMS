import { TestBed } from '@angular/core/testing';

import { TicketdataService } from './ticketdata.service';

describe('TicketdataService', () => {
  let service: TicketdataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicketdataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
