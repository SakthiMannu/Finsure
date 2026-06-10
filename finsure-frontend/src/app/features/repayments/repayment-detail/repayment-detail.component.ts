import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-repayment-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './repayment-detail.component.html'
})
export class RepaymentDetailComponent implements OnInit {
  repayment:   any    = null;
  loading      = false;
  showEdit     = false;
  editAmount   = 0;
  editMethod   = 'ONLINE';
  editStatus   = 'PAID';
  editError    = '';
  editSuccess  = false;

  constructor(
    private api:   ApiService,
    public  auth:  AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.params['id'];
    this.loading = true;
    this.api.getRepaymentById(id).subscribe({
      next: d => {
        this.repayment  = d; this.editAmount = d.amount;
        this.editMethod = d.method; this.editStatus = d.status;
        this.loading    = false;
      },
      error: () => this.loading = false
    });
  }

  toggleEdit(): void { this.showEdit = !this.showEdit; this.editError = ''; this.editSuccess = false; }

  updateRepayment(): void {
    this.api.updateRepayment(this.repayment.repaymentId, {
      loanId: this.repayment.loanId, amount: this.editAmount,
      method: this.editMethod, status: this.editStatus
    }).subscribe({
      next:  d   => { this.repayment = d; this.editSuccess = true; this.showEdit = false; },
      error: err => this.editError = err.error?.message || 'Update failed'
    });
  }

  hasRole(...roles: string[]): boolean { return this.auth.hasRole(...roles); }
}