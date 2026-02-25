import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router'; 
import { AuthService } from '../../services/auth';

@Component(
{
  selector: 'app-login',
  standalone: true,
  imports: 
  [
    CommonModule, 
    ReactiveFormsModule, 
  ],
  templateUrl: './login.html', 
  styleUrl: './login.scss'     
})
export class LoginComponent 
{
  loginForm: FormGroup;
  hidePassword = true;
             
  constructor(private authService: AuthService, private router: Router) 
  {
    this.loginForm = new FormGroup(
    {
      email: new FormControl('', [
        Validators.required, 
        Validators.pattern('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$')
      ]),

      rawPassword: new FormControl('', [
        Validators.required,
      ])
    });
  }
    

  togglePassword() 
  {
    this.hidePassword = !this.hidePassword;
  }

  goToRegister() 
  {
    this.router.navigate(['/register']);
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