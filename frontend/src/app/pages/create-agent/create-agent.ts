import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component(
{
  selector: 'app-create-agent',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-agent.html',
  styleUrls: ['./create-agent.scss']
})
export class CreateAgentComponent implements OnInit 
{
  agentForm!: FormGroup;
  isSubmitted = false;
  serverErrorMessage = '';

  constructor(
    private fb: FormBuilder, 
    private http: HttpClient, 
    private router: Router
  ) 
  {}

  ngOnInit(): void 
  {
    this.initForm();
  }

  private initForm(): void 
  {
    this.agentForm = this.fb.group(
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

    if (this.agentForm.invalid) 
    {
        return;
    }

    const payload = 
    {
        firstName: this.agentForm.value.firstName,
        lastName: this.agentForm.value.lastName,
        email: this.agentForm.value.email,
        securityLevel: 'AGENT'
    };

    const url = '/api/company/create'; 

    this.http.post(url, payload, { responseType: 'text' }).subscribe(
    {
        next: (response) => 
        {
            console.log('Success:', response);
            alert('Agent account created successfully!'); 
            
            this.router.navigate(['/']);
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
    this.router.navigate(['/']); 
  }
}