import { Component, OnDestroy, OnInit } from '@angular/core';
import { ProductService } from '../service/product.service';
import { Product } from '../models/product.interface';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../service/auth.service'; 
import { Subscription } from 'rxjs';

import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-prodotti-sconto-piu-alto',
  templateUrl: './prodotti-sconto-piu-alto.component.html',
  styleUrls: ['./prodotti-sconto-piu-alto.component.css'],
  imports: [CommonModule, RouterModule],
  standalone: true
})

export class ProdottiScontoPiuAltoComponent implements OnInit, OnDestroy {
  prodotti: Product[] = [];
  error: string = '';
  loading: boolean = true;
  sessoProdotto: string = ''; 
  isAuthenticated = false;
  private authSubscription!: Subscription;

  constructor(private productService: ProductService,public authService: AuthService) {}

  ngOnInit(): void {
    this.authSubscription = this.authService.isAuthenticated$.subscribe(
      (isAuth: boolean) => this.isAuthenticated = isAuth
    );
    this.loadProdotti();
  }

  ngOnDestroy(): void {
    this.authSubscription?.unsubscribe();
  }

  logout(): void {
    this.authService.logout();
  }


  private loadProdotti(): void {
    this.loading = true;
    this.productService.getProdottiConScontoPiuAlto().subscribe({
      next: (products) => {
        const totalSconto = products.reduce((sum, product) => sum + product.sconto, 0);
        const mediaSconto = totalSconto / products.length;

        this.prodotti = products.filter(product => product.sconto > mediaSconto);

        this.loading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.error = 'Si è verificato un errore durante il caricamento dei prodotti';
        this.loading = false;
        console.error('Errore:', error);
      }
    });
  }
}
