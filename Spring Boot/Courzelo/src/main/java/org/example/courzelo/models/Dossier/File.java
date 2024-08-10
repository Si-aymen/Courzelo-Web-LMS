package org.example.courzelo.models.Dossier;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "files")
public class File {

    @Id
    private String id;
    private String name;
    private String type;
    private byte[] data;


}
