import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-delinquency-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './delinquency-detail.component.html'
})
export class DelinquencyDetailComponent implements OnInit {
  record:            any     = null;
  loading            = false;
  showEdit           = false;
  showDeleteConfirm  = false;
  editStatus         = '';
  editNotes          = '';
  error              = '';
  deleteSuccess      = false;

  constructor(
    private api:   ApiService,
    public  auth:  AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.params['id'];
    this.loading = true;
    this.api.getDelinquencyById(id).subscribe({
      next:  d  => { this.record = d; this.editStatus = d.status; this.editNotes = d.notes; this.loading = false; },
      error: () => this.loading = false
    });
  }

  updateRecord(): void {
    this.api.updateDelinquency(this.record.delinqId,
      { status: this.editStatus, notes: this.editNotes }
    ).subscribe({
      next:  d   => { this.record = d; this.showEdit = false; },
      error: err => this.error = err.error?.message || 'Update failed'
    });
  }

  confirmDelete(): void {
    this.api.deleteDelinquency(this.record.delinqId).subscribe({
      next:  () => { this.deleteSuccess = true; this.showDeleteConfirm = false; },
      error: err => this.error = err.error?.message || 'Delete failed'
    });
  }

  hasRole(...roles: string[]): boolean { return this.auth.hasRole(...roles); }
}