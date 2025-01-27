import { Component, OnInit } from '@angular/core';
import { ProductService } from '../service/product.service';
import { CommonModule } from '@angular/common';

interface Product {
  descrizione: string;
  prezzo: number;
  quantita: number;
  quantitaVenduta: number;
  sconto: number;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  featuredProducts: Product[] = [];


  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    // Uso il servizio per ottenere i prodotti dal backend
    this.productService.getProducts().subscribe(
      (data) => {
        this.products = data;
      },
      (error) => {
        console.error('Errore durante il recupero dei prodotti:', error);
      }
    );

    // Uso il servizio per ottenere i prodotti piu venduti dal backend
    this.productService.getTopSellingProducts(5).subscribe({
      next: (data) => {
        console.log('Featured products data:', data); // Debug
        this.featuredProducts = data;
      },
      error: (err) => console.error('Error loading featured products:', err)    
    });
  }
}
