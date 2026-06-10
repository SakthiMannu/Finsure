import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div style="min-height:100vh;display:flex;align-items:center;
                justify-content:center;background:var(--gray-50);padding:24px">
      <div style="background:white;border-radius:var(--radius-lg);padding:36px;
                  box-shadow:var(--shadow-lg);width:100%;max-width:440px">

        <div style="text-align:center;margin-bottom:28px">
          <div style="width:56px;height:56px;background:var(--accent);
                      border-radius:16px;display:flex;align-items:center;
                      justify-content:center;margin:0 auto 12px">
            <span class="material-icons" style="color:white;font-size:28px">person_add</span>
          </div>
          <h2 style="font-size:22px;margin-bottom:4px">Create Account</h2>
          <p style="color:var(--gray-400);font-size:13px">
            You will be registered as MEMBER.<br>
            Contact admin to get staff access.
          </p>
        </div>

        <div *ngIf="success" class="alert alert-success">
          <span class="material-icons">check_circle</span>
          Account created! <a routerLink="/login" style="color:inherit;font-weight:600">Sign in now</a>
        </div>

        <div *ngIf="error" class="alert alert-danger">
          <span class="material-icons">error_outline</span>{{ error }}
        </div>

        <form (ngSubmit)="onRegister()" *ngIf="!success">
          <div class="form-group">
            <label class="form-label">Full Name *</label>
            <input class="form-control" [(ngModel)]="name" name="name"
                   placeholder="Your full name" required>
          </div>

          <div class="form-group">
            <label class="form-label">Email Address *</label>
            <input type="email" class="form-control" [(ngModel)]="email" name="email"
                   placeholder="your@email.com" required>
          </div>

          <div class="form-group">
            <label class="form-label">Phone Number *</label>
            <input class="form-control" [(ngModel)]="phone" name="phone"
                   placeholder="10-digit mobile number" maxlength="10" required>
            <div class="form-error" *ngIf="phone && phone.length !== 10">
              Must be exactly 10 digits
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Password *</label>
            <input type="password" class="form-control" [(ngModel)]="password"
                   name="password" placeholder="Minimum 5 characters" required minlength="5">
          </div>

          <button type="submit" class="btn btn-primary w-full"
                  style="justify-content:center;padding:12px;margin-top:8px"
                  [disabled]="loading || phone.length !== 10">
            {{ loading ? 'Creating account...' : 'Create Account' }}
          </button>
        </form>

        <p style="text-align:center;margin-top:16px;color:var(--gray-400);font-size:14px">
          Already have an account?
          <a routerLink="/login" style="color:var(--primary)">Sign in</a>
        </p>
      </div>
    </div>
  `
})
export class RegisterComponent {
  name = '';
  email = '';
  phone = '';
  password = '';
  loading = false;
  error = '';
  success = false;

  constructor(private auth: AuthService, private router: Router) {}

  onRegister(): void {
    if (this.phone.length !== 10) {
      this.error = 'Phone must be 10 digits';
      return;
    }
    if (this.password.length < 5) {
      this.error = 'Password must be at least 5 characters';
      return;
    }

    this.loading = true;
    this.error = '';

    this.auth.register({
      name: this.name,
      email: this.email,
      phone: this.phone,
      password: this.password,
      role: 'MEMBER'
    }).subscribe({
      next: () => { this.loading = false; this.success = true; },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Registration failed. Email may already exist.';
      }
    });
  }
}