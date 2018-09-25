import { Component, OnInit } from '@angular/core';
import { UserProfileService } from './services/user-profile.service';
import { UserProfile } from './types/user-profile';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  // DEVELOPEMENT MODE
  devProfile = true;


  menteeView = false;
  mentorView = false;

  constructor(private userProfileService: UserProfileService) {}

  ngOnInit() {
    if (this.devProfile) {
      // MANUALLY SET THE VIEW
      this.mentorView = true;
    } else {
      this.userProfileService.getSessionProfile().subscribe((profile: UserProfile) => {
        if (profile.role === 'MENTOR') {
          this.mentorView = true;
        } else if (profile.role === 'MENTEE') {
          this.menteeView = true;
        }
      });
    }

  }

}
