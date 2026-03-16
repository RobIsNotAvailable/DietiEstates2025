import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth';
import { AccountService } from '../../services/account';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { SearchBarComponent } from '../../components/search-bar/search-bar.component';
import { FilterPanelComponent } from '../../components/filter-panel/filter-panel';

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
  imports: [CommonModule, SearchBarComponent, FilterPanelComponent],
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
  isFiltersOpen: boolean = false;
  quickOptions: QuickOption[] = [];

  constructor(private authService: AuthService, private router: Router, private cd: ChangeDetectorRef, private accountService: AccountService) 
  {}

  ngOnInit(): void 
  {
    this.isLoading = true;
    this.isUserLoggedIn = this.authService.isLoggedIn();
    this.loadingMessage = 'Finalizing details...';

    if (this.isUserLoggedIn) 
    {
      this.sectionTitle = 'Quick explore';

      this.accountService.getAccountDetails().subscribe(
      {
        next: (res) => 
        {
          const currentRole = this.authService.getUserRole();
          console.log('Token decoded, role found:', currentRole);
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

  toggleDropdown(): void
  {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  setupQuickOptions(): void
  {
    const role = this.userData?.role;

    if (!this.isUserLoggedIn || role == 'CLIENT')
    {
      this.quickOptions = [
        { title: 'Your offers', subtitle: 'Check on your previous offers', icon: 'bx bx-edit-alt', action: 'OFFERS' },
        { title: 'Your appointments', subtitle: 'Manage and track your scheduled appointments', icon: 'bx-calendar', action: 'APPOINTMENTS' },
        { title: 'Saved', subtitle: 'Check back on your favorite houses', icon: 'bx bx-bookmark-alt', action: 'SAVED' },
        { title: 'History', subtitle: 'Review your previous activities', icon: 'bx-history', action: 'HISTORY' }
      ];
    }
    else
    {
      this.quickOptions = [
        { title: 'New Listing', subtitle: 'Create a new listing', icon: 'bx-plus-circle', action: 'CREATE_LISTING' },
        { title: 'Dashboard', subtitle: 'Overview of your statistics', icon: 'bx-line-chart', action: 'VIEW_STATS' },
        { title: 'Your Listings', subtitle: 'Manage your active listings', icon: 'bx-list-ul', action: 'MANAGE_LISTINGS' },
        { title: 'Your Appointments', subtitle: 'Your appointment calendar', icon: 'bx-calendar', action: 'VIEW_APPOINTMENTS' },
        
      ];

      if (this.userData?.role === 'SUPPORT' || this.userData?.role === 'ADMIN') 
      {
        this.quickOptions.push({ 
          title: 'New Agent', 
          subtitle: 'Register a new agent', 
          icon: 'bx-user-plus', 
          action: 'CREATE_AGENT' 
        });
      }

      if (this.userData?.role === 'ADMIN') 
      {
        this.quickOptions.push({ 
          title: 'New Support', 
          subtitle: 'Register a new support account', 
          icon: 'bx-support', 
          action: 'CREATE_SUPPORT' 
        });
      }
    }
  }

  handleAction(action: string): void
  {
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
        this.router.navigate(['/create-listing']);
        break;

      default:
        this.router.navigate(['/not-implemented']);
        break;
    }
  }


  goToLogin(): void 
  {
    this.router.navigate(['/login']);
  }

  goToRegister(): void
  {
    this.router.navigate(['/register']);
  }

  goToChangePassword() 
  {
    this.router.navigate(['/change-password']);
  }

  toggleFilterPanel() 
  {
    this.isFiltersOpen = !this.isFiltersOpen;
  }
}

