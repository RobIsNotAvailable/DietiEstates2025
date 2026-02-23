import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router'; 

@Component({
  selector: 'app-login',
  standalone: true,
  imports: 
  [
    CommonModule, 
    ReactiveFormsModule, 
    RouterLink 
  ],
  templateUrl: './login.html', 
  styleUrl: './login.scss'     
})
export class LoginComponent 
{
  loginForm: FormGroup;

  constructor() 
  {
    this.loginForm = new FormGroup(
    {
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(6)])
    });
  }

  onSubmit() 
  {
    if (this.loginForm.valid) 
    {
      console.log("Dati inviati:", this.loginForm.value);
    }
  }
}