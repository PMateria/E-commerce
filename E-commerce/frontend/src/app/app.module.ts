import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http'; // Importa HttpClientModule
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { routes } from './app.routes';
import { AuthInterceptor } from './service/auth-interceptor';

@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule, // Aggiungi HttpClientModule qui
    RouterModule.forRoot(routes),
    CommonModule,
    AppComponent,
    HomeComponent,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }

  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
