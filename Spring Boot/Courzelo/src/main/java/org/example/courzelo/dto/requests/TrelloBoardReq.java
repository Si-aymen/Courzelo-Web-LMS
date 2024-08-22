package org.example.courzelo.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


public class TrelloBoardReq {
    private String idBoard;
    private String idListToDo;
    private String idListDoing;
    private String idListDone;

    private String type;

    public String getIdBoard() {
        return idBoard;
    }

    public void setIdBoard(String idBoard) {
        this.idBoard = idBoard;
    }

    public String getIdListToDo() {
        return idListToDo;
    }

    public void setIdListToDo(String idListToDo) {
        this.idListToDo = idListToDo;
    }

    public String getIdListDoing() {
        return idListDoing;
    }

    public void setIdListDoing(String idListDoing) {
        this.idListDoing = idListDoing;
    }

    public String getIdListDone() {
        return idListDone;
    }

    public void setIdListDone(String idListDone) {
        this.idListDone = idListDone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
