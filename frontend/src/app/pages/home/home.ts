import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';

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
  isDropdownOpen: boolean = false;
  isLoading: boolean = false;
  loadingMessage: string = '';
  userData: any = null;

  constructor(private authService: AuthService, private router: Router, private cd: ChangeDetectorRef) 
  {}

  ngOnInit(): void 
  {
    this.isLoading = true;
    this.isUserLoggedIn = this.authService.isLoggedIn();
    this.loadingMessage = 'logging in...';

    if (this.isUserLoggedIn) 
    {
      this.authService.getAccountDetails().subscribe(
      {
        next: (res) => 
        {
          this.userData = res;
          localStorage.setItem('user', JSON.stringify(res));

          setTimeout(() => 
          {
            this.isLoading = false;
            this.cd.detectChanges(); 
          }, 800); 
        },
        error: (err) => 
        {
          this.isLoading = false; 
          this.onLogout();
        }
      });
    } 
    else 
    {
      this.isLoading = false;
    }
  }

  onLogout() 
  {
    this.loadingMessage = 'Logging out...';
    this.isLoading = true;
    this.isDropdownOpen = false;
    this.cd.detectChanges();
    this.authService.logout();

    setTimeout(() => 
    {
      localStorage.clear();
      this.isUserLoggedIn = false;
      this.userData = null;
      this.isLoading = false; 
      
      this.router.navigate(['/login']).then(() => 
      {
        window.location.reload(); 
      });
    }, 1500);
  }

  goToLogin(): void 
  {
    this.router.navigate(['/login']);
  }

  goToRegister(): void
  {
    this.router.navigate(['/register']);
  }

  toggleDropdown(): void
  {
    this.isDropdownOpen = !this.isDropdownOpen;
  }
}
