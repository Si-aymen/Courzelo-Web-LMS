import { TestBed } from '@angular/core/testing';

import { RatingserviceService } from './ratingservice.service';

describe('RatingserviceService', () => {
  let service: RatingserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RatingserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
