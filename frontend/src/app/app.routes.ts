import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { RegisterComponent } from './pages/register/register'; 
import { HomeComponent } from './pages/home/home';
import { SetupPasswordComponent } from './auth/setup-password/setup-password';
import { CreateAgentComponent } from './pages/create-agent/create-agent';


export const routes: Routes = 
[
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent }, 
    { path: 'home', component: HomeComponent },
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'link-login/:token', component: SetupPasswordComponent },
    { path: 'create-agent', component: CreateAgentComponent },
    // Wildcard route for a 404 page 
    { path: '**', redirectTo: '/home' }
];