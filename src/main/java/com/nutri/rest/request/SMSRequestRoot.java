package com.nutri.rest.request;

import com.nutri.rest.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SMSRequestRoot {
    public String ver;
    public String key;
    public String encrpt;
    //public String sch_at;
    public List<Message> messages;
}
