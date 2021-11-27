import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RestApiService } from 'src/app/services/rest-api.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  constructor(private router: Router, private _api:RestApiService) { }

  ngOnInit(): void {
  }

  logout(){
    let chapters:any = localStorage.cart
    chapters = JSON.parse(chapters)

    let selected_chapters = []
    for(let i=0;i<chapters.length;i++){
      selected_chapters.push(chapters[i].chapterId)
    }

    let obj = {
      "selected_chapters": selected_chapters,
      "custom_name": ""
    }

    // saving the state
    let response = this._api.saveStates(obj);
    console.log(response);
    
    
    // remove the token and go to auth
    localStorage.removeItem('jwtToken');
    localStorage.setItem('cart', JSON.stringify([]));
    this.router.navigate(['auth']);
  }
}
