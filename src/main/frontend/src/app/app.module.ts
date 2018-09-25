import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { ClarityModule, ClrFormsNextModule } from '@clr/angular';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { UserProfileService } from './services/user-profile.service';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { MenteeComponent } from './mentee/mentee.component';
import { MentorComponent } from './mentor/mentor.component';
import { MenteeFormComponent } from './mentee-form/mentee-form.component';

@NgModule({
  declarations: [
    AppComponent,
    MenteeComponent,
    MentorComponent,
    MenteeFormComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ClarityModule,
    ClrFormsNextModule,
    BrowserAnimationsModule,
    HttpClientModule
  ],
  providers: [
    HttpClient,
    UserProfileService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
