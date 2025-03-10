package com.app.pojo;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// Line 1 containing the Flat no, building,
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
		
		
		@Column(name="street_address")
		private String streetAddress;
		private String state;
		private String city;
		
		// 6 digit pincode of the address
		@Column(name="zip_code")
		private String zipCode;
		@ManyToOne
		@JoinColumn(name="user_id")
		@JsonIgnore
		private User user;
		
		private String mobile;
		
		public Address() {
			// TODO Auto-generated constructor stub
		}
		

		public Address(Long id, String firstName, String lastName, String streetAddress, String state, String city,
				String zipCode, User user, String mobile) {
			super();
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.streetAddress = streetAddress;
			this.state = state;
			this.city = city;
			this.zipCode = zipCode;
			this.user = user;
			this.mobile = mobile;
		}


		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getStreetAddress() {
			return streetAddress;
		}

		public void setStreetAddress(String streetAddress) {
			this.streetAddress = streetAddress;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getZipCode() {
			return zipCode;
		}

		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		
		
		
		
		

}
