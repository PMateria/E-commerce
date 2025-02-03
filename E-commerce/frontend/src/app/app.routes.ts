import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { CollectionDetailComponent } from './collection-detail/collection-detail.component';
import { ProductCategoryComponent } from './product-category/product-category.component';
import { FilteredProductsComponent } from './filtered-products/filtered-products.component';

import { NgModule } from '@angular/core';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'collection/:id', component: CollectionDetailComponent },
  { path: 'product/:id', component: ProductCategoryComponent },
  { path: 'filtered-products/:sesso', component: FilteredProductsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }