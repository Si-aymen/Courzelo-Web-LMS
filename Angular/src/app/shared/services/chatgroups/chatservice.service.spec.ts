import { TestBed } from '@angular/core/testing';

import { SharedChatserviceService } from './Sharedchatservice.service';

describe('SharedChatserviceService', () => {
  let service: SharedChatserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SharedChatserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
