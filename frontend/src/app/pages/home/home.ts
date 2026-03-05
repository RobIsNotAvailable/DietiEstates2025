import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { SearchBarComponent } from '../../components/search-bar/search-bar.component';

interface QuickOption
{
  title: string;
  subtitle: string;
  icon: string;
  action: string;
}


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, SearchBarComponent],
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
  sectionTitle: string = '';
  
  quickOptions: QuickOption[] = [];

  constructor(private authService: AuthService, private router: Router, private cd: ChangeDetectorRef) 
  {}

  ngOnInit(): void 
  {
    this.isLoading = true;
    this.isUserLoggedIn = this.authService.isLoggedIn();
    this.loadingMessage = 'Finalizing details...';

    if (this.isUserLoggedIn) 
    {
      this.sectionTitle = 'Quick explore';

      this.authService.getAccountDetails().subscribe(
      {
        next: (res) => 
        {
          const currentRole = this.authService.getUserRole();
          console.log('Token decodificato, ruolo trovato:', currentRole);
          this.userData = { ...res, role: currentRole };;
          localStorage.setItem('user', JSON.stringify(this.userData));
          this.setupQuickOptions();
          
          setTimeout(() => 
          {
            this.isLoading = false;
            this.cd.detectChanges(); 
          }, 500); 
        },
        error: () => 
        {
          this.isLoading = false; 
          this.onLogout();
        }
      });
    } 
    else 
    {
      this.isLoading = false;
      this.sectionTitle = 'Quick explore - (Login to unlock all features!)';
      this.setupQuickOptions();
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
      
      this.router.navigate(['/']).then(() => 
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

  setupQuickOptions(): void
  {
    const role = this.userData?.role;
    console.log('DEBUG setupQuickOptions - Ruolo attuale:', role);

    if (!this.isUserLoggedIn || role == 'CLIENT')
    {
      this.quickOptions = [
        { title: 'New appointment', subtitle: 'DA MODIFICARE', icon: 'bx-search-alt', action: 'SEARCH' },
        { title: 'Your appointments', subtitle: 'DA MODIFICARE', icon: 'bx-building-house', action: 'RENT' },
        { title: 'Make an offer', subtitle: 'DA MODIFICARE?', icon: 'bx-stats', action: 'VALUATE' },
        { title: 'Your offers', subtitle: 'DA MODIFICARE', icon: 'bx-support', action: 'SUPPORT' }
      ];
    }
    else
    {
      this.quickOptions = [
        { title: 'New Listing', subtitle: 'Aggiungi una nuova proprietà', icon: 'bx-plus-circle', action: 'CREATE_LISTING' },
        { title: 'Dashboard', subtitle: 'Le tue statistiche e overview', icon: 'bx-line-chart', action: 'VIEW_STATS' },
        { title: 'Your Listings', subtitle: 'Gestisci le tue proprietà attive', icon: 'bx-list-ul', action: 'MANAGE_LISTINGS' },
        { title: 'Your Appointments', subtitle: 'Il tuo calendario e visite', icon: 'bx-calendar', action: 'VIEW_APPOINTMENTS' },
        
      ];

      if (this.userData?.role === 'SUPPORT' || this.userData?.role === 'ADMIN') 
      {
        this.quickOptions.push({ 
          title: 'New Agent', 
          subtitle: 'Registra un nuovo collaboratore', 
          icon: 'bx-user-plus', 
          action: 'CREATE_AGENT' 
        });
      }

      if (this.userData?.role === 'ADMIN') 
      {
        this.quickOptions.push({ 
          title: 'New Support', 
          subtitle: 'Aggiungi personale di assistenza', 
          icon: 'bx-support', 
          action: 'CREATE_SUPPORT' 
        });
      }
    }
  }

  handleAction(action: string): void
  {
    console.log('Action activated:', action);

    switch (action) 
    {
      case 'CREATE_AGENT':
        this.router.navigate(['/create-agent']);
        break;
      
      case 'CREATE_SUPPORT':
        this.router.navigate(['/create-support']);
        break;

      case 'VIEW_STATS':
        this.router.navigate(['/dashboard']);
        break;

      case 'CREATE_LISTING':
        this.router.navigate(['/new-listing']);
        break;

      default:
        console.warn('Action not implemented yet:', action);
        break;
    }
  }
}
