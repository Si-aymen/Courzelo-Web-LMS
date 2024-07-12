import { TestBed } from '@angular/core/testing';

import { JobofferService } from './joboffer.service';

describe('JobofferService', () => {
  let service: JobofferService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JobofferService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
