// all-products.component.ts
import { Component, OnInit } from '@angular/core';
import { ProductService } from '../service/product.service';
import { Product } from '../models/product.interface';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-all-products',
  templateUrl: './all-products.component.html',
  styleUrls: ['./all-products.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule]
})
export class AllProductsComponent implements OnInit {
  products: Product[] = [];
  isLoading = true;
  selectedProduct: Product | null = null;
  quantity: number = 1;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (products) => {
        this.products = products;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Errore di caricamento prodotto:', error);
        this.isLoading = false;
      }
    });
  }


getSubtotal(): number {
  if (!this.selectedProduct) return 0;
  
  const originalPrice = this.selectedProduct.prezzo;
  const discount = this.selectedProduct.sconto || 0;
  const discountedPrice = originalPrice * (1 - discount / 100);
  
  return discountedPrice * this.quantity;
}
  

  openQuickView(product: Product) {
    this.selectedProduct = product;
    this.quantity = 1; 
  }

  openCartView(product: Product) {
    this.selectedProduct = product;
    this.quantity = 1;
  }
  

  closeQuickView() {
    this.selectedProduct = null;
  }

  incrementQty() {
    if (this.selectedProduct && this.quantity < (this.selectedProduct.quantita || 1)) {
      this.quantity++;
    }
  }

  decrementQty() {
    console.log('Decrementing quantity', this.quantity);  // Debug log
    if (this.quantity > 1) {
      this.quantity--;
      console.log('Updated quantity:', this.quantity);  // Debug log
    }
  }

  buyNow(product: Product) {
    alert(`Hai acquistato: ${product.nome} (Quantità: ${this.quantity})`);
    this.closeQuickView();
  }

  addToCart(product: Product) {
    alert(`Aggiunto al carrello: ${product.nome} (Quantità: ${this.quantity})`);
    this.closeQuickView();
  }

  resetQuantity() {
    this.quantity = 1;
  }
}