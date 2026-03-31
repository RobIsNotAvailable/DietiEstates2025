import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from './services/auth';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn())
  {
    router.navigate(['/login'], { queryParams: { returnUrl: state.url }});
    return false;
  }

  const expectedMinimumRole = route.data['expectedRole'];

  if (expectedMinimumRole && !authService.hasAtLeastRole(expectedMinimumRole))
  {
    router.navigate(['/not-implemented']);
    return false;
  }

  return true;
};