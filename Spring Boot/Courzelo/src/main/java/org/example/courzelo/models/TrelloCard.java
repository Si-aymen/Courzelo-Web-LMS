package org.example.courzelo.models;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
public class TrelloCard {
    @Id
    private String id;

    @Size(max = 255)
    private String idCard;

    @DBRef
    private Ticket ticket;
}
