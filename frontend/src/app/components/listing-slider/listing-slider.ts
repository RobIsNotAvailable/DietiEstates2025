import { Component, Input, Output, EventEmitter, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-listing-slider',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './listing-slider.html',
  styleUrls: ['./listing-slider.scss'],
  host: { 'tabindex': '0' }
})

export class ListingSliderComponent 
{
  @Input() images: string[] = [];
  @Input() currentIndex: number = 0;
  @Output() currentIndexChange = new EventEmitter<number>();

  @HostListener('keydown', ['$event'])
  handleKeyboard(event: KeyboardEvent) 
  {
    const target = event.target as HTMLElement;
    if (target.tagName === 'INPUT' || target.tagName === 'TEXTAREA') return;

    if (this.images.length > 1) {
      if (event.key === 'ArrowRight') this.nextSlide();
      if (event.key === 'ArrowLeft') this.prevSlide();
    }
  }

  nextSlide() 
  {
    this.currentIndex = (this.currentIndex + 1) % this.images.length;
    this.currentIndexChange.emit(this.currentIndex);
  }

  prevSlide() 
  {
    this.currentIndex = (this.currentIndex - 1 + this.images.length) % this.images.length;
    this.currentIndexChange.emit(this.currentIndex);
  }

  goToSlide(index: number) 
  {
    this.currentIndex = index;
    this.currentIndexChange.emit(this.currentIndex);
  }
}