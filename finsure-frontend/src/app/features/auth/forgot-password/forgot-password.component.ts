import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './forgot-password.component.html'
})
export class ForgotPasswordComponent {
  email       = '';
  newPassword = '';
  loading     = false;
  error       = '';
  success     = false;

  constructor(private auth: AuthService) {}

  onReset(): void {
    this.loading = true;
    this.error   = '';
    this.auth.forgotPassword(this.email, this.newPassword).subscribe({
      next:  ()  => { this.loading = false; this.success = true; },
      error: err => { this.loading = false; this.error = err.error?.message || 'Reset failed'; }
    });
  }
}