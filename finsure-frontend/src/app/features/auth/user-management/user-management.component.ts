import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-management.component.html'
})
export class UserManagementComponent implements OnInit {
  users:    any[] = [];
  filtered: any[] = [];
  roleFilter     = '';
  loading        = false;
  success        = '';
  error          = '';
  showCreateForm = false;
  createSuccess  = '';
  createError    = '';
  creating       = false;
  newUser        = { name: '', email: '', phone: '', role: 'TELLER' };

  constructor(private api: ApiService, public auth: AuthService) {}

  ngOnInit(): void { this.loadUsers(); }

  loadUsers(): void {
    this.loading = true;
    this.api.getAllUsers().subscribe({
      next:  d   => { this.users = d.map((u: any) => ({ ...u, _newRole: '' })); this.filtered = this.users; this.loading = false; },
      error: err => { this.error = err.error?.message || 'Failed to load users'; this.loading = false; }
    });
  }

  filterUsers(): void {
    this.filtered = this.roleFilter
        ? this.users.filter(u => u.role === this.roleFilter)
        : this.users;
  }

  get previewPassword(): string {
    if (!this.newUser.name || !this.newUser.phone) return '';
    return this.newUser.name.substring(0, 3) + this.newUser.phone.slice(-3);
  }

  createUser(): void {
    this.createError = '';
    if (!this.newUser.name || !this.newUser.email || !this.newUser.phone || !this.newUser.role) {
      this.createError = 'All fields are required'; return;
    }
    if (this.newUser.phone.length !== 10) {
      this.createError = 'Phone must be exactly 10 digits'; return;
    }
    this.creating = true;
    this.api.createUser(this.newUser).subscribe({
      next: (res: any) => {
        this.creating      = false;
        this.createSuccess = typeof res === 'string' ? res : (res.message || 'User created successfully');
        this.showCreateForm = false;
        this.newUser        = { name: '', email: '', phone: '', role: 'TELLER' };
        this.loadUsers();
        setTimeout(() => this.createSuccess = '', 15000);
      },
      error: err => {
        this.creating    = false;
        this.createError = err.error?.message || err.error || 'Failed to create user.';
      }
    });
  }

  changeRole(user: any): void {
    if (!user._newRole || user._newRole === user.role) return;
    const newRole = user._newRole; user._newRole = '';
    this.api.updateUserRole(user.userId, newRole).subscribe({
      next:  () => { user.role = newRole; this.flash('success', `Role updated to ${newRole} for ${user.name}`); },
      error: err => this.flash('error', err.error?.message || 'Role update failed')
    });
  }

  toggleStatus(user: any): void {
    const newStatus = user.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    this.api.updateUserStatus(user.userId, newStatus).subscribe({
      next:  () => { user.status = newStatus; this.flash('success', `${user.name} is now ${newStatus}`); },
      error: err => this.flash('error', err.error?.message || 'Status update failed')
    });
  }

  deleteUser(id: number): void {
    if (!confirm('Delete this user permanently?')) return;
    this.api.deleteUser(id).subscribe({
      next: () => {
        this.users    = this.users.filter(u => u.userId !== id);
        this.filtered = this.filtered.filter(u => u.userId !== id);
        this.flash('success', 'User deleted successfully');
      },
      error: err => this.flash('error', err.error?.message || 'Delete failed')
    });
  }

  private flash(type: 'success' | 'error', msg: string): void {
    if (type === 'success') { this.success = msg; setTimeout(() => this.success = '', 3000); }
    else                    { this.error   = msg; setTimeout(() => this.error   = '', 3000); }
  }
}