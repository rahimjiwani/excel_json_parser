import { environment } from './../environments/environment.prod';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ParserServiceService {

  constructor(private http: HttpClient) { }

  postParse(data) {
    return this.http.post<any>(environment.BASE_URL + environment.PARSER_URL, data);
  }

  getTest() {
    return this.http.get(environment.BASE_URL + "/api/hello");
  }
}
