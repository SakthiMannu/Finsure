import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-loan-review',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './loan-review.component.html'
})
export class LoanReviewComponent implements OnInit {
  loan:            any    = null;
  documents:       any[]  = [];
  loading          = false;
  error            = '';
  success          = false;
  successMsg       = '';
  approvedBy       = '';
  rejectReason     = '';
  showRejectReason = false;
  showUpload       = false;
  loanId:          number = 0;
  selectedFileName = '';
  docUri           = '';

  constructor(
    private api:   ApiService,
    public  auth:  AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loanId     = +this.route.snapshot.params['id'];
    this.approvedBy = this.auth.getDisplayName();
    this.loadLoan();
    this.loadDocuments();
  }

  loadLoan(): void {
    this.api.getLoanById(this.loanId).subscribe({
      next:  d  => this.loan  = d,
      error: () => this.error = 'Failed to load loan details'
    });
  }

  loadDocuments(): void {
    this.api.getDocumentsByLoan(this.loanId).subscribe({
      next:  d  => this.documents = d || [],
      error: () => this.documents = []   
    });
  }

  approveLoan(): void {
    
    if (!this.approvedBy) { 
      this.error = 'Please enter who is approving this loan';
       return; 
    }
    this.loading = true;
    this.error   = '';

    this.api.approveLoan(this.loanId, this.approvedBy).subscribe({
      next: (res: any) => {
        this.loading    = false;
        this.successMsg = typeof res === 'string' ? res : (res?.message || `Loan #${this.loanId} approved`);
        this.success    = true;
        this.loadLoan();
        setTimeout(() => this.router.navigate(['/app/loans/pending']), 2000);
      },
     error: err => {
    this.loading = false;
    let msg = 'Approval failed. Please try again.';
    try {
        if (typeof err.error === 'string') {
            const parsed = JSON.parse(err.error);
            msg = parsed.message || msg;
        } else if (err.error?.message) {
            msg = err.error.message;
        }
    } catch {
        msg = err.error || msg;
    }
    this.error = msg;
}
    });
  }

  onFileSelected(event: any): void {
  const file = event.target.files[0];
  if (file) {
    
    if (file.type !== 'application/pdf') {
      this.error = 'Only PDF files are allowed';
      return;
    }
    this.selectedFileName = file.name;
    this.docUri = `docs/loan${this.loanId}/${file.name}`;
    
  }
}
  toggleReject(): void {
    if (!this.showRejectReason) { this.showRejectReason = true; return; }
    if (!this.approvedBy || !this.rejectReason) {
      this.error = 'Please enter officer name and rejection reason'; return;
    }
    this.loading = true;
    this.error   = '';

    this.api.rejectLoan(this.loanId, this.approvedBy, this.rejectReason).subscribe({
      next: () => {
        this.loading    = false;
        this.success    = true;
        this.successMsg = `Loan #${this.loanId} rejected.`;
        this.loadLoan();
        setTimeout(() => this.router.navigate(['/app/loans/pending']), 2000);
      },
      error: err => {
    this.loading = false;
    let msg = 'Approval failed. Please try again.';
    try {
        if (typeof err.error === 'string') {
            const parsed = JSON.parse(err.error);
            msg = parsed.message || msg;
        } else if (err.error?.message) {
            msg = err.error.message;
        }
    } catch {
        msg = err.error || msg;
    }
    this.error = msg;
}
    });
  }

  uploadDocument(): void {
    if (!this.docUri) { this.error = 'Please enter the document URI'; return; }
    this.api.uploadDocument({ fileURI: this.docUri, loanId: this.loanId }).subscribe({
      next:  () => { this.showUpload = false; this.docUri = ''; this.loadDocuments(); },
      error: err => this.error = err.error?.message || 'Upload failed'
    });
  }

  
  
  loanRows(): { label: string; value: string | null; color?: string }[] {
    if (!this.loan) return [];
    return [
      { label: 'Loan ID',           value: `#${this.loan.loanId}` },
      { label: 'Member ID',         value: `#${this.loan.memberId}` },
      { label: 'Amount',            value: `₹${this.loan.amount?.toLocaleString('en-IN', { minimumFractionDigits: 2 })}`, color: 'var(--primary)' },
      { label: 'Purpose',           value: this.loan.purpose },
      { label: 'Specified Purpose', value: this.loan.customPurpose || null },
      { label: 'Applied On',        value: this.loan.submittedAt },
      { label: 'Processed By',      value: this.loan.approvedBy || null },
      { label: 'Next Due Date',     value: this.loan.nextDueDate || null, color: 'var(--warning)' }
    ];
  }

  hasRole(...roles: string[]): boolean { return this.auth.hasRole(...roles); }
}