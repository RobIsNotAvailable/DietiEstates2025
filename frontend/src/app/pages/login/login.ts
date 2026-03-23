import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router'; 
import { AuthService } from '../../services/auth';
import { ChangeDetectorRef } from '@angular/core';


@Component(
{
  selector: 'app-login',
  standalone: true,
  imports: 
  [
    CommonModule, 
    ReactiveFormsModule, 
    RouterModule
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
             
  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute, private cd: ChangeDetectorRef) 
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

  onForgotPassword()
  {
    this.router.navigate(['/forgot-password']);
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
      next: () => 
      {
        const to = this.route.snapshot.queryParams['to'] || '/home';

        this.router.navigateByUrl(to);
      },
      error: (err) => 
      {
        if (err.status === 500 || err.status === 0) 
        {
          alert("Something went wrong on our side. Please try again or refresh the page");
        }
        else
        {
          this.serverErrorMessage = err.error; 
        }

        if (err.status !== 500) 
        {
          this.loginForm.get('rawPassword')?.markAsTouched();
          this.loginForm.get('email')?.markAsTouched();
          this.cd.detectChanges();
        }
      }
    });
  }
}