import { TestBed } from '@angular/core/testing';

import { TrelloserviceService } from './trelloservice.service';

describe('TrelloserviceService', () => {
  let service: TrelloserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrelloserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
