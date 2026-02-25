import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

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

  constructor(private authService: AuthService, private router: Router) 
  {
    this.registerForm = new FormGroup(
    {
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email: new FormControl('', [
        Validators.required, 
        Validators.pattern('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$')
      ]),
      rawPassword: new FormControl('', [
        Validators.required, 
        Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d).{8,32}$')
      ]),
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

  onRegister() 
  {
      if (this.registerForm.valid) 
      {
          const rawData = this.registerForm.value;

          const payload = 
          {
              firstName: rawData.firstName,
              lastName: rawData.lastName,
              email: rawData.email,
              rawPassword: rawData.rawPassword 
          };

          this.authService.register(payload).subscribe(
          {
              next: (res) => 
              {
                  this.router.navigate(['/login']); 
              },
              error: (err) => 
              {
                  console.error('Registration failed', err);
              }
          });
      }
  }

  goToLogin()
  {
    console.log('GO TO LOGIN!');
    this.router.navigate(['/login']);
  }
}