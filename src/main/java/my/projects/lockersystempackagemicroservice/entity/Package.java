package my.projects.lockersystempackagemicroservice.entity;

import jakarta.persistence.*;
import my.projects.lockersystempackagemicroservice.enums.PackageSizeEnum;
import my.projects.lockersystempackagemicroservice.enums.PackageStatusEnum;

import java.time.LocalDateTime;

@Entity
@Table(name = "packages")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "sender_email")
    private String senderEmail;

    @Column(nullable = false, name = "recipient_email")
    private String recipientEmail;

    @Column(nullable = false, name = "package_size")
    @Enumerated(EnumType.STRING)
    private PackageSizeEnum packageSize;

    @Column(nullable = false)
    private String location;

    @Column
    private String description;

    @Column(nullable = false, unique = true, name = "access_code")
    private String accessCode;

    @Column(name = "locker_unit_id")
    private Long lockerUnitId;

    @Column(nullable = false, name = "package_status")
    private PackageStatusEnum packageStatus;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "picked_up_at")
    private LocalDateTime pickedUpAt;

    public Package() {
    }

    public Package(String senderEmail, String recipientEmail, PackageSizeEnum packageSize, String location, String description, String accessCode, Long lockerUnitId, PackageStatusEnum packageStatus, LocalDateTime createdAt) {
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.packageSize = packageSize;
        this.location = location;
        this.description = description;
        this.accessCode = accessCode;
        this.lockerUnitId = lockerUnitId;
        this.packageStatus = packageStatus;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Package setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public Package setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
        return this;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public Package setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
        return this;
    }

    public PackageSizeEnum getPackageSize() {
        return packageSize;
    }

    public Package setPackageSize(PackageSizeEnum packageSize) {
        this.packageSize = packageSize;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Package setLocation(String location) {
        this.location = location;
        return this;
    }

    public Long getLockerUnitId() {
        return lockerUnitId;
    }

    public Package setLockerUnitId(Long lockerUnitId) {
        this.lockerUnitId = lockerUnitId;
        return this;
    }

    public PackageStatusEnum getPackageStatus() {
        return packageStatus;
    }

    public Package setPackageStatus(PackageStatusEnum packageStatus) {
        this.packageStatus = packageStatus;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Package setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getPickedUpAt() {
        return pickedUpAt;
    }

    public Package setPickedUpAt(LocalDateTime pickedUpAt) {
        this.pickedUpAt = pickedUpAt;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Package setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
