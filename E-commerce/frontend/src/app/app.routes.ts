import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { CollectionDetailComponent } from './collection-detail/collection-detail.component';
import { ProductCategoryComponent } from './product-category/product-category.component';
import { FilteredProductsComponent } from './filtered-products/filtered-products.component';
import { ProdottiScontoPiuAltoComponent } from './prodotti-sconto-piu-alto/prodotti-sconto-piu-alto.component';
import { FeaturedProductsListComponent } from './featured-products-list/featured-products-list.component';
import { AllProductsComponent } from './all-products/all-products.component';
import { NgModule } from '@angular/core';


export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'collection/:id', component: CollectionDetailComponent },
  { path: 'product/:id', component: ProductCategoryComponent },
  { path: 'filtered-products/:sesso', component: FilteredProductsComponent },
  { path: 'sale', component: ProdottiScontoPiuAltoComponent },
  { path: 'featured-products', component: FeaturedProductsListComponent },
  { path: 'all-products', component: AllProductsComponent }



];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }