package model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class exampleJPAEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // Persistent Fields:
    @Id @GeneratedValue
    Long id;
    private String name;

    // Constructors:
    public exampleJPAEntity() {
    }

    public exampleJPAEntity(String name) {
        this.name = name;
    }

    // String Representation:
    @Override
    public String toString() {
        return name + " (signed on)";
    }
} 
