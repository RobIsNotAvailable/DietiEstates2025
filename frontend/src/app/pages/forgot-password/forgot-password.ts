import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router'; 
import { AccountService } from '../../services/account';
import { ChangeDetectorRef } from '@angular/core';


@Component(
{
  selector: 'app-forgot-password',
  standalone: true,
  imports: 
  [
    CommonModule, 
    ReactiveFormsModule, 
    RouterModule
  ],
  templateUrl: './forgot-password.html', 
  styleUrl: './forgot-password.scss'     
})
export class ForgotPasswordComponent
{
  forgotForm: FormGroup;
  serverErrorMessage: string | null = null;
  isSubmitted = false;

  constructor(private accountService: AccountService, private router: Router, private cd: ChangeDetectorRef) 
  {
    this.forgotForm = new FormGroup(
    {
      email: new FormControl('', [Validators.required]),
    });
  }

  onSubmit()
  {
    this.serverErrorMessage = null;
    this.isSubmitted = true;

    if (this.forgotForm.invalid) 
    {
      return;
    }

    this.accountService.forgotPassword(this.forgotForm.value).subscribe(
    {
      next: () => 
      {
        alert('If the mail is registered, you\'ll recieve an email');
        this.router.navigate(['/login']); 
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
          this.forgotForm.get('email')?.markAsTouched();
          this.cd.detectChanges();
        }
      }
    });
  }
}
