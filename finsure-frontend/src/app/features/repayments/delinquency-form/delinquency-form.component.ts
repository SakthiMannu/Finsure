import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-delinquency-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './delinquency-form.component.html'
})
export class DelinquencyFormComponent {
  loanId:     number | null = null;
  notes       = '';
  loading     = false;
  error       = '';
  success     = false;
  lastLoanId: number = 0;

  constructor(private api: ApiService) {}

  onSubmit(): void {
    if (!this.loanId || !this.notes) { this.error = 'Please fill all fields'; return; }
    this.loading = true; this.error = ''; this.success = false;
    this.api.createDelinquency(this.loanId, { status: 'DELINQUENT', notes: this.notes }).subscribe({
      next: () => {
        this.loading = false; this.success = true;
        this.lastLoanId = this.loanId!; this.notes = ''; this.loanId = null;
      },
      error: err => { this.loading = false; this.error = err.error?.message || 'Failed'; }
    });
  }
}