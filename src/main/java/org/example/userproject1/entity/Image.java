//package org.example.userproject1.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Builder
//@Entity
//@Table(name = "images")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class Image {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//    private String fileName;
//    private Long size;
//    private String contentType;
//    @Lob
//    private byte[] bytes;
//    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;
//}
