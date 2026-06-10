import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../../core/services/api.service';

@Component({
  selector: 'app-member-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './member-form.component.html'
})
export class MemberFormComponent implements OnInit {
  name        = '';
  dob         = '';
  contactInfo = '';
  loading     = false;
  error       = '';
  success     = false;
  isEdit      = false;
  memberId:   number | null = null;

  constructor(
    private api:    ApiService,
    private router: Router,
    private route:  ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id     = this.route.snapshot.params['id'];
    this.memberId = id ? +id : null;
    this.isEdit   = !!this.memberId;

    if (this.isEdit && this.memberId) {
      this.api.getMemberById(this.memberId).subscribe({
        next:  data => { this.name = data.name; this.dob = data.dob; this.contactInfo = data.contactInfo; },
        error: ()   => this.error = 'Failed to load member details'
      });
    }
  }

  onSubmit(): void {
    if (this.contactInfo.length !== 10) { this.error = 'Contact must be 10 digits'; return; }
    this.loading = true;
    this.error   = '';

    const payload = { name: this.name, dob: this.dob, contactInfo: this.contactInfo };
    const obs     = this.isEdit && this.memberId
        ? this.api.updateMember(this.memberId, payload)
        : this.api.createMember(payload);

    obs.subscribe({
      next:  () => { this.loading = false; this.success = true; setTimeout(() => this.router.navigate(['/app/members']), 1500); },
      error: err => { this.loading = false; this.error = err.error?.message || 'Operation failed'; }
    });
  }
}