import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ProductService } from '../service/product.service';
import { CommonModule } from '@angular/common';
import { tap, map, filter, switchMap } from 'rxjs/operators';
import { ProductDetail } from '../models/ProductDetail.interface';

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

  navigateToProductDetails(product: ProductDetail, index: number) {
    if (!product || !product.id) {
      console.error('Errore: ID prodotto non valido', product);
      return;
    }
    
    console.log('Navigazione verso prodotto con ID:', product.id);

    this.router.navigate(['/product', product.id], {
      state: { product }
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
        console.log('Received products:', products);
        this.products = products.map((product, index) => ({ ...product, id: index + 1 }));
        console.log(products, "ID PRODOTTO")
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.error = 'Error loading category details';
        this.isLoading = false;
      }
    });
  }
}