import { Component } from '@angular/core';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.html',
  styleUrl: './home.scss',
  imports: [CommonModule]
})
export class HomeComponent 
{
  isUserLoggedIn: boolean = false;

  constructor(private authService: AuthService, private router: Router) 
  {}

  ngOnInit(): void 
  {
    this.isUserLoggedIn = this.authService.isLoggedIn();
    console.log('Stato login in Home:', this.isUserLoggedIn);
  }

  onLogout(): void 
  {
    this.authService.logout(); 
    this.isUserLoggedIn = false; 
  }

  goToLogin(): void 
  {
    this.router.navigate(['/login']);
  }
}
