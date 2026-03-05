import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../auth/auth';
import { ChangeDetectorRef } from '@angular/core';

@Component(
{
  selector: 'app-change-password',
  standalone: true, 
  imports: [ReactiveFormsModule, CommonModule], 
  templateUrl: './change-password.html',
  styleUrls: ['./change-password.scss']
})
export class ChangePasswordComponent implements OnInit 
{
  setupForm!: FormGroup; 
  hideOldPassword = true;
  hideNewPassword = true;
  isSubmitted = false;
  serverErrorMessage = '';

  constructor(
    private fb: FormBuilder, 
    private authService: AuthService, 
    private http: HttpClient, 
    private router: Router, 
    private route: ActivatedRoute, 
    private cd: ChangeDetectorRef,  
  ) 
  {}

  ngOnInit(): void 
  {
    this.setupForm = this.fb.group(
    {
      oldPassword: ['', [Validators.required]], 
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      repeatPassword: ['', [Validators.required]]
    }, 
    {
      validators: this.passwordMatchValidator
    });
  }

  passwordMatchValidator(g: any) 
  {
    return g.get('newPassword')?.value === g.get('repeatPassword')?.value
      ? null : { 'mismatch': true };
  }

  toggleOldPassword() 
  {
    this.hideOldPassword = !this.hideOldPassword;
  }
  
  toggleNewPassword() 
  {
    this.hideNewPassword = !this.hideNewPassword;
    
  }
  isButtonDisabled() 
  {
    return this.setupForm.invalid;
  }

  onSubmit() 
  {
    this.isSubmitted = true;
    
    if (this.setupForm.invalid) 
    {
      return;
    }

    const { oldPassword, newPassword } = this.setupForm.value;

    this.authService.changePassword(oldPassword, newPassword).subscribe(
    {
      next: () => 
      {
        alert('Password cambiata con successo!');
        localStorage.removeItem('setup_email');
        this.router.navigate(['/login'], { queryParams: { setupSuccess: true } });
      },
      error: (err) => 
      {
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

        this.setupForm.get('oldPassword')?.markAsTouched();
        this.setupForm.get('newPassword')?.markAsTouched();
        this.setupForm.get('repeatPassword')?.markAsTouched();
        
        this.cd.detectChanges();
      }
    });
  }
}