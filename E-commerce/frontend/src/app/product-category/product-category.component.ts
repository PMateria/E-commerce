import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductDetail } from '../models/ProductDetail.interface';
import { ProductService } from '../service/product.service';

@Component({
  selector: 'app-product-category',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './product-category.component.html',
  styleUrls: ['./product-category.component.css']
})
export class ProductCategoryComponent implements OnInit {
  product: ProductDetail | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService
  ) {}

  ngOnInit() {
    this.loadProductDetails();
  }

  private loadProductDetails() {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras.state?.['product']) {
      this.product = navigation.extras.state['product'] as ProductDetail;
      this.isLoading = false;
      return;
    }
  
    // Usare paramMap per recuperare l'ID in modo più sicuro
    this.route.paramMap.subscribe(params => {
      const productId = Number(params.get('id')); // Convertire in numero
  
      if (!productId || isNaN(productId)) {
        this.error = 'ID del prodotto non valido';
        this.isLoading = false;
        return;
      }
  
      this.productService.getProductById(productId).subscribe({
        next: (product) => {
          this.product = product;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Errore nel caricamento del prodotto:', error);
          this.error = 'Errore nel caricamento del prodotto';
          this.isLoading = false;
        }
      });
    });
  }
  

  navigateToHome() {
    this.router.navigate(['/']);
  }
}
