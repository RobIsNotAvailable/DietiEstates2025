import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { RouterLink, Router } from '@angular/router'; 
import { AuthService } from '../../services/auth';

@Component(
{
  selector: 'app-login',
  standalone: true,
  imports: 
  [
    CommonModule, 
    ReactiveFormsModule, 
    RouterLink 
  ],
  templateUrl: './login.html', 
  styleUrl: './login.scss'     
})
export class LoginComponent 
{
  loginForm: FormGroup;


  constructor(private authService: AuthService, private router: Router) 
  {
    this.loginForm = new FormGroup(
    {
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(6)])
    });
  }

  onSubmit() 
  {
    if (this.loginForm.valid) 
    {
      this.authService.login(this.loginForm.value).subscribe(
      {
        next: (res) => 
        {
          console.log('Login OK!', res);
          this.router.navigate(['/home']); 
        },
        error: (err) => 
        {
          console.error('Errore login:', err);
          alert('Credenziali non valide o server non raggiungibile');
        }
      });
    }
  }
}