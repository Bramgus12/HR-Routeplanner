import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import { ElectionCourse, ElectionCourseExtended } from '../shared/dataclasses';

@Injectable({
  providedIn: 'root'
})
export class ElectionCourseService {

  constructor(private http: HttpClient) { }

  private API_URL = "/api/"

  getElectionCourses(){
    return this.http.get<ElectionCourse[]>(this.API_URL + 'election-course');
  }

  getElectionCourse(code: string){
    return this.http.get<ElectionCourseExtended>(this.API_URL + 'election-course/' + code);
  }
}
