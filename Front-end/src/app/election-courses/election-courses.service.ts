import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ElectionCourse } from '../shared/dataclasses';

@Injectable({
  providedIn: 'root'
})
export class ElectionCourseService {

  constructor(private http: HttpClient) { }

  private API_URL = "/api/"
  private electionCourses: ElectionCourse[] = null;

  getElectionCourses(){
    return new Observable<ElectionCourse[]>(subscriber => {
      if(!this.electionCourses){
        this.http.get<ElectionCourse[]>(this.API_URL + 'election-course').subscribe(data => {
          this.electionCourses = data;
          subscriber.next(this.electionCourses);
          subscriber.complete();
        });
      } else {
        subscriber.next(this.electionCourses);
        subscriber.complete();
      }
    });
  }
}
