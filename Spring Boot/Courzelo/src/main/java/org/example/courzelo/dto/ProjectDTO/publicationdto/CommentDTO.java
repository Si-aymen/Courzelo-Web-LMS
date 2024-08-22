package org.example.courzelo.dto.ProjectDTO.publicationdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private String id;

    private String content;

    private LocalDateTime dateTime;

    private PublicationDTO publication;
}
