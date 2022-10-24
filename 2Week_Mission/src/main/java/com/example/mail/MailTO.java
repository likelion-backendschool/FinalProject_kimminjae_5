package com.example.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MailTO {
    private String address;
    private String title;
    private String message;
}
