import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-loan-pending',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './loan-pending.component.html'
})
export class LoanPendingComponent implements OnInit {
  loans:   any[] = [];
  loading  = false;

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.loading = true;
    this.api.getAllLoans().subscribe({
      next:  data => { this.loans = data.filter((l: any) => l.status === 'PENDING'); this.loading = false; },
      error: ()   => this.loading = false
    });
  }
}