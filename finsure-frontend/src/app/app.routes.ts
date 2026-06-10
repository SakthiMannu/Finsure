import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { ForgotPasswordComponent } from './features/auth/forgot-password/forgot-password.component';

export const routes: Routes = [
  { path: '', 
    redirectTo: '/login', 
    pathMatch: 'full' 
  },
  {
    path: 'login',
    component:LoginComponent
  },
  {
    path: 'forgot-password',
    component:ForgotPasswordComponent
  },
  {
    path: 'app',
    loadComponent: () => import('./shared/components/layout/layout.component')
        .then(m => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard.component')
            .then(m => m.DashboardComponent)
      },

      
      {
        path: 'members/new',
        canActivate: [roleGuard('TELLER','ADMIN')],
        loadComponent: () => import('./features/members/member-form/member-form.component')
            .then(m => m.MemberFormComponent)
      },
      {
        path: 'members/edit/:id',
        canActivate: [roleGuard('TELLER','ADMIN')],
        loadComponent: () => import('./features/members/member-form/member-form.component')
            .then(m => m.MemberFormComponent)
      },
      {
        path: 'members/:id/account',
        loadComponent: () => import('./features/members/account-details/account-details.component')
            .then(m => m.AccountDetailsComponent)
      },
      {
        path: 'members',
        canActivate: [roleGuard('TELLER','ADMIN','LOAN_OFFICER',
                                'BRANCH_MANAGER','AUDITOR')],
        loadComponent: () => import('./features/members/member-list/member-list.component')
            .then(m => m.MemberListComponent)
      },

      
      
      
      {
        path: 'loans/apply',
        canActivate: [roleGuard('MEMBER','LOAN_OFFICER','ADMIN')],
        loadComponent: () => import('./features/loans/loan-form/loan-form.component')
            .then(m => m.LoanFormComponent)
      },
      {
        path: 'loans/pending',
        canActivate: [roleGuard('LOAN_OFFICER','BRANCH_MANAGER','ADMIN','AUDITOR')],
        loadComponent: () => import('./features/loans/loan-pending/loan-pending.component')
            .then(m => m.LoanPendingComponent)
      },
      {
    path: 'loans/review/:id',
    canActivate: [roleGuard('LOAN_OFFICER','BRANCH_MANAGER',
                            'ADMIN','AUDITOR','MEMBER')],
    loadComponent: () => import('./features/loans/loan-review/loan-review.component')
        .then(m => m.LoanReviewComponent)
},
      {
        path: 'loans',
        canActivate: [roleGuard('LOAN_OFFICER','BRANCH_MANAGER','ADMIN','AUDITOR')],
        loadComponent: () => import('./features/loans/loan-list/loan-list.component')
            .then(m => m.LoanListComponent)
      },

      
      {
        path: 'repayments/history',
        canActivate: [roleGuard('LOAN_OFFICER','BRANCH_MANAGER','ADMIN','AUDITOR')],
        loadComponent: () => import('./features/repayments/repayment-history/repayment-history.component')
            .then(m => m.RepaymentHistoryComponent)
      },
      {
        path: 'repayments/:id',
        loadComponent: () => import('./features/repayments/repayment-detail/repayment-detail.component')
            .then(m => m.RepaymentDetailComponent)
      },
      {
        path: 'repayments',
        canActivate: [roleGuard('LOAN_OFFICER','ADMIN')],
        loadComponent: () => import('./features/repayments/repayment-form/repayment-form.component')
            .then(m => m.RepaymentFormComponent)
      },

      
      {
        path: 'delinquencies/history',
        canActivate: [roleGuard('LOAN_OFFICER','BRANCH_MANAGER','ADMIN','AUDITOR')],
        loadComponent: () => import('./features/repayments/delinquency-history/delinquency-history.component')
            .then(m => m.DelinquencyHistoryComponent)
      },
      {
        path: 'delinquencies/:id',
        canActivate: [roleGuard('LOAN_OFFICER','BRANCH_MANAGER','ADMIN','AUDITOR')],
        loadComponent: () => import('./features/repayments/delinquency-detail/delinquency-detail.component')
            .then(m => m.DelinquencyDetailComponent)
      },
      {
        path: 'delinquencies',
        canActivate: [roleGuard('LOAN_OFFICER','ADMIN')],
        loadComponent: () => import('./features/repayments/delinquency-form/delinquency-form.component')
            .then(m => m.DelinquencyFormComponent)
      },

      
      
      {
        path: 'transactions/deposit',
        canActivate: [roleGuard('TELLER','ADMIN')],
        loadComponent: () => import('./features/transactions/deposit/deposit.component')
            .then(m => m.DepositComponent)
      },
      {
        path: 'transactions/withdraw',
        canActivate: [roleGuard('TELLER','ADMIN')],
        loadComponent: () => import('./features/transactions/withdraw/withdraw.component')
            .then(m => m.WithdrawComponent)
      },
      {
        path: 'transactions/balance',
        canActivate: [roleGuard('TELLER','ADMIN','BRANCH_MANAGER','MEMBER')],
        loadComponent: () => import('./features/transactions/balance/balance.component')
            .then(m => m.BalanceComponent)
      },
      {
        path: 'transactions/history',
        canActivate: [roleGuard('TELLER','ADMIN','BRANCH_MANAGER',
                                'AUDITOR','MEMBER')],
        loadComponent: () => import('./features/transactions/history/history.component')
            .then(m => m.HistoryComponent)
      },

      
      {
        path: 'reports/kpi',
        canActivate: [roleGuard('BRANCH_MANAGER','ADMIN','AUDITOR','LOAN_OFFICER')],
        loadComponent: () => import('./features/reporting/kpi/kpi.component')
            .then(m => m.KpiComponent)
      },
      {
        path: 'reports',
        canActivate: [roleGuard('BRANCH_MANAGER','ADMIN','AUDITOR')],
        loadComponent: () => import('./features/reporting/reports/reports.component')
            .then(m => m.ReportsComponent)
      },
      {
        path: 'audit-packages',
        canActivate: [roleGuard('AUDITOR','ADMIN','BRANCH_MANAGER')],
        loadComponent: () => import('./features/reporting/audit-packages/audit-packages.component')
            .then(m => m.AuditPackagesComponent)
      },

      
      {
        path: 'users',
        canActivate: [roleGuard('ADMIN')],
        loadComponent: () => import('./features/auth/user-management/user-management.component')
            .then(m => m.UserManagementComponent)
      },
      {
        path: 'audit-logs',
        canActivate: [roleGuard('ADMIN','AUDITOR')],
        loadComponent: () => import('./features/auth/audit-logs/audit-logs.component')
            .then(m => m.AuditLogsComponent)
      }
    ]
  },
  { path: '**', redirectTo: '/login' }
];