package net.stevencai.stevenweb.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="verificationToken")
@Data
public class VerificationToken {
    private static final int EXPIRATION= 60*24;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String token;

    @Column
    private LocalDateTime expireDate;

    @Column
    private String type;

    @Column(name="createData")
    private LocalDateTime createDateTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="userId")
    private User user;

    public VerificationToken() {
        createDateTime = LocalDateTime.now();
    }

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        expireDate = calculateExpiryDate();
        createDateTime = LocalDateTime.now();
    }
    public void setExpiration(int minutes){
        expireDate = createDateTime.plusMinutes(minutes);
    }
    private LocalDateTime calculateExpiryDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        return  dateTime.plusMinutes(VerificationToken.EXPIRATION);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VerificationTokenType getType() {
        return VerificationTokenType.valueOf(type);
    }

    public void setType(VerificationTokenType type) {
        this.type = type.name();
    }

    public boolean isCreatedMinutesAgo(long minutes){
        return LocalDateTime.now().minusMinutes(minutes).isBefore(createDateTime);
    }
}
