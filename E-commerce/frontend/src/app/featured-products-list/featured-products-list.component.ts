import { Component, OnInit } from '@angular/core';
import { ProductService } from '../service/product.service';
import { Product } from '../models/product.interface';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-featured-products-list',
  templateUrl: './featured-products-list.component.html',
  styleUrls: ['./featured-products-list.component.css'],
  imports: [CommonModule, RouterModule, FormsModule],
  standalone: true
})
export class FeaturedProductsListComponent implements OnInit {
  featuredProducts: Product[] = [];
  isLoading = true;
  selectedProduct: Product | null = null;
  quantity: number = 1;

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.productService.getTopSellingProducts(100).subscribe({
      next: (products) => {
        this.featuredProducts = products;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Errore di caricamento prodotto:', error);
        this.isLoading = false;
      }
    });
  }

  // Apre la quick view
  openQuickView(product: Product) {
    this.selectedProduct = product;
    this.quantity = 1; 
  }

  // Chiude la quick view
  closeQuickView() {
    this.selectedProduct = null;
  }

  // Incrementa la quantità
  incrementQty() {
    if (this.selectedProduct && this.quantity < (this.selectedProduct.quantita ?? 1)) {
      this.quantity++;
    }
  }

  // Decrementa la quantità
  decrementQty() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  // Azione per "Acquista"
  buyNow(product: Product) {
    alert(`Hai acquistato: ${product.nome} (Quantità: ${this.quantity})`);
    this.closeQuickView();
  }

  openCartView(product: Product) {
    this.selectedProduct = product;
    this.quantity = 1;
  }

 // Calcolo totale scontato
 getSubtotal(): number {
  if (!this.selectedProduct) return 0;
  const discount = this.selectedProduct.sconto / 100;
  const subtotal = this.selectedProduct.prezzo * this.quantity * (1 - discount);
  return Number(subtotal.toFixed(2)); // Converti a numero con 2 decimali
}
  // Azione per "Aggiungi al carrello"
  addToCart(product: Product) {
    alert(`Aggiunto al carrello: ${product.nome} (Quantità: ${this.quantity})`);
    this.closeQuickView();
  }

  // Resetta la quantità
  resetQuantity() {
    this.quantity = 1;
  }
}