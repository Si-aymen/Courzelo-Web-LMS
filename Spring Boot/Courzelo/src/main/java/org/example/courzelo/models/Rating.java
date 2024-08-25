package org.example.courzelo.models;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
@NoArgsConstructor
public class Rating {
    @Id
    private String id;

    @Min(1)
    @Max(5)
    private double rating;

    @DBRef
    private Ticket ticket;

}
