import { Routes } from '@angular/router';
import { authGuard } from './app.guard';
import { LoginComponent } from './pages/login/login';
import { RegisterComponent } from './pages/register/register'; 
import { HomeComponent } from './pages/home/home';
import { ChangePasswordComponent } from './pages/change-password/change-password';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password';
import { SetupPasswordComponent } from './pages/setup-password/setup-password';
import { CreateStaffComponent } from './pages/create-staff/create-staff';
import { CreateListingComponent } from './pages/create-listing/create-listing';
import { NotImplementedComponent } from './pages/not-implemented/not-implemented';
import { ListingsPageComponent } from './pages/listings-page/listings-page';
import { DashboardComponent } from './pages/dashboard/dashboard';



export const routes: Routes = 
[
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'listings', component: ListingsPageComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'link-login/:token', component: SetupPasswordComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },

  {
    path: '',
    canActivateChild: [authGuard],
    children: [
      { path: 'change-password', component: ChangePasswordComponent },
      { path: 'create-listing', component: CreateListingComponent ,data: { expectedRole: 'AGENT' } },
      { path: 'create-agent', component: CreateStaffComponent, data: { role: 'AGENT', expectedRole: 'SUPPORT' } },
      { path: 'create-support', component: CreateStaffComponent, data: { role: 'SUPPORT', expectedRole: 'ADMIN' } },
      { path: 'dashboard', component: DashboardComponent, data: { expectedRole: 'AGENT' } },
    ]
  },

  { path: 'not-implemented', component: NotImplementedComponent },
  { path: '**', redirectTo: '/home' }
];