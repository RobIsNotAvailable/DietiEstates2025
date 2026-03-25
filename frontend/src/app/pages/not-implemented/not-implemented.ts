import { Component } from '@angular/core';
import { CommonModule, Location} from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component(
{
  selector: 'app-not-implemented',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './not-implemented.html',
  styleUrls: ['./not-implemented.scss']
})
export class NotImplementedComponent 
{
  constructor(private router: Router, private location: Location) 
  {}

  goBack() 
  {
    this.location.back();
  }
}