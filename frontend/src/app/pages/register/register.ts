import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class RegisterComponent 
{
  registerForm: FormGroup;
  hidePassword = true;

  constructor(private router: Router) 
  {
    this.registerForm = new FormGroup(
    {
      nome: new FormControl('', [Validators.required]),
      cognome: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(6)])
    });
  }

  togglePassword() 
  {
    this.hidePassword = !this.hidePassword;
  }

  onRegister() 
  {
    if (this.registerForm.valid) 
    {
      console.log("Dati registrazione:", this.registerForm.value);
    }
  }

  goToLogin()
  {
    this.router.navigate(['/login']);
  }
}