import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {

  constructor(private http: HttpClient) {}

  

  
  createUser(data: any): Observable<any> {
    return this.http.post(
      '/auth/register', data,
      { responseType: 'text' as 'json' }
    );
  }

  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>('/users');
  }

  getUserById(id: number): Observable<any> {
    return this.http.get<any>(`/users/${id}`);
  }

  getUsersByRole(role: string): Observable<any[]> {
    return this.http.get<any[]>(`/users/role/${role}`);
  }

  updateUserRole(id: number, role: string): Observable<any> {
    return this.http.put(
      `/users/${id}/role?newRole=${role}`, {},
      { responseType: 'text' as 'json' }
    );
  }

  updateUserStatus(id: number, status: string): Observable<any> {
    return this.http.put(
      `/users/${id}/status?status=${status}`, {},
      { responseType: 'text' as 'json' }
    );
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(
      `/users/${id}`,
      { responseType: 'text' as 'json' }
    );
  }

  
  getAllAuditLogs(): Observable<any[]> {
    return this.http.get<any[]>('/audit');
  }

  getAuditLogsByUser(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`/audit/user/${userId}`);
  }

  getAuditLogsByAction(action: string): Observable<any[]> {
    return this.http.get<any[]>(`/audit/action/${action}`);
  }

  
  createMember(data: any): Observable<any> {
    return this.http.post<any>('/members', data);
  }

  getAllMembers(): Observable<any[]> {
    return this.http.get<any[]>('/members');
  }

  getMemberById(id: number): Observable<any> {
    return this.http.get<any>(`/members/${id}`);
  }

  updateMember(id: number, data: any): Observable<any> {
    return this.http.put(
      `/members/${id}`, data,
      { responseType: 'text' as 'json' }
    );
  }

  deleteMember(id: number): Observable<any> {
    return this.http.delete(
      `/members/${id}`,
      { responseType: 'text' as 'json' }
    );
  }

  getMyMemberId(): Observable<number> {
    return this.http.get<number>('/members/my-member-id');
  }

  
  createAccount(data: any): Observable<any> {
    return this.http.post<any>('/accounts', data);
  }

  getAllAccounts(): Observable<any[]> {
    return this.http.get<any[]>('/accounts');
  }

  getAccountById(id: number): Observable<any> {
    return this.http.get<any>(`/accounts/${id}`);
  }

  getAccountsByMember(memberId: number): Observable<any[]> {
    return this.http.get<any[]>(`/accounts/member/${memberId}`);
  }

  updateAccount(id: number, data: any): Observable<any> {
    return this.http.put<any>(`/accounts/${id}`, data);
  }

  deleteAccount(id: number): Observable<any> {
    return this.http.delete(
      `/accounts/${id}`,
      { responseType: 'text' as 'json' }
    );
  }

  getMyAccounts(): Observable<any[]> {
    return this.http.get<any[]>('/accounts/my-accounts');
  }

  
  applyLoan(data: any): Observable<any> {
    return this.http.post<any>('/loans', data);
  }

  getAllLoans(): Observable<any[]> {
    return this.http.get<any[]>('/loans');
  }

  getLoanById(id: number): Observable<any> {
    return this.http.get<any>(`/loans/${id}`);
  }

  getLoansByMember(memberId: number): Observable<any[]> {
    return this.http.get<any[]>(`/loans/member/${memberId}`);
  }

  updateLoan(id: number, data: any): Observable<any> {
    return this.http.put<any>(`/loans/${id}`, data);
  }

  approveLoan(id: number, approvedBy: string): Observable<any> {
    return this.http.put(
      `/loan-approval/approve/${id}?approvedBy=${encodeURIComponent(approvedBy)}`,
      {},
      { responseType: 'text' as 'json' }
    );
  }

  rejectLoan(id: number, approvedBy: string, reason: string): Observable<any> {
    return this.http.put(
      `/loan-approval/reject/${id}?approvedBy=${encodeURIComponent(approvedBy)}&reason=${encodeURIComponent(reason)}`,
      {},
      { responseType: 'text' as 'json' }
    );
  }

  
  uploadDocument(data: any): Observable<any> {
    return this.http.post<any>('/loans/documents', data);
  }

  getDocumentsByLoan(loanId: number): Observable<any[]> {
    return this.http.get<any[]>(`/loans/documents/loan/${loanId}`);
  }

  deleteDocument(docId: number): Observable<any> {
    return this.http.delete(
      `/loans/documents/${docId}`,
      { responseType: 'text' as 'json' }
    );
  }

  
  makeRepayment(data: any): Observable<any> {
    return this.http.post<any>('/api/repayments', data);
  }

  getRepaymentHistory(loanId: number): Observable<any[]> {
    return this.http.get<any[]>(`/api/repayments/history/${loanId}`);
  }

  getRepaymentById(id: number): Observable<any> {
    return this.http.get<any>(`/api/repayments/${id}`);
  }

  updateRepayment(id: number, data: any): Observable<any> {
    return this.http.put(
      `/api/repayments/${id}`, data,
      { responseType: 'text' as 'json' }
    );
  }

  deleteRepayment(id: number): Observable<any> {
    return this.http.delete(
      `/api/repayments/${id}`,
      { responseType: 'text' as 'json' }
    );
  }

  
  createDelinquency(loanId: number, data: any): Observable<any> {
    return this.http.post<any>(`/api/delinquencies?loanId=${loanId}`, data);
  }

  getDelinquenciesByLoan(loanId: number): Observable<any[]> {
    return this.http.get<any[]>(`/api/delinquencies/loan/${loanId}`);
  }

  getDelinquencyById(id: number): Observable<any> {
    return this.http.get<any>(`/api/delinquencies/${id}`);
  }

  updateDelinquency(id: number, data: any): Observable<any> {
    return this.http.put(
      `/api/delinquencies/${id}`, data,
      { responseType: 'text' as 'json' }
    );
  }

  deleteDelinquency(id: number): Observable<any> {
    return this.http.delete(
      `/api/delinquencies/${id}`,
      { responseType: 'text' as 'json' }
    );
  }

  
  deposit(data: any): Observable<any> {
    return this.http.post<any>('/api/transactions/deposit', data);
  }

  withdraw(data: any): Observable<any> {
    return this.http.post<any>('/api/transactions/withdraw', data);
  }

  getBalance(accountId: number): Observable<any> {
    return this.http.get<any>(`/api/account/balance/${accountId}`);
  }

  getTransactionHistory(accountId: number): Observable<any[]> {
    return this.http.get<any[]>(`/api/transactions/history/${accountId}`);
  }

  
  generateReport(scope: string, generatedBy: string): Observable<any> {
    return this.http.post<any>(
      `/reports/generate?scope=${scope}&generatedBy=${encodeURIComponent(generatedBy)}`,
      {}
    );
  }

  getAllReports(): Observable<any[]> {
    return this.http.get<any[]>('/reports');
  }

  getReportsByScope(scope: string): Observable<any[]> {
    return this.http.get<any[]>(`/reports/filter?scope=${scope}`);
  }

  getReportById(id: number): Observable<any> {
    return this.http.get<any>(`/reports/${id}`);
  }

  deleteReport(id: number): Observable<any> {
    return this.http.delete(
      `/reports/${id}`,
      { responseType: 'text' as 'json' }
    );
  }

  
  createKpi(data: any): Observable<any> {
    return this.http.post<any>('/kpi', data);
  }

  getAllKpis(): Observable<any[]> {
    return this.http.get<any[]>('/kpi');
  }

  getKpisByCategory(category: string): Observable<any[]> {
    return this.http.get<any[]>(`/kpi/filter?category=${category}`);
  }

  getKpiById(id: number): Observable<any> {
    return this.http.get<any>(`/kpi/${id}`);
  }

  updateKpi(id: number, data: any): Observable<any> {
    return this.http.put<any>(`/kpi/${id}`, data);
  }

  deleteKpi(id: number): Observable<any> {
    return this.http.delete(
      `/kpi/${id}`,
      { responseType: 'text' as 'json' }
    );
  }

  calculateKpi(category: string): Observable<any> {
    return this.http.post<any>(
        `/kpi/calculate?category=${category}`, {}
    );
}

  
  generateAuditPackage(periodStart: string, periodEnd: string): Observable<any> {
    return this.http.post<any>(
        `/audit-packages/generate`,
        { periodStart, periodEnd }
    );
}

  getAllAuditPackages(): Observable<any[]> {
    return this.http.get<any[]>('/audit-packages');
  }

  getAuditPackageById(id: number): Observable<any> {
    return this.http.get<any>(`/audit-packages/${id}`);
  }

  deleteAuditPackage(id: number): Observable<any> {
    return this.http.delete(
      `/audit-packages/${id}`,
      { responseType: 'text' as 'json' }
    );
  }

  
  forgotPassword(email: string, newPassword: string): Observable<any> {
    return this.http.post(
      '/auth/forgot-password',
      { email, newPassword },
      { responseType: 'text' as 'json' }
    );
  }

  
  getNotificationsByUser(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`/api/notifications/user/${userId}`);
  }
  getNotifications(): Observable<any> {
  return this.http.get<any>('/api/notifications');
}

  markNotificationRead(id: number): Observable<any> {
    return this.http.put(
      `/api/notifications/${id}/read`,
      {},
      { responseType: 'text' as 'json' }
    );
  }

  
  getTasksByUser(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`/api/tasks/user/${userId}`);
  }

  updateTask(id: number, data: any): Observable<any> {
    return this.http.put(
      `/api/tasks/${id}`,
      data,
      { responseType: 'text' as 'json' }
    );
  }
}