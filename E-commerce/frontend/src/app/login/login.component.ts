import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

interface LoginResponse {
  responseStatus: string;
  responseMessage: string;
  token: string;
  username: string;
}

interface RegisterResponse {
  responseStatus: string;
  responseMessage: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly BASE_URL = 'http://localhost:8080';
  private readonly LOGIN_ENDPOINT = `${this.BASE_URL}/gestione_utenti/login`;
  
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasValidToken());
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();
  private username: string = '';

  constructor(private http: HttpClient, private router: Router) {
    this.checkAuthStatus();
  }

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      this.LOGIN_ENDPOINT, 
      { username, password }, 
      { withCredentials: true }
    ).pipe(
      tap(response => {
        if (response.responseStatus === '200') {
          // Aggiorna il nome utente e lo stato di autenticazione
          this.username = response.username;
          this.isAuthenticatedSubject.next(true);
          // Memorizza il JWT nel cookie
          document.cookie = `JWT=${response.token}; Secure; HttpOnly; SameSite=Strict`;
        }
      }),
      catchError(error => {
        console.error('Login error', error);
        return throwError(error);
      })
    );
  }

  logout(): void {
    // Puoi aggiungere una chiamata API per invalidare il token lato server
    this.isAuthenticatedSubject.next(false);
    this.username = '';
    // Rimuovi il token dal cookie
    document.cookie = 'JWT=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    this.router.navigate(['/login']); // Reindirizza alla pagina di login
  }

  private hasValidToken(): boolean {
    // Verifica se il token JWT è presente nel cookie
    const token = this.getCookie('JWT');
    return !!token;
  }

  private checkAuthStatus(): void {
    const isAuthenticated = this.hasValidToken();
    this.isAuthenticatedSubject.next(isAuthenticated);
  }

  private getCookie(name: string): string | null {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop()?.split(';').shift() || null;
    return null;
  }

  getAuthStatus(): boolean {
    return this.isAuthenticatedSubject.value;
  }

  getUsername(): string {
    return this.username;
  }
}