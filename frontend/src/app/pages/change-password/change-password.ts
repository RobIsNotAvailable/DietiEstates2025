import { Component, OnInit } from '@angular/core';
import { AbstractControlOptions, FormGroup, ReactiveFormsModule, Validators, FormControl } from '@angular/forms';
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
export class ChangePasswordComponent
{
  setupForm: FormGroup; 
  hideOldPassword = true;
  hideNewPassword = true;
  isSubmitted = false;
  serverErrorMessage: string | null = null;

  constructor(private accountService: AccountService, private router: Router, private cd: ChangeDetectorRef)
  {
    this.setupForm = new FormGroup(
      {
      oldPassword: new FormControl ('', [Validators.required]), 
      newPassword: new FormControl ('', [Validators.required]),
      repeatPassword: new FormControl ('', [Validators.required])
      },
      {
        validators: (g: any) => this.passwordMatchValidator(g)
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

  isButtonDisabled(): boolean 
  {
    const controls = this.setupForm.controls;
    
    return (
      controls['oldPassword'].invalid ||
      controls['newPassword'].invalid ||
      controls['repeatPassword'].invalid
    );
  }

  onSubmit() 
  { 
    this.serverErrorMessage = null;
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
            alert("Something went wrong on our side. Please try again or refresh the page");
          }
          else
          {
            this.serverErrorMessage = err.error; 
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


