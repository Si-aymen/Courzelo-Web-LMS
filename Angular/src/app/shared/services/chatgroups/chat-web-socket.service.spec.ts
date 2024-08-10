import { TestBed } from '@angular/core/testing';

import { ChatWebSocketService } from './chat-web-socket.service';

describe('ChatWebSocketService', () => {
  let service: ChatWebSocketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChatWebSocketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
