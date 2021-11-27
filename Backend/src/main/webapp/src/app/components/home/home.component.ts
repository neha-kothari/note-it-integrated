import { Component, OnInit } from '@angular/core';
import { RestApiService } from 'src/app/services/rest-api.service';
import Fuse from 'fuse.js';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  
  fuse:any;
  all_books_fuse:any;

  constructor(private _api: RestApiService) { }
  all_books: any;
  ngOnInit(): void {
    this.getAllBooks();
    
  }

  async getAllBooks(){
    this.all_books = await this._api.getAllBooks({});
    console.log(this.all_books);
    this.fuseSearch();
  }

  async fuseSearch(){
    const options = {
      // isCaseSensitive: false,
      // includeScore: false,
      // shouldSort: true,
      // includeMatches: false,
      // findAllMatches: false,
      // minMatchCharLength: 1,
      // location: 0,
      // threshold: 0.6,
      // distance: 100,
      // useExtendedSearch: false,
      // ignoreLocation: false,
      // ignoreFieldNorm: false,
      keys: [
        'bookName',
      ]
    };
    this.fuse = await new Fuse(this.all_books, options);

  }

  // tslint:disable-next-line:typedef
  async textInputChange(e:any){
    
    let parameter: any = document.getElementById("fuzeSearch")
    console.log(this.fuse.search, parameter.value, e.value);

    this.all_books_fuse =  await this.fuse.search(parameter.value);
    console.log(this.all_books_fuse);
  }

}
