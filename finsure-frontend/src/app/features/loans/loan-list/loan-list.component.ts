import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-loan-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './loan-list.component.html'
})
export class LoanListComponent implements OnInit {
  loans:        any[] = [];
  filtered:     any[] = [];
  filterStatus  = '';
  loading       = false;
  error         = '';

  constructor(private api: ApiService, public auth: AuthService) {}

  ngOnInit(): void {
    this.loading = true;
    this.api.getAllLoans().subscribe({
      next:  data => { 
        this.loans = data;
        this.filtered = data;
        this.loading = false;
       },
      error: err  => { 
        this.error = err.error?.message || 'Failed to load loans';
        this.loading = false; }
    });
  }

  setFilter(status: string): void {
    this.filterStatus = status;
    this.filtered = status ? this.loans.filter(l => l.status === status) : this.loans;
  }

  countByStatus(s: string): number {
     return this.loans.filter(l => l.status === s).length;
     }
     
  hasRole(...roles: string[]): boolean { return this.auth.hasRole(...roles); }
}