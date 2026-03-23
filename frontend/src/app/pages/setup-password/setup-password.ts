import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';

@Component(
{
  selector: 'app-setup-password',
  standalone: true,
  imports:
  [
    CommonModule,
    ReactiveFormsModule,
    RouterModule
  ], 
  templateUrl: './setup-password.html',
  styleUrls: ['./setup-password.scss']
})
export class SetupPasswordComponent implements OnInit 
{
  setupForm: FormGroup; 
  token: string | null = null;
  serverErrorMessage = '';
  isSubmitted = false;
  hidePassword = true;

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router, private cd: ChangeDetectorRef) 
  {
    this.setupForm = new FormGroup(
    {
      newPassword: new FormControl('', [Validators.required]),
      repeatPassword: new FormControl('', [Validators.required])
    },
    { 
      validators: (g: any) => this.passwordMatchValidator(g) 
    });
  }

  ngOnInit(): void 
  {
    this.token = this.route.snapshot.paramMap.get('token');
  }

  private passwordMatchValidator(g: FormGroup) 
  {
    const pass = g.get('newPassword')?.value;
    const confirmPass = g.get('repeatPassword')?.value;
    return pass === confirmPass ? null : { mismatch: true };
  }

  togglePassword(): void 
  {
    this.hidePassword = !this.hidePassword;
  }

  isButtonDisabled(): boolean 
  {
    const controls = this.setupForm.controls;
    
    return (
      controls['newPassword'].invalid ||
      controls['repeatPassword'].invalid
    );
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
        alert('Password set correctly');
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

        if (this.serverErrorMessage.toLowerCase().includes('token')) 
        {
            alert("Link is expired, you'll be redirected to the home");
            this.router.navigate(['/']);
        }
        else if (err.status !== 500) 
        {
          this.setupForm.get('newPassword')?.markAsTouched();
          this.setupForm.get('repeatPassword')?.markAsTouched();
          this.cd.detectChanges();
        }
      }
    });
  }
}