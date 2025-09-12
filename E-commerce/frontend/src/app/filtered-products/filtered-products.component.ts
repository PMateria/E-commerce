import { Component, OnDestroy, OnInit } from '@angular/core';
import { ProductService } from '../service/product.service';
import { Product } from '../models/product.interface';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { AuthService } from '../service/auth.service'; // Aggiungi questa importazione
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-filtered-products',
  templateUrl: './filtered-products.component.html',
  styleUrls: ['./filtered-products.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule]
})
export class FilteredProductsComponent implements OnInit, OnDestroy {
  filteredProducts: Product[] = [];
  isLoading = true;
  error: string | null = null;
  selectedProduct: Product | null = null;
  sessoProdotto: string = '';
  isAuthenticated = false;
  private authSubscription!: Subscription;

  constructor(
    private productService: ProductService, 
    private route: ActivatedRoute,
    public authService: AuthService 
  ) {} 


  ngOnInit(): void {
    // Sottoscrizione ai parametri della rotta
    this.authSubscription = this.authService.isAuthenticated$.subscribe(
      (isAuth: boolean) => this.isAuthenticated = isAuth
    );
    
    this.route.params.subscribe(params => {
      this.sessoProdotto = params['sesso']; // Ottieni il parametro 'sesso' dalla rotta
      console.log('Sesso selezionato:', this.sessoProdotto); // Debug
      this.loadFilteredProducts();
    });
  }
  ngOnDestroy(): void {
    this.authSubscription?.unsubscribe();
  }

  logout(): void {
    this.authService.logout();
  }

  loadFilteredProducts(): void {
    this.isLoading = true;
    this.error = null;

    this.productService.getProducts().subscribe({
      next: (products) => {
        console.log('Prodotti caricati:', products); // Debug
        // Filtra i prodotti in base al sesso
        this.filteredProducts = products.filter(product => product.sessoProdotto === this.sessoProdotto);
        console.log('Prodotti filtrati:', this.filteredProducts); // Debug
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.error = 'Errore caricamento dati';
        this.isLoading = false;
      }
    });
  }

  openModal(product: Product): void {
    this.selectedProduct = product;
  }

  addToCart(product: Product): void {
    console.log('Aggiunto al carrello:', product);
    this.closeModal();
  }

  buyNow(product: Product): void {
    console.log('Acquisto immediato:', product);
    this.closeModal();
  }

  closeModal(): void {
    const modal = document.getElementById('productModal');
    if (modal) {
      modal.classList.remove('show');
      modal.setAttribute('aria-hidden', 'true');
      modal.style.display = 'none';
      document.body.classList.remove('modal-open');
      const modalBackdrop = document.querySelector('.modal-backdrop');
      if (modalBackdrop) {
        modalBackdrop.remove();
      }
    }
  }

    getTranslatedSessoProdotto(): string {
    if (this.sessoProdotto.toLowerCase() === 'donna') {
      return 'Woman';
    }

    if (this.sessoProdotto.toLowerCase() === 'uomo') {
      return 'Men';
    }
    return this.sessoProdotto;
  }
}
