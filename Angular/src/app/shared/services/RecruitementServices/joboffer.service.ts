import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
const baseurl = "http://localhost:8081/jobOffer";
const baseurlgetall = "http://localhost:8081/jobOffer/all";
const baseurladd = "http://localhost:8081/jobOffer/add";
const baseurlupdate = "http://localhost:8081/jobOffer/update";
const baseurldelete = "http://localhost:8081/jobOffer/delete";
const baseurlgetbyid = "http://localhost:8081/jobOffer/id";

@Injectable({
  providedIn: "root",
})
export class JobofferService {
  constructor(private http: HttpClient) {}
  getAll() {
    return this.http.get(baseurlgetall);
  }
  get(id: any) {
    return this.http.get(`${baseurlgetbyid}/${id}`);
  }
  create(data: any) {
    return this.http.post(baseurladd, data);
  }
  update(id: any, data: any) {
    return this.http.put(`${baseurlupdate}/${id}`, data);
  }
  delete(id: any) {
    return this.http.delete(`${baseurldelete}/${id}`);
  }
  findbytitle(title: any) {
    return this.http.get(`${baseurl}?title=${title}`);
  }
}
