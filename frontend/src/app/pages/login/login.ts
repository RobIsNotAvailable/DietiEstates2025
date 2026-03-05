import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router'; 
import { AuthService } from '../../auth/auth';
import { ChangeDetectorRef } from '@angular/core';


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
  serverErrorMessage: string | null = null;
  isSubmitted = false;
             
  constructor(private authService: AuthService, private router: Router, private cd: ChangeDetectorRef) 
  {
    this.loginForm = new FormGroup(
    {
      email: new FormControl('', [Validators.required]),
      rawPassword: new FormControl('', [Validators.required])
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

  onLogin() 
  {
    this.serverErrorMessage = null;
    this.isSubmitted = true;

    if (this.loginForm.invalid) 
    {
      return;
    }

    this.authService.login(this.loginForm.value).subscribe(
    {
      next: (res) => 
      {
        console.log('Login OK!', res);
        this.router.navigate(['/home']); 
      },
      error: (err) => 
      {
        console.log("Server error:", err.error);
        
        this.serverErrorMessage = "Credentials not valid";
        
        this.loginForm.get('rawPassword')?.reset(); 
        this.cd.detectChanges();
      }
    });
  }
}