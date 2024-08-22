import { TestBed } from '@angular/core/testing';

import { MailexchangeService } from './mailexchange.service';

describe('MailexchangeService', () => {
  let service: MailexchangeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MailexchangeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
