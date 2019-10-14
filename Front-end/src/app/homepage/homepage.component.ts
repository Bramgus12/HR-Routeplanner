import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {

  navigationModel:{from: string, to: string} = { from: '', to: '' }

  constructor(private router: Router) { }

  ngOnInit() {
  }

  goToNavigation(){
    // Ways of sending data to other components (no data-binding) using states!
    // https://stackoverflow.com/a/54365098
    // Other alternatives: https://stackoverflow.com/a/44865817
    console.log(this.navigationModel.from, this.navigationModel.to)
    this.router.navigate(['maps-navigation'], { state: { from: this.navigationModel.from, to: this.navigationModel.to } })
  }

}
