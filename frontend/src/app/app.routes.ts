import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { RegisterComponent } from './pages/register/register'; 
import { HomeComponent } from './pages/home/home';
import { SetupPasswordComponent } from './auth/setup-password/setup-password';
import { CreateStaffComponent } from './pages/create-staff/create-staff';


export const routes: Routes = 
[
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent }, 
    { path: 'home', component: HomeComponent },
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'link-login/:token', component: SetupPasswordComponent },
    { 
    path: 'create-agent', 
    component: CreateStaffComponent, 
    data: { role: 'AGENT' } 
    },
    { 
        path: 'create-support', 
        component: CreateStaffComponent, 
        data: { role: 'SUPPORT' } 
    },

    // Wildcard route for a 404 page 
    { path: '**', redirectTo: '/home' }
];