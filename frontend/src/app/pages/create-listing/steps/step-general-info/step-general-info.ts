import { CommonModule } from '@angular/common';
import { Component, Input, ChangeDetectorRef} from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-step-general-info',
  standalone: true, 
  imports: [CommonModule, ReactiveFormsModule], 
  templateUrl: './step-general-info.html',
  styleUrls: ['./step-general-info.scss']
})
export class StepGeneralInfoComponent 
{
  @Input() parentForm!: FormGroup;
  imagePreviews: string[] = [];
  isUploading = false;

  constructor(private cd: ChangeDetectorRef) {}

  get generalGroup() 
  {
    return this.parentForm.get('generalInfo') as FormGroup;
  }

  currentIndex: number = 0;

  nextSlide() 
  {
      this.currentIndex = (this.currentIndex + 1) % this.imagePreviews.length;
  }

  prevSlide() 
  {
      this.currentIndex = (this.currentIndex - 1 + this.imagePreviews.length) % this.imagePreviews.length;
  }

  removeImage(index: number) 
  {
      this.imagePreviews.splice(index, 1);
      
      if (this.currentIndex >= this.imagePreviews.length) 
      {
        this.currentIndex = Math.max(0, this.imagePreviews.length - 1);
      }

      this.cd.detectChanges();
  }

  onFilesSelected(event: any) 
  {
      const files: FileList = event.target.files;
      if (!files || files.length === 0) return;

      this.isUploading = true;
      const fileArray = Array.from(files);
      let processed = 0;

      fileArray.forEach((file: File) => 
      {
          const reader = new FileReader();
          
          reader.onload = (e: any) => 
          {
              this.imagePreviews.push(e.target.result);
              processed++;
              
              if (processed === fileArray.length) 
              {
                  setTimeout(() => 
                  {
                      this.isUploading = false;
                      
                      this.currentIndex = this.imagePreviews.length - 1;
                      
                      this.cd.detectChanges();
                  }, 500);
              }
          };

          reader.onerror = () => 
          {
              processed++;
              if (processed === fileArray.length) 
              {
                  this.isUploading = false;
                  this.cd.detectChanges();
              }
          };

          reader.readAsDataURL(file);
      });

      event.target.value = '';
  }
}