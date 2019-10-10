import { Component, OnInit, OnDestroy } from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import { MatDialog, MatDialogRef } from '@angular/material';

import { TestService } from './test.service';
import { Address } from '../shared/dataclasses';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0', display: 'none'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class TestComponent implements OnInit {

  addresses: Address[] = [];
  columnsToDisplay = ['street', 'number', 'city', 'postal'];
  expandedElement: Address | null;

  constructor(private service: TestService, private dialog: MatDialog) { }

  ngOnInit() {
    // Temp
    this.addresses = [
      {
        id: 1,
        street: "Test St.",
        number: 69,
        city: "Rotterdam",
        postal: "0123 AB"
      },{
        id: 2,
        street: "Dev St.",
        number: 420,
        city: "Utrecht",
        postal: "4567 CD"
      },
    ]

    this.get()
  }

  create(){
    const dialogRef = this.dialog.open(TestCreateDialog);
    dialogRef.afterClosed().subscribe((data: Address | undefined) => {
      if(data){
        console.log(data);
        this.service.createAddress(data).subscribe(() => this.get());
      }
    });
  }

  get(){
    this.service.getAddresses().subscribe(res => {
      this.addresses = res;
    })
  }

  update(index: number){
    const address = this.addresses[index]
    this.service.updateAddress(address.id, address).subscribe(() => {
      this.get(); // Live data from the database (incase anyone else edited something)
    });
  }

  delete(id: number){
    const dialogRef = this.dialog.open(TestDeleteDialog);
    dialogRef.afterClosed().subscribe((data: number | undefined) => {
      if(data){
        console.log("id", data);
        this.service.deleteAddress(id).subscribe(() => this.get());
      }
    });
  }

}

@Component({
  selector: 'test-create-dialog',
  templateUrl: 'test-create-dialog.html'
})
export class TestCreateDialog {

  newAddress: Address = {
    street: "",
    number: 0,
    city: "",
    postal: ""
  }

  constructor() {}

}

@Component({
  selector: 'test-delete-dialog',
  templateUrl: 'test-delete-dialog.html'
})
export class TestDeleteDialog {

  constructor() {}
  
}
