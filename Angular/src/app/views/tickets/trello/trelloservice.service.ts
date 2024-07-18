import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TrelloserviceService {

  constructor(private http: HttpClient) { }

  addBoard(name:any){
    return this.http.post('/api/1/boards/', null,{params:{
      key: environment.KEY,
      token: environment.TOKEN,
      name:name
    }})
  }

  getBoardByType(type: any){
    return this.http.get('/tk/api/v1/tickettype/bytype',{params:{
      type: type
    }})
  }
  addBoardDB(designation: any){
    return this.http.get('http://localhost:8081/api/board/',{params:{
      projet: designation
    }})
  }

  getBoardList(id:any){
    return this.http.get('https://api.trello.com/1/boards/'+ id +'/lists',{params:{
      key: environment.KEY,
      token: environment.TOKEN,
    }})
  }

  getListOfCard(id:any){
    return this.http.get('/api/1/cards/'+ id +'/list',{params:{
      key: environment.KEY,
      token: environment.TOKEN,
    }})
  }
  //api.trello.com/1/lists/{id}/cards?key=APIKey&token=APIToken
  getCardList(id:any){
    return this.http.get('/api/1/lists/'+ id +'/cards',{params:{
      key: environment.KEY,
      token: environment.TOKEN,
    }})
  }
  getTrelloUserId(username:any){
    return this.http.get('/api/1/members/'+username,{params:{
      key: environment.KEY,
      token: environment.TOKEN,
    }
    });
  }

  createCard(idListToDo:any,cardName:any,description:any) {
    return this.http.post('https://api.trello.com/1/cards',null,{
      params:{
        key: environment.KEY,
        token: environment.TOKEN,
         idList: idListToDo,
         name: cardName,
         desc: description
      }
    })
  }

  addCard1(idListToDo:any,cardName:any,description:any){
    return this.http.post('https://api.trello.com/1/cards',null,{params:{
     key: environment.KEY,
     token: environment.TOKEN,
      idList: idListToDo,
      name: cardName,
      desc: description
    }})
  }

  addEmployeToCard(cardId:any,memberId:any){
    return this.http.post('https://api.trello.com/1/cards/' + cardId+ '/idMembers',null,{params:{
      key: environment.KEY,
      token: environment.TOKEN,
      value: memberId
    }})
  }

  addCheckList(cardId:any){
    return this.http.post('https://api.trello.com/1/checklists/',null,{params:{
      key: environment.KEY,
      token: environment.TOKEN,
      idCard: cardId,
      name: 'What must to do'

    }})
  }

  addItemToCheckList(idCheckList:any,name:any){
    return this.http.post('https://api.trello.com/1/checklists/'+ idCheckList+ '/checkItems',null,{params:{
      key: environment.KEY,
      token: environment.TOKEN,
      name: name
    }})
  }
  

  getAllCardInListDone(idListDone:any){
    return this.http.get('/api/1/lists/'+idListDone+'/cards',{params:{
      key: environment.KEY,
      token: environment.TOKEN,

    }})
  }
  getAllcardInListDoing(idListDoing:any){
    return this.http.get('https://api.trello.com/1/lists/'+ idListDoing+'/cards',{params:{
      key: environment.KEY,
      token: environment.TOKEN,

    }})
  }
  getAllcardInListToDo(idListDo:any){
    return this.http.get('/api/1/lists/'+ idListDo+'/cards',{params:{
      key: environment.KEY,
      token: environment.TOKEN,

    }})
  }
  deleteCardInTrello(idCard:any){
    return this.http.delete('/api/1/cards/'+idCard,{params:{
      key: environment.KEY,
      token: environment.TOKEN,
    }})
  }
}
