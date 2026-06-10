import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-kpi',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './kpi.component.html'
})
export class KpiComponent implements OnInit {
  kpis:        any[] = [];
  filtered:    any[] = [];
  filter       = '';
  loading      = false;
  showForm     = false;
  showCalcMenu = false;
  saving       = false;
  editId:      number | null = null;
  formError    = '';
  calcMessage  = '';
  form = { name: '', definition: '', category: 'LOANS', target: 0, currentValue: 0 };

  constructor(private api: ApiService, public auth: AuthService) {}

  ngOnInit(): void { this.loadKpis(); }

  loadKpis(): void {
    this.loading = true;
    this.api.getAllKpis().subscribe({
      next:  d  => { this.kpis = d; this.filtered = d; this.loading = false; },
      error: () => this.loading = false
    });
  }

  calculateKpi(category: string): void {
    this.calcMessage = `Calculating ${category}...`;
    this.api.calculateKpi(category).subscribe({
      next: () => {
        this.calcMessage = `${category} KPI calculated successfully`;
        this.loadKpis();
        setTimeout(() => { this.calcMessage = ''; this.showCalcMenu = false; }, 2000);
      },
      error: err => {
        const msg = err.error?.message || err.error;
        this.calcMessage = typeof msg === 'string' ? msg : `Failed to calculate ${category} KPI`;
      }
    });
  }

  setFilter(f: string): void {
    this.filter   = f;
    this.filtered = f ? this.kpis.filter(k => k.category === f) : this.kpis;
  }

  countByStatus(status: string): number {
    return this.filtered.filter(k => this.getStatus(k) === status).length;
  }

  getProgress(k: any): number {
    if (!k.target || k.target === 0) return 0;
    return Math.min(100, Math.round((k.currentValue / k.target) * 100));
  }

  getProgressColor(k: any): string {
    const p = this.getProgress(k);
    if (p >= 90) return 'var(--success)';
    if (p >= 70) return 'var(--warning)';
    return 'var(--danger)';
  }

  getStatus(k: any): string {
    const p = this.getProgress(k);
    if (p >= 90) return 'On Target';
    if (p >= 70) return 'Below Target';
    return 'Critical';
  }

  editKpi(k: any): void {
    this.editId   = k.kpiId;
    this.form     = { name: k.name, definition: k.definition, category: k.category, target: k.target, currentValue: k.currentValue };
    this.showForm = true;
    this.formError = '';
  }

  saveKpi(): void {
    if (!this.form.name) { this.formError = 'KPI name is required'; return; }
    this.saving    = true;
    this.formError = '';
    this.api.updateKpi(this.editId!, this.form).subscribe({
      next:  () => { this.saving = false; this.cancelForm(); this.loadKpis(); },
      error: err => { this.saving = false; this.formError = err.error?.message || 'Update failed'; }
    });
  }

  deleteKpi(id: number): void {
    if (!confirm('Delete this KPI? This cannot be undone.')) return;
    this.api.deleteKpi(id).subscribe({ next: () => this.loadKpis(), error: () => {} });
  }

  cancelForm(): void {
    this.showForm  = false; this.editId = null; this.formError = '';
    this.form      = { name: '', definition: '', category: 'LOANS', target: 0, currentValue: 0 };
  }

  hasRole(...roles: string[]): boolean { return this.auth.hasRole(...roles); }
}