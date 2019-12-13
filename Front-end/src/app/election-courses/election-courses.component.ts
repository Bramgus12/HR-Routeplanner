import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-election-courses',
  templateUrl: './election-courses.component.html',
  styleUrls: ['./election-courses.component.scss']
})
export class ElectionCoursesComponent implements OnInit {
  displayedColumns: string[] = ['code', 'name'];
  electionCourses = [];


  constructor() { }

  ngOnInit() {
    
  }

}
