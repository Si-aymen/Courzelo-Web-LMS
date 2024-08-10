import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import { UserService } from 'src/app/shared/services/user/user.service';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { ZegoUIKitPrebuilt } from '@zegocloud/zego-uikit-prebuilt';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { ChatService } from 'src/app/shared/services/chatgroups/chat.service';
import { Router } from '@angular/router';

// get token
function generateToken(tokenServerUrl: string, userID: string) {
  // Obtain the token interface provided by the App Server
  return fetch(
    `${tokenServerUrl}/access_token?userID=${userID}&expired_ts=7200`,
    {
      method: 'GET',
    }
  ).then((res) => res.json());
}

function randomID(len) {
  let result = '';
  if (result) return result;
  var chars = '12345qwertyuiopasdfgh67890jklmnbvcxzMNBVCZXASDQWERTYHGFUIOLKJP',
    maxPos = chars.length,
    i;
  len = len || 5;
  for (i = 0; i < len; i++) {
    result += chars.charAt(Math.floor(Math.random() * maxPos));
  }
  return result;
}

export function getUrlParams(
  url: string = window.location.href
): URLSearchParams {
  let urlStr = url.split('?')[1];
  return new URLSearchParams(urlStr);
}

@Component({
  selector: 'app-video-call',
  templateUrl: './video-call.component.html',
  styleUrls: ['./video-call.component.scss']
})
export class VideoCallComponent {
  @ViewChild('root')
  root: ElementRef;
connectedUser:UserResponse;
members:any [];
public constructor(
  private router:Router,
  private userService: UserService,
  private SessionStorageService:SessionStorageService,
private groupService:ChatService
){}

ngAfterViewInit() {
  this.connectedUser = this.SessionStorageService.getUser();
  
  this.groupService.getGroupById("66afa207a022d1705e213d20").subscribe((res) => {
    this.members = res.members;
    console.log("Members", this.members);
    console.log(this.connectedUser.email)

    // Ensure members is an object or array before checking
    if (this.members) {
      if (this.members.includes(this.connectedUser.email)) {
        const roomID = getUrlParams().get('roomID') || randomID(5);
        const appID = 1117206895;
        const serverSecret = "a576addc43d3eb1d3c53eb33f2dab9e1";
        const kitToken = ZegoUIKitPrebuilt.generateKitTokenForTest(
          appID,
          serverSecret,
          roomID,
          this.connectedUser.profile.lastname,
          this.connectedUser.profile.name,
        );

        const zp = ZegoUIKitPrebuilt.create(kitToken);

        zp.joinRoom({
          container: this.root.nativeElement,
          sharedLinks: [
            {
              name: 'Personal link',
              url: window.location.protocol + '//' + 
                    window.location.host + window.location.pathname +
                    '?roomID=' + roomID,
            },
          ],
          scenario: {
            mode: ZegoUIKitPrebuilt.VideoConference,
          },
        })
      } else {
        console.log("not a member");
        this.router.navigate(['others/404']); // Navigate to 404 component
      }
    } else {
      console.error("Members list is undefined");
    }
  }, (error) => {
    console.error("Failed to fetch group members", error);
  });
}
}