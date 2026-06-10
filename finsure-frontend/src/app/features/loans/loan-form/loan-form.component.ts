import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-loan-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './loan-form.component.html'
})
export class LoanFormComponent implements OnInit {
  memberId:      number | null = null;
  amount:        number        = 0;
  purpose        = '';
  customPurpose  = '';
  loading        = false;
  error          = '';
  success        = false;
  createdLoanId: number | null = null;
  isMember       = false;
  showCustomPurpose = false;

  constructor(
    private api:    ApiService,
    public  auth:   AuthService,
    private router: Router
  ) {}

 ngOnInit(): void {
    this.isMember = this.auth.hasRole('MEMBER');
    if (this.isMember) {
        this.api.getMyMemberId().subscribe({
            next: id => {
                this.memberId = id;
                this.error = ''; 
            },
            error: () => {
                this.error =
                    'No member linked to your account. Contact branch.';
            }
        });
    }
}

  onPurposeChange(): void {
    this.showCustomPurpose = this.purpose === 'Other';
    if (!this.showCustomPurpose) this.customPurpose = '';
  }

  calculateEmi(): number {
    if (!this.amount || this.amount <= 0) return 0;
    const r = 12 / (12 * 100);
    const n = 12;
    return Math.round(
      (this.amount * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1) * 100
    ) / 100;
  }

  calculateTotal(): number {
    return Math.round(this.calculateEmi() * 12 * 100) / 100;
  }

  onSubmit(): void {
    if (!this.memberId || !this.amount || !this.purpose) {
      this.error = 'Please fill all required fields'; return;
    }
    if (this.purpose === 'Other' && !this.customPurpose) {
      this.error = 'Please describe the loan purpose'; return;
    }

    this.loading = true;
    this.error   = '';

    this.api.applyLoan({
      memberId:      this.memberId,
      amount:        this.amount,
      purpose:       this.purpose,
      customPurpose: this.purpose === 'Other' ? this.customPurpose : null
    }).subscribe({
      next: data => {
        this.loading       = false;
        this.success       = true;
        this.createdLoanId = data.loanId;
      },
      error: err => {
        this.loading = false;
        this.error   = this.parseError(err, 'Submission failed. Please try again.');
      }
    });
  }

  resetForm(): void {
    this.success       = false;
    this.memberId      = null;
    this.amount        = 0;
    this.purpose       = '';
    this.createdLoanId = null;
  }

  parseError(err: any, fallback: string): string {
    try {
      if (typeof err.error === 'string' && err.error.length > 0) {
        try { 
          const p = JSON.parse(err.error);
          return p.message || err.error;
         }
        catch { return err.error; }
      }
      if (err.error?.message) return err.error.message;
      if (err.status === 404) return 'Member not found. Please check the Member ID.';
      if (err.status === 403) return 'Access denied.';
    } catch { }
    return fallback;
  }
}