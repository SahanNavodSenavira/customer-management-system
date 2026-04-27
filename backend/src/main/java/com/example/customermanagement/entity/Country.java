package com.example.customermanagement.entity;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data                    
@Entity                  
@Table(name = "country") 
public class Country {
	 @Id                                    // This is the Primary Key
	    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	    private Long id;

	    @Column(nullable = false, unique = true, length = 10)
	    private String code;  // LK, US, UK etc

	    @Column(nullable = false, length = 100)
	    private String name;  // Sri Lanka, United States etc

	    @Column(name = "created_at")
	    private LocalDateTime createdAt;

}
