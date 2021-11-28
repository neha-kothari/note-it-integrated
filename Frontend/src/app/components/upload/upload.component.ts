import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RestApiService } from 'src/app/services/rest-api.service';
import Swal from 'sweetalert2';
declare var $: any;

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent implements OnInit {

  constructor( private _api:RestApiService, public router: Router) { }
  selectedFiles: any;
  progress: { percentage: number } = { percentage: 0 };
  currentFileUpload: any;
  chapters: any;

  ngOnInit(): void {
    $('form').jsonForm({
      schema: {
        chapters: {
          type: "array",
          items: {
            type: "object",
            title: "-----------------------------------------",
            properties: {
              "chapterNumber": {
                "type": "integer",
                "title": "Chapter Number",
                "required": true
              },
              "chapterName": {
                "type": "string",
                "title": "Chapter Name",
                "required": true
              },
              "startPage": {
                "type": "integer",
                "title": "Start Page",
                "required": true
              },
              "endPage": {
                "type": "integer",
                "title": "End Page",
                "required": true
              },
              "description": {
                "type": "string",
                "title": "Description",
                "required": true
              },
            }
          }
        }
      },
      onSubmit: function (errors:any, values: any){

        if (errors) {
          console.log(errors);
        }
        else {          
          this.chapters = values.chapters;
          console.log(values.chapters);
          localStorage.setItem("chapters", JSON.stringify(values.chapters));

          Swal.fire(
            'Chapters Submitted!',
            'Click on Save Changes!',
            'success'
          )
        }
      }
    });
  }

  selectFile(event:any) {
    console.log("Came to selected file");
    
    this.selectedFiles = event.target.files;
  }

  async upload() {
    
    this.chapters = localStorage.chapters
    console.log("Chapters", JSON.parse(this.chapters));
    
    
    this.progress.percentage = 0;
    this.currentFileUpload = await this.selectedFiles.item(0);
    console.log("currentFileUpload",this.currentFileUpload);

    let json:any = {}

    // get isbn
    let isbnNumber:any = document.getElementById("isbn")
    json.isbnNumber = isbnNumber.value

    // get title
    let bookName:any = document.getElementById("title")
    json.bookName = bookName.value

    // author
    let author:any = document.getElementById("author")
    json.author = author.value

    // publisher
    let publisher :any= document.getElementById("publisher")
    json.publisher = publisher.value

    // yearOfRelease
    let yearOfRelease:any = document.getElementById("yearOfRelease");
    json.yearOfRelease = yearOfRelease.value

    // Image Location
    let imageLocation:any = document.getElementById("imageLocation");
    json.imageLocation = imageLocation.value

    // Description
    let description:any = document.getElementById("description");
    json.description = description.value

    // my obj
    console.log("OBJJJ",json);
    
    
    let formData = new FormData();
    formData.set("json", JSON.stringify(json));
    let file:any = document.getElementById("file")
    let file2 = file.files[0];
    console.log("BEFOREEE", file2);
    
    if (file2 !== undefined) {
      console.log("BEFOREEE appended");
      formData.append("file", file2);
    }
    
    let response:any = await this._api.push_final(formData);
    console.log("RESPONSEEEEEE", response);
    
    
  //   {
  //     "bookId": 1,
  //     "bookName": "Dummy",
  //     "isbnNumber": null,
  //     "author": "neha",
  //     "publisher": null,
  //     "yearOfRelease": null,
  //     "imageLocation": null,
  //     "uploadedByUser": "Neha Kothari",
  //     "error": null,
  //     "chapters" : [
  //     {
  //         "chapterNumber" : "1",
  //         "chapterName" : "Hello World!",
  //         "startPage" : "1",
  //         "endPage" : "10",
  //         "description" : "This is the first chapter!"
  //     },
  //     {
  //         "chapterNumber" : "2",
  //         "chapterName" : "okay World",
  //         "startPage" : "11",
  //         "endPage" : "20",
  //         "description" : "This is the 2nd chapter!"
  //     },
  //     {
  //         "chapterNumber" : "3",
  //         "chapterName" : "Goodbye",
  //         "startPage" : "31",
  //         "endPage" : "40",
  //         "description" : "This is the last chapter!"
  //     }
  // ]
  // }
    // Sending the chapters:
    if(this.chapters){
      console.log("now going to chapters");
      
      let obj = JSON.parse(JSON.stringify(json));
      obj.bookId = response.bookId;
      obj.uploadedByUser = "user"
      obj.chapters = JSON.parse(this.chapters);

      // console.log("Split obj", obj);
      let response1:any = this._api.push_chapters_to_backend(obj);
      if(response1){
        Swal.fire(
          'Yayy!',
          'Book Submitted!',
          'success'
        )
      }
      // route to dashboard.
    this.router.navigate(['home']);
      
    }
  }


}
