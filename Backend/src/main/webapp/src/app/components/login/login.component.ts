import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RestApiService } from 'src/app/services/rest-api.service';
import Swal from 'sweetalert2'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private _api:RestApiService, public router: Router) { }

  ngOnInit(): void {
    const signUpButton: any = document.getElementById('signUp');
    const signInButton: any = document.getElementById('signIn');
    const container: any = document.getElementById('container');

    signUpButton.addEventListener('click', () => {
      container.classList.add('right-panel-active');
    });

    signInButton.addEventListener('click', () => {
      container.classList.remove('right-panel-active');
    });
  }

  async signUp(){
  let obj:any = {
    userType : 0
  };

  // Getting First name
  let fname:any = document.getElementById("sign-up-fname");
  obj.firstName = fname.value

  // Getting Last name
  let lname:any = document.getElementById("sign-up-lname");
  obj.lastName = lname.value

  // Getting email
  let email:any = document.getElementById("sign-up-email");
  obj.emailAddress = email.value

  // Getting Password
  let password:any = document.getElementById("sign-up-password");
  obj.password = password.value

  // Phone num
  let num:any = document.getElementById("sign-up-phone");
  obj.phoneNumber = parseInt(num.value)

  // send to backend
  let response:any = await this._api.signUpAPI(obj);
  console.log("signup",response);
  // {'data':'Registered Successfully'}
  Swal.fire(
    'User Registered!',
    'Please Sign in!',
    'success'
  )
  
  if(!response){
    return
  }
  response = JSON.parse(response)
  console.log("Data",response.data);
  
  if(response.data === 'Registered Successfully'){

  }else{
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Something went wrong!',
    })
  }
  }

  async login(){
    
    let obj:any = {
    };
  // Getting email
  let email:any = document.getElementById("login-email");
  obj.username = email.value

  // Getting Password
  let password:any = document.getElementById("login-password");
  obj.password = password.value

    // Send to backend
    console.log(obj);
    let response:any = await this._api.loginAPI(obj);
    console.log("login", response.userId);
    
    // If I did not get the UserID
    if(!response.userId){
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Something went wrong!',
      })
      return
    }

    // store in localstorage
    localStorage.setItem("userId",response.userId);
    localStorage.setItem("jwtToken",response.jwtToken);

    await this.getCarState();

    // route to dashboard.
    this.router.navigate(['home']);
  }


  async getCarState(){
    console.log("Cart state");
    
    let userId = localStorage.userId
        // get the cart state
        console.log("Requesting");
        
        let res1:any = await this._api.getStates(userId)
        console.log(res1, userId);
        console.log("Before saving the states");
        
        if(res1 != null){
          localStorage.setItem("cart",JSON.stringify(res1.chapters))
        }else{
          localStorage.setItem("cart",JSON.stringify([]))
        }
        console.log("After Saving the states");
  }

}
