import { AfterViewInit, Component, OnInit } from "@angular/core";
import { JobofferService } from "../../../../shared/services/RecruitementServices/joboffer.service";
import { Router } from "@angular/router";
import * as L from "leaflet";

@Component({
  selector: "app-joboffer",
  templateUrl: "./joboffer.component.html",
  styleUrls: ["./joboffer.component.scss"],
})
export class JobofferComponent implements OnInit, AfterViewInit {
  jobOffers: any;
  title = "";
  currentJobOffer: any;
  currentIndex = -1;
  message = "";
  private map: L.Map;
  constructor(
    private jobofferService: JobofferService,
    private router: Router
  ) {}
  ngOnInit() {
    this.message = "";
    this.retrieveJobOffers();
    this.initMap();
  }
  ngAfterViewInit(): void {
    this.initMap();
    setTimeout(() => {
      this.map.invalidateSize();
    }, 0);
  }
  initMap(): void {
    this.map = L.map("map").setView([51.505, -0.09], 13); // Initialize map with center coordinates and zoom level
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      maxZoom: 19,
      attribution:
        'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    }).addTo(this.map);
  }
  retrieveJobOffers() {
    this.jobofferService.getAll().subscribe(
      (data) => {
        this.jobOffers = data;
        console.log(data);
      },
      (error) => {
        console.log(error);
      }
    );
  }
  searchTitle() {
    this.jobofferService.findbytitle(this.title).subscribe(
      (data) => {
        this.jobOffers = data;
        console.log(data);
      },
      (error) => {
        console.log(error);
      }
    );
  }
  setActiveJobOffer(jobOffer: any, index: any) {
    this.currentJobOffer = jobOffer;
    this.currentIndex = index;
  }
  deletejobOffer(id: any) {
    this.jobofferService.delete(id).subscribe(
      (response) => {
        console.log(response);
        this.retrieveJobOffers();
      },
      (error) => {
        console.log(error);
      }
    );
  }
  getJobOffer(id: any) {
    this.jobofferService.get(id).subscribe(
      (data) => {
        this.currentJobOffer = data;
        console.log(data);
      },
      (error) => {
        console.log(error);
      }
    );
  }
  updatejobOffer() {
    this.jobofferService
      .update(this.currentJobOffer.id, this.currentJobOffer)
      .subscribe(
        (response) => {
          console.log(response);
          this.message = "The job offer was updated successfully!";
          this.retrieveJobOffers();
        },
        (error) => {
          console.log(error);
        }
      );
  }
}
