import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-repayment-history',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './repayment-history.component.html'
})
export class RepaymentHistoryComponent {
  loanId:     number | null = null;
  repayments: any[]         = [];
  loading     = false;
  error       = '';
  searched    = false;

  constructor(private api: ApiService) {}

  search(): void {
    if (!this.loanId) return;
    this.loading = true; this.error = ''; this.searched = true;
    this.api.getRepaymentHistory(this.loanId).subscribe({
      next:  d   => { this.repayments = d; this.loading = false; },
      error: err => { this.error = err.error?.message || 'Not found'; this.loading = false; this.repayments = []; }
    });
  }
}