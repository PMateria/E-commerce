import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, of, retry, switchMap, tap } from 'rxjs';
import { Product } from '../models/product.interface';
import { Category } from '../models/category.interface';
import { FeaturedProductsResponse } from '../models/featuredProductResponse';


interface ProdottiResponse {
  [key: string]: Product[]; // Map fascia -> array of products
}

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly BASE_URL = 'http://localhost:8080';
  private readonly ENDPOINTS = {
    products: `${this.BASE_URL}/gestione_prodotti/leggiProdotti`,
    categories: `${this.BASE_URL}/gestione_categorie/ottieniCategorie`,
    productsByCategory: `${this.BASE_URL}/gestione_prodotti/prodottiPerCategoria`,
    topSelling: `${this.BASE_URL}/gestione_prodotti/prodottiPiuVendutiPerTutteLeFasce`
  };

  constructor(private http: HttpClient) {}

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.ENDPOINTS.products);
  }

 
  getCategories(): Observable<Category[]> {
    return  this.http.get<Category[]>(this.ENDPOINTS.categories).pipe(
      catchError(error => {
        console.error('Error fetching categories:', error);
        return of([]);
      })
    );
  }

  getProductsByCategory(categoryName: string): Observable<Product[]> {
    const url = `${this.BASE_URL}/gestione_categorie/prodottiPerCategoria/${categoryName}`;
    return this.http.get<Product[]>(url).pipe(
      catchError(error => {
        console.error('Error fetching products by category:', error);
        return of([]);
      })
    );
  }

  getTopSellingProducts(limit: number): Observable<Product[]> {
    const url = `${this.BASE_URL}/gestione_prodotti/prodottiPiuVendutiPerTutteLeFasce?limitPerFascia=${limit}`;
    return this.http.get<FeaturedProductsResponse>(url).pipe(
      map(response => {
        const allProducts = [
          ...(response.ALTA || []),
          ...(response.MEDIA || []),
          ...(response.BASSA || [])
        ];
        return allProducts.slice(0, limit);
      }),
      catchError(error => {
        console.error('Error fetching top selling products:', error);
        return of([]);
      })
    );
  }
}