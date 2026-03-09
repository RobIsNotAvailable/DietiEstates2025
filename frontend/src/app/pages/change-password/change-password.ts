import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router,RouterModule } from '@angular/router';
import { AccountService } from '../../services/account';
import { ChangeDetectorRef } from '@angular/core';

@Component(
{
  selector: 'app-change-password',
  standalone: true, 
  imports: [ReactiveFormsModule, CommonModule,RouterModule], 
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
    private accountService: AccountService, 
    private router: Router, 
    private cd: ChangeDetectorRef,  
  ) 
  {}

  ngOnInit(): void 
  {
    this.setupForm = this.fb.group(
    {
      oldPassword: ['', [Validators.required]], 
      newPassword: ['', [Validators.required]],
      repeatPassword: ['', [Validators.required]]
    }, 
    {
      validators: this.passwordMatchValidator
    });
  }

  passwordMatchValidator(g: FormGroup) 
  {
    const pass = g.get('newPassword')?.value;
    const confirmPass = g.get('repeatPassword')?.value;
    return pass === confirmPass ? null : { mismatch: true };
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

    this.accountService.changePassword(this.setupForm.value).subscribe(
    {
        next: () => 
        {
            alert('Password changed successfully');
            localStorage.removeItem('setup_email');
            this.router.navigate(['/login'], { queryParams: { setupSuccess: true } });
        },
        error: (err) => 
        {
          if (err.status === 500 || err.status === 0) 
          {
            alert("Something went wrong on our side. Please try again or refresh the page.");
          }
          else if (typeof err.error === 'string') 
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

          if (err.status !== 500) 
          {
            this.setupForm.get('newPassword')?.markAsTouched();
            this.setupForm.get('oldPassword')?.markAsTouched();
            this.cd.detectChanges();
          }
        }
    });
  }
}


