import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Router } from '@angular/router';

interface LoginResponse {
  responseStatus: string;
  responseMessage: string;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly BASE_URL = 'http://localhost:8080';
  private readonly LOGIN_ENDPOINT = `${this.BASE_URL}/gestione_utenti/login`;

  // Token in memoria
  private jwtToken: string | null = null;


  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post<LoginResponse>(this.LOGIN_ENDPOINT, { username, password }).pipe(
      map((response) => {
        // Memorizziamo il token in memoria
        this.jwtToken = response.token;
        return response;
      }),
      catchError(error => {
        // Gestiamo eventuali errori
        console.error('Login error', error);
        throw error;
      })
    );
  }

  logout() {
    // Svuotiamo il token in memoria
    this.jwtToken = null;
    // Possiamo reindirizzare a una pagina di login o home
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this.jwtToken;
  }

  isAuthenticated(): boolean {
    // Verifica che il token esista in memoria
    return this.jwtToken !== null;
  }
}
