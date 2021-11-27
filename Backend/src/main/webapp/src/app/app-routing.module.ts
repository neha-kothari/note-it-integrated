import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { BookDetailsComponent } from './components/book-details/book-details.component';
import { CartComponent } from './components/cart/cart.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { UploadNotesComponent } from './components/upload-notes/upload-notes.component';
import { UploadComponent } from './components/upload/upload.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';

const routes: Routes = [
  {
      path: '',
      redirectTo: 'home',
      pathMatch: 'full',
  },
  {
      path: 'home',
      component: HomeComponent,
      canActivate:[AuthGuard]
  },
  {
    path: 'cart',
    component: CartComponent,
    canActivate:[AuthGuard]
  },
  {
    path: 'auth',
    component: LoginComponent,
  },
  {
    path: 'book/:isbn',
    component: BookDetailsComponent,
    canActivate:[AuthGuard]
  },
  {
    path: 'user-profile',
    component: UserProfileComponent,
    canActivate:[AuthGuard]
  },
  {
    path: 'upload-book',
    component: UploadComponent,
    canActivate:[AuthGuard]
  },
  {
    path: 'upload-notes',
    component: UploadNotesComponent,
    canActivate:[AuthGuard]
  },
  {
      path: '**',
      redirectTo: '/home',
  },
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
