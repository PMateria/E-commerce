import { Component, OnInit } from '@angular/core';
import { ProductService } from '../service/product.service';
import { Product } from '../models/product.interface';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-prodotti-sconto-piu-alto',
  templateUrl: './prodotti-sconto-piu-alto.component.html',
  styleUrls: ['./prodotti-sconto-piu-alto.component.css'],
  imports: [CommonModule, RouterModule],
  standalone: true
})

export class ProdottiScontoPiuAltoComponent implements OnInit {
  prodotti: Product[] = [];
  error: string = '';
  loading: boolean = true;
  sessoProdotto: string = ''; 
  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadProdotti();
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
