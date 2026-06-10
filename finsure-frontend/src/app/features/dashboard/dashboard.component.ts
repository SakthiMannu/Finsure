import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ApiService } from '../../core/services/api.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  memberCount       = 0;
  loanCount         = 0;
  pendingLoanCount  = 0;
  approvedLoanCount = 0;

  constructor(public auth: AuthService, private api: ApiService) {}

  get timeGreeting(): string {
    const h = new Date().getHours();
    if (h < 12) return 'Morning';
    if (h < 17) return 'Afternoon';
    return 'Evening';
  }

  ngOnInit(): void {
    if (this.hasRole('TELLER','ADMIN','BRANCH_MANAGER','AUDITOR','LOAN_OFFICER')) {
      this.api.getAllMembers().subscribe({
        next:  d  => this.memberCount = d.length,
        error: () => {}
      });
    }
    if (this.hasRole('LOAN_OFFICER','ADMIN','BRANCH_MANAGER','AUDITOR')) {
      this.api.getAllLoans().subscribe({
        next: d => {
          this.loanCount         = d.length;
          this.pendingLoanCount  = d.filter((l: any) => l.status === 'PENDING').length;
          this.approvedLoanCount = d.filter((l: any) => l.status === 'APPROVED').length;
        },
        error: () => {}
      });
    }
  }

  hasRole(...roles: string[]): boolean {
     return this.auth.hasRole(...roles); 
    }
}