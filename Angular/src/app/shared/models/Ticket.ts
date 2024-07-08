export class Ticket {
    id: number;
    type: string;
    sujet: string;
    description: string;
    date: string;
    status: string;
  
    constructor(id: number, type: string, sujet: string, description: string, date: string, status: string) {
      this.id = id;
      this.type = type;
      this.sujet = sujet;
      this.description = description;
      this.date = date;
      this.status = status;
    }
  }