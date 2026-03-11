import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth';
import { ChangeDetectorRef } from '@angular/core';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
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
      email: new FormControl('', [Validators.required]),
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
    this.router.navigate(['/login']);
  }

  isButtonDisabled(): boolean 
  {
    const controls = this.registerForm.controls;
    
    return (
      controls['firstName'].invalid ||
      controls['lastName'].invalid ||
      controls['email'].invalid ||
      controls['rawPassword'].invalid ||
      controls['repeatPassword'].invalid
    );
  }


  onRegister() 
  {
    this.serverErrorMessage = null;
    this.isSubmitted = true;

    if (this.registerForm.invalid) 
    {
      return;
    }

    this.authService.register(this.registerForm.value).subscribe(
    {
      next: () => 
      {
        this.router.navigate(['/home']); 
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
          this.registerForm.get('rawPassword')?.markAsTouched();
          this.registerForm.get('email')?.markAsTouched();
          this.cd.detectChanges();
        }
      }
    });
  }
}