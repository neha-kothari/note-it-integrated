import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RestApiService } from 'src/app/services/rest-api.service';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.scss']
})
export class BookDetailsComponent implements OnInit {
  activateID: any;

  constructor(private activatedRoute: ActivatedRoute, private _api: RestApiService) {
    this.activatedRoute.params.subscribe(params => this.activateID = params);
    console.log(this.activateID.isbn);
  }

  book_details: any;

  ngOnInit(): void {
    this.getBookDetails()
    
  }

  async getBookDetails() {
    
    this.book_details = await this._api.getBookDetails({},this.activateID.isbn);
    console.log(this.book_details)

  }

  addtoLocal() {

    let local = localStorage.getItem('cart');

    // if localstorage variable does not exist
    if (local === null) {

      // Store the selected. 
      let array: any = [];

      // adding the selected once to the array
      let chapters = this.book_details.chapters;
      for (let i = 0; i < chapters.length; i++) {
        let element: any = document.getElementById(`${chapters[i].chapterId}`)
        if (element.checked) {
          array.push(chapters[i]);
        }
      }

      // update the localstorge
      localStorage.setItem('cart', JSON.stringify(array));

    } else {
      // if localstorage variable does exist      
      let array: any = JSON.parse(localStorage.cart);
      console.log(array,array.length);

      // Inject all the previous checks-----------------------------

      // for (let i = 0; i < this.book_details.chapters.length; i++) {
      //   let element: any = document.getElementById(`${this.book_details.chapters[i].chapterId}`)
      //   if(array.includes(this.book_details.chapters[i].chapterId)){
      //     console.log(this.book_details.chapters[i].chapterId,"Included");
      //     element.checked = true;
      //   }
      // }
      // -----------------------------------------------------------


      // Add all the selected notes:
      let chapters = this.book_details.chapters;
      for (let i = 0; i < chapters.length; i++) {
        let element: any = document.getElementById(`${chapters[i].chapterId}`)
        if (element.checked) {

          // check if aleady exists in the array
          if (!this.toCheckifValueExistsinArray(array,chapters[i].chapterId)) {
            console.log("Adding", chapters[i].chapterId);
            array.push(chapters[i]);
          }
        }

        // update the localstorge
        localStorage.setItem('cart', JSON.stringify(array));
      }
    }

  //  Uncheck al
  for (let i = 0; i < this.book_details.chapters.length; i++) {
    let element: any = document.getElementById(`${this.book_details.chapters[i].chapterId}`)
    element.checked = false;
  }

  }

  async downloadEbook(){
    let book_id = this.book_details.bookId;
    console.log(book_id);

    // Download Ebook
    let response:any = await this._api.downloadEbook(book_id);
    console.log("END OF THE ERRA",response);

    let blob = new Blob([response], {type: 'application/pdf'});
    let downloadURL = window.URL.createObjectURL(response);
    let link:any = document.createElement('a');
    link.href = downloadURL;
    link.download = `${this.book_details.bookName}.pdf`;
    link.click();
    
  }


  toCheckifValueExistsinArray(array:any, value:any){
    for(let i=0;i<array.length;i++){
      if(array[i].chapterId === value){
        return true;
      }
    }

    return false;
  }

}
