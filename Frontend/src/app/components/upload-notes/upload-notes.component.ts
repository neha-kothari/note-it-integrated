import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RestApiService } from 'src/app/services/rest-api.service';
import Swal from 'sweetalert2';
declare var $: any;

@Component({
  selector: 'app-upload-notes',
  templateUrl: './upload-notes.component.html',
  styleUrls: ['./upload-notes.component.scss']
})
export class UploadNotesComponent implements OnInit {

  constructor(private _api:RestApiService, public router: Router) { }
  note_chapters:any;

  ngOnInit(): void {
    $('form').jsonForm({
      schema: {
        Chapters: {
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
          localStorage.setItem("note_chapters", JSON.stringify(values.chapters));

          Swal.fire(
            'Chapters Submitted!',
            'Click on Save Changes!',
            'success'
          )
        }
      }
    });
  }

  async upload(){
    this.note_chapters = localStorage.note_chapters
    this.note_chapters = JSON.parse(this.note_chapters);

    let json: any = {}

    // Get title
    let title:any = document.getElementById("title");
    json.bookName = title.value

    // get description
    let description:any = document.getElementById("description");
    json.description = description.value

    // author
    let author:any = document.getElementById("author")
    json.author = author.value

    // file
    let formData = new FormData();
    console.log("JSON", json);
    
    formData.set("json", JSON.stringify(json));
    let file:any = document.getElementById("file")
    let file2 = file.files[0];
    console.log("BEFOREEE", file2);

    if (file2 !== undefined) {
      console.log("BEFOREEE appended");
      formData.append("file", file2);
    }

    // push to backend
    console.log("form data", formData);
    
    let response:any = await this._api.push_final(formData);
    console.log("RESPONSEEEEEE", response);

    // if chapters are present
        // Sending the chapters:
        if(this.note_chapters){
          console.log("now going to chapters");
          
          let obj = JSON.parse(JSON.stringify(json));
          obj.bookId = response.bookId;
          obj.uploadedByUser = "user"
          obj.chapters = this.note_chapters;
          // obj.author = author.value
          console.log("Split obj", obj);
          let response1:any = this._api.push_chapters_to_backend(obj);
          if(response1){
            Swal.fire(
              'Yayy!',
              'Notes Submitted!',
              'success'
            )
          }
          // route to dashboard.
        this.router.navigate(['home']);
          
        }
  }

}
