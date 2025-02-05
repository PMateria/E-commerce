import { Component, OnInit, OnDestroy, HostListener, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { ProductService } from '../service/product.service';
import { Router, NavigationEnd, RouterModule } from '@angular/router';
import { filter, retry, catchError } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin, Subscription, EMPTY, firstValueFrom, of } from 'rxjs';
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

export class HomeComponent implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild('sliderTrack') sliderTrack!: ElementRef;

  products: any[] = [];
  featuredProducts: any[] = [];
  productsByCategories: CategoryGroup[] = [];
  isLoading = true;
  error: string | null = null;
  private loadDataSubscription?: Subscription;
  private navigationSubscription?: Subscription;
  selectedProduct: any;
  quantity: number = 1;
  topSellingProducts: any[] = [];
  private sliderInitialized = false;
  private resizeObserver!: ResizeObserver;
  


  constructor(
    private productService: ProductService,
    private router: Router,
    
  ) {}

  ngOnInit(): void {
    this.loadData();
    
    this.navigationSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      console.log('Navigazione verso:', event.urlAfterRedirects);
      if (event.urlAfterRedirects === '/' || event.urlAfterRedirects === '/home') {
        console.log('Forzo ricaricamento dati');
        this.loadData(); // Ricarica i dati
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
  
  ngAfterViewInit() {
    this.initSlider();
    this.setupResizeObserver();
  }

  ngOnDestroy(): void {
    this.resizeObserver?.disconnect();
    this.loadDataSubscription?.unsubscribe();
    this.navigationSubscription?.unsubscribe();
  }

  loadData(): void {
    this.isLoading = true;
    this.error = null;
    

    forkJoin({
      products: this.productService.getProducts().pipe(
        catchError(error => {
          console.error('Error loading products:', error);
          return of([]); 
        })
      ),
      // Aggiungi catchError a tutte le chiamate
      featuredProducts: this.productService.getTopSellingProducts(5).pipe(
        catchError(error => {
          console.error('Error loading featured:', error);
          return of([]);
        })
      ),
      categories: this.productService.getCategories().pipe(
        catchError(error => {
          console.error('Error loading categories:', error);
          return of([]);
        })
      ),
      topSellingProducts: this.productService.getTopSellingProducts(5).pipe(
        catchError(error => {
          console.error('Error loading top selling:', error);
          return of([]);
        })
      )
    }).subscribe({
      next: (data) => {
        this.products = (data.products || []).slice(0, 5);
        this.featuredProducts = data.featuredProducts || [];
        this.topSellingProducts = data.topSellingProducts || [];
        
        this.isLoading = false; 
        setTimeout(() => {
          this.sliderInitialized = false;
          this.initSlider();
        }, 0);
      },
      error: () => {
        this.isLoading = false; 
        this.error = 'Errore nel caricamento dati';
      }
    });
  }

  
  private initSlider() {
    if (this.topSellingProducts.length > 0 && !this.sliderInitialized) {
      // Verifica che sliderTrack sia disponibile
      if (this.sliderTrack && this.sliderTrack.nativeElement) {
        const sliderElement = this.sliderTrack.nativeElement;
        const firstSlide = sliderElement.children[0];

        if (firstSlide) {
          const slideWidth = firstSlide.offsetWidth;
          sliderElement.style.width = `${slideWidth * this.topSellingProducts.length}px`;
          this.sliderInitialized = true; // Imposta come inizializzato
        }
      }
    }
  }

  private setupResizeObserver() {
    // Crea un ResizeObserver per adattare lo slider in base alla dimensione del viewport
    this.resizeObserver = new ResizeObserver(() => {
      this.sliderInitialized = false;  // Ri-inizializza lo slider al ridimensionamento
      this.initSlider();
    });

    if (this.sliderTrack?.nativeElement) {
      this.resizeObserver.observe(this.sliderTrack.nativeElement); // Inizia a osservare
    }
  }
}