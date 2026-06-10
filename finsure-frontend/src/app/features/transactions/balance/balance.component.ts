import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-balance',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './balance.component.html'
})
export class BalanceComponent implements OnInit {
  accountId:         number | null = null;
  selectedAccountId: number | null = null;
  memberAccounts:    any[]         = [];
  balance:           number        = 0;
  displayAccountId:  number | null = null;
  balanceFetched   = false;
  loading          = false;
  loadingAccounts  = false;
  error            = '';

  constructor(private api: ApiService, public auth: AuthService) {}

  ngOnInit(): void {
    if (this.auth.hasRole('MEMBER')) {
      this.loadingAccounts = true;
      this.api.getMyAccounts().subscribe({
        next:  accounts => { this.memberAccounts = accounts || []; this.loadingAccounts = false; },
        error: ()       => { this.memberAccounts = [];             this.loadingAccounts = false; }
      });
    }
  }

  checkBalance(): void {
    const id = this.auth.hasRole('MEMBER') ? this.selectedAccountId : Number(this.accountId);
    if (!id || id <= 0) { this.error = 'Please enter a valid account ID'; return; }

    this.loading = true;
    this.error   = '';
    this.balanceFetched   = false;
    this.displayAccountId = id;

    this.api.getBalance(id).subscribe({
      next: (data: any) => {
        this.loading        = false;
        this.balance        = typeof data === 'number' ? data : (data?.balance ?? data?.currentBalance ?? 0);
        this.balanceFetched = true;
      },
      error: (err: any) => {
        this.loading = false;
        this.error   = this.parseError(err, 'Could not fetch balance. Please try again.');
      }
    });
  }

  parseError(err: any, fallback: string): string {
    try {
      if (typeof err.error === 'string' && err.error.length > 0) {
        try { const p = JSON.parse(err.error); return p.message || p.error || err.error; }
        catch { return err.error; }
      }
      if (err.error?.message)                    return err.error.message;
      if (err.status === 404)                    return 'Account not found. Please check the Account ID.';
      if (err.status === 403)                    return 'You do not have permission to view this account.';
      if (err.status === 0 || err.status === 503) return 'Service is currently unavailable.';
    } catch { }
    return fallback;
  }
}