package com.limtic.lab.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "roles")
public class Role {
    @Id
    private String id;

    private String name; // "USER", "ADMIN", "PERMANENT", "VISITOR"
}
