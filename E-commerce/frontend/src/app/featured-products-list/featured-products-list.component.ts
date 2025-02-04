import { Component, OnInit } from '@angular/core';
import { ProductService } from '../service/product.service';
import { Product } from '../models/product.interface';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router'; 

@Component({
  selector: 'app-featured-products-list',
  templateUrl: './featured-products-list.component.html',
  styleUrls: ['./featured-products-list.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule]
})
export class FeaturedProductsListComponent implements OnInit {
  featuredProducts: Product[] = [];
  isLoading = true;

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
}