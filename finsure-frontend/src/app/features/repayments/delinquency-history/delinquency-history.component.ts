import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-delinquency-history',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './delinquency-history.component.html'
})
export class DelinquencyHistoryComponent {
  loanId:  number | null = null;
  records: any[]         = [];
  loading  = false;
  error    = '';
  searched = false;

  constructor(private api: ApiService, public auth: AuthService) {}

  search(): void {
    if (!this.loanId) return;
    this.loading  = true;
    this.error    = '';        
    this.records  = [];        
    this.searched = true;
    this.api.getDelinquenciesByLoan(this.loanId).subscribe({
      next:  d   => { this.records = d; this.loading = false; },
      error: err => { this.error = err.error?.message || 'Not found'; this.loading = false; this.records = []; }
    });
  }

  hasRole(...roles: string[]): boolean { return this.auth.hasRole(...roles); }
}