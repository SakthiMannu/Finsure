import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-account-details',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './account-details.component.html'
})
export class AccountDetailsComponent implements OnInit {
  member:            any    = null;
  accounts:          any[]  = [];
  loans:             any[]  = [];
  loading            = false;
  showCreateAccount  = false;
  newAccountType     = 'SAVINGS';
  newAccountBalance  = 0;
  memberId:          number = 0;

  constructor(
    private api:   ApiService,
    public  auth:  AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.memberId = +this.route.snapshot.params['id'];
    this.api.getMemberById(this.memberId).subscribe({
      next: d => this.member = d, error: () => {}
    });
    this.loadAccounts();
    this.api.getLoansByMember(this.memberId).subscribe({
      next: d => this.loans = d, error: () => {}
    });
  }

  loadAccounts(): void {
    this.api.getAccountsByMember(this.memberId).subscribe({
      next: d => this.accounts = d, error: () => {}
    });
  }

  createAccount(): void {

    if (this.newAccountBalance < 100) {
        alert('Opening balance must be at least Rs. 100');
    return; 
  }

    const today = new Date().toISOString().split('T')[0];
    this.api.createAccount({
      type: this.newAccountType,
      balance: this.newAccountBalance,
      createdAt: today,
      memberId: this.memberId
    }).subscribe({
      next:  () => { this.showCreateAccount = false; this.loadAccounts(); },
      error: err => alert(err.error?.message || 'Failed to create account')
    });
  }

  deleteAccount(id: number): void {
    if (!confirm('Delete this account?')) return;
    this.api.deleteAccount(id).subscribe({
      next:  () => this.loadAccounts(),
      error: ()  => alert('Failed to delete')
    });
  }

  hasRole(...roles: string[]): boolean { return this.auth.hasRole(...roles); }
}