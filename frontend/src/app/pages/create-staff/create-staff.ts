import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { CreateStaffRequest, StatsService } from '../../services/company';


@Component(
{
  selector: 'app-create-staff',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,RouterModule],
  templateUrl: './create-staff.html',
  styleUrls: ['./create-staff.scss']
})
export class CreateStaffComponent implements OnInit 
{
  staffForm!: FormGroup;
  isSubmitted: boolean = false;
  serverErrorMessage = '';
  isLoading: boolean = false;
  loadingMessage: string = '';

  roleType: 'AGENT' | 'SUPPORT' = 'AGENT';
  pageTitle = '';

  constructor(
    private fb: FormBuilder, 
    private http: HttpClient, 
    private router: Router,
    private route: ActivatedRoute,
    private cd: ChangeDetectorRef,
    private statsService: StatsService
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
      email: ['', [Validators.required]]
    });
  }

  onSubmit(): void 
  {
    this.isLoading = true;
    this.loadingMessage = 'Sending email...';
    this.isSubmitted = true;  
    this.serverErrorMessage = '';

    if (this.staffForm.invalid) 
    {
      this.isLoading = false;
      return;
    }

    const payload: CreateStaffRequest = 
    {
      firstName: this.staffForm.value.firstName,
      lastName: this.staffForm.value.lastName,
      email: this.staffForm.value.email,
      securityLevel: this.roleType 
    };

    this.statsService.createStaffMember(payload).subscribe(
    {
      next: () => 
      {
        this.isLoading = false;
        var role = this.roleType.charAt(0).toUpperCase() + this.roleType.slice(1).toLowerCase();
        alert(`${role} account created successfully!`); 
        this.router.navigate(['/home']);
      },
      error: (err) => 
      {
        this.isLoading = false;
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
          this.staffForm.get('email')?.markAsTouched();
          this.cd.detectChanges();
        }
      }
    });
  }

  onCancel(): void 
  {
    this.router.navigate(['/home']); 
  }

}