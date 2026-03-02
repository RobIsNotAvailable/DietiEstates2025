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
  userData: any = null;

  constructor(private authService: AuthService, private router: Router, private cd: ChangeDetectorRef) 
  {}

  ngOnInit(): void 
  {
      this.isUserLoggedIn = this.authService.isLoggedIn();
      console.log("Stato Login nella Home:", this.isUserLoggedIn);

      if (this.isUserLoggedIn) 
      {
          this.authService.getAccountDetails().subscribe(
          {
              next: (res) => 
              {
                  console.log("Dati ricevuti dal /me:", res);
                  this.userData = res;
                  localStorage.setItem('user', JSON.stringify(res));
                  this.cd.markForCheck();
              },
              error: (err) => 
              {
                  console.error("Errore recupero dettagli:", err);
                  this.onLogout();
              }
          });
      }
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

  toggleDropdown(): void
  {
    this.isDropdownOpen = !this.isDropdownOpen;
  }
}
