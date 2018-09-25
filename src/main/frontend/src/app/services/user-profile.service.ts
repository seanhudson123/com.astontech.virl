import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


@Injectable()
export class UserProfileService {
  public serviceMessage = 'Service Injected Successfully';

  constructor(private http: HttpClient) {}

  getUserProfile(name: string) {
    this.http.get('/api/profile/' + name)
              .subscribe(profile => {
                console.log(profile);
              });
  }

  getSessionProfile() {
    return this.http.get('/api/profile/');
  }
}
