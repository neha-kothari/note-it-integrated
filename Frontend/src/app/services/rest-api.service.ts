import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import Swal from 'sweetalert2';

const httpOptions = {
  headers: new HttpHeaders(
    {
      'Access-Control-Allow-Origin': '*',
      'jwtToken': localStorage.jwtToken,
    }
  )
};

const API = "http://localhost:8082"

@Injectable({
  providedIn: 'root'
})
export class RestApiService {
  token: any;


  constructor(private http: HttpClient) { 
  }

  // --------------- LOGIN AUTH APIS --------------
  isUserLoggedIn() {
    // logic
    if (localStorage.getItem('jwtToken') !== null) {
        this.token = localStorage.getItem('jwtToken');
        return true;
    } else {
        return false;
    }
}

  loginAPI(parameters: any){
    return new Promise((resolve, reject) => {
      this.http.post(`${API}/login`,parameters,
      {
        headers: new HttpHeaders({
          // 'Access-Control-Allow-Origin': '*'
        }),
    })
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        resolve(error);
    });
    })
  }

  signUpAPI(parameters: any){
    return new Promise((resolve, reject) => {
      this.http.post(`${API}/registration`,parameters, {responseType: 'text'})
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        // resolve(error);
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Something went wrong!',
        })
        console.log("Error",error);
        
    });
    })
  }

  // --------------- Home page --------------------
  getAllBooks(paramter: any){
    // API 1: /vi/books
    // REQ: GET
    console.log(localStorage.jwtToken,"JWT");
    
    return new Promise((resolve, reject) => {
      this.http.get(`${API}/books`, httpOptions)
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        resolve(error)
    });

    })
  }

  // ------------- BOOK DETAILS ---------------------
  dummy(){
    console.log("doing dummy");
    return new Promise((resolve, reject) => {
      this.http.get("https://api.canvasboard.live", httpOptions)
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        console.log(error); 
    });

    })

  }

  getBookDetails(parameter: any, book_id:any){
    // API: /books/{book_id}
    return new Promise((resolve, reject) => {
      this.http.get(`${API}/books/${book_id}`, httpOptions)
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        console.log(error); 
    });

    })
  }

  searchAPI(){
    
  }

  downloadEbook(book_id:any){
    return new Promise((resolve, reject) => {
      
      this.http.get(`${API}/books/${book_id}/download`,{
        responseType: 'blob' as 'json',
        headers: new HttpHeaders(
          {
            'Access-Control-Allow-Origin': '*',
            'jwtToken': localStorage.jwtToken,
          }
        )
      })
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        resolve(error)
        // console.log("Error",error); 
    });

    })
  }

  // ------------ PROFILE DETAILS ---------------------
  getProfileDetails(){
    let userId = localStorage.getItem('userId')
    return new Promise((resolve, reject) => {
      this.http.get(`${API}/users/${userId}/profile`, httpOptions)
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        console.log(error); 
    });

    })
  }

  // ------------ UPLOAD BOOK -------------------------

  push_final(formData:any){
    let userId = localStorage.getItem('userId')

    return new Promise((resolve, reject) => {
      this.http.post(`${API}/users/${userId}/upload`,formData, httpOptions)
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        console.log(error); 
    });

    })
  }

  push_chapters_to_backend(paramters: any){
    return new Promise((resolve, reject) => {
      this.http.post(`${API}/books/split`,paramters, httpOptions)
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        console.log(error); 
    });

    })

  }

  // ------------- CART -----------------------------
  push_cart_to_backend(parameters: any){
    let userId = localStorage.getItem('userId')
    return new Promise((resolve, reject) => {
      
      this.http.post(`${API}/users/${userId}/notes/merge`,parameters,{
        responseType: 'blob' as 'json',
        headers: new HttpHeaders(
          {
            'Access-Control-Allow-Origin': '*',
            'jwtToken': localStorage.jwtToken,
          }
        )
      })
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        resolve(error)
        // console.log("Error",error); 
    });

    })
  }

  // Saving and retriving the states.
  saveStates(obj:any){
    let userId = localStorage.getItem('userId')

    return new Promise((resolve, reject) => {
      this.http.post(`${API}/users/${userId}/notes`,obj, httpOptions)
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        console.log(error); 
    });

    })
  }

  getStates(userId: any){

    return new Promise((resolve, reject) => {
      this.http.get(`${API}/users/${userId}/notes`, httpOptions)
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        console.log(error); 
    });

    })
  }

  // Download in the profile
  download_books(book_id: any){
    
    return new Promise((resolve, reject) => {
      
      this.http.get(`${API}/books/${book_id}/download`,{
        responseType: 'blob' as 'json',
        headers: new HttpHeaders(
          {
            'Access-Control-Allow-Origin': '*',
            'jwtToken': localStorage.jwtToken,
          }
        )
      })
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        resolve(error)
        // console.log("Error",error); 
    });

    })
  }

  download_notes(notes_id: any){
    
    return new Promise((resolve, reject) => {
      
      this.http.get(`${API}/notes/${notes_id}/download`,{
        responseType: 'blob' as 'json',
        headers: new HttpHeaders(
          {
            'Access-Control-Allow-Origin': '*',
            'jwtToken': localStorage.jwtToken,
          }
        )
      })
      .toPromise()
      .then(
        (res:any) => { // Success
          resolve(res);
        }
      )
      .catch((error:any) => { 
        resolve(error)
        // console.log("Error",error); 
    });

    })
  }
  
  

 
}