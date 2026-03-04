import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';

@Component(
{
  selector: 'app-setup-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule], 
  templateUrl: './setup-password.html',
  styleUrls: ['./setup-password.scss']
})
export class SetupPasswordComponent implements OnInit 
{
  setupForm!: FormGroup; 
  token: string | null = null;
  serverErrorMessage = '';
  isSubmitted = false;
  hidePassword = true;

  constructor(private fb: FormBuilder, private route: ActivatedRoute, private http: HttpClient, private router: Router, private cd: ChangeDetectorRef) 
  {
    this.initForm();
  }

  ngOnInit(): void 
  {
    this.token = this.route.snapshot.paramMap.get('token');
  }

  private initForm(): void 
  {
    this.setupForm = this.fb.group(
    {
      newPassword: ['', [Validators.required]],
      repeatPassword: ['', [Validators.required]]
    }, 
    { 
      validators: this.passwordMatchValidator 
    });
  }

  private passwordMatchValidator(g: FormGroup) 
  {
    return g.get('newPassword')?.value === g.get('repeatPassword')?.value
      ? null : { 'mismatch': true };
  }

  togglePassword(): void 
  {
    this.hidePassword = !this.hidePassword;
  }

  isButtonDisabled(): boolean 
  {
    const newPass = this.setupForm.get('newPassword')?.value;
    const repeatPass = this.setupForm.get('repeatPassword')?.value;
    return !newPass || !repeatPass;
  }

  onSubmit(): void 
  {
    this.isSubmitted = true;
    this.serverErrorMessage = '';

    if (this.setupForm.invalid) 
    {
      return;
    }

    const payload = { newPassword: this.setupForm.value.newPassword };

    this.http.post(`http://localhost:8080/api/auth/link-login/${this.token}`, payload).subscribe(
    {
      next: () => 
      {
        alert('Account attivato!');
        this.router.navigate(['/login']);
      },
      error: (err) => 
      {
          let tempErrorMessage = "";

          if (typeof err.error === 'string') 
          {
              tempErrorMessage = err.error; 
          } 
          else if (err.error && typeof err.error === 'object') 
          {
              const errorKeys = Object.keys(err.error);
              tempErrorMessage = errorKeys.length > 0 ? err.error[errorKeys[0]] : "Unknown Error";
          } 
          else 
          {
              tempErrorMessage = "An unexpected error occurred.";
          }

          if (tempErrorMessage.toLowerCase().includes('token')) 
          {
              alert("Link is expired, you'll be redirected to the home");
              this.router.navigate(['/']);
          } 
          else 
          {
              this.serverErrorMessage = tempErrorMessage; 
          }

          this.setupForm.get('newPassword')?.markAsTouched();
          this.cd.detectChanges();
      }
    });
  }
}