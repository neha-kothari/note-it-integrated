import { Component, OnInit } from '@angular/core';
import { RestApiService } from 'src/app/services/rest-api.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  constructor(private _api: RestApiService) { }
  profile_details: any;

  ngOnInit(): void {
    this.get_profile_details()
  }

  async get_profile_details(){
    this.profile_details = await this._api.getProfileDetails()
    console.log(this.profile_details);
  
  }

  async downloadUpload(id: any, name:any){
    console.log("Download", id, name);

    // send to backend
    let response:any = await this._api.download_books(id);

    let blob = new Blob([response], {type: 'application/pdf'});
    let downloadURL = window.URL.createObjectURL(response);
    let link:any = document.createElement('a');
    link.href = downloadURL;
    link.download = `${name}.pdf`;
    link.click();
    
  }

  async downloadMerge(id:any, name:any){
    console.log(id,name);
    

    let response:any = await this._api.download_notes(id);
    
    let blob = new Blob([response], {type: 'application/pdf'});
    let downloadURL = window.URL.createObjectURL(response);
    let link:any = document.createElement('a');
    link.href = downloadURL;
    link.download = `${name}.pdf`;
    link.click();
  }

}