import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

interface Product {
  descrizione: string;
  prezzo: number;
  quantita: number;
  quantitaVenduta: number;
  sconto: number;
}

interface ProdottiResponse {
  [key: string]: Product[]; // Map fascia -> array of products
}

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly BASE_URL = 'http://localhost:8080/gestione_prodotti';
  private readonly ENDPOINTS = {
    products: `${this.BASE_URL}/leggiProdotti`,
    topSelling: `${this.BASE_URL}/prodottiPiuVendutiPerTutteLeFasce`
  };

  constructor(private http: HttpClient) {}

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.ENDPOINTS.products);
  }

  getTopSellingProducts(limitPerFascia: number): Observable<Product[]> {
    return this.http.get<ProdottiResponse>(`${this.ENDPOINTS.topSelling}?limitPerFascia=${limitPerFascia}`)
      .pipe(
        map(response => Object.values(response).flat())
      );
  }
}