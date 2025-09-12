declare const bootstrap: any;
import { Component, OnInit, OnDestroy, HostListener, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { ProductService } from '../service/product.service';
import { Router, NavigationEnd, RouterModule } from '@angular/router';
import { filter, catchError } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { forkJoin, Subscription, of } from 'rxjs';
import { Category } from '../models/category.interface';
import { Product } from '../models/product.interface';
import { AuthService } from '../service/auth.service';

interface CategoryGroup {
  category: Category;
  products: Product[];
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, ReactiveFormsModule]
})
export class HomeComponent implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild('sliderTrack') sliderTrack!: ElementRef;
  @ViewChild('modallogin') modallogin!: ElementRef;
  
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
  username: string = '';
  password: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  isAuthenticated: boolean = false;
  registerForm: FormGroup;

  constructor(
    private productService: ProductService,
    private router: Router,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.registerForm = this.fb.group({
      nome: ['', Validators.required],
      cognome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      genere: [''],
      ruolo: "User"
    });
  }

  private authSubscription?: Subscription;


  ngOnInit(): void {
    this.isAuthenticated = this.authService.getAuthStatus();
    this.username = this.authService.getUsername();
    this.authSubscription = this.authService.isAuthenticated$.subscribe(isAuth => {
      this.isAuthenticated = isAuth;
      this.username = this.authService.getUsername();
    });
    this.loadData();

    this.navigationSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      console.log('Navigazione verso:', event.urlAfterRedirects);
      if (event.urlAfterRedirects === '/' || event.urlAfterRedirects === '/home') {
        console.log('Forzo ricaricamento dati');
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
  
  ngAfterViewInit() {
    this.initSlider();
    this.setupResizeObserver();
  }

  ngOnDestroy(): void {
    this.resizeObserver?.disconnect();
    this.loadDataSubscription?.unsubscribe();
    this.navigationSubscription?.unsubscribe();
    this.authSubscription?.unsubscribe();

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

  // Modifica il metodo onSubmit così:
onSubmit() {
  this.authService.login(this.username, this.password).subscribe({
    next: (response) => {
      if (response.responseStatus === '200') {
        this.successMessage = 'Login effettuato con successo!';
        
        const modalElement = this.modallogin.nativeElement;
        const modal = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);

        // Aggiungi questo evento listener per gestire la chiusura completa
        modalElement.addEventListener('hidden.bs.modal', () => {
          const backdrop = document.querySelector('.modal-backdrop');
          if (backdrop) {
            backdrop.remove();
          }
          document.body.style.overflow = 'auto';
          document.body.style.paddingRight = '0';
        });

        setTimeout(() => {
          modal.hide();
          this.isAuthenticated = true;
          this.username = response.username;
          this.successMessage = '';
        }, 1500);
      }
    },
    error: (error) => {
      this.errorMessage = 'Errore durante il login, per favore riprova';
    }
  });
}

// Aggiungi questo handler per l'evento hidden
@HostListener('hidden.bs.modal', ['$event'])
onModalHidden(event: any) {
  const backdrop = document.querySelector('.modal-backdrop');
  if (backdrop) {
    backdrop.remove();
  }
  document.body.classList.remove('modal-open');
  document.body.style.paddingRight = '';
}

  logout() {
    this.authService.logout();
    this.isAuthenticated = false;
    this.username = '';
  }

  onSubmitRegister() {
    if (this.registerForm.valid) {
      const userData = this.registerForm.value;
      this.authService.register(userData).subscribe({
        next: (response) => {
          this.successMessage = 'Registrazione effettuata con successo!';
          setTimeout(() => {
            // Reset del form
            this.registerForm.reset();
            this.successMessage = '';
          }, 1500);
        },
        error: (error) => {
          this.errorMessage = 'Errore durante la registrazione, per favore riprova';
        }
      });
    } else {
      this.errorMessage = 'Per favore, compila tutti i campi obbligatori correttamente';
    }
  }
}