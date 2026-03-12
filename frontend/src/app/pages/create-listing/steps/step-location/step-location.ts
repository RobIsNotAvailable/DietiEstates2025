import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-step-location',
  imports: [],
  templateUrl: './step-location.html',
  styleUrl: './step-location.scss',
})
export class StepLocationComponent {
  @Input() parentForm!: FormGroup;

}
