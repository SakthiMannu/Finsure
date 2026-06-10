import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-audit-logs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './audit-logs.component.html'
})
export class AuditLogsComponent implements OnInit {
  logs:    any[] = [];
  loading  = false;
  error    = '';
  mode     = 'all';
  userId:  number | null = null;
  action   = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void { this.loadAll(); }

  loadAll(): void {
    this.loading = true; this.mode = 'all';
    this.api.getAllAuditLogs().subscribe({
      next:  d   => { this.logs = d; this.loading = false; },
      error: err => { this.error = err.error?.message || 'Failed'; this.loading = false; }
    });
  }

  loadByUser(): void {
    if (!this.userId) return;
    this.loading = true; this.mode = 'user';
    this.api.getAuditLogsByUser(this.userId).subscribe({
      next:  d  => { this.logs = d; this.loading = false; },
      error: () => { this.logs = []; this.loading = false; }
    });
  }

  loadByAction(): void {
    if (!this.action) return;
    this.loading = true; this.mode = 'action';
    this.api.getAuditLogsByAction(this.action).subscribe({
      next:  d  => { this.logs = d; this.loading = false; },
      error: () => { this.logs = []; this.loading = false; }
    });
  }
}