export interface Product {
  id: any;
  nome: string;
  descrizione: string;
  prezzo: number;
  quantita: number;
  quantitaVenduta: number;
  categoriaId: number;
  sconto: number;
  categoria: string;
  immagine: string;
  immagineUrl: string;
  sessoProdotto: string;
}