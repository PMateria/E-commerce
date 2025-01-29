import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ProductService } from '../service/product.service';
import { CommonModule } from '@angular/common';
import { tap, map, filter, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-collection-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './collection-detail.component.html',
  styleUrls: ['./collection-detail.component.css']
})


export class CollectionDetailComponent implements OnInit {
  products: any[] = [];
  category: any;
  isLoading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService
  ) {}



  ngOnInit() {
    this.route.params.subscribe(params => {
      const categoryId = +params['id'];
      this.loadCategoryDetails(categoryId);
    });
  }

  navigateToHome() {
    this.router.navigateByUrl('/', { skipLocationChange: false }).then(() => {
      window.location.reload();
    });
  }

  private loadCategoryDetails(categoryId: number) {
    this.isLoading = true;
    
    this.productService.getCategories().pipe(
      map(categories => {
        const category = categories.find(c => c.id === categoryId);
        if (!category) throw new Error('Category not found');
        this.category = category;
        return category.nome;
      }),
      switchMap(categoryName => this.productService.getProductsByCategory(categoryName))
    ).subscribe({
      next: (products) => {
        this.products = products;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.error = 'Error loading category details';
        this.isLoading = false;
      }
    });
  }
  

 private loadProducts(categoryId: number) {
  this.isLoading = true;

  // First get category name using categoryId
  this.productService.getCategories().pipe(
    tap(categories => console.log('Categories loaded:', categories)),
    map(categories => {
      const category = categories.find(c => c.id === categoryId);
      console.log('Found category:', category);
      if (!category) {
        throw new Error('Category not found');
      }
      return category.nome;
    }),
    tap(nome => console.log('Using category name:', nome)),
    switchMap(categoryName => this.productService.getProductsByCategory(categoryName))
  ).subscribe({
    next: (products) => {
      console.log('Products loaded:', products);
      this.products = products;
      this.isLoading = false;
    },
    error: (err) => {
      console.error('Detailed error:', err);
      this.error = `Error loading products: ${err.message}`;
      this.isLoading = false;
    }
  });
}
}
