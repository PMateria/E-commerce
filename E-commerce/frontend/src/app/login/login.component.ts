import { Component } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  isLoading: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        if (response.responseStatus === '200') {
          this.successMessage = 'Login effettuato con successo!';
          setTimeout(() => {
            // Aggiorna lo stato di autenticazione e il nome dell'utente
            (this.authService.isAuthenticated$ as BehaviorSubject<boolean>).next(true);
            this.successMessage = '';

            // Reindirizza alla home
            this.router.navigate(['/home']);
          }, 1500);
        }
      },
      error: (error) => {
        this.errorMessage = 'Errore durante il login, per favore riprova';
        this.isLoading = false;
      }
    });
  }
}