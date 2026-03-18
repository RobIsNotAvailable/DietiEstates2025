import { Component, Input, OnChanges, SimpleChanges, AfterViewInit, OnDestroy } from '@angular/core';
import * as L from 'leaflet';

@Component({
  selector: 'app-map',
  templateUrl: './map.html',
  styleUrls: ['./map.scss']
})
export class MapComponent implements AfterViewInit, OnChanges, OnDestroy 
{
  @Input() lat: number = 40.8518; 
  @Input() lon: number = 14.2681;
  @Input() zoom: number = 13;

  instanceId = Math.random().toString(36).substring(2, 9);
  
  private map!: L.Map;
  private marker!: L.Marker;

  private defaultIcon = L.icon({
    iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
    shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41]
  });

  ngAfterViewInit() 
  {
    this.initMap();
  }

  ngOnChanges(changes: SimpleChanges) 
  {
    if (this.map && (changes['lat'] || changes['lon'])) 
    {
      this.updatePosition(this.lat, this.lon);
    }
  }

  private initMap() 
  {
      this.map = L.map(`map-${this.instanceId}`, {
          scrollWheelZoom: false 
      }).setView([this.lat, this.lon], this.zoom);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: '© OpenStreetMap contributors'
      }).addTo(this.map);

      this.marker = L.marker([this.lat, this.lon], { icon: this.defaultIcon }).addTo(this.map);

      this.map.on('focus', () => 
      {
          this.map.scrollWheelZoom.enable();
      });

      this.map.on('blur', () => 
      {
          this.map.scrollWheelZoom.disable();
      });

      setTimeout(() => 
        {
        if (this.map) {
            this.map.invalidateSize();
            this.map.setView([this.lat, this.lon], this.zoom);
        }
      }, 300);
  }

  public updatePosition(lat: number, lon: number) 
  {
    if (!lat || !lon) return;

    this.marker.setLatLng([lat, lon]);
    this.map.flyTo([lat, lon], 17, {
        animate: true,
        duration: 1.5
    });
  }

  ngOnDestroy() 
  {
    if (this.map) 
    {
      this.map.remove();
    }
  }
}