import { TestBed } from '@angular/core/testing';

import { ParserServiceService } from './parser-service.service';

describe('ParserServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ParserServiceService = TestBed.get(ParserServiceService);
    expect(service).toBeTruthy();
  });
});
