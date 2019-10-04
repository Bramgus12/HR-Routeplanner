import { Component, OnInit, Output } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivationEnd } from '@angular/router';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  @Output() title = 'No title';

  constructor(private router: Router, private titleService: Title){}

  ngOnInit(){
    this.router.events.pipe(
      filter(event => event instanceof ActivationEnd),
      map((event: ActivationEnd) => event.snapshot.data)
    ).subscribe(data => {
      this.title = data['title'];
      this.titleService.setTitle(data['title']);
    });
  }
}
