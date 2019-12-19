import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material';

import { NavigationState } from '../navigation-state';

@Component({
  selector: 'app-next-state-button',
  templateUrl: './next-state-button.component.html',
  styleUrls: ['./next-state-button.component.scss']
})
export class NextStateButtonComponent {

  @Input() state: NavigationState;

  constructor(private router: Router, private dialog: MatDialog) {}

  navigateToNextComponent(){
    if(this.state instanceof NavigationState){
      const dialogRef = this.dialog.open(NextStateDialog);
      dialogRef.afterClosed().subscribe(data => {
        if(data) this.router.navigateByUrl(this.state.nextComponentUrl, { state: this.state });
      });
    } else console.error(Error("State is missing or invalid!"));

  }

}

@Component({
  selector: 'next-state-dialog',
  templateUrl: 'next-state-dialog.html'
})
export class NextStateDialog {}
