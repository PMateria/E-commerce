export interface ProductDetail {
    id: number;
    descrizione: string;
    prezzo: number;
    quantita?: string;
    immagine?: string;
    quantitaVenduta?: string;
    categoriaId?: number;
    sconto?: number;
  }