import { TestBed } from '@angular/core/testing';

import { TickettypeService } from './tickettype.service';

describe('TickettypeService', () => {
  let service: TickettypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TickettypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
