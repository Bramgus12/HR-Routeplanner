import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, MatTableDataSource, MatPaginator, PageEvent } from '@angular/material';

import { ElectionCourseService } from './election-courses.service';
import { ElectionCourse } from '../shared/dataclasses';
@Component({
  selector: 'app-election-courses',
  templateUrl: './election-courses.component.html',
  styleUrls: ['./election-courses.component.scss']
})
export class ElectionCoursesComponent implements OnInit {
  displayedColumns: string[] = ['courseCode', 'period', 'groupNumber', 'name'];
  electionCourses = new MatTableDataSource([]);

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private service: ElectionCourseService) { }

  ngOnInit() {
    this.service.getElectionCourses().subscribe(data => {
      data = data.filter(course => course.courseCode != '')

      this.electionCourses = new MatTableDataSource(data);
      this.electionCourses.sort = this.sort;
      this.electionCourses.paginator = this.paginator;
    })
  }

  onPageUpdate(event: PageEvent){
    // Use setTimeout to make sure it gets executed after the page change 
    setTimeout(() => window.scroll({ top: 0, left: 0, behavior: 'smooth' }));
  }

}
