import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { ChangeDetectorRef } from '@angular/core';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class RegisterComponent 
{
  registerForm: FormGroup;
  hidePassword = true;
  isSubmitted = false;
  serverErrorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router, private cd: ChangeDetectorRef) 
  {
    this.registerForm = new FormGroup(
    {
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      rawPassword: new FormControl('', [Validators.required]),
      repeatPassword: new FormControl('', [Validators.required])
    },
    { 
      validators: (g: any) => this.passwordMatchValidator(g) 
    });
  }

  passwordMatchValidator(g: FormGroup) 
  {
    const pass = g.get('rawPassword')?.value;
    const confirmPass = g.get('repeatPassword')?.value;
    return pass === confirmPass ? null : { mismatch: true };
  }

  togglePassword() 
  {
    this.hidePassword = !this.hidePassword;
  }

  goToLogin()
  {
    console.log('GO TO LOGIN!');
    this.router.navigate(['/login']);
  }

  onRegister() 
  {
    this.serverErrorMessage = null;
    this.isSubmitted = true;

    if (this.registerForm.valid) 
    {
        this.authService.register(this.registerForm.value).subscribe(
        {
            next: (res) => 
            {
              console.log('Registration OK!', res);
              this.router.navigate(['/home']); 
            },
            error: (err) => 
            {
              console.log("Server error:", err.error);

              if (typeof err.error === 'string') 
              {
                this.serverErrorMessage = err.error; 
              } 
              else if (err.error && typeof err.error === 'object') 
              {
                const errorKeys = Object.keys(err.error);
                if (errorKeys.length > 0) 
                {
                  this.serverErrorMessage = err.error[errorKeys[0]]; 
                }
              } 
              else 
              {
                this.serverErrorMessage = "An unexpected error occurred. Please try again.";
              }

              this.registerForm.get('rawPassword')?.markAsTouched();
              this.registerForm.get('email')?.markAsTouched();
              this.cd.detectChanges();
            }
        });
    }
  }

}