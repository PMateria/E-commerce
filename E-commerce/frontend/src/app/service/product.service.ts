import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, of, retry, switchMap, tap, throwError } from 'rxjs';
import { Product } from '../models/product.interface';
import { Category } from '../models/category.interface';
import { FeaturedProductsResponse } from '../models/featuredProductResponse';
import { ProductDetail } from '../models/ProductDetail.interface';
import { AuthService } from './auth.service';



@Injectable({
  providedIn: 'root',
})

export class ProductService {
  private readonly BASE_URL = 'http://localhost:8080';
  private readonly ENDPOINTS = {
    products: `${this.BASE_URL}/gestione_prodotti/leggiProdotti`,
    categories: `${this.BASE_URL}/gestione_categorie/ottieniCategorie`,
    productsByCategory: `${this.BASE_URL}/gestione_prodotti/prodottiPerCategoria`,
    topSelling: `${this.BASE_URL}/gestione_prodotti/prodottiPiuVendutiPerTutteLeFasce`,
    getProdottiConScontoPiuAlto: `${this.BASE_URL}/gestione_prodotti/filtratiPerSaldo`, 
  };

  constructor(private http: HttpClient, private authService: AuthService) {}

 

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.ENDPOINTS.products);
  }

 
  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.ENDPOINTS.categories).pipe(
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
        return [
          ...(response.ALTA || []),
          ...(response.MEDIA || []),
          ...(response.BASSA || [])
        ];
      }),
      catchError(error => {
        console.error('Error fetching top selling products:', error);
        return of([]);
      })
    );
  }


  getProductById(productId: number): Observable<ProductDetail> {
    const url = `${this.BASE_URL}/gestione_prodotti/getProdottoById/${productId}`;
    return this.http.get<ProductDetail>(url).pipe(
      catchError(error => {
        console.error('Errore nel recupero del prodotto:', error);
        return throwError(() => new Error('Errore nel recupero del prodotto'));
      })
    );
  }

  getProdottiConScontoPiuAlto(): Observable<Product[]> {  
    return this.http.get<Product[]>(this.ENDPOINTS.getProdottiConScontoPiuAlto).pipe(
      catchError(error => {
        console.error('Errore nel recupero dei prodotti in saldo:', error);
        return of([]); // Gestisci l'errore e ritorna un array vuoto
      })
    );
  }
  
}
