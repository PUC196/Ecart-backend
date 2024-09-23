package com.app.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.app.pojo.Address;
import com.app.pojo.PaymentInformation;
//import com.app.pojos.Rating;
//import com.app.pojos.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
        //@UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
   
    @NotBlank
    @Size(max = 20)
    private String firstName;
    private String lastName;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(max = 120)
    @Column(name = "password")
    private String password;
    
   
     @JsonIgnore
    @Setter
    @Getter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
                fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
   



	public User(Long userId,  String firstName, String lastName,
			 String email,  String password) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		
	}



	public User(@NotBlank @Size(max = 20) String firstName, String lastName,
			@NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 120) String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
	@OneToMany(mappedBy ="user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<Address>addresses=new ArrayList<>();
	@Embedded
	@ElementCollection(fetch = FetchType.EAGER) 
	@CollectionTable(name="payment_information",joinColumns = @JoinColumn(name="user_id") )
	private List<PaymentInformation>paymentInformation=new ArrayList<>();
//	@OneToMany(mappedBy ="user",cascade = CascadeType.ALL)
//	@JsonIgnore
//	private List<Rating>ratings=new ArrayList<>();
//	@JsonIgnore
//	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
//	private List<Review>reviews=new ArrayList<>();
	

//    @Getter
//    @Setter
//    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
////    @JoinTable(name = "user_address",
////                joinColumns = @JoinColumn(name = "user_id"),
////                inverseJoinColumns = @JoinColumn(name = "address_id"))
//    private List<Address> addresses = new ArrayList<>();
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Cart cart;

//    @ToString.Exclude
//    @OneToMany(mappedBy = "user",
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
//            orphanRemoval = true)
//    private Set<Product> products;
}
