import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Mentee } from '../types/mentee';

@Component({
  selector: 'app-mentor',
  templateUrl: './mentor.component.html',
  styleUrls: ['./mentor.component.css']
})
export class MentorComponent implements OnInit {
  mentees: Mentee[] = [];
  newMenteeModal = false;
  errorModal = false;

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.getMentees();
  }

  getMentees() {
    this.http.get('/api/mentee/')
        .subscribe((menteeList: Mentee[]) => {
          this.mentees = menteeList;
        });
  }

  saveMentee() {
    console.log('SAVING MENTEE');
  }
}
