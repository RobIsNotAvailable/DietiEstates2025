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
  @Input() @HostBinding('class.open') isOpen = false;
  @Output() close = new EventEmitter<void>();
}