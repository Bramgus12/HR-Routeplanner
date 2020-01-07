import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ElectiveCourse } from '../shared/dataclasses';

@Injectable({
  providedIn: 'root'
})
export class ElectiveCourseService {

  constructor(private http: HttpClient) { }

  private API_URL = "/api/"
  private electiveCourses: ElectiveCourse[] = null;

  getElectiveCourses(){
    return new Observable<ElectiveCourse[]>(subscriber => {
      if(!this.electiveCourses){
        this.http.get<ElectiveCourse[]>(this.API_URL + 'elective-course').subscribe(data => {
          this.electiveCourses = data;
          subscriber.next(this.electiveCourses);
          subscriber.complete();
        }, err => subscriber.error(err));
      } else {
        subscriber.next(this.electiveCourses);
        subscriber.complete();
      }
    });
  }
}
