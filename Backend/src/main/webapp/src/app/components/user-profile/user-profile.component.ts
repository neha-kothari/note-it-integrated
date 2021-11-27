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

}
