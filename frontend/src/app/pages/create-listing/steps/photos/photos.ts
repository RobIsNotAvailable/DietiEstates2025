import { Component, Input, ChangeDetectorRef, OnInit } from '@angular/core'; 
import { CommonModule } from '@angular/common';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ListingSliderComponent } from '../../../../components/listing-slider/listing-slider';

@Component({
  selector: 'app-photos',
  standalone: true, 
  imports: [
    CommonModule,         
    ReactiveFormsModule,   
    FormsModule,
    ListingSliderComponent
  ],
  templateUrl: './photos.html',
  styleUrl: './photos.scss',
})
export class PhotosComponent implements OnInit
{
  @Input() parentForm!: FormGroup;
  @Input() showErrors: boolean = false;

  imagePreviews: string[] = [];
  imageDescriptions: string[] = [];
  currentIndex: number = 0;
  isUploading = false;

  planimetryPreview: string[] = []; 
  currentPlanimetryIndex: number = 0;
  isUploadingPlanimetry: boolean = false;

  constructor(private cd: ChangeDetectorRef) {}

  ngOnInit() 
  {
      const savedImages = this.photosGroup.get('images')?.value;
      if (savedImages && savedImages.length > 0) 
      {
          this.imagePreviews = [...savedImages];
          this.currentIndex = 0; 
      }

      const savedDescriptions = this.photosGroup.get('descriptions')?.value;
      if (savedDescriptions && savedDescriptions.length > 0) 
      {
        this.imageDescriptions = [...savedDescriptions];
      } 
      else 
      {
        this.imageDescriptions = new Array(this.imagePreviews.length).fill('');
      }

      const savedPlanimetry = this.photosGroup.get('planimetry')?.value;
      if (savedPlanimetry && savedPlanimetry.length > 0) 
      {
          this.planimetryPreview = [...savedPlanimetry];
          this.currentPlanimetryIndex = 0;
      }
  }

  get photosGroup() 
  {
    return this.parentForm.get('photos') as FormGroup;
  }

  onFilesSelected(event: any) 
  {
    const files: FileList = event.target.files;
    if (!files || files.length === 0) return;

    const MAX_SIZE_MB = 5; 
    const fileArray = Array.from(files);
    
    const validFiles = fileArray.filter(file => 
    {
      if (file.size > MAX_SIZE_MB * 1024 * 1024) 
      {
        alert(`Il file è troppo grande! Massimo ${MAX_SIZE_MB}MB.`);
        return false;
      }
      return true;
    });

    if (validFiles.length === 0) return;

    this.isUploading = true;
    let processed = 0;

    validFiles.forEach((file: File) => 
    {
      this.compressImage(file).then((compressedBase64: string) => 
      {
        this.imagePreviews.push(compressedBase64);
        this.imageDescriptions.push(''); // Aggiunge una descrizione vuota per la nuova immagine
        processed++;
        
        if (processed === validFiles.length) 
        {
          this.updateFormAndUI();
        }
      });
    });
    event.target.value = '';
  }

  removeImage(index: number) 
  {
    this.imagePreviews.splice(index, 1);
    this.imageDescriptions.splice(index, 1); 

    this.photosGroup.get('images')?.setValue([...this.imagePreviews]);
    this.photosGroup.get('descriptions')?.setValue([...this.imageDescriptions]);

    if (this.currentIndex >= this.imagePreviews.length) 
    {
      this.currentIndex = Math.max(0, this.imagePreviews.length - 1);
    }
    
    this.cd.markForCheck();
    this.cd.detectChanges();
  }

  onPlanimetrySelected(event: any) 
  {
      const files = event.target.files;
      if (!files || files.length === 0) return;

      this.isUploadingPlanimetry = true; 
      const fileArray = Array.from(files);
      let processed = 0;

      fileArray.forEach((file: any) => 
      {
          const reader = new FileReader();
          reader.onload = (e: any) => 
          {
              this.planimetryPreview.push(e.target.result);
              processed++;

              if (processed === fileArray.length) 
              {
                  this.planimetryPreview = [...this.planimetryPreview];
                  this.photosGroup.get('planimetry')?.setValue(this.planimetryPreview);
                  
                  setTimeout(() => 
                  {
                      this.isUploadingPlanimetry = false;
                      this.currentPlanimetryIndex = this.planimetryPreview.length - 1;
                      this.cd.markForCheck();
                      this.cd.detectChanges();
                  }, 100);
              }
          };
          reader.readAsDataURL(file);
      });
      event.target.value = '';
  }

  removePlanimetry(index: number) 
  {
      this.planimetryPreview.splice(index, 1);
      this.photosGroup.get('planimetry')?.setValue([...this.planimetryPreview]);

      if (this.currentPlanimetryIndex >= this.planimetryPreview.length) 
      {
          this.currentPlanimetryIndex = Math.max(0, this.planimetryPreview.length - 1);
      }
      this.cd.detectChanges();
  }

  compressImage(file: File): Promise<string> 
  {
    return new Promise((resolve) => 
    {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (event: any) => 
      {
        const img = new Image();
        img.src = event.target.result;
        img.onload = () => 
        {
          const canvas = document.createElement('canvas');
          const MAX_WIDTH = 1280;
          let width = img.width;
          let height = img.height;

          if (width > MAX_WIDTH) {
            height = (height * MAX_WIDTH) / width;
            width = MAX_WIDTH;
          }

          canvas.width = width;
          canvas.height = height;
          const ctx = canvas.getContext('2d');
          ctx?.drawImage(img, 0, 0, width, height);

          const compressedBase64 = canvas.toDataURL('image/jpeg', 0.7);
          resolve(compressedBase64);
        };
      };
    });
  }

  updateFormDescriptions() 
  {
    this.photosGroup.get('descriptions')?.setValue([...this.imageDescriptions]);
  }

  private updateFormAndUI() 
  {
    this.imagePreviews = [...this.imagePreviews]; 
    this.photosGroup.get('images')?.setValue(this.imagePreviews);
    
    this.photosGroup.get('descriptions')?.setValue([...this.imageDescriptions]);
    
    setTimeout(() => 
    {
      this.isUploading = false;
      this.currentIndex = this.imagePreviews.length - 1;
      this.cd.markForCheck(); 
      this.cd.detectChanges(); 
    }, 200); 
  }


}