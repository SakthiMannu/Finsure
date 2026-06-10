import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {

  constructor(private http: HttpClient, private router: Router) {}

  
  login(email: string, password: string): Observable<any> {
  return this.http.post<any>('/auth/login', { email, password }).pipe(
    tap(response => {
      if (response.token) {
        localStorage.setItem('token', response.token);
        localStorage.setItem('role', response.role);
        const payload = JSON.parse(atob(response.token.split('.')[1]));
        localStorage.setItem('email', payload.sub);
        
      }
    })
  );
}

  
  register(data: any): Observable<any> {
    return this.http.post<any>('/auth/register', data);
  }

  
  forgotPassword(email: string, newPassword: string): Observable<any> {
    return this.http.post(
        '/auth/forgot-password',
        { email, newPassword },
        { responseType: 'text' as 'json' }
    );
}

  
  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  
  getRole(): string {
    return localStorage.getItem('role') || '';
  }

  
  getEmail(): string {
    return localStorage.getItem('email') || '';
  }

  
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  
  hasRole(...roles: string[]): boolean {
    const currentRole = this.getRole();
    return roles.includes(currentRole);
  }

  
  getInitials(): string {
    const email = this.getEmail();
    return email ? email.charAt(0).toUpperCase() : 'U';
  }

  
  getDisplayName(): string {
    const email = this.getEmail();
    return email ? email.split('@')[0] : 'User';
  }
}
