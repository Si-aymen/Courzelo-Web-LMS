import { Publication } from "./Publication";

export interface Commment {

  id?: string; 
    content: string;
    dateTime: Date;  
    publication: Publication;
    
  }