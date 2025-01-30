import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { ProductService } from '../service/product.service';
import { Router, NavigationEnd, RouterModule } from '@angular/router';
import { filter, retry, catchError } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin, Subscription, EMPTY, firstValueFrom } from 'rxjs';
import { Category } from '../models/category.interface';
import { Product } from '../models/product.interface';


interface CategoryGroup {
  category: Category;
  products: Product[];
}

@Component({
  selector: 'app-home',
  templateUrl:'./home.component.html',
  styleUrls: ['./home.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule]
})

export class HomeComponent implements OnInit, OnDestroy {
  products: any[] = [];
  featuredProducts: any[] = [];
  productsByCategories: CategoryGroup[] = [];
  isLoading = true;
  error: string | null = null;
  private loadDataSubscription?: Subscription;
  private navigationSubscription?: Subscription;
  selectedProduct: any;
  quantity: number = 1;
  


  constructor(
    private productService: ProductService,
    private router: Router,
    
  ) {}

  ngOnInit(): void {
    this.loadData();
    
    this.navigationSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      if (this.router.url === '/' || this.router.url === '/home') {
        this.loadData();
      }
    });
  }
  openQuickView(product: Product) {
    console.log('Selected product:', product); // Debug log
    this.selectedProduct = product;
  }

  incrementQty() {
    if((this.quantity + 1) <= this.selectedProduct.quantita) {
      this.quantity++;
    }
  }
  

  decrementQty(){
    if(this.quantity > 1) {
      this.quantity--;
    }
  }

  
  @HostListener('document:keydown.escape', ['$event'])
  handleEscapeKey(event: KeyboardEvent) {
    this.resetQuantity();
  }
  
  resetQuantity() {
    this.quantity = 1;
  }

  ngOnDestroy(): void {
    this.loadDataSubscription?.unsubscribe();
    this.navigationSubscription?.unsubscribe();
  }

  loadData(): void {
    this.isLoading = true;
    this.error = null;
    

    forkJoin({
      products: this.productService.getProducts(),
      featuredProducts: this.productService.getTopSellingProducts(5),
      categories: this.productService.getCategories()
    }).subscribe({
      next: (data) => {
        this.products = Array.isArray(data.products) ? data.products : [];
        this.featuredProducts = data.featuredProducts;
        
        if (Array.isArray(data.categories)) {
          const categoryPromises = data.categories.map(async category => {
            try {
              const productsInCategory = await firstValueFrom(
                this.productService.getProductsByCategory(category.nome)
              );
              return {
                category,
                products: productsInCategory || []
              };
            } catch (error) {
              console.error(`Error loading products for ${category.nome}:`, error);
              return { category, products: [] };
            }
          });
  
          Promise.all(categoryPromises).then(results => {
            this.productsByCategories = results;
            this.isLoading = false;
          });
        }
      },
      error: (error) => {
        console.error('Error:', error);
        this.error = 'Errore caricamento dati';
        this.isLoading = false;
      }
    });
  }
}