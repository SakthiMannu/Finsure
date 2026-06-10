import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-member-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './member-list.component.html'
})
export class MemberListComponent implements OnInit {
  members:    any[] = [];
  filtered:   any[] = [];
  searchTerm  = '';
  loading     = false;
  error       = '';

  constructor(private api: ApiService, public auth: AuthService) {}

  ngOnInit(): void {
    this.loading = true;
    this.api.getAllMembers().subscribe({
      next: data => { this.members = data; this.filtered = data; this.loading = false; },
      error: err => {
        this.loading = false;
        if (err.status === 403)
          this.error = 'You do not have permission to view members.';
        else if (err.status === 0 || err.status === 503)
          this.error = 'Member service is currently unavailable.';
        else
          this.error = err.error?.message || 'Failed to load members. Please try again.';
      }
    });
  }

  filter(): void {
    const term = this.searchTerm.toLowerCase();
    this.filtered = this.members.filter(m =>
      m.name.toLowerCase().includes(term)
    );
  }

  hasRole(...roles: string[]): boolean { return this.auth.hasRole(...roles); }
}