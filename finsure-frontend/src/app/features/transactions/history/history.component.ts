import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './history.component.html'
})
export class HistoryComponent implements OnInit {
  accountId:         number | null = null;
  selectedAccountId: number | null = null;
  memberAccounts:    any[] = [];
  transactions:      any[] = [];
  loading          = false;
  loadingAccounts  = false;
  error            = '';
  searched         = false;

  constructor(
    private api:   ApiService,
    private route: ActivatedRoute,
    public  auth:  AuthService
  ) {}

  ngOnInit(): void {
    if (this.hasRole('MEMBER')) {
      this.loadingAccounts = true;
      this.api.getMyAccounts().subscribe({
        next: accounts => {
          this.memberAccounts  = accounts || [];
          this.loadingAccounts = false;
          
          if (this.memberAccounts.length === 1) {
            this.selectedAccountId = this.memberAccounts[0].accountId;
            this.accountId         = this.selectedAccountId;
            this.search();
          }
        },
        error: () => { this.memberAccounts = []; this.loadingAccounts = false; }
      });
    } else {
      
      const qId = this.route.snapshot.queryParams['accountId'];
      if (qId) { this.accountId = +qId; this.search(); }
    }
  }

  onAccountSelect(id: number | null): void {
    if (!id) return;
    this.accountId = id;
    this.search();
  }

  hasRole(...roles: string[]): boolean { return this.auth.hasRole(...roles); }

  search(): void {
    if (!this.accountId) return;
    this.loading  = true;
    this.error    = '';
    this.searched = true;
    this.api.getTransactionHistory(this.accountId).subscribe({
      next:  d   => { this.transactions = d; this.loading = false; },
      error: err => {
        this.loading      = false;
        this.transactions = [];
        this.error        = this.parseError(err, 'No transactions found for this account');
      }
    });
  }

  parseError(err: any, fallback: string): string {
    try {
      if (typeof err.error === 'string' && err.error.length > 0) {
        try { const p = JSON.parse(err.error); return p.message || p.error || err.error; }
        catch { return err.error; }
      }
      if (err.error?.message) return err.error.message;
      if (err.status === 404) return 'No transactions found for this account.';
      if (err.status === 403) return 'Access denied. Insufficient permissions.';
    } catch { }
    return fallback;
  }
}