import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-audit-packages',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './audit-packages.component.html'
})
export class AuditPackagesComponent implements OnInit {
  tab          = 0;
  periodStart  = '';
  periodEnd    = '';
  generating   = false;
  genSuccess   = '';
  genError     = '';
  packages:    any[] = [];
  loading      = false;
  selectedPackage: any = null;

  constructor(private api: ApiService, public auth: AuthService) {}

  ngOnInit(): void {
    if (!this.hasRole('AUDITOR', 'ADMIN')) this.tab = 1;
    this.loadPackages();
  }

  generatePackage(): void {
    if (!this.periodStart || !this.periodEnd) {
      this.genError = 'Please select both period start and end dates'; return;
    }
    if (this.periodEnd <= this.periodStart) {
      this.genError = 'Period End must be after Period Start'; return;
    }
    this.generating = true; this.genSuccess = ''; this.genError = '';

    const start = this.periodStart.length === 16 ? this.periodStart + ':00' : this.periodStart;
    const end   = this.periodEnd.length   === 16 ? this.periodEnd   + ':00' : this.periodEnd;

    this.api.generateAuditPackage(start, end).subscribe({
      next: data => {
        this.generating = false;
        this.genSuccess = `Audit Package #${data.packageId} generated successfully`;
        this.loadPackages();
        setTimeout(() => { this.tab = 1; this.genSuccess = ''; }, 1500);
      },
      error: err => {
        this.generating = false;
        let msg = '';
        if (typeof err.error === 'string' && err.error.length > 0) {
          try { const p = JSON.parse(err.error); msg = p.message || err.error; }
          catch { msg = err.error; }
        } else { msg = err.error?.message || 'Generation failed'; }
        this.genError = msg;
      }
    });
  }

  loadPackages(): void {
    this.loading = true;
    this.api.getAllAuditPackages().subscribe({
      next:  d  => { this.packages = d; this.loading = false; },
      error: () => this.loading = false
    });
  }

  deletePackage(id: number, event: Event): void {
    event.stopPropagation();
    if (!confirm('Delete this audit package permanently?')) return;
    this.api.deleteAuditPackage(id).subscribe({
      next: () => {
        if (this.selectedPackage?.packageId === id) this.selectedPackage = null;
        this.loadPackages();
      },
      error: () => {}
    });
  }

  formatContents(json: string): { key: string; value: string }[] {
    if (!json) return [];
    try {
      return Object.entries(JSON.parse(json)).map(([k, v]) => ({
        key:   k.replace(/([A-Z])/g, ' $1').replace(/_/g, ' ').replace(/^./, s => s.toUpperCase()),
        value: typeof v === 'object' ? JSON.stringify(v, null, 2) : String(v)
      }));
    } catch { return [{ key: 'Data', value: json }]; }
  }

  downloadAuditPdf(pkg: any): void {
    const rows = this.formatContents(pkg.contentsJSON).map(m => `
      <tr>
        <td style="padding:10px;border-bottom:1px solid #CFD8DC;font-weight:500;width:40%">${m.key}</td>
        <td style="padding:10px;border-bottom:1px solid #CFD8DC;font-family:monospace;font-size:12px">${m.value}</td>
      </tr>`).join('');

    const win = window.open('', '_blank');
    if (!win) { alert('Please allow popups to download PDF'); return; }
    win.document.write(`<!DOCTYPE html><html><head>
      <title>FinSure Audit Package #${pkg.packageId}</title>
      <style>
        body{font-family:Arial,sans-serif;padding:40px;color:#263238}
        .header{border-bottom:2px solid #1565C0;padding-bottom:16px;margin-bottom:24px}
        h1{color:#1565C0;margin:0;font-size:22px}
        .meta{color:#546E7A;font-size:14px;margin-bottom:24px}
        table{width:100%;border-collapse:collapse}
        th{background:#1565C0;color:white;padding:12px;text-align:left}
        .footer{margin-top:32px;text-align:center;color:#90A4AE;font-size:12px;
                border-top:1px solid #CFD8DC;padding-top:16px}
        @media print{button{display:none}}
      </style></head><body>
      <div class="header"><h1>FinSure — Audit Package #${pkg.packageId}</h1></div>
      <div class="meta">
        <p>Period: <strong>${new Date(pkg.periodStart).toLocaleDateString()} → ${new Date(pkg.periodEnd).toLocaleDateString()}</strong></p>
        <p>Generated at: <strong>${new Date(pkg.generatedAt).toLocaleString()}</strong></p>
      </div>
      <table><thead><tr><th>Category</th><th>Data</th></tr></thead>
      <tbody>${rows}</tbody></table>
      <div class="footer">FinSure Cooperative Banking System — Confidential Audit Record</div>
      </body></html>`);
    win.document.close();
    setTimeout(() => win.print(), 500);
  }

  hasRole(...roles: string[]): boolean { return this.auth.hasRole(...roles); }
}