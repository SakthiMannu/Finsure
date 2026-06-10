import { Component, OnInit, HostListener, OnDestroy } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { ApiService } from '../../../core/services/api.service';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './layout.component.html'
})
export class LayoutComponent implements OnInit,OnDestroy {
  notifications: any[] = [];
  tasks:         any[] = [];
  showNotifications = false;
  showTasks         = false;
  sidebarOpen       = false;
  private pollingInterval: Subscription | null = null; 

  get unreadCount():     number { return this.notifications.filter(n => n.status === 'UNREAD').length; }
  get pendingTaskCount(): number { return this.tasks.filter(t => t.status === 'PENDING').length; }

  constructor(
    public  auth:   AuthService,
    private api:    ApiService,
    private router: Router

  ) {}

  ngOnInit(): void {
    this.loadNotifications();
    this.loadTasks();
    this.startPolling();
  }

  
startPolling(): void {
  this.pollingInterval = interval(10000).subscribe(() => {
    this.loadNotifications();
    this.loadTasks();
  });
}
ngOnDestroy(): void {
    if (this.pollingInterval) {
      this.pollingInterval.unsubscribe();
    }
  }
 loadNotifications(): void {
  if (!this.auth.hasRole('MEMBER')) return;
  this.api.getNotifications().subscribe({
    next:  data => this.notifications = data || [],
    error: ()   => {}
  });
}

  loadTasks(): void {
    if (!this.auth.hasRole('LOAN_OFFICER', 'ADMIN')) return;
    const userId = parseInt(localStorage.getItem('userId') || '1');
    this.api.getTasksByUser(userId).subscribe({
      next:  data => this.tasks = (data || []).map((t: any) => ({ ...t, expanded: false })),
      error: ()   => {}
    });
  }

  completeTask(task: any, event: Event): void {
    event.stopPropagation();
    this.api.updateTask(task.taskId, { ...task, status: 'COMPLETED' }).subscribe({
      next:  () => task.status = 'COMPLETED',
      error: () => {}
    });
  }

  toggleNotifications(event: Event): void {
    event.stopPropagation();
    this.showNotifications = !this.showNotifications;
    this.showTasks = false;
  }

  toggleTasks(event: Event): void {
    event.stopPropagation();
    this.showTasks = !this.showTasks;
    this.showNotifications = false;
  }
toggleSidebar(): void {
   this.sidebarOpen = !this.sidebarOpen;
   }  

  closeSidebar(): void  { 
    this.sidebarOpen = false; 
  }

  markRead(notification: any): void {
    if (notification.status !== 'UNREAD') return;
    this.api.markNotificationRead(notification.notificationId).subscribe({
      next:  () => notification.status = 'READ',
      error: () => {}
    });
  }

  @HostListener('document:click')
  closeDropdowns(): void {
    this.showNotifications = false;
    this.showTasks = false;
  }

  hasRole(...roles: string[]): boolean { 
    return this.auth.hasRole(...roles); 
  }

  logout(): void {
     this.auth.logout();
     }

  getPageTitle(): string {
    const url = this.router.url;
    if (url.includes('dashboard'))              return 'Dashboard';
    if (url.includes('members/new'))            return 'Register Member';
    if (url.includes('members/edit'))           return 'Edit Member';
    if (url.includes('members') &&
        url.includes('account'))                return 'Account Details';
    if (url.includes('members'))                return 'Member Registry';
    if (url.includes('loans/apply'))            return 'Apply for Loan';
    if (url.includes('loans/pending'))          return 'Pending Loan Applications';
    if (url.includes('loans/review'))           return 'Loan Review & Approval';
    if (url.includes('loans'))                  return 'Loan Applications';
    if (url.includes('repayments/history'))     return 'Repayment History';
    if (url.includes('repayments'))             return 'Make Repayment';
    if (url.includes('delinquencies/history'))  return 'Delinquency Records';
    if (url.includes('delinquencies'))          return 'Mark Loan Delinquent';
    if (url.includes('transactions/deposit'))   return 'Deposit Money';
    if (url.includes('transactions/withdraw'))  return 'Withdraw Money';
    if (url.includes('transactions/balance'))   return 'Check Balance';
    if (url.includes('transactions/history'))   return 'Transaction History';
    if (url.includes('reports/kpi'))            return 'KPI Dashboard';
    if (url.includes('audit-packages'))         return 'Audit Packages';
    if (url.includes('reports'))                return 'Reports Hub';
    if (url.includes('users'))                  return 'User Management';
    if (url.includes('audit-logs'))             return 'Audit Logs';
    return 'FinSure';
  }
}