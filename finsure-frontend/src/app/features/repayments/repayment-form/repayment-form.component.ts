import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-repayment-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './repayment-form.component.html'
})
export class RepaymentFormComponent {
  loanId:         number | null = null;
  amount:         number | null = null;
  method          = 'ONLINE';
  loading         = false;
  loanLoading     = false;
  error           = '';
  success         = false;
  lastAmount:     number = 0;
  lastMethod      = '';
  loanInfo:       any    = null;
  monthlyEmi:     number = 0;
  totalRepayable: number = 0;
  private loanFetchTimeout: any;

  constructor(private api: ApiService) {}

  onLoanIdChange(value: number): void {
    this.loanInfo = null; this.monthlyEmi = 0;
    this.totalRepayable = 0; this.amount = null;
    if (!value) return;
    clearTimeout(this.loanFetchTimeout);
    this.loanFetchTimeout = setTimeout(() => {
      this.loanLoading = true;
      this.api.getLoanById(value).subscribe({
        next: loan => {
          this.loanInfo       = loan;
          this.monthlyEmi     = this.calculateEmi(loan.amount);
          this.totalRepayable = Math.round(this.monthlyEmi * 12 * 100) / 100;
          this.amount         = this.monthlyEmi;
          this.loanLoading    = false;
        },
        error: () => { this.loanInfo = null; this.loanLoading = false; }
      });
    }, 600);
  }

  calculateEmi(principal: number): number {
    const r = 12 / (12 * 100), n = 12;
    return Math.round((principal * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1) * 100) / 100;
  }

  onSubmit(): void {
    if (!this.loanId || !this.amount) { this.error = 'Please fill all fields'; return; }
    this.loading = true; this.error = ''; this.success = false;
    this.api.makeRepayment({ loanId: this.loanId, amount: this.amount, method: this.method, status: 'PAID' }).subscribe({
      next: () => {
        this.loading = false; this.success = true;
        this.lastAmount = this.amount!; this.lastMethod = this.method;
        this.amount = null; this.loanInfo = null; this.monthlyEmi = 0; this.totalRepayable = 0;
      },
      error: err => { this.loading = false; this.error = err.error?.message || 'Payment failed'; }
    });
  }
}