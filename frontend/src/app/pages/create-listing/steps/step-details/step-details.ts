import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-step-details',
  imports: [],
  templateUrl: './step-details.html',
  styleUrl: './step-details.scss',
})
export class StepDetailsComponent
{
  @Input() parentForm!: FormGroup;

}
