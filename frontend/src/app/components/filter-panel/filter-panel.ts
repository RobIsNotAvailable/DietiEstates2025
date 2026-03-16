import { Component, Input, Output, EventEmitter, HostBinding } from '@angular/core';

@Component
({
  selector: 'app-filter-panel',
  standalone: true,    
  templateUrl: './filter-panel.html',
  styleUrls: ['./filter-panel.scss'],
})
export class FilterPanelComponent 
{
  @Input() isOpen: boolean = false;
  
  @Output() close = new EventEmitter<void>();

  private hasBeenOpened = false;

  @HostBinding('class.active') get active() {
    if (this.isOpen) this.hasBeenOpened = true;
    return this.isOpen;
  }

  @HostBinding('class.closing') get closing() {
    return !this.isOpen && this.hasBeenOpened;
  }

  onCloseClick() {
    this.close.emit();
  }
}