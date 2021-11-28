import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RestApiService } from 'src/app/services/rest-api.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  constructor(private _api:RestApiService, public router: Router) { }
  local_cart: any;
  merge:boolean = false;

  ngOnInit(): void {
    this.local_cart = JSON.parse(localStorage.cart);
    if(this.local_cart.length != 0){
      this.merge = true;
      console.log(this.local_cart.length);
    }
  }

  deleteFromCart(id:any){
    console.log(id);
    // remove from array local_Cart
    console.log("before",this.local_cart);

    // get index of object with id of 37
    const removeIndex = this.local_cart.findIndex( (item:any) => item.chapterId === id );
    // remove object
    this.local_cart.splice( removeIndex, 1 );

    console.log("After",this.local_cart);
    
    // Update the localstorage
    localStorage.setItem('cart', JSON.stringify(this.local_cart));
  }

  async sendCartToBackend(){

    let obj:any = {}
    // collect custom name:
    let customName:any = document.getElementById("customName");
    obj.custom_name = customName.value

    let arr = []
    for(let i=0;i<this.local_cart.length;i++){
      console.log(this.local_cart[i], "kasjljlskdfjsldfjk");
        arr.push(this.local_cart[i].chapterId)
    }

    obj.selected_chapters = arr;

    // Send this to the backend. 
    let response:any = await this._api.push_cart_to_backend(obj);
    console.log("END OF THE ERRA",response);

    let blob = new Blob([response], {type: 'application/pdf'});
    let downloadURL = window.URL.createObjectURL(response);
    let link:any = document.createElement('a');
    link.href = downloadURL;
    link.download = `${customName.value}.pdf`;
    link.click();
    

    //After the post
    this.local_cart = [];
    localStorage.setItem('cart', JSON.stringify(this.local_cart));

    // empty the input
    this.router.navigate(['home']);
    
  }

}
