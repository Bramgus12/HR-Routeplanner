import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, MatTableDataSource, MatPaginator, PageEvent } from '@angular/material';

import { ElectiveCourseService } from './elective-courses.service';
import { ElectiveCourse } from '../shared/dataclasses';
@Component({
  selector: 'app-elective-courses',
  templateUrl: './elective-courses.component.html',
  styleUrls: ['./elective-courses.component.scss']
})
export class ElectiveCoursesComponent implements OnInit {
  displayedColumns: string[] = ['courseCode', 'period', 'groupNumber', 'name'];
  electiveCourses = new MatTableDataSource([]);

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private service: ElectiveCourseService) { }

  ngOnInit() {
    this.service.getElectiveCourses().subscribe(data => {
      data = data.filter(course => course.courseCode != '')

      this.electiveCourses = new MatTableDataSource(data);
      this.electiveCourses.sort = this.sort;
      this.electiveCourses.paginator = this.paginator;
    })
  }

  onPageUpdate(event: PageEvent){
    // Use setTimeout to make sure it gets executed after the page change 
    setTimeout(() => window.scroll({ top: 0, left: 0, behavior: 'smooth' }));
  }

}
