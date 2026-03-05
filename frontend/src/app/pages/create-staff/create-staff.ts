import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

@Component(
{
  selector: 'app-create-staff',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-staff.html',
  styleUrls: ['./create-staff.scss']
})
export class CreateStaffComponent implements OnInit 
{
  staffForm!: FormGroup;
  isSubmitted = false;
  serverErrorMessage = '';

  roleType: 'AGENT' | 'SUPPORT' = 'AGENT';
  pageTitle = '';

  constructor(
    private fb: FormBuilder, 
    private http: HttpClient, 
    private router: Router,
    private route: ActivatedRoute
  ) 
  {}

  ngOnInit(): void 
  {
    const routeData = this.route.snapshot.data;
    this.roleType = routeData['role'] || 'AGENT';
    
    this.pageTitle = this.roleType === 'AGENT' ? 'Register New Agent' : 'Create New Staff';
    
    this.initForm();
  }

  private initForm(): void 
  {
    this.staffForm = this.fb.group(
    {
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void 
  {
    this.isSubmitted = true;
    this.serverErrorMessage = '';

    if (this.staffForm.invalid) 
    {
      return;
    }

    const payload = 
    {
      firstName: this.staffForm.value.firstName,
      lastName: this.staffForm.value.lastName,
      email: this.staffForm.value.email,
      securityLevel: this.roleType 
    };

    const url = '/api/company/create'; 

    this.http.post(url, payload, { responseType: 'text' }).subscribe(
    {
      next: (response) => 
      {
        console.log('Success:', response);
        alert(`${this.roleType} account created successfully!`); 
        this.router.navigate(['/home']);
      },
      error: (err) => 
      {
        console.error('Error details:', err);
        if (err.status === 409) 
        {
          this.serverErrorMessage = "This email is already in use."; 
        } 
        else 
        {
          this.serverErrorMessage = "An error occurred. Please try again."; 
        }
      }
    });
  }

  onCancel(): void 
  {
    this.router.navigate(['/home']); 
  }

}