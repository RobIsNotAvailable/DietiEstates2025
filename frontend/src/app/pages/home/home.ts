import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class HomeComponent implements OnInit
{
  isUserLoggedIn: boolean = false;

  constructor(private authService: AuthService, private router: Router) 
  {}

  ngOnInit(): void 
  {
    this.isUserLoggedIn = this.authService.isLoggedIn();
  }

  onLogout(): void 
  {
    this.authService.logout(); 
    this.isUserLoggedIn = false; 
    this.router.navigate(['/home']);
  }

  goToLogin(): void 
  {
    this.router.navigate(['/login']);
  }

  goToRegister(): void
  {
    this.router.navigate(['/register']);
  }
}
