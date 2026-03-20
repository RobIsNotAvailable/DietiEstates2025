import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http'; 
import { routes } from './app.routes';
import { authInterceptor } from './interceptors/auth.interceptor';
import { LucideAngularModule, School, Ruler} from 'lucide-angular';

export const appConfig: ApplicationConfig = 
{
  providers: 
  [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    importProvidersFrom(LucideAngularModule.pick({ School,Ruler}))
  ]
};