import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl:    './login.component.css'
})
export class LoginComponent {
  email        = '';
  password     = '';
  loading      = false;
  error        = '';
  showPassword = false;

  constructor(private auth: AuthService, private router: Router) {}

  onLogin(): void {
    if (!this.email || !this.password) {
      this.error = 'Please enter email and password'; return;
    }
    this.loading = true;
    this.error   = '';

    this.auth.login(this.email, this.password).subscribe({
      next:  ()  => { this.loading = false; this.router.navigate(['/app/dashboard']); },
      error: err => { this.loading = false; this.error = err.error?.message || 'Invalid email or password'; }
    });
  }
}