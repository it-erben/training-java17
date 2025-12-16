package tech.erben.assignments.fulfillment.domain;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonbTransient
    @ManyToOne
    private FulfillmentOrder order;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status = ShipmentStatus.CREATED;

    private String carrier;

    private String trackingNumber;

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime handoverAt;

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime createdAt;

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime updatedAt;

    @Version
    private long version;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipmentItem> items = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public void markHandover(OffsetDateTime at) {
        status = ShipmentStatus.IN_TRANSIT;
        handoverAt = at;
        updatedAt = OffsetDateTime.now();
    }

    public void addItem(ShipmentItem item) {
        items.add(item);
        item.setShipment(this);
    }

    public Long getId() {
        return id;
    }

    public FulfillmentOrder getOrder() {
        return order;
    }

    public void setOrder(FulfillmentOrder order) {
        this.order = order;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public OffsetDateTime getHandoverAt() {
        return handoverAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public long getVersion() {
        return version;
    }

    public List<ShipmentItem> getItems() {
        return items;
    }
}
