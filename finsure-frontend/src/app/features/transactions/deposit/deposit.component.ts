import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-deposit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './deposit.component.html'
})
export class DepositComponent {
  accountId: number | null = null;
  amount:    number | null = null;
  loading  = false;
  errorMsg = '';
  result: any = null;

  constructor(private api: ApiService) {}

  onDeposit(): void {
    if (!this.accountId || !this.amount) {
      this.errorMsg = 'Please fill all fields'; return;
    }
    if (this.amount <= 0) {
      this.errorMsg = 'Amount must be greater than zero'; return;
    }
    if (this.amount > 1000000) {
      this.errorMsg = 'Maximum deposit limit is ₹10,00,000 per transaction'; return;
    }
    if (!Number.isInteger(this.amount * 100)) {
      this.errorMsg = 'Amount cannot have more than 2 decimal places'; return;
    }
    this.loading = true;
    this.errorMsg = '';
    this.api.deposit({ accountId: this.accountId, amount: this.amount }).subscribe({
      next:  data => { this.loading = false; this.result = data; },
      error: err  => { this.loading = false; this.errorMsg = this.parseError(err, 'Deposit failed. Please try again.'); }
    });
  }

  reset(): void { this.result = null; this.amount = null; this.errorMsg = ''; }

  parseError(err: any, fallback: string): string {
    try {
      if (typeof err.error === 'string' && err.error.length > 0) {
        try { const p = JSON.parse(err.error); return p.message || p.error || err.error; }
        catch { return err.error; }
      }
      if (err.error?.message)                    return err.error.message;
      if (err.status === 404)                    return 'Account not found. Please check the Account ID.';
      if (err.status === 400)                    return 'Invalid request. Please check the details entered.';
      if (err.status === 403)                    return 'Access denied. Insufficient permissions.';
      if (err.status === 0 || err.status === 503) return 'Service is currently unavailable.';
    } catch { }
    return fallback;
  }
}